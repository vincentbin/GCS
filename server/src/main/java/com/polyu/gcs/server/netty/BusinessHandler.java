package com.polyu.gcs.server.netty;

import com.polyu.gcs.common.info.Message;
import com.polyu.gcs.server.connect.SubClientConnectKeeper;
import com.polyu.gcs.server.task.SendingTask;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class BusinessHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BusinessHandler.class);
    /**
     * 业务线程池
     */
    private ThreadPoolExecutor businessTaskThreadPool;

    BusinessHandler(ThreadPoolExecutor businessTaskThreadPool) {
        this.businessTaskThreadPool = businessTaskThreadPool;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message message = (Message) msg;
        logger.info("server receive message: {}.", message.toString());
        Channel channel = ctx.channel();
        if (message.isClientInitMsg()) {
            SubClientConnectKeeper.add(channel);
            return;
        }
        businessTaskThreadPool.execute(new SendingTask(message, message.isServerBoardCast()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        SubClientConnectKeeper.remove(ctx.channel());
    }
}
