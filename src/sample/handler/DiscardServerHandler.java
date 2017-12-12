package sample.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 处理服务器端通道。
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { //（1）


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { //（2）
        ByteBuf in = (ByteBuf) msg;
        System.out.println(in.toString(CharsetUtil.US_ASCII));

        //以静默方式丢弃收到的数据。
//        ((ByteBuf) msg).release(); //（3）
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { //（4）
        //当发生异常时关闭连接。
        cause.printStackTrace();
        ctx.close();
    }


    /**
     *      DiscardServerHandler扩展ChannelInboundHandlerAdapter，这是一个实现ChannelInboundHandler。
     * ChannelInboundHandler提供了可以覆盖的各种事件处理程序方法。就目前而言，扩展ChannelInboundHandlerAdapter
     * 而不是自己实现处理程序接口就足够了。
     *      我们在channelRead()这里重写事件处理程序方法。无论何时接收到来自客户端的新数据，都会使用收到的消息调用此方法。
     * 在这个例子中，收到的消息的类型是ByteBuf。
     *      为了实现DISCARD协议，处理程序必须忽略收到的消息。ByteBuf是一个引用计数的对象，必须通过该release()方法显式释放。
     * 请记住，处理程序的职责是释放传递给处理程序的任何引用计数的对象
     *      该exceptionCaught()事件处理方法被调用，可抛出异常时被提出的Netty由于I / O错误或由处理器实现，由于在处理事件引发的异常。
     * 在大多数情况下，捕获到的异常应该被记录下来，并且关联的通道应该在这里被关闭，尽管这个方法的实现可能会根据你想要处理的异常情况而
     * 有所不同。例如，您可能希望在关闭连接之前发送带有错误代码的响应消息。
     */

}