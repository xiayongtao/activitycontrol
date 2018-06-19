package com.happok.live.activitycontrol.ffmpeg.api;


import com.happok.live.activitycontrol.ffmpeg.config.FFmpegConfig;
import com.happok.live.activitycontrol.ffmpeg.config.FFmpegConfigStatic;
import com.happok.live.activitycontrol.ffmpeg.dao.TaskDao;
import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;
import com.happok.live.activitycontrol.ffmpeg.service.CommandAssembly;
import com.happok.live.activitycontrol.ffmpeg.service.TaskHandler;

import java.util.Collection;
import java.util.Map;

public interface FFmpegManager {

    public static FFmpegConfig config = FFmpegConfigStatic.getFFmpegConfig();

    public void setTaskDao(TaskDao taskDao);

    public void setTaskHandler(TaskHandler taskHandler);

    public void setCommandAssembly(CommandAssembly commandAssembly);

    public String start(String id, String command);

    public String start(String id, String commond, boolean hasPath);

    public String start(Map<String, String> assembly);

    public boolean stop(String id);

    public int stopAll();

    public TaskEntity query(String id);

    public Collection<TaskEntity> queryAll();

}
