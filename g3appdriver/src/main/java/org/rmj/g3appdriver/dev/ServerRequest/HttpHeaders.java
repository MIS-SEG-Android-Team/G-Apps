/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.g3appdriver.dev.ServerRequest;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.etc.SecUtil;
import org.rmj.g3appdriver.etc.Telephony;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.utils.SQLUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    private final Telephony poTlphony;
    private final GuanzonAppConfig poConfigx;
    private final AccountInfo poAccount;

    public HttpHeaders(Context context){
        this.poTlphony = new Telephony(context);
        this.poConfigx = new GuanzonAppConfig(context);
        this.poAccount = new AccountInfo(context);
    }

    private Map<String, String> initHttpHeaders() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Calendar calendar = Calendar.getInstance();

        //TODO: Change Values on user testing and production
        String lsUserIDx = poAccount.getUserID();
        String lsClientx = poAccount.getClientID();
        String lsLogNoxx = "";
        String lsTokenxx = poConfigx.getAppToken();
        String lsProduct = "GuanzonApp";
        String lsDevcIDx = poTlphony.getDeviceID();
        String lsDateTme = SQLUtil.dateFormat(calendar.getTime(), "yyyyMMddHHmmss");
        String lsDevcMdl = Build.MODEL;
        String lsMobileN = poAccount.getMobileNo();

        if(lsTokenxx.isEmpty()){
            lsTokenxx = "fI3zDqZDSqq71gpLK_zZye:APA91bHyFYFkvmYMAi5DxmxriXzZOsIlk2TtFacdzv4PcQIw23fxHR80TRtpAluFEIuNLXcon0Ee6nP1R9LQq7WA256viZdfjA9pOSrlxW-qZKGI763d54H5d5OPgpkj6oc79e2Ejq1h";
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("g-api-id", lsProduct);
        headers.put("g-api-client", lsClientx);
        headers.put("g-api-imei", lsDevcIDx);
        headers.put("g-api-model", lsDevcMdl);
        headers.put("g-api-mobile", lsMobileN);
        headers.put("g-api-token", lsTokenxx);
        headers.put("g-api-user", lsUserIDx);
        headers.put("g-api-key", lsDateTme);
        String hash_toLower = SecUtil.md5Hex(headers.get("g-api-imei") + headers.get("g-api-key"));
        hash_toLower = hash_toLower.toLowerCase();
        headers.put("g-api-hash", hash_toLower);
        headers.put("g-api-log", lsLogNoxx);
        return headers;
    }

    public HashMap<String, String> getHeaders(){
        return (HashMap<String, String>) initHttpHeaders();
    }
}
