package com.android.roundup.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static final String TAG = "Util";

    private SharedPreferences preferences;
    private static String LOGIN_PREF = "login_pref";
    public static String IS_LOGGED_IN = "is_logged_in";

    /**
     * Get id from youtube url
     *
     * @param url
     * @return
     */
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

    /**
     * Save data in shared pref
     *
     * @param context
     * @param key
     * @param isLoggedIn
     */
    public static void saveLoginData(Context context, String key, boolean isLoggedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isLoggedIn);
        editor.apply();
    }

    /**
     * Get data from shared pref
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getLoginData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

}

