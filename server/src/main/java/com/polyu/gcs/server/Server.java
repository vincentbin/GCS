package com.polyu.gcs.server;

import com.polyu.gcs.server.netty.BootStrap;

public class Server {
    public static void start(String serverAddress, String registryAddress) {
        new Thread(new BootStrap(serverAddress, registryAddress)).start();
    }
}
