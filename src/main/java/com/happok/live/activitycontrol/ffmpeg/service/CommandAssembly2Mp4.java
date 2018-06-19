package com.happok.live.activitycontrol.ffmpeg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommandAssembly2Mp4 implements CommandAssembly {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String assembly(Map<String, String> paramMap) {
        try {
            if (paramMap.containsKey("ffmpegPath")) {
                String ffmpegPath = (String) paramMap.get("ffmpegPath");

                StringBuilder comm = new StringBuilder(ffmpegPath + " -y -i ");

                if (paramMap.containsKey("input")
                        && paramMap.containsKey("output")
                        && paramMap.containsKey("appName")) {

                    String input = (String) paramMap.get("input");
                    String output = (String) paramMap.get("output");
                    String appName = (String) paramMap.get("appName");

                    comm.append(input);
                    comm.append(" -vcodec copy ");
                    comm.append(" -acodec copy ");
                    comm.append(" " + output + "/" + appName);


                    String rest = comm.toString();

                    logger.info("cmd:" + rest);
                    return rest;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
