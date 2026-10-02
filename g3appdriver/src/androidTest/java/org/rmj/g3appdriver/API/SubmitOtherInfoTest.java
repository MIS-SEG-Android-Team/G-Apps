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
import org.rmj.g3appdriver.lib.Account.AccountVerification;
import org.rmj.g3appdriver.lib.CreditApp.model.MpCreditApp;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SubmitOtherInfoTest {
    private static final String TAG = SubmitMeansInfoTest.class.getSimpleName();

    private Context mContext;

    private AccountVerification poSys;

    private static boolean isSuccess = false;
    private String message;

    @Before
    public void setUp() throws Exception {
        this.mContext = ApplicationProvider.getApplicationContext();
        new GuanzonAppConfig(mContext).setTestCase(true);
        this.poSys = new AccountVerification(mContext);
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
    }

    @Test
    public void test02SubmitOtherInfo() throws Exception{
        MpCreditApp loApp = new MpCreditApp();
        loApp.disbursementInfo().bankInfo().setBankName("BDO");
        loApp.disbursementInfo().bankInfo().setAccountType("0");
        if(!poSys.SubmitOtherInfo(loApp.getData())){
            message = poSys.getMessage();
            Log.e(TAG, message);
        } else {
            isSuccess = true;
        }
        assertTrue(isSuccess);
    }
}
