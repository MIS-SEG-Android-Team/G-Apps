package org.rmj.g3appdriver.GCardCore;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class GCardTransactionTest {

    private Context mContext;
    private GCardSystem poGcard;
    private iGCardSystem poSystem;

    private boolean isSuccess = false;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poGcard = new GCardSystem(mContext);
        poSystem = poGcard.getInstance(GCardSystem.CoreFunctions.GCARD);
    }

    @Test
    public void test01AuthenticateAccount() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSuccess = true;
            }

            @Override
            public void OnFailedLogin(String fsMessage) {
                isSuccess = false;
            }
            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        assertTrue(isSuccess);
    }

    @Test
    public void test02AddGCardNumber() throws Exception{
        poSystem.AddGCard(new GcardCredentials("0011800067804", "2000-04-01"), new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try{
                    JSONObject loJson = new JSONObject(args);
                    poSystem.SaveGCardInfo(loJson);
                    isSuccess = true;
                } catch (Exception e){
                    e.printStackTrace();
                    isSuccess = false;
                }
            }

            @Override
            public void OnFailed(String message) {
                try {
                    JSONObject loJson = new JSONObject(message);
                    poSystem.ConfirmAddGCard(new GcardCredentials("0011800067804", "2000-04-01"), new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            try{
                                JSONObject loJson = new JSONObject(args);
                                poSystem.SaveGCardInfo(loJson);
                                isSuccess = true;
                            } catch (Exception e){
                                e.printStackTrace();
                                isSuccess = false;
                            }
                        }

                        @Override
                        public void OnFailed(String message) {
                            isSuccess = false;
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                    isSuccess = false;
                }
            }
        });
        assertTrue(isSuccess);
    }

    @Test
    public void test03DownloadGCardNumbers() throws Exception{
        poSystem.DownloadGcardNumbers(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    JSONObject loDetail = new JSONObject(args);
                    poSystem.SaveGCardInfo(loDetail);
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                }
                isSuccess = true;
            }

            @Override
            public void OnFailed(String message) {
                isSuccess = false;
            }
        });
        assertTrue(isSuccess);
    }
}
