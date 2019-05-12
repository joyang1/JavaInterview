package cn.tommyyang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author TommyYang on 2019-05-12
 */
public class TimeClientHandler implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandler(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        while (!stop) {
            try {
                this.selector.select(1000);
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> itKeys = keys.iterator();
                while (itKeys.hasNext()) {
                    SelectionKey key = itKeys.next();
                    itKeys.remove();
                    try {
                        handleDataFromServer(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        if (this.selector != null) {
            try {
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDataFromServer(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();
            //判断先前是否连接成功，如果构造器里面的socketChannel已经连接成功了，
            // 则此处不会有connectable的操作select到
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(this.selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    //连接失败，程序退出
                    System.out.println("connect failed!!!");
                    System.exit(-1);
                }
            }

            // read the data from server
            if (key.isReadable()) {
                ByteBuffer readBuf = ByteBuffer.allocate(1024);
                int readByteLen = sc.read(readBuf);
                if (readByteLen > 0) {
                    readBuf.flip();
                    byte[] readBytes = new byte[readBuf.remaining()];
                    readBuf.get(readBytes);
                    String body = new String(readBytes, "UTF-8");
                    System.out.println("Now is:" + body);
                    this.stop = true;
                }
            }
        }
    }

    private void doConnect() throws IOException {
        //如果连接成功，则注册到多路复用器上，发送请求消息，读应答
        if (this.socketChannel.connect(new InetSocketAddress(this.host, this.port))) {
            this.socketChannel.register(this.selector, SelectionKey.OP_READ);
            doWrite(this.socketChannel);
        } else {
            //连接失败，则重新尝试connect, 在handleInput方法里面可以看到
            this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        System.out.println("Send order 2 server succeed!!!");
//        while (!writeBuffer.hasRemaining()) {
//            System.out.println("Send order 2 server succeed!!!");
//        }
    }

}
