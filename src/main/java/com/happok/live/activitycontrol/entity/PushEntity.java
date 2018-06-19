package com.happok.live.activitycontrol.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happok.live.activitycontrol.config.ConfigStatic;
import com.happok.live.activitycontrol.config.StreamServerConfig;
import com.happok.live.activitycontrol.ffmpeg.api.FFmpegCmdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PushEntity {

    private static Logger LogUtil = LoggerFactory.getLogger(PushEntity.class);
    private static StreamServerConfig srsConfig = ConfigStatic.getStreamServerConfig();

    private RestTemplate restTemplate = new RestTemplate();

    private String srcUrl;
    private String name;

    private String dstHost;
    private String dstPort;
    private String apiport;

    private Integer filedCount = 0;

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getDstHost() {
        return dstHost;
    }

    public void setDstHost(String dstHost) {
        this.dstHost = dstHost;
    }

    public String getApiport() {
        return apiport;
    }

    public void setApiport(String apiport) {
        this.apiport = apiport;
    }

    private String dstUrl;

    private FFmpegCmdApi manager = new FFmpegCmdApi("live");


    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDstUrl() {
        return dstUrl;
    }

    private Map<String, String> getCmd() {
        Map<String, String> cmd = new HashMap<String, String>();

        dstUrl = "rtmp://" + this.dstHost + ":" + this.dstPort + "/live/";
        cmd.put("appName", this.name);
        cmd.put("input", this.srcUrl);
        cmd.put("output", dstUrl);

        return cmd;
    }

    public boolean Start() {

        Map<String, String> cmd = getCmd();
        String taskName = manager.start(cmd);
        if (null != taskName) {
            dstUrl = cmd.get("output") + cmd.get("appName");
            return true;
        }

        return false;
    }

    public boolean Stop() {
        if (!manager.stop(name)) {
            LogUtil.info("stop if failed");
            return false;
        }

        return true;
    }

    public boolean getStreamStatus() {
        String url = srsConfig.getProtocol() + this.dstHost + ":" + this.apiport + srsConfig.getPrefix();
        url += "streams";

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            JSONObject body = JSONObject.parseObject(responseEntity.getBody());

            JSONArray streams = body.getJSONArray("streams");
            for (int j = 0; j < streams.size(); j++) {
                JSONObject stream = streams.getJSONObject(j);
                Integer streamId = stream.getInteger("id");

                if (streamId != -1 && name.equals(stream.getString("name"))) {
                    JSONObject publish = stream.getJSONObject("publish");
                    if (publish.getBoolean("active")) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (RestClientException e) {
            LogUtil.error(e.toString());
            return false;
        }

        return false;
    }

    public void Chekc() {
        try {

            if (!getStreamStatus()) {

                if (filedCount++ > 3) {
                    Stop();
                    Thread.sleep(5000);
                    Start();
                    LogUtil.warn("name:" + name + " is status:false---");
                    filedCount = 0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogUtil.error("Chekc: e" + e.toString());
        }
    }
}
