package com.happok.live.activitycontrol.ffmpeg.util;

import com.happok.live.activitycontrol.ffmpeg.api.FFmpegManager;
import com.happok.live.activitycontrol.ffmpeg.config.FFmpegConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class FormatAnalysis {

    private static String fileSize;
    private static FFmpegConfig config = FFmpegManager.config;
    private static Logger LogUtil = LoggerFactory.getLogger(FormatAnalysis.class);

    public static String getVideoFormat(String input) {

        return FFprobeCommond(input, "v:0", null);
    }

    public static String getAudioFormat(String input) {
        return FFprobeCommond(input, "a:0", null);
    }

    public static boolean IsH264(String input) {
        if (FFprobeCommond(input, "v:0", null).toLowerCase() == "h264") {
            return true;
        }
        return false;
    }

    public static boolean IsAAC(String input) {
        if (FFprobeCommond(input, "a:0", null).toLowerCase() == "aac") {
            return true;
        }
        return false;
    }


    private static String execCmdString(List<String> commands) {
        fileSize = null;
        try {
            StringBuffer sb = new StringBuffer();
            for (String tmp : commands) {
                sb.append(tmp + " ");
            }


            final Process process = Runtime.getRuntime().exec(sb.toString());
            LogUtil.info("start run cmd=" + sb.toString());

            //处理InputStream的线程
            new Thread() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;

                    try {
                        while ((line = in.readLine()) != null) {
                            if (line.contains("Duration:")) {
                                LogUtil.info("Duration: " + line);
                                String[] tmp = line.split(",");
                                if (tmp.length > 0) {
                                    String Duration = tmp[0];
                                    fileSize = Duration.replace("Duration: ", "");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null;

                    try {
                        while ((line = err.readLine()) != null) {

                            if (line.contains("Duration:")) {
                                LogUtil.info("Duration: " + line);
                                String[] tmp = line.split(",");
                                if (tmp.length > 0) {
                                    String Duration = tmp[0];
                                    fileSize = Duration.replace("Duration: ", "");
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            process.waitFor();
            LogUtil.info("finish run cmd=" + sb.toString());
            return fileSize;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static boolean execCmd(List<String> commands) {

        try {
            StringBuffer sb = new StringBuffer();
            for (String tmp : commands) {
                sb.append(tmp + " ");
            }


            final Process process = Runtime.getRuntime().exec(sb.toString());
            LogUtil.info("start run cmd=" + sb.toString());

            //处理InputStream的线程
            new Thread() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;

                    try {
                        while ((line = in.readLine()) != null) {
                            LogUtil.info("output: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null;

                    try {
                        while ((line = err.readLine()) != null) {
                            LogUtil.info("err: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            process.waitFor();
            LogUtil.info("finish run cmd=" + sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileDuration(String mp4File) {
        String basePath = config.getPath();
        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "ffprobe");
        commands.add(mp4File);

        return execCmdString(commands);
    }

    private static String flv2Mp4(String input) {

        String file = input.substring(0, input.lastIndexOf("."));

        String output = file + ".mp4";
        String basePath = config.getPath();
        List<String> commands = new ArrayList<String>();
        commands.add(basePath + "ffmpeg");
        commands.add(" -i ");
        commands.add(input);
        commands.add(" -vcodec copy -acodec copy ");
        commands.add(" -y ");
        commands.add(output);

        if (execCmd(commands)) {
            return output;
        }

        return null;
    }

    public static String MP4BoxCommond(String input) {
        List<String> commands = new ArrayList<String>();
        String basePath = config.getPath();

        String output = flv2Mp4(input);
        if (null != output) {
            commands.add(basePath + "MP4Box");
            commands.add(" -isma ");
            commands.add(output);
            execCmd(commands);
        }

        return output;
    }


    private static String FFprobeCommond(String input, String streamIdex, String filter) {

        String line = null;
        StringBuilder comm = new StringBuilder(config.getPath() + " -show_streams -select_streams ");
        filter = (filter == null ? "codec_name" : filter);

        comm.append(streamIdex);
        comm.append(input);


        StringBuilder sb = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(comm.toString());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream()));


            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
                if (line.contains(filter)) {
                    System.out.println(line);
                    return line;
                }
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        return null;
    }
}
