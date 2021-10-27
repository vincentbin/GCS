package com.polyu.gcs.client.netty;

import com.polyu.gcs.client.BootStrap;
import com.polyu.gcs.client.Client;
import com.polyu.gcs.common.info.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BusinessHandler.class);

    private static Channel channel;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        BusinessHandler.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message message = (Message) msg;
        logger.info("receive message from {}, content is {}.", message.getFromUser(), message.getContent());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 连接失败也会调用到这一步 自动触发重新连接
     * 休眠1s 等待zk更新
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Thread.sleep(1000L);
        Client.start();
    }

    /**
     * 发送消息
     * @param content 消息内容
     */
    public static void sendPackage(String content) {
        Message message = new Message();
        message.setFromUser(BootStrap.getClientName());
        message.setContent(content);
        BusinessHandler.channel.writeAndFlush(message);
    }

}
