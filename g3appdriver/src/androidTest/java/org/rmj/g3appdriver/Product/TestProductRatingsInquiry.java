package org.rmj.g3appdriver.Product;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestProductRatingsInquiry {

    private Context mContext;
    private RProduct poProdct;

    private static boolean isSuccess = false;
    private String message;

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poProdct = new RProduct(mContext);

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
    public void test01SendProdctInquiry() throws Exception{
        isSuccess = poProdct.SendProductInquiry("C00122000006", "May ibang variant po bang available?");
        assertTrue(isSuccess);
    }

    @Test
    public void test02SendProdctInquiry() throws Exception{
        isSuccess = poProdct.SendProductReview("","C00122000006", 2, "Walang bubble wrap ung item");
        assertTrue(isSuccess);
    }
}