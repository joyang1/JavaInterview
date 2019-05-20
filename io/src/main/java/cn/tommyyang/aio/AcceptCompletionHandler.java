package cn.tommyyang.aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author TommyYang on 2019-05-20
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {

    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {

    }
}
