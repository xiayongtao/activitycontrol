package com.happok.live.activitycontrol.ffmpeg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommandAssemblyRecord implements CommandAssembly {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String assembly(Map<String, String> paramMap) {
        try {
            if (paramMap.containsKey("ffmpegPath")) {
                String ffmpegPath = (String) paramMap.get("ffmpegPath");

                StringBuilder comm = new StringBuilder(ffmpegPath + " -i ");

                if (paramMap.containsKey("input")
                        && paramMap.containsKey("output")
                        && paramMap.containsKey("appName")) {

                    String input = (String) paramMap.get("input");
                    String output = (String) paramMap.get("output");
                    String appName = (String) paramMap.get("appName");


                    comm.append(input);
                    if (paramMap.containsKey("vcodec")) {
                        String vcodec = (String) paramMap.get("vcodec");
                        comm.append(" -vcodec " + vcodec);
                    } else {
                        comm.append(" -vcodec copy ");
                    }

                    if (paramMap.containsKey("acodec")) {
                        String acodec = (String) paramMap.get("acodec");
                        comm.append(" -acodec " + acodec);
                    } else {
                        comm.append(" -acodec copy ");
                    }

                    if (paramMap.containsKey("rs")) {
                        String rs = (String) paramMap.get("rs");
                        comm.append(" -s " + rs);
                    }

                    if (paramMap.containsKey("fps")) {
                        String fps = (String) paramMap.get("fps");
                        comm.append(" -r " + fps);
                        comm.append(" -g " + fps);
                    }
                    comm.append(" " + output + "/" + appName);

                    if (paramMap.containsKey("fmt")) {

                        String fmtp = (String) paramMap.get("fmt");
                        comm.append("." + fmtp);
                    }


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
