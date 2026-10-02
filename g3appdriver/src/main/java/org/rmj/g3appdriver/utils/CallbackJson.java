package org.rmj.g3appdriver.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class CallbackJson {

    public static String parse(CallbackStatus foCStatus, String fsMessage) {
        JSONObject loJson = new JSONObject();
        try {
            loJson.put("status", foCStatus);
            loJson.put("message", fsMessage);

            return loJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();

            return loJson.toString();
        }
    }

    public enum CallbackStatus {
        SUCCESS,
        FAILED
    }

}
