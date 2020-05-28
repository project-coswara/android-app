package com.project.coswara.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.project.coswara.BuildConfig;
import com.project.coswara.model.HealthData;
import com.project.coswara.model.Metadata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.project.coswara.util.Constants.REC_COMPLETED_PREFS;
import static com.project.coswara.util.Constants.REC_TRACK_COMPLETION_PREFS;

public class Utils {
    //date string - UTC
    public static String getDateString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    public static String getISODate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    public static void storeHashMap(Context context, HashMap<String, Object> map, String strKey) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.HASHMAP_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.putString(strKey, gson.toJson(map, type));
        editor.apply();
    }

    public static HashMap<String, Object> getStoredHashMap(Context context, String strKey) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();

        SharedPreferences prefs = context.getSharedPreferences(Constants.HASHMAP_PREFS, Context.MODE_PRIVATE);
        String jsonStr = prefs.getString(strKey, "");
        if (!jsonStr.isEmpty()) return gson.fromJson(jsonStr, type);

        return null;
    }

    public static void storeMetadata(Context context, Metadata obj) {
        Gson gson = new Gson();
        Type type = new TypeToken<Metadata>() {
        }.getType();

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.METADATA_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.putString("metadata_json", gson.toJson(obj, type));
        editor.apply();
    }

    public static Metadata getStoredMetadata(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.METADATA_PREFS,
                Context.MODE_PRIVATE);

        if (preferences.contains("metadata_json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Metadata>() {
            }.getType();

            String json = preferences.getString("metadata_json", "");
            return gson.fromJson(json, type);
        }

        return null;
    }

    public static void clearMetadata(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.METADATA_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static void updateRecCompletion(Context context, int completedPos, int totalLength) {
        Gson gson = new Gson();
        Type type = new TypeToken<boolean[]>() {
        }.getType();

        boolean[] storedArr = getRecCompletion(context, totalLength);

        SharedPreferences.Editor editor = context.getSharedPreferences(REC_TRACK_COMPLETION_PREFS,
                Context.MODE_PRIVATE).edit();
        storedArr[completedPos] = true; //update completed position
        editor.putString("completion_arr", gson.toJson(storedArr, type));
        editor.apply();
    }

    public static boolean[] getRecCompletion(Context context, int totalLength) {
        Gson gson = new Gson();
        Type type = new TypeToken<boolean[]>() {
        }.getType();

        SharedPreferences prefs = context.getSharedPreferences(REC_TRACK_COMPLETION_PREFS,
                Context.MODE_PRIVATE);

        String jsonStr = prefs.getString("completion_arr", "");
        return !jsonStr.isEmpty() ? (boolean[]) gson.fromJson(jsonStr, type) : new boolean[totalLength];
    }

    public static void clearRecCompletion(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REC_TRACK_COMPLETION_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static void storeHealthData(Context context, HealthData obj) {
        Gson gson = new Gson();
        Type type = new TypeToken<HealthData>() {
        }.getType();

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.HEALTH_DATA_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.putString("health_data_json", gson.toJson(obj, type));
        editor.apply();
    }

    public static HealthData getStoredHealthData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.HEALTH_DATA_PREFS,
                Context.MODE_PRIVATE);

        if (preferences.contains("health_data_json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<HealthData>() {
            }.getType();

            String json = preferences.getString("health_data_json", "");
            return gson.fromJson(json, type);
        }

        return null;
    }

    public static void clearHealthData(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.HEALTH_DATA_PREFS,
                Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isDebugMode(Context context){
        return BuildConfig.DEBUG;
    }

    public static void updateLanguage(Context context, String lang){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.LANGUAGE_PREFS, Context.MODE_PRIVATE).edit();
        editor.putString("displayed_lang", lang);
        editor.apply();
    }

    public static String getDisplayLang(Context context, String defaultLang){
        SharedPreferences prefs = context.getSharedPreferences(Constants.LANGUAGE_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("displayed_lang", defaultLang);
    }

    public static void updateBooleanFlag(Context context, boolean val, String type) {
        SharedPreferences.Editor editor = null;

        switch (type) {
            case "details":
                editor = context.getSharedPreferences(Constants.DETAILS_PREFS,
                        Context.MODE_PRIVATE).edit();
                break;
            case "rec_state":
                editor = context.getSharedPreferences(REC_COMPLETED_PREFS,
                        Context.MODE_PRIVATE).edit();
                break;
            case "feedback":
                editor = context.getSharedPreferences(Constants.FEEDBACK_PREFS,
                        Context.MODE_PRIVATE).edit();
                break;
        }

        if (editor != null) {
            editor.putBoolean("submitted", val);
            editor.apply();
        }
    }

    public static boolean isSubmitted(Context context, String type) {
        SharedPreferences prefs = null;

        switch (type) {
            case "details":
                prefs = context.getSharedPreferences(Constants.DETAILS_PREFS,
                        Context.MODE_PRIVATE);
                break;
            case "rec_state":
                prefs = context.getSharedPreferences(REC_COMPLETED_PREFS,
                        Context.MODE_PRIVATE);
                break;
            case "feedback":
                prefs = context.getSharedPreferences(Constants.FEEDBACK_PREFS,
                        Context.MODE_PRIVATE);
                break;
        }

        if (prefs != null) {
            return prefs.getBoolean("submitted", false);
        }

        return false;
    }

    public static void clearAllPrefs(Context context) {
        String[] prefKeys = { Constants.METADATA_PREFS, Constants.HEALTH_DATA_PREFS,
                Constants.DETAILS_PREFS, Constants.HASHMAP_PREFS, REC_COMPLETED_PREFS,
                REC_TRACK_COMPLETION_PREFS, Constants.FEEDBACK_PREFS };

        for(String key : prefKeys){
            SharedPreferences.Editor editor = context.getSharedPreferences(key, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
        }
    }

    public static boolean validString(String s) {
        return s != null && !s.isEmpty();
    }

    public static int getArrayPos(String[] arr, String val, int defaultVal) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase(val)) return i;
        }

        return defaultVal;
    }

    public static int getFirstMatchingPos(boolean[] arr, boolean match, int defaultVal) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == match) return i;
        }
        return defaultVal;
    }

    public static void openInBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void enableLinksInText(TextView textView, String content){
        textView.setText(HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY));
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
