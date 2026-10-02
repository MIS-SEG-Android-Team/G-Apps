package org.rmj.g3appdriver.API;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class DownloadPurchasesTest {

    private Context mContext;

    private static boolean isSuccess = false;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void test01LoginAccount() throws Exception{
        new GuanzonAppConfig(mContext).setTestCase(true);
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSuccess = true;
                try {
                    Thread.sleep(1000);
                    new RClientInfo(mContext).ImportAccountInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
    public void test02DownloadPurchases() throws Exception{
        String lsAddress = "http://192.168.10.141/integsys/marketplace/download_place_orders.php";

        String lsResponse = WebClient.httpsPostJSon(lsAddress, new JSONObject().toString(), new HttpHeaders(mContext).getHeaders());
        if(lsResponse == null){
            isSuccess = false;
        }
        assertTrue(isSuccess);
    }
}
