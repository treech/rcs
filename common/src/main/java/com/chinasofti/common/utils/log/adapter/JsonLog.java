package com.chinasofti.common.utils.log.adapter;

import android.util.Log;

import com.chinasofti.common.utils.log.YLog;
import com.chinasofti.common.utils.log.YLogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(YLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(YLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        YLogUtil.printLine(tag, true);
        message = headString + YLog.LINE_SEPARATOR + message;
        String[] lines = message.split(YLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        YLogUtil.printLine(tag, false);
    }
}
