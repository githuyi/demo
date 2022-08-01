package com.hy.demo.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable /*共享同一个服务端*/
public class EchoServerHandle extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(0);
    private int a;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("服务端接收到信息：" + in.toString(CharsetUtil.UTF_8));
        ctx.write(in); // 写入缓冲区
        counter.addAndGet(1);
        a++;
        System.out.println("counter : " + counter);
        System.out.println("a : " + a);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*channelRead中读完了，把写入缓冲区的数据刷如客户端*/
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
