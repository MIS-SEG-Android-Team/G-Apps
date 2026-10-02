package org.rmj.g3appdriver.Product;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestImportPurchases {

    private Context mContext;

    private static boolean isSuccess = false;
    private String message;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();

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
    }

    @Test
    public void test01ImportPurchases() throws Exception{
        ROrder loOrder = new ROrder(mContext);
        if(loOrder.ImportPurchases()){
            isSuccess = true;
        } else {
            message = loOrder.getMessage();
        }

        assertTrue(isSuccess);
        assertNull(message);
    }
}
