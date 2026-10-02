package org.rmj.g3appdriver.API;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class UpdateClientMobile {
    private static final String TAG = UpdateClientMobile.class.getSimpleName();
    private Context mContext;

    private RClientInfo poClient;

    private static boolean isSuccess = false;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        new GuanzonAppConfig(mContext).setTestCase(true);
        poClient = new RClientInfo(mContext);

        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "123456"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                Log.e(TAG, message);
            }

            @Override
            public void OnFailedLogin(String fsMessage) {
                Log.e(TAG, fsMessage);
            }

            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        Thread.sleep(1000);
    }

    @Test
    public void test01UpdateClientMobile(){
        isSuccess = poClient.UpdateMobileNo("09270359402");
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
//        assertFalse(isSuccess);
    }

    @Test
    public void test02UpdateClientEmail(){
        isSuccess = poClient.UpdateEmailAddress("mikegarcia8748@gmail.com");
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
//        assertFalse(isSuccess);
    }

    @Test
    public void test03ImportClientEmail(){
        isSuccess = poClient.GetEmails();
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
    }

    @Test
    public void test04ImportClientMobile(){
        isSuccess = poClient.GetMobileNos();
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
    }

}
