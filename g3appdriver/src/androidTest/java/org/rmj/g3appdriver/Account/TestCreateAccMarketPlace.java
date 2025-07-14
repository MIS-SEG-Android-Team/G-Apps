package org.rmj.g3appdriver.Account;

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
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestCreateAccMarketPlace {

    private Context mContext;
    private AccountAuthentication poLogin;
    private AccountAuthentication.LoginCredentials poCredentials;
    private AccountAuthentication.AccountCredentials poAccount;
    private GuanzonAppConfig poConfig;
    private RClientInfo poClient;
    private static EClientInfo poUserInfo;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private boolean isSucccess = false;
    private String message = "";

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poLogin = new AccountAuthentication(mContext);
        poConfig = new GuanzonAppConfig(mContext);
        poClient = new RClientInfo(mContext);
        poConfig.setTestCase(true);
    }

    @Test
    public void test01LoginAccount() throws Exception{
        poLogin.LoginAccount(
                new AccountAuthentication.LoginCredentials("mikegarcia8748@gmail.com", "12345678")
                , new AccountAuthentication.OnLoginCallback() {
                    @Override
                    public void OnSuccessLogin(String message) {
                        isSucccess = true;
                    }

                    @Override
                    public void OnFailedLogin(String message) {
                        isSucccess = false;
                    }
                    @Override
                    public void OnAccountVerification(String args, String args1) {

                    }
                });
        assertTrue(isSucccess);
    }

    @Test
    public void test02RetrieveAccount() throws Exception{
        isSucccess = poClient.ImportAccountInfo();
        assertTrue(isSucccess);
    }

    @Test
    public void test03PreviewAccountInfo() throws Exception{
        poClient.getClientInfo().observeForever(new Observer<EClientInfo>() {
            @Override
            public void onChanged(EClientInfo eClientInfo) {
                poUserInfo = eClientInfo;
                isSucccess = eClientInfo != null;
            }
        });
        assertTrue(isSucccess);
    }

    @Test
    public void test04CompleteAccount() throws Exception{
        poUserInfo.setLastName("Garcia");
        poUserInfo.setFrstName("Michael");
        poUserInfo.setMiddName("Permison");
        poUserInfo.setSuffixNm("");
        poUserInfo.setGenderCd("0");
        poUserInfo.setCvilStat("0");
        poUserInfo.setCitizenx("");
        poUserInfo.setBirthDte("1996-11-26");
        poUserInfo.setBirthPlc("0346");
        poUserInfo.setHouseNo1("231");
        poUserInfo.setAddress1("Sitio Tawi-Tawi");
        poUserInfo.setTownIDx1("0346");
        poUserInfo.setBrgyIDx1("1100170");
        poUserInfo.setTaxIDNox("");

        isSucccess = poClient.CompleteClientInfo(poUserInfo);
        String lsMessage;
        if(poClient.CompleteClientInfo(poUserInfo)){
            //perform
        } else {
            lsMessage = poClient.getMessage();
        }
        assertTrue(isSucccess);
    }
}
