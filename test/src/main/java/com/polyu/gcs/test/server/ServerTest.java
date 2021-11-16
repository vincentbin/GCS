package com.polyu.gcs.test.server;

import com.polyu.gcs.server.Server;

public class ServerTest {
    public static void main(String[] args) {
        Server.start("127.0.0.1:8893", "127.0.0.1:2181");
    }
}
