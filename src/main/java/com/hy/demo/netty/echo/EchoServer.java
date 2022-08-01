package com.hy.demo.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {


    public static void main(String[] args) throws InterruptedException {
        System.out.println("即将启动服务端");
        EchoServer echoServer = new EchoServer();
        echoServer.start();
        System.out.println("服务端关闭");
    }

    public void start() throws InterruptedException {
        final EchoServerHandle echoServerHandle = new EchoServerHandle();
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /*服务端启动必备*/
            ServerBootstrap b = new ServerBootstrap();
            /*
            * 将线程组传入
            * 指定使用NIO进行网络传输
            * 指定服务端监听端口
            * 服务端没收到一个连接请求，就会新启动一个socket通信，也就是channel
            * */
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8081))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /*channel添加handle*/
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandle);
                        }
                    });
            /*异步绑定到服务器，sync() 会阻塞直到完成*/
            ChannelFuture f = b.bind().sync();
            /*阻塞直到服务器的channel关闭*/
            f.channel().closeFuture().sync();
        }finally {
            /*关闭线程组*/
            group.shutdownGracefully().sync();
        }
    }
}
