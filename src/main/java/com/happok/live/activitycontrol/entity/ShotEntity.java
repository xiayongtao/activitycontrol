package com.happok.live.activitycontrol.entity;

import com.happok.live.activitycontrol.config.ImageConfig;
import com.happok.live.activitycontrol.ffmpeg.api.FFmpegCmdApi;
import com.happok.live.activitycontrol.config.ConfigStatic;
import com.happok.live.activitycontrol.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ShotEntity {

    private ImageConfig imageConfig = ConfigStatic.getImageConfig();
    private Logger LogUtil = LoggerFactory.getLogger(getClass());

    private FFmpegCmdApi manager = null;

    private String dirName;
    private String srcUrl;
    private String path;

    private String ImagePath;
    private String baseImagePath;

    public ShotEntity() {
        manager = new FFmpegCmdApi("shot");
        ImagePath = imageConfig.getPath();
        baseImagePath = imageConfig.getRoot();
    }

    public String getType() {
        return imageConfig.getType();
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getBaseImagePath() {
        return baseImagePath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean Shot() {

        Map<String, String> cmmondmap = new HashMap<String, String>();

        long nowData = new Date().getTime();

        String filePath = ImagePath + "/" + this.dirName + "/";
        String fileName = Long.toString(nowData) + "." + imageConfig.getType();

        if (!FileUtil.createDir(baseImagePath + "/" + filePath)) {
            LogUtil.warn("创建目录" + filePath + " 目标目录已经存在");
        }

        cmmondmap.put("appName", Long.toString(nowData));
        cmmondmap.put("input", this.srcUrl);
        cmmondmap.put("output", baseImagePath + "/" + filePath + fileName);

        String processId = manager.start(cmmondmap);

        if (null != processId) {
            this.path = filePath + fileName;
            return true;
        }

        return false;
    }
}
