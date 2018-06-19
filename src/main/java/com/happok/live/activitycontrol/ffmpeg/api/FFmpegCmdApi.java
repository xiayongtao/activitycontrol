package com.happok.live.activitycontrol.ffmpeg.api;


import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;
import com.happok.live.activitycontrol.ffmpeg.service.*;

import java.util.Collection;
import java.util.Map;

public class FFmpegCmdApi {


    private FFmpegManager fFmpegManager = new FFmpegManagerImpl();
    private CommandAssembly commandAssembly = null;
    private String type;


    public String getType() {
        return type;
    }

    public FFmpegCmdApi(String type) {
        this.type = type;

        switch (type) {
            case "live": {

                commandAssembly = new CommandAssemblyLive();
                fFmpegManager.setCommandAssembly(commandAssembly);
                break;
            }
            case "record": {

                commandAssembly = new CommandAssemblyRecord();
                fFmpegManager.setCommandAssembly(commandAssembly);
                break;
            }
            case "shot": {

                commandAssembly = new CommandAssemblyShot();
                fFmpegManager.setCommandAssembly(commandAssembly);
                break;
            }
            case "toMp4": {

                commandAssembly = new CommandAssembly2Mp4();
                fFmpegManager.setCommandAssembly(commandAssembly);
                break;
            }
            default:
                break;
        }
    }

    public String start(String id, String command) {
        return fFmpegManager.start(id, command);
    }

    public String start(String id, String commond, boolean hasPath) {
        return fFmpegManager.start(id, commond, hasPath);
    }

    public String start(Map<String, String> assembly) {
        return fFmpegManager.start(assembly);
    }

    public boolean stop(String id) {
        return fFmpegManager.stop(id);
    }

    public int stopAll() {
        return fFmpegManager.stopAll();
    }

    public TaskEntity query(String id) {
        return fFmpegManager.query(id);
    }

    public Collection<TaskEntity> queryAll() {
        return fFmpegManager.queryAll();
    }
}
