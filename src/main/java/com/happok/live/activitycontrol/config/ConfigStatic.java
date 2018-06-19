package com.happok.live.activitycontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigStatic {

    @Autowired
    private ImageConfig imageConfigAutowired;
    private static ImageConfig imageConfig;

    @Autowired
    private StreamServerConfig streamServerConfigAutowired;
    private static StreamServerConfig streamServerConfig;


    @Autowired
    private RecordConfig recordConfigAutowired;
    private static RecordConfig recordConfig;

    @PostConstruct
    public void init() {
        imageConfig = this.imageConfigAutowired;
        streamServerConfig = this.streamServerConfigAutowired;
        recordConfig = this.recordConfigAutowired;
    }

    public static ImageConfig getImageConfig() {
        return imageConfig;
    }

    public static StreamServerConfig getStreamServerConfig() {
        return streamServerConfig;
    }

    public static RecordConfig getRecordConfig() {
        return recordConfig;
    }

}
