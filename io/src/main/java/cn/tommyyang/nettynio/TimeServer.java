package cn.tommyyang.nettynio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author TommyYang on 2019-05-21
 */
public class TimeServer {

    private void bind(int port) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            //绑定端口，同步等待成功
            ChannelFuture cf = bootstrap.bind(port).sync();

            System.out.println("Netty Nio server start-001");

            //等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            //优雅的退出
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }

    }

    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args != null && args.length > 0){
            try {
                port  = Integer.valueOf(args[0]);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

        }

        new TimeServer().bind(port);

    }

}
