package com.hy.demo.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    String host = "127.0.0.1";
    int port = 8081;

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0 ; i < 10; i++){
            new MyThread().start();
        }

    }

    public static class MyThread extends Thread {
        public void run() {
            EchoClient echoClient = new EchoClient();
            try {
                echoClient.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void start() throws InterruptedException {
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /*客户端启动必备*/
            Bootstrap b = new Bootstrap();
            /*
             * 传入线程组
             * 指定使用NIO进行网络传输
             * */
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(host, port)
                    .handler(new EchoClientHandle());

            /*连接到远程节点，阻塞直到连接完成*/
            ChannelFuture f = b.connect().sync();
            /*阻塞程序，直到Channel发生，关闭*/
            //f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
