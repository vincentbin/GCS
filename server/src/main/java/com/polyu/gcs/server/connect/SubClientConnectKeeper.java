package com.polyu.gcs.server.connect;

import com.polyu.gcs.common.info.RegistryPackage;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SubClientConnectKeeper {
    private static List<Channel> subClients = new CopyOnWriteArrayList<Channel>();
    private static Map<Channel, Boolean> repeatCheckMap = new ConcurrentHashMap<Channel, Boolean>();

    public synchronized static void add(Channel channel) {
        if (repeatCheckMap.containsKey(channel)) {
            return;
        }
        repeatCheckMap.put(channel, true);
        subClients.add(channel);
    }

    public static void remove(Channel channel) {
        repeatCheckMap.remove(channel);
        subClients.remove(channel);
    }

    public static List<Channel> getList() {
        return subClients;
    }
}
