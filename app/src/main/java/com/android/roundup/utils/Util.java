package com.android.roundup.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static final String TAG = "Util";

    public static String getVideoIdFromYoutubeUrl(String url) {
        String videoId = null;
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}

