package com.happok.live.activitycontrol.ffmpeg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FFmpegConfigStatic {

    @Autowired
    private FFmpegConfig configPropertiesAutowired;
    private static FFmpegConfig config;

    @PostConstruct
    public void init() {
        config = this.configPropertiesAutowired;
    }

    public static FFmpegConfig getFFmpegConfig() {
        return config;
    }
}
