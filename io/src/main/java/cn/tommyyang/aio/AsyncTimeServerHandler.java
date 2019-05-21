package cn.tommyyang.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author TommyYang on 2019-05-20
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;
    AsynchronousServerSocketChannel asyncServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            this.asyncServerSocketChannel = AsynchronousServerSocketChannel.open();
            this.asyncServerSocketChannel.bind(new InetSocketAddress(this.port));
            System.out.println("The time server is start in port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.latch = new CountDownLatch(1);
        doAccept();
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept(){
        this.asyncServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
