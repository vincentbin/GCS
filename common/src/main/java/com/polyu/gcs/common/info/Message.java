package com.polyu.gcs.common.info;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {
    private boolean clientInitMsg;

    private boolean serverBoardCast;
    private String content;
    private String fromUser;
}
