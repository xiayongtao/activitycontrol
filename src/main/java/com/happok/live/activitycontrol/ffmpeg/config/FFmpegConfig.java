package com.happok.live.activitycontrol.ffmpeg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
@ConfigurationProperties(prefix = "ffmpeg")

public class FFmpegConfig {
    private String path;
    private boolean debug;
    private Integer size;
    private boolean issyspath;
    private String rootPath = ClassUtils.getDefaultClassLoader().getResource("ffmpeg").getPath();

    public boolean isIssyspath() {
        return issyspath;
    }

    public void setIssyspath(boolean issyspath) {
        this.issyspath = issyspath;
    }


    public String getPath() {
        if (issyspath) {
            return rootPath + "/" + path;
        } else {
            return path;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FFmpegConfig [path=" + path + ", debug=" + debug + ", size=" + size + "]";
    }
}
