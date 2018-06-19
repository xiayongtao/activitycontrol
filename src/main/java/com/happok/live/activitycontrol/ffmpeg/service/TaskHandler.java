package com.happok.live.activitycontrol.ffmpeg.service;


import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;

public interface TaskHandler {

    public TaskEntity process(String id, String command);

    public boolean stop(Process process);

    public boolean stop(Thread thread);

    public boolean stop(Process process, Thread thread);
}
