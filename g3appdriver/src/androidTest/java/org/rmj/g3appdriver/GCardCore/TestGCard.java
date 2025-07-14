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
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestGCard {
    private static final String TAG = TestGCard.class.getSimpleName();

    private Context mContext;
    private GCardSystem poGcard;
    private iGCardSystem poSystem;

    private boolean isSuccess = false;
    private String message = "";

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poGcard = new GCardSystem(mContext);
        poSystem = poGcard.getInstance(GCardSystem.CoreFunctions.GCARD);
        new GuanzonAppConfig(mContext).setTestCase(true);
    }

    @Test
    public void test01Login() throws Exception{
        new AccountAuthentication(mContext).LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
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
    public void test02AddGcard() throws Exception{
        poSystem.AddGCard(new GcardCredentials("0011800067804", "2000-04-01"), new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    poSystem.SaveGCardInfo(new JSONObject(args));
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Test
    public void test03ImportMcService() throws Exception{
        poSystem.DownloadMCServiceInfo(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                isSuccess = true;
            }

            @Override
            public void OnFailed(String message) {
                isSuccess = false;
            }
        });
        assertTrue(isSuccess);
    }

    @Test
    public void test04ImportMcRegistration() throws Exception{
        poSystem.DownloadRegistrationInfo(new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    poSystem.SaveRegistrationInfo(new JSONObject(args));
                } catch (Exception e) {
                    e.printStackTrace();
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
