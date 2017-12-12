import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Server {


    private int port;

    public Server(int port){
        this.port = port;
    }

    public void run(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //用于处理服务器端接收客户端连接

        EventLoopGroup workGroup = new NioEventLoopGroup();//用于处理网络通信（rw）

        ServerBootstrap bootstrap = new ServerBootstrap();//用于服务器通道的一系列配置

//        bootstrap.group(bossGroup,workGroup) //绑定两个线程组

    }
}
