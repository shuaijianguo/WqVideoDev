package com.wq.enums;

/**
 * Created by wuqingvika on 2018/7/8.
 */
public enum  VideoStatusEnum {
    SUCCESS(1),//允许播放
    FORBID(2);//禁止播放

    VideoStatusEnum(int value) {
        this.value = value;
    }

    public final int value;

    public int getValue() {
        return value;
    }
}
