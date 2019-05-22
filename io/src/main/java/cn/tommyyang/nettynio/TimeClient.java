package cn.tommyyang.nettynio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author TommyYang on 2019-05-21
 */
public class TimeClient {

    private void connect(String host, int port) throws Exception{
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new TimeClientHandler());
                    }
                });

        //发起异步连接操作
        ChannelFuture cf = bootstrap.connect(host, port).sync();

        //等待 客户端链路关闭
        cf.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;

        new TimeClient().connect("127.0.0.1", port);
    }

}
