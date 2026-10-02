package org.rmj.g3appdriver.API;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ImportNotificationTest {

    private Context mContext;


    private static boolean isSuccess = false;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void test01LoginAccount() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSuccess = true;
            }

            @Override
            public void OnFailedLogin(String message) {
                isSuccess = false;
            }

            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        assertTrue(isSuccess);
    }

    @Test
    public void test02ImportNotifications() throws Exception{
        String lsAddress = "http://192.168.10.141/integsys/notification/request_previous_notifications.php";
        JSONObject params = new JSONObject();
        params.put("nLimitxxx", 0);
        String lsResponse = WebClient.httpsPostJSon(lsAddress, params.toString(), new HttpHeaders(mContext).getHeaders());
        if(lsResponse == null){
            isSuccess = false;
        } else {
            JSONObject loJson = new JSONObject(lsResponse);
            if(loJson.getString("result").equalsIgnoreCase("success")){
                JSONArray laJson = loJson.getJSONArray("detail");
                JSONObject loDetail = laJson.getJSONObject(0);
                String cRecdStat = loDetail.getString("cRecdStat");
                String dTimeStmp = loDetail.getString("dTimeStmp");
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        }
        assertTrue(isSuccess);
    }
}
