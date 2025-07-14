package org.rmj.g3appdriver.Panalo;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.Panalo.GPanalo;
import org.rmj.g3appdriver.lib.Panalo.PanaloRewards;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class GPanaloTest {
    private static final String TAG = GPanalo.class.getSimpleName();

    private Context mContext;
    private GPanalo poPanalo;

    private boolean isSuccess = false;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poPanalo = new GPanalo(mContext);
        new GuanzonAppConfig(mContext).setTestCase(true);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test01Login() throws Exception{
        new AccountAuthentication(mContext).
                LoginAccount(
                        new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "123456"),
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
    public void test02GetPanaloRewards() {
        List<PanaloRewards> loRewards = poPanalo.GetRewards("0");
        if(loRewards == null){
            isSuccess = false;
            Log.d(TAG, poPanalo.getMessage());
        } else {
            isSuccess = true;
        }
        assertTrue(isSuccess);
    }
}
