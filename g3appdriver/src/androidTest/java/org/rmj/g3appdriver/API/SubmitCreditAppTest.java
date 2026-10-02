package org.rmj.g3appdriver.API;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.CreditApp.CreditApplication;
import org.rmj.g3appdriver.lib.CreditApp.model.MpCreditApp;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class SubmitCreditAppTest {
    private static final String TAG = SubmitCreditAppTest.class.getSimpleName();

    private Context mContext;

    private CreditApplication poSys;

    private static boolean isSuccess = false;
    private String message;

    @Before
    public void setUp() throws Exception {
        this.mContext = ApplicationProvider.getApplicationContext();
        new GuanzonAppConfig(mContext).setTestCase(true);
        this.poSys = new CreditApplication(mContext);
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

        assertTrue(isSuccess);
    }

    @Test
    public void test02SubmitApplication() throws Exception{
        MpCreditApp loApp = new MpCreditApp();
        loApp.setDateApplied(new AppConstants().DATE_MODIFIED);
        loApp.setDateCreated(new AppConstants().DATE_MODIFIED);
        loApp.setUnitApplied("1");
        loApp.setModel("Sample");
        loApp.setUnitType("1");
        loApp.setDiscount("50");
        loApp.setAmortization("9000");
        loApp.setDownpayment("1500");
        loApp.setInstallmentTerm("");
        loApp.setMiscellaneousExpense("");

        loApp.clientInfo().setLastName("Garcia");
        loApp.clientInfo().setFirstName("Michael");
        loApp.clientInfo().setMiddleName("Permison");
        loApp.clientInfo().setGender("0");
        loApp.clientInfo().setCivilStatus("0");

        loApp.clientInfo().addressInfo().setHouseNo("231");
        loApp.clientInfo().addressInfo().setAddress1("Sitio Tawi-Tawi");
        loApp.clientInfo().addressInfo().setBarangayID("211");
        loApp.clientInfo().addressInfo().setTownID("123");

        loApp.meansInfo().setSourceCD("0");
        loApp.meansInfo().employment().setBusinessIndustry("Retail");
        loApp.meansInfo().employment().setPosition("Programmer");
        loApp.meansInfo().employment().setSalary("10000");

        loApp.disbursementInfo().bankInfo().setAccountType("0");
        loApp.disbursementInfo().bankInfo().setBankName("BDO");

        if(!poSys.SubmitApplication(loApp.getData())){
            message = poSys.getMessage();
            Log.e(TAG, message);
        } else {
            isSuccess = true;
        }

        Thread.sleep(1000);
        assertTrue(isSuccess);
    }

    @Test
    public void test03DownloadOtherInfoAndMerge() throws Exception{
        String lsResult = poSys.GetOtherApplicationInfo();
        if(lsResult == null){
            message = poSys.getMessage();
            Log.e(TAG, message);
        }

        JSONObject loJson = new JSONObject(lsResult);
        String lsMeans = loJson.getString("sMeansInf");
        String lsOther = loJson.getString("sOtherInf");

        MpCreditApp loApp = new MpCreditApp();
        loApp.setDateApplied(new AppConstants().DATE_MODIFIED);
        loApp.setDateCreated(new AppConstants().DATE_MODIFIED);
        loApp.setUnitApplied("1");
        loApp.setModel("Sample");
        loApp.setUnitType("1");
        loApp.setDiscount("50");
        loApp.setAmortization("9000");
        loApp.setDownpayment("1500");
        loApp.setInstallmentTerm("");
        loApp.setMiscellaneousExpense("");

        loApp.clientInfo().setLastName("Garcia");
        loApp.clientInfo().setFirstName("Michael");
        loApp.clientInfo().setMiddleName("Permison");
        loApp.clientInfo().setGender("0");
        loApp.clientInfo().setCivilStatus("0");

        MpCreditApp loMeans = new MpCreditApp();
        loMeans.setData(lsMeans);
        loApp.meansInfo().setData(loMeans.getData());

        MpCreditApp loOther = new MpCreditApp();
        loMeans.setData(lsOther);
        loApp.disbursementInfo().setData(loOther.getData());

        Thread.sleep(1000);

        if(!poSys.SubmitApplication(loApp.getData())){
            message = poSys.getMessage();
            Log.e(TAG, message);
        } else {
            isSuccess = true;
        }

        assertTrue(isSuccess);
    }
}
