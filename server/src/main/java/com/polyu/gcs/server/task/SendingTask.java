package com.polyu.gcs.server.task;

import com.polyu.gcs.common.info.Message;
import com.polyu.gcs.server.connect.PeerServerConnectKeeper;
import com.polyu.gcs.server.connect.SubClientConnectKeeper;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SendingTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SendingTask.class);

    private Message message;
    private boolean peerBoardCast;

    public SendingTask(Message message, boolean peerBoardCast) {
        this.message = message;
        this.peerBoardCast = peerBoardCast;
    }

    @Override
    public void run() {
        if (!this.peerBoardCast) {
            PeerServerNotification();
        }
        subNodeNotification();
    }

    private void subNodeNotification() {
        List<Channel> subClients = SubClientConnectKeeper.getList();
        for (Channel subClient : subClients) {
            Message message = new Message();
            message.setContent(this.message.getContent());
            message.setFromUser(this.message.getFromUser());
            subClient.writeAndFlush(message);
        }
    }

    private void PeerServerNotification() {
        List<Channel> peerServers = PeerServerConnectKeeper.getList();
        for (Channel peerServer : peerServers) {
            Message message = new Message();
            message.setServerBoardCast(true);
            message.setContent(this.message.getContent());
            message.setFromUser(this.message.getFromUser());
            peerServer.writeAndFlush(message);
        }
    }
}
