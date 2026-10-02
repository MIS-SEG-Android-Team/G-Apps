package org.rmj.g3appdriver.lib.Version;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.ServerRequest.APILocation;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.etc.Telephony;
import org.rmj.g3appdriver.lib.Account.AccountInfo;

public class AppVersion {
    private final AccountInfo loAccount;
    private final GuanzonAppConfig loConfig;
    private final Telephony poTelephony;
    private final ServerAPIs poApi;
    private final HttpHeaders poHeaders;
    private String message;

    public AppVersion(Context context){
        this.loAccount = new AccountInfo(context);
        this.loConfig = new GuanzonAppConfig(context);
        this.poTelephony = new Telephony(context);
        this.poApi = new ServerAPIs(loConfig.getTestCase());
        this.poHeaders = new HttpHeaders(context);
    }
    public String getMessage() {
        return message;
    }
    public boolean SubmitUserAppVersion(){
        try{
            JSONObject params = new JSONObject();
            params.put("sUserIDxx", loAccount.getUserID());
            params.put("sProdctID", loConfig.getProductID());
            params.put("sIMEINoxx", poTelephony.getDeviceID());
            params.put("sAppVersn", loConfig.getAppversionCode());

            String lsResponse = WebClient.httpsPostJSon(poApi.getUrlSubmitAppVersion(), params.toString(), poHeaders.getHeaders());
            if (lsResponse == null) {
                message = "Server no response";
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if (lsResult.equalsIgnoreCase("success")) {
                message = "User app version updated";
                return true;
            }else {
                JSONObject loError = loResponse.getJSONObject("error");
                message = loError.getString("message");
                return false;
            }
        } catch (Exception e){
            message = getLocalMessage(e);
            return false;
        }
    }
}
