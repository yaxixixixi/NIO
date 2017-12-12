package sample.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import sample.handler.DiscardServerHandler;
import sample.handler.TimeServerHandler;

/**
 * http://netty.io/wiki/user-guide-for-4.x.html
 */
public class DiscardServer
{
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }


    public void run() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)

        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();// (2)
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)//(3)
                    .childHandler(new ChannelInitializer<SocketChannel>() {// (4)
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)// (5)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);// (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = bootstrap.bind(port).sync();// (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0){
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        new DiscardServer(port).run();
    }
}


/**
 *  All introduction below trans from an url which plated on above.please read the original English.
 *
 * 1.NioEventLoopGroup是一个处理I/O操作的多线程事件循环。Netty EventLoopGroup为不同类型的传输提供了各种实现。在这个例子中，我们正在
 * 实现一个服务器端应用程序，因此NioEventLoopGroup将使用两个应用程序。第一个，通常被称为“老板”，接受传入的连接。第二个通常被称为“工作者”，
 * 一旦老板接受连接并将接受的连接注册给工作人员，则处理被接受连接的流量。使用多少个线程以及它们如何映射到创建的Channels取决于
 * EventLoopGroup实现，甚至可以通过构造函数来配置。
 * 2.ServerBootstrap是一个帮助类，建立一个服务器。您可以使用Channel直接设置服务器。但是请注意，这是一个单调乏味的过程，
 * 在大多数情况下你不需要这样做。
 * 3.在这里，我们指定使用NioServerSocketChannel用于实例化新的类Channel来接受传入的连接。
 * 4.此处指定的处理程序将始终由新接受的评估Channel。这ChannelInitializer是一个特殊的处理程序，旨在帮助用户配置一个新的Channel。
 * 最有可能的ChannelPipeline是，Channel通过添加一些处理程序来配置新的处理程序，DiscardServerHandler以实现您的网络应用程序。
 * 随着应用程序变得复杂，您可能会在管道中添加更多的处理程序，并最终将此匿名类提取到顶级类中。
 * 5.您也可以设置特定于Channel实现的参数。我们正在编写一个TCP / IP服务器，所以我们被允许设置套接字选项，如tcpNoDelay和keepAlive。
 * 请参考apidocs ChannelOption和特定的ChannelConfig实现来获得关于支持ChannelOption的概述。
 * 6.你有没有注意到option()和childOption()？option()是为了NioServerSocketChannel接受传入的连接。childOption()是Channel由
 * 父母接受的ServerChannel，NioServerSocketChannel在这种情况下。
 * 7.我们现在准备走了。剩下的就是绑定到端口并启动服务器。在这里，我们绑定到机器8080中所有NIC（网卡）的端口。您现在可以bind()根据需要\
 * 多次调用该方法（使用不同的绑定地址）。
 */
