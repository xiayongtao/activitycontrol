package com.happok.live.activitycontrol.ffmpeg.service;


import com.happok.live.activitycontrol.ffmpeg.api.FFmpegManager;
import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class TaskHandlerImpl implements TaskHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    private Runtime runtime = null;

    private OutHandlerMethod ohm = null;

    public TaskHandlerImpl(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    @Override
    public TaskEntity process(String id, String command) {
        Process process = null;
        OutHandler outHandler = null;
        TaskEntity tasker = null;
        try {
            if (runtime == null) {
                runtime = Runtime.getRuntime();
            }
            if (FFmpegManager.config.isDebug()) {
                logger.debug("执行命令：" + command);
            }

            process = runtime.exec(command);// 执行本地命令获取任务主进程
            outHandler = new OutHandler(process.getErrorStream(), id, this.ohm);
            outHandler.start();
            tasker = new TaskEntity(id, process, outHandler);
        } catch (IOException e) {
            if (FFmpegManager.config.isDebug()) {
                logger.debug("执行命令失败！正在停止进程和输出线程... error:" + e.toString());
            }
            stop(outHandler);
            stop(process);
            // 出现异常说明开启失败，返回null
            return null;
        }
        return tasker;
    }

    @Override
    public boolean stop(Process process) {
        if (process != null) {
            process.destroy();
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean stop(Thread outHandler) {
        if (outHandler != null && outHandler.isAlive()) {
            outHandler.stop();
            //outHandler.destroy();
            return true;
        }
        return false;
    }

    @Override
    public boolean stop(Process process, Thread thread) {
        boolean ret;
        ret = stop(thread);
        ret = stop(process);
        return ret;
    }
}
