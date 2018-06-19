package com.happok.live.activitycontrol.config;


import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

@Configuration   //组件类
@ConfigurationProperties(prefix = "image")
public class ImageConfig {

    private String root;
    private String path;
    private String size;
    private Integer begintime;
    private String type;

    public ImageConfig() {
        Logger logger = LoggerFactory.getLogger(getClass());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getBegintime() {
        return begintime;
    }

    public void setBegintime(Integer begintime) {
        this.begintime = begintime;
    }


}
