package com.polyu.gcs.server.netty;

import com.polyu.gcs.common.codec.Decoder;
import com.polyu.gcs.common.codec.Encoder;
import com.polyu.gcs.common.serializer.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ThreadPoolExecutor businessTaskThreadPool = new ThreadPoolExecutor(
            20,
            35,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            r -> new Thread(r, "server-worker-thread"),
            new ThreadPoolExecutor.AbortPolicy());

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        cp.addLast(new Decoder(KryoSerializer.getInstance()));
        cp.addLast(new Encoder(KryoSerializer.getInstance()));
        cp.addLast(new BusinessHandler(businessTaskThreadPool));
    }

}
