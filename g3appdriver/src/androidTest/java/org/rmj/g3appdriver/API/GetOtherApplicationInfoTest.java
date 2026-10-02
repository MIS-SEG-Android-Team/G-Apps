package org.rmj.g3appdriver.API;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.CreditApp.CreditApplication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class GetOtherApplicationInfoTest {
    private static final String TAG = GetOtherApplicationInfoTest.class.getSimpleName();

    private Context mContext;

    private CreditApplication poSys;

    private static boolean isSuccess = false;
    private String message;


    @Before
    public void setUp() throws Exception {
        this.mContext = ApplicationProvider.getApplicationContext();
        new GuanzonAppConfig(mContext).setTestCase(true);
        this.poSys = new CreditApplication(mContext);
    }

    @After
    public void tearDown() throws Exception {
        Log.d(TAG, "Test ended");
    }

    @Test
    public void test01LoginAccount() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "123456"), new AccountAuthentication.OnLoginCallback() {
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
        Thread.sleep(1000);

        assertTrue(isSuccess);
    }

    @Test
    public void test02GetOtherApplicationInfo() {
        String lsResult = poSys.GetOtherApplicationInfo();

        if(lsResult == null){
            message = poSys.getMessage();
            Log.e(TAG, message);
        } else {
            Log.d(TAG, lsResult);
            isSuccess = true;
        }

        assertTrue(isSuccess);
    }
}
