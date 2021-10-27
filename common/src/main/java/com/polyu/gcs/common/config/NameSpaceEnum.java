package com.polyu.gcs.common.config;

/**
 * 注册中心配置枚举
 */
public enum NameSpaceEnum {

    ZK_REGISTRY_PATH("/gcs/registry"),
    ZK_NAME_SPACE("GCS"),
    ZK_SESSION_TIMEOUT(5000),
    ZK_CONNECTION_TIMEOUT(5000);


    private String value;
    private int timeOutLength;

    NameSpaceEnum(String value) {
        this.value = value;
    }

    NameSpaceEnum(int timeOutLength) {
        this.timeOutLength = timeOutLength;
    }

    public String getValue() {
        return value;
    }

    public int getTimeOutLength() {
        return timeOutLength;
    }

}
