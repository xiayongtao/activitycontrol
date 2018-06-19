package com.happok.live.activitycontrol.service;


import com.happok.live.activitycontrol.entity.ShotEntity;
import com.happok.live.activitycontrol.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;


@Service
public class ShotService {

    private Logger LogUtil = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShotEntity shotEntity;

    public Object StartShot(String dirName, String srcUrl) {

        shotEntity.setDirName(dirName);
        shotEntity.setSrcUrl(srcUrl);
        if (!shotEntity.Shot()) {
            LogUtil.error("shotEntity.Shot() failed");
        }

        JSONObject images = new JSONObject(true);
        images.put("type", shotEntity.getType());
        images.put("path", shotEntity.getPath());

        return images;
    }

    public boolean deleteImage(String dirName, String filename) {

        String path = shotEntity.getBaseImagePath() + "/" + shotEntity.getImagePath() + "/" + dirName + "/" + filename;
        LogUtil.info("deleteImage:" + path);
        return FileUtil.deleteFile(path);
    }

    public boolean deleteAllImage(String dirName) {
        return FileUtil.deleteDirectory(shotEntity.getBaseImagePath() + "/" + shotEntity.getImagePath() + "/" + dirName);
    }
}
