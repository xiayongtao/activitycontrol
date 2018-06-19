package com.happok.live.activitycontrol.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.happok.live.activitycontrol.common.ResultCode;
import com.happok.live.activitycontrol.entity.PushEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@Service
public class PushService {

    private Logger LogUtil = LoggerFactory.getLogger(getClass());


    private Map<String, PushEntity> mapPushEntty = new HashMap<String, PushEntity>();

    public ResultCode Start(String srcUrl, String dstIP, String dstPort, String apiPort, String name) {

        if (null != mapPushEntty.get(name)) {
            return ResultCode.IS_EXIST;
        }

        PushEntity pushEntity = new PushEntity();

        pushEntity.setSrcUrl(srcUrl);
        pushEntity.setApiport(apiPort);
        pushEntity.setDstHost(dstIP);
        pushEntity.setDstPort(dstPort);
        pushEntity.setName(name);

        if (!pushEntity.Start()) {
            return ResultCode.RESULE_DATA_NONE;
        }

        mapPushEntty.put(name, pushEntity);

        return ResultCode.SUCCESS;
    }

    public ResultCode Stop(String name) {

        PushEntity pushEntity = mapPushEntty.get(name);
        if (null == pushEntity) {
            return ResultCode.IS_NOTEXIST;
        }

        if (!pushEntity.Stop()) {
            return ResultCode.RESULE_DATA_NONE;
        }

        mapPushEntty.remove(name);
        return ResultCode.SUCCESS;
    }

    public Object getStreams() {

        JSONArray results = new JSONArray();
        for (String key : mapPushEntty.keySet()) {
            PushEntity pushEntity = mapPushEntty.get(key);
            if (null != pushEntity) {

                JSONObject obj = new JSONObject(true);
                obj.put("name", pushEntity.getName());
                obj.put("status", pushEntity.getStreamStatus());
                results.add(obj);
            }
        }

        return results;
    }

    public JSONObject getStream(String name) {
        JSONObject obj = null;
        PushEntity pushEntity = mapPushEntty.get(name);
        if (null != pushEntity) {
            obj = new JSONObject(true);
            obj.put("name", pushEntity.getName());
            obj.put("status", pushEntity.getStreamStatus());
        }

        return obj;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void timerCheckStatus() {
        for (String key : mapPushEntty.keySet()) {
            PushEntity pushEntity = mapPushEntty.get(key);
            if (null != pushEntity) {
                pushEntity.Chekc();
            }
        }
    }
}
