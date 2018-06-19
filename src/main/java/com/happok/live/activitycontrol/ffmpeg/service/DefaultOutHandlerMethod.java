package com.happok.live.activitycontrol.ffmpeg.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOutHandlerMethod implements OutHandlerMethod {

    Logger logger = LoggerFactory.getLogger(getClass());
    private Integer count = 0;

    @Override
    public void parse(String type, String msg) {

        if (count++ > 100) {
            if (msg.indexOf("[rtsp") != -1) {
                logger.warn(type + "发生网络异常丢包，消息体：" + msg);
            } else if (msg.indexOf("frame=") != -1) {
                logger.warn(type + "<====>" + msg);
            }
            count = 0;
        }
    }
}
