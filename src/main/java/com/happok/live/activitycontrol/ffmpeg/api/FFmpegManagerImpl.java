package com.happok.live.activitycontrol.ffmpeg.api;


import com.happok.live.activitycontrol.ffmpeg.config.FFmpegConfig;
import com.happok.live.activitycontrol.ffmpeg.dao.TaskDao;
import com.happok.live.activitycontrol.ffmpeg.dao.TaskDaoImpl;
import com.happok.live.activitycontrol.ffmpeg.entity.TaskEntity;
import com.happok.live.activitycontrol.ffmpeg.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Repository
public class FFmpegManagerImpl implements FFmpegManager {

    @Autowired
    private FFmpegConfig fFmpegConfig = null;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private TaskDao taskDao = null;
    private TaskHandler taskHandler = null;
    private CommandAssembly commandAssembly = null;
    private OutHandlerMethod ohm = null;


    public FFmpegManagerImpl() {
        init(100);
    }

    public FFmpegManagerImpl(CommandAssembly commandAssembly) {
        this.commandAssembly = commandAssembly;
    }

    public FFmpegManagerImpl(Integer size) {
        init(size);
    }

    public void init(Integer size) {

        if (size == null) {
            size = config.getSize();
        }
        if (this.ohm == null) {
            this.ohm = new DefaultOutHandlerMethod();
        }
        if (this.taskDao == null) {
            this.taskDao = new TaskDaoImpl(size);
        }
        if (this.taskHandler == null) {
            this.taskHandler = new TaskHandlerImpl(this.ohm);
        }
        if (this.commandAssembly == null) {
            this.commandAssembly = new CommandAssemblyImpl();
        }
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void setTaskHandler(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    public void setCommandAssembly(CommandAssembly commandAssembly) {
        this.commandAssembly = commandAssembly;
    }

    public void setOhm(OutHandlerMethod ohm) {
        this.ohm = ohm;
    }

    public boolean isInit(boolean b) {
        boolean ret = this.ohm == null || this.taskDao == null || this.taskHandler == null || this.commandAssembly == null;
        if (ret && b) {
            init(null);
        }
        return ret;
    }

    @Override
    public String start(String id, String command) {
        return start(id, command, false);
    }

    @Override
    public String start(String id, String command, boolean hasPath) {
        if (isInit(true)) {
            logger.error("执行失败，未进行初始化或初始化失败！");
            return null;
        }
        if (id != null && command != null) {
            TaskEntity tasker = taskHandler.process(id, hasPath ? command : config.getPath() + command);
            if (tasker != null) {
                int ret = taskDao.add(tasker);
                if (ret > 0) {
                    return tasker.getId();
                } else {
                    // 持久化信息失败，停止处理
                    taskHandler.stop(tasker.getProcess(), tasker.getThread());
                    if (config.isDebug()) {
                        logger.debug("持久化失败，停止任务！");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String start(Map<String, String> assembly) {
        // ffmpeg环境是否配置正确

        // 参数是否符合要求
        if (assembly == null || assembly.isEmpty() || !assembly.containsKey("appName")) {
            logger.error("参数不正确，无法执行");
            return null;
        }
        String appName = (String) assembly.get("appName");
        if (appName != null && "".equals(appName.trim())) {
            logger.error("appName不能为空");
            return null;
        }
        assembly.put("ffmpegPath", config.getPath() + "ffmpeg");
        String command = commandAssembly.assembly(assembly);
        if (command != null) {
            return start(appName, command, true);
        }

        return null;
    }

    @Override
    public boolean stop(String id) {
        if (id != null && taskDao.isHave(id)) {
            if (config.isDebug()) {
                logger.debug("正在停止任务：" + id);
            }

            TaskEntity tasker = taskDao.get(id);
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(id);
                return true;
            }
        }

        logger.error("停止任务失败！id=" + id);
        return false;
    }

    @Override
    public int stopAll() {
        Collection<TaskEntity> list = taskDao.getAll();
        Iterator<TaskEntity> iter = list.iterator();
        TaskEntity tasker = null;
        int index = 0;
        while (iter.hasNext()) {
            tasker = iter.next();
            if (taskHandler.stop(tasker.getProcess(), tasker.getThread())) {
                taskDao.remove(tasker.getId());
                index++;
            }
        }
        if (config.isDebug()) {
            logger.debug("停止了" + index + "个任务！");
        }

        return index;
    }

    @Override
    public TaskEntity query(String id) {
        return taskDao.get(id);
    }

    @Override
    public Collection<TaskEntity> queryAll() {
        return taskDao.getAll();
    }

}
