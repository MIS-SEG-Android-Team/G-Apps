package org.rmj.g3appdriver.Account;

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
import org.rmj.g3appdriver.lib.Account.AccountVerification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class UpdateVerificationTest {
    private static final String TAG = UpdateVerificationTest.class.getSimpleName();
    private Context mContext;

    private AccountVerification poClient;

    private static boolean isSuccess = false;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        new GuanzonAppConfig(mContext).setTestCase(true);
        poClient = new AccountVerification(mContext);

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
    }

    @Test
    public void test01VerifyEmail() throws Exception{
        Thread.sleep(1000);
        isSuccess = poClient.VerifyEmailAddress("mikegarcia8748@gmail.com", "486254");
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
//        assertFalse(isSuccess);
    }

    @Test
    public void test01VerifyMobileNo() throws Exception{
        Thread.sleep(1000);
        isSuccess = poClient.VerifyMobileNo("09270359402", "088407");
        if(!isSuccess){
            Log.e(TAG, poClient.getMessage());
        }
        assertTrue(isSuccess);
//        assertFalse(isSuccess);
    }
}
