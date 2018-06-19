package com.happok.live.activitycontrol.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.common.Result;
import com.happok.live.activitycontrol.common.ResultCode;
import com.happok.live.activitycontrol.config.ConfigStatic;
import com.happok.live.activitycontrol.config.RecordConfig;
import com.happok.live.activitycontrol.entity.RecordEntity;
import com.happok.live.activitycontrol.ffmpeg.util.FormatAnalysis;
import com.happok.live.activitycontrol.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordService {

    private RecordConfig recordConfig = ConfigStatic.getRecordConfig();
    private Logger LogUtil = LoggerFactory.getLogger(getClass());
    private Map<String, RecordEntity> mapRecordEntty = new HashMap<String, RecordEntity>();

    public ResultCode Start(String dirName, String srcUrl) {
        if (null != mapRecordEntty.get(dirName)) {
            return ResultCode.IS_EXIST;
        }

        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setDirName(dirName);
        recordEntity.setSrcUrl(srcUrl);

        mapRecordEntty.put(dirName, recordEntity);

        if (!recordEntity.Start()) {
            return ResultCode.RESULE_DATA_NONE;
        }

        return ResultCode.SUCCESS;

    }

    public Object getRecordStatus(String dirName) {
        RecordEntity recordEntity = mapRecordEntty.get(dirName);
        if (null == recordEntity) {
            return Result.failure(ResultCode.IS_NOTEXIST);
        }

        return Result.success(recordEntity.getRecordStatus());
    }

    public ResultCode Stop(String dirName) {
        RecordEntity recordEntity = mapRecordEntty.get(dirName);
        if (null == recordEntity) {
            return ResultCode.IS_NOTEXIST;
        }

        if (!recordEntity.Stop()) {
            return ResultCode.RESULE_DATA_NONE;
        }

        mapRecordEntty.remove(dirName);
        return ResultCode.SUCCESS;
    }

    /*
        public Object getRecords() {
            JSONArray results = new JSONArray();
            for (String key : mapRecordEntty.keySet()) {
                RecordEntity recordEntity = mapRecordEntty.get(key);
                if (null != recordEntity) {

                    JSONObject obj = new JSONObject(true);
                    obj.put("fileName", recordEntity.getRecordPath());
                    results.add(obj);
                }
            }

            return results;
        }
    */
    public Object getRecord(String dirName) {
        return getFiles(dirName);
    }

    public ResultCode deleteDirName(String dirName) {

        if (!rmDirName(dirName)) {
            return ResultCode.IS_NOTEXIST;
        }

        return ResultCode.SUCCESS;
    }

    private Object getFiles(String dirName) {
        JSONArray fileList = new JSONArray();
        List<String> files = FileUtil.getFiles(recordConfig.getRoot() + "/" + recordConfig.getPath() + "/" + dirName, "mp4");
        for (String file : files) {

            JSONObject objfile = new JSONObject(true);
            objfile.put("size", FileUtil.getFileSize(file));

            //getFileDuration
            objfile.put("duration", FormatAnalysis.getFileDuration(file));

            String newFile = file.replaceAll("\\\\", "/");
            file = newFile.replace(recordConfig.getRoot(), "/");
            objfile.put("name", file);
            System.out.println("fileName:" + file + " rootPat:" + recordConfig.getRoot());
            fileList.add(objfile);
        }

        return fileList;
    }

    private boolean rmDirName(String dirName) {
        String path = recordConfig.getRoot() + "/" + recordConfig.getPath() + "/" + dirName;
        return FileUtil.deleteDirectory(path);
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void timerCheck() {
        for (String key : mapRecordEntty.keySet()) {
            RecordEntity recordEntity = mapRecordEntty.get(key);
            if (null != recordEntity) {
                recordEntity.CheckStatus();
            }
        }
    }


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void timerSubsection() {
        for (String key : mapRecordEntty.keySet()) {
            RecordEntity recordEntity = mapRecordEntty.get(key);
            if (null != recordEntity) {
                recordEntity.reRecord();
            }
        }
    }

}
