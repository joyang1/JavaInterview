package cn.tommyyang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author TommyYang on 2019-05-09
 */
public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器
     *
     * @param port
     *
     * */
    public MultiplexerTimeServer(int port) {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("The Multiplexer Time Server is start on port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void run() {
        while (!stop){
            try {
                this.selector.select(1000);
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> itkeys = keys.iterator();
                while (itkeys.hasNext()){
                    SelectionKey key = itkeys.next();
                    itkeys.remove();
                    try{
                        handleDataFromClient(key);
                    }catch (IOException e){
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (this.selector != null){
            try {
                this.selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.stop = true;
    }

    private void handleDataFromClient(SelectionKey key) throws IOException {
        if(!key.isValid()){
            return;
        }
        //处理新接入的消息
        if (key.isAcceptable()){
            ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            //添加新的connection到selector
            sc.register(this.selector, SelectionKey.OP_READ);
        }

        //read the data from client
        if (key.isReadable()){
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int size = sc.read(buf);
            if (size > 0){
                buf.flip();
                byte[] bytes = new byte[buf.remaining()];
                buf.get(bytes);
                String body = new String(bytes, "UTF-8");
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                        ? new Date().toString()
                        : "ERROR ORDER";
                doWrite(sc, currentTime);
            } else if (size < 0){
                //客户端链路关闭
                key.cancel();
                sc.close();
            } else {
                //没有数据 不处理
            }
        }
    }

    private void doWrite(SocketChannel sc, String respone) throws IOException {
        if (respone != null && respone.trim().length() > 0){
            byte[] respArr = respone.getBytes();
            ByteBuffer buf = ByteBuffer.allocate(respArr.length);
            buf.put(respArr);
            buf.flip();
            sc.write(buf);
        }
    }

}
