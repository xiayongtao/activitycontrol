package com.happok.live.activitycontrol.ffmpeg.util;

import java.io.File;
import java.util.UUID;

public class CommonUtil {

    public static final String rootPath = getProjectRootPath();
    public static final String TRUE = "true";
    public static final String NULL_STRING = "";
    public static final String H_LINE = "-";

    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll(H_LINE, NULL_STRING);
    }

    public static boolean isNull(String str) {
        return str == null || NULL_STRING.equals(str.trim());
    }

    public static boolean isTrue(String str) {
        return TRUE.equals(str) ? true : false;
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getProjectRootPath() {
        String path = null;
        try {
            path = CommonUtil.class.getResource("/").getPath();
        } catch (Exception e) {
            File directory = new File(NULL_STRING);
            path = directory.getAbsolutePath() + File.separator;
        }
        return path;
    }

    public static String getClassPath(Class<?> cla) {
        return cla.getResource("").getPath();
    }

}
