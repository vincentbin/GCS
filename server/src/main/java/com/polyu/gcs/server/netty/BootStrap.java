package com.polyu.gcs.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.polyu.gcs.server.registry.RegistryCenter;

public class BootStrap implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(BootStrap.class);

    private String serverAddress;
    private RegistryCenter registry;

    public BootStrap(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.registry = new RegistryCenter(registryAddress);
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors() / 2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture future = bootstrap.bind(port).sync();
            registry.pullServerInfo();
            registry.registerService(host, port);
            logger.info("Server started on ip: {}, port: {}.", host, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("GCS server error, exception: {}.", e.getMessage(), e);
        } finally {
            registry.unregisterService();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
