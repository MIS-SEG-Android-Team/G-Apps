/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver.dev.Repositories;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.apprdiver.util.SQLUtil;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DMcBrand;
import org.rmj.g3appdriver.dev.Database.Entities.EMcBrand;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;

import java.util.Date;
import java.util.List;

public class RMcBrand {
    private static final String TAG = RMcBrand.class.getSimpleName();

    private final DMcBrand poDao;

    private final ServerAPIs poApi;
    private final HttpHeaders poHeaders;
    private final GuanzonAppConfig poConfig;

    private String message;

    public RMcBrand(Context instance){
        poDao = GGC_GuanzonAppDB.getInstance(instance).McBrandDao();
        this.poConfig = new GuanzonAppConfig(instance);
        this.poApi = new ServerAPIs(poConfig.getTestCase());
        this.poHeaders = new HttpHeaders(instance);
    }

    public String getMessage() {
        return message;
    }

    public void insertBulkData(List<EMcBrand> brandList){
        poDao.insertBulkData(brandList);
    }

    public String getLatestDataTime(){
        return poDao.getLatestDataTime();
    }

    public boolean ImportMCBrands(){
        try{
            JSONObject params = new JSONObject();
            params.put("bsearch", true);
            params.put("descript", "All");

            EMcBrand loObj = poDao.GetLatestBrandInfo();
            if(loObj != null){
                params.put("timestamp", loObj.getTimeStmp());
            }

            String lsResponse = WebClient.httpsPostJSon(
                    poApi.getUrlImportBrand(),
                    params.toString(),
                    poHeaders.getHeaders());
            if(lsResponse == null){
                message =  "Server no response while importing response.";;
                return false;
            }

            Log.d(TAG, lsResponse);
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if(lsResult.equalsIgnoreCase("error")){
                JSONObject loError = loResponse.getJSONObject("error");
                message = AppConstants.getErrorMessage(loError);
                return false;
            }

            JSONArray laJson = loResponse.getJSONArray("detail");
            for(int x = 0 ; x < laJson.length(); x++){
                JSONObject loJson = laJson.getJSONObject(x);
                EMcBrand loDetail = poDao.getMcBrandInfo(loJson.getString("sBrandIDx"));

                if(loDetail == null){

                    if(loJson.getString("cRecdStat").equalsIgnoreCase("1")){
                        EMcBrand loBrand = new EMcBrand();
                        loBrand.setBrandIDx(loJson.getString("sBrandIDx"));
                        loBrand.setBrandNme(loJson.getString("sBrandNme"));
                        loBrand.setRecdStat(loJson.getString("cRecdStat"));
                        loBrand.setTimeStmp(loJson.getString("dTimeStmp"));
                        poDao.insert(loBrand);
                        Log.d(TAG, "MC brand info has been saved.");
                    }

                } else {
                    Date ldDate1 = SQLUtil.toDate(loDetail.getTimeStmp(), SQLUtil.FORMAT_TIMESTAMP);
                    Date ldDate2 = SQLUtil.toDate((String) loJson.get("dTimeStmp"), SQLUtil.FORMAT_TIMESTAMP);
                    if (!ldDate1.equals(ldDate2)) {
                        loDetail.setBrandIDx(loJson.getString("sBrandIDx"));
                        loDetail.setBrandNme(loJson.getString("sBrandNme"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        poDao.update(loDetail);
                        Log.d(TAG, "MC brand info has been updated.");
                    }
                }
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = getLocalMessage(e);
            return false;
        }
    }

    public String getBrandID(String sBrandNme){
        return poDao.GetBrandID(sBrandNme);
    }
}
