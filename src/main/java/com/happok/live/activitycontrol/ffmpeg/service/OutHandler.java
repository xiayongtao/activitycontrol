package com.happok.live.activitycontrol.ffmpeg.service;


import com.happok.live.activitycontrol.ffmpeg.api.FFmpegManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OutHandler extends Thread {
    /**
     * 控制状态
     */

    Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean desstatus = true;

    /**
     * 读取输出流
     */
    private BufferedReader br = null;

    /**
     * 输出类型
     */
    private String type = null;

    /**
     * 消息处理方法
     */
    private OutHandlerMethod ohm;

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setDesStatus(boolean desStatus) {
        this.desstatus = desStatus;
    }

    public OutHandler(InputStream is, String type, OutHandlerMethod ohm) {
        br = new BufferedReader(new InputStreamReader(is));
        this.type = type;
        this.ohm = ohm;
    }

    /**
     * 重写线程销毁方法，安全的关闭线程
     */
    @Override
    public void destroy() {
        setDesStatus(false);
    }

    /**
     * 执行输出线程
     */
    @Override
    public void run() {
        String msg = null;
        try {
            if (FFmpegManager.config.isDebug()) {
                logger.info(type + "<====>开始推流！");
                while (desstatus && (msg = br.readLine()) != null) {
                    ohm.parse(type, msg);
                }
            } else {
                Thread.yield();
            }
        } catch (IOException e) {
            logger.error("发生内部异常错误，自动关闭[" + this.getId() + "]线程 <===> " + "[" + type + "]任务名称");
            destroy();
        } finally {
            if (this.isAlive()) {
                destroy();
            }
        }
    }

}
