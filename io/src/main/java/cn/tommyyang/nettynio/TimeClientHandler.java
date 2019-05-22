package cn.tommyyang.nettynio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author TommyYang on 2019-05-22
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf msg;

    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        this.msg = Unpooled.buffer(req.length);
        this.msg.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(this.msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf resp = (ByteBuf)msg;
        byte[] respBytes = new byte[resp.readableBytes()];
        resp.readBytes(respBytes);

        System.out.println("Now is:" + new String(respBytes, "UTF-8"));

        //查询完毕，释放资源，结束程序
        ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        ctx.close();
    }
}
