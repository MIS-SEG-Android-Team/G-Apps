package org.rmj.g3appdriver.etc;

import android.content.Context;
import android.content.SharedPreferences;

public class GuanzonAppConfig {
    private static final String TAG = GuanzonAppConfig.class.getSimpleName();

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private static final String ACCOUNT_CREDENTIALS = "GApp_Config";

    private static final String TEST_CASE = "cTestStat";

    private static final String PRODUCT_ID = "sProdctID";
    private static final String CLIENT_ID = "sClientID";
    private static final String FIRST_LAUNCH = "sFrstlnch";
    private static final String MESSAGING_TOKEN = "sMsgToken";
    private static final String PERMISSIONS_GRANT = "cPrmnGrnt";
    private static final String APPVERSION_CODE = "sVsrnCd";
    private static final String APPVERSION_NAME = "sVsrnNm";

    public GuanzonAppConfig(Context context){
        pref = context.getSharedPreferences(ACCOUNT_CREDENTIALS, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     *
     * @param val set if All Class that will access
     *            API will be using Local Server for Testing
     *            or Live Data for Production.
     *            True = Local Testing
     *            False = Live Data
     *            Default value: False
     */
    public void setTestCase(boolean val){
        editor.putBoolean(TEST_CASE, val);
        editor.commit();
    }

    public boolean getTestCase(){
        boolean isTest = pref.getBoolean(TEST_CASE, false);
        return isTest;
    }

    public void setIfPermissionsGranted(boolean val){
        editor.putBoolean(PERMISSIONS_GRANT, val);
        editor.commit();
    }

    public boolean IsPermissionsGranted(){
        return pref.getBoolean(PERMISSIONS_GRANT, false);
    }

    public void setAppToken(String val){
        editor.putString(MESSAGING_TOKEN, val);
        editor.commit();
    }

    public String getAppToken(){
        return pref.getString(MESSAGING_TOKEN, "");
    }

    public void setFirstLaunch(boolean fbVal){
        editor.putBoolean(FIRST_LAUNCH, fbVal);
        editor.commit();
    }

    public boolean isAppFirstLaunch(){
        return pref.getBoolean(FIRST_LAUNCH, true);
    }

    public void setProductID(String fsVal){
        editor.putString(PRODUCT_ID, fsVal);
        editor.commit();
    }

    public String getProductID(){
        return pref.getString(PRODUCT_ID, "");
    }

    public void setClientID(String fsVal){
        editor.putString(CLIENT_ID, fsVal);
        editor.commit();
    }

    public String getClientID(){
        return pref.getString(CLIENT_ID, "");
    }
    public void setAppversionCode(String appversionCode){
        editor.putString(APPVERSION_CODE, appversionCode);
        editor.commit();
    }
    public String getAppversionCode(){
        return pref.getString(APPVERSION_CODE, "");
    }
    public void setAppversionName(String appversionName){
        editor.putString(APPVERSION_NAME, appversionName);
        editor.commit();
    }
    public String getAppversionName(){
        return pref.getString(APPVERSION_NAME, "");
    }
}
