package com.happok.live.activitycontrol.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.config.ConfigStatic;
import com.happok.live.activitycontrol.config.RecordConfig;
import com.happok.live.activitycontrol.ffmpeg.api.FFmpegCmdApi;
import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;
import com.happok.live.activitycontrol.ffmpeg.util.FormatAnalysis;
import com.happok.live.activitycontrol.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RecordEntity {

    private Logger LogUtil = LoggerFactory.getLogger(getClass());

    private String srcUrl;
    private String dirName;
    private String recordPath;
    private String processId;
    private Integer filedCount = 0;


    private Boolean isRecordStatus;

    private RecordConfig recordConfig = ConfigStatic.getRecordConfig();

    private FFmpegCmdApi manager = new FFmpegCmdApi("record");

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public Boolean getRecordStatus() {
        return isRecordStatus;
    }


    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public boolean Start() {

        Map<String, String> cmmondmap = new HashMap<String, String>();

        long nowData = new Date().getTime();

        String filePath = recordConfig.getPath() + "/" + this.dirName + "/";
        String fileName = Long.toString(nowData);

        if (!FileUtil.createDir(recordConfig.getRoot() + "/" + filePath)) {
            LogUtil.warn("创建目录" + filePath + " 目标目录已经存在");
        }

        cmmondmap.put("appName", fileName + ".flv");
        cmmondmap.put("input", this.srcUrl);
        cmmondmap.put("output", recordConfig.getRoot() + "/" + filePath);

        processId = manager.start(cmmondmap);
        if (null != processId) {
            return true;
        }

        return false;
    }

    public boolean Stop() {

        TaskEntity taskEntity = manager.query(processId);
        if (null != taskEntity && manager.stop(processId)) {

            String filePath = recordConfig.getPath() + "/" + this.dirName + "/";
            String fileName = taskEntity.getId();
            if (recordConfig.getType().equals("mp4")) {

                String input = recordConfig.getRoot() + "/" + filePath + fileName;
                if (null != FormatAnalysis.MP4BoxCommond(input)) {
                    String file = input.substring(0, fileName.lastIndexOf("."));
                    this.recordPath = filePath + file + ".mp4";
                    FileUtil.delete(recordConfig.getRoot() + "/" + filePath + fileName);
                }
            } else {
                this.recordPath = filePath + fileName;
            }
        }


        return true;
    }


    public boolean rmDirName() {

        String path = recordConfig.getRoot() + "/" + recordConfig.getPath() + "/" + this.dirName;
        return FileUtil.deleteDirectory(path);
    }

    public boolean rmFile() {

        String file = recordConfig.getRoot() + "/" + this.recordPath;
        return FileUtil.delete(file);
    }

    public void CheckStatus() {
        TaskEntity task = manager.query(processId);
        Process pro = task.getProcess();
        if (null != pro && pro.isAlive()) {
            isRecordStatus = true;
        } else {
            if (filedCount++ > 3) {
                isRecordStatus = false;
                filedCount = 0;
            }
        }
    }

    public void reRecord() {

        Stop();
        Start();
    }
}
