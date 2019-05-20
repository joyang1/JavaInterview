package cn.tommyyang.aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author TommyYang on 2019-05-20
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;
    AsynchronousSocketChannel asynchronousSocketChannel;


    public void run() {

    }
}
