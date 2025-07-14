package org.rmj.g3appdriver.Product;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestAddToCartItem {

    private Context mContext;
    private RProduct poProdct;
    private ROrder poOrders;

    private List<EProducts> loList;
    private boolean isSuccess = false;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poProdct = new RProduct(mContext);
        poOrders = new ROrder(mContext);
        new GuanzonAppConfig(mContext).setTestCase(true);
    }

    @Test
    public void test01Login() throws Exception{
        new GuanzonAppConfig(mContext).setTestCase(true);
        new AccountAuthentication(mContext).
                LoginAccount(
                        new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "12345678"),
                        new AccountAuthentication.OnLoginCallback() {
                            @Override
                            public void OnSuccessLogin(String message) {
                                new RClientInfo(mContext).ImportAccountInfo();
                            }

                            @Override
                            public void OnFailedLogin(String message) {

                            }
                            @Override
                            public void OnAccountVerification(String args, String args1) {

                            }
                        });
    }

    @Test
    public void test02AddToCart(){
        poProdct.GetProductList(0).observeForever(eProducts -> loList = eProducts);
        if(loList.size() > 0){
            String lsLstID = loList.get(0).getListngID();
            isSuccess = poOrders.AddUpdateCart(lsLstID, 1);
        } else {
            isSuccess = false;
        }
        assertTrue(isSuccess);
    }
}
