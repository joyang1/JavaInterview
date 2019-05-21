package cn.tommyyang.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author TommyYang on 2019-05-21
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private String host;
    private int port;
    private AsynchronousSocketChannel timeClient;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.timeClient = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer reqBuffer = ByteBuffer.allocate(req.length);
        reqBuffer.put(req);
        reqBuffer.flip();

        this.timeClient.write(reqBuffer, reqBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()){
                    timeClient.write(attachment, attachment, this);
                } else {
                    ByteBuffer respBuffer = ByteBuffer.allocate(1024);
                    timeClient.read(respBuffer, respBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] respBytes = new byte[attachment.remaining()];
                            attachment.get(respBytes);
                            try {
                                String respBody = new String(respBytes, "UTF-8");
                                System.out.println("Now is:" + respBody);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            exc.printStackTrace();
                            close();
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
                close();
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        exc.printStackTrace();
        this.close();
    }

    @Override
    public void run() {
        this.latch = new CountDownLatch(1);
        this.timeClient.connect(new InetSocketAddress(this.host, this.port), this, this);

        try {
            this.latch.await();//让当前client执行线程一直处于挂起状态，直到当前线程执行完毕，latch 释放。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            this.timeClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close(){
        try {
            timeClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

}
