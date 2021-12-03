package me.einjojo.survivalengine.util;

public class TextUtil {

    public static String getTimeString(long time) {
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;

        return  String.format("%02d:%02d:%02d", hour, minute, second);

    }

}
