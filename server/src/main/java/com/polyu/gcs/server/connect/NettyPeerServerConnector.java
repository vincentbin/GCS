package com.polyu.gcs.server.connect;

import com.polyu.gcs.common.info.RegistryPackage;
import com.polyu.gcs.server.netty.NettyChannelInitializer;
import com.polyu.gcs.server.registry.RegistryCenter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyPeerServerConnector {
    private static final Logger logger = LoggerFactory.getLogger(NettyPeerServerConnector.class);

    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors() / 2);

    public static void connect(String peerServerIp, int peerServerPort) {
        InetSocketAddress remotePeer = new InetSocketAddress(peerServerIp, peerServerPort);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyChannelInitializer());

        ChannelFuture channelFuture = bootstrap.connect(remotePeer);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    logger.info("Peer server connect success.");
                    PeerServerConnectKeeper.add(channelFuture.channel(), new RegistryPackage(peerServerIp, peerServerPort));
                } else {
                    logger.error("Peer server connect failed.");
                }
            }
        });
    }

}
