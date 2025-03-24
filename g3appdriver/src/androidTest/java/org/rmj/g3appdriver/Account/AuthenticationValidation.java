package org.rmj.g3appdriver.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.lib.Account.AccountInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AuthenticationValidation {

    private Context mContext;
    private AccountAuthentication poLogin;
    private AccountAuthentication.LoginCredentials poCredentials;
    private AccountAuthentication.AccountCredentials poAccount;
    private GuanzonAppConfig poConfig;

    private boolean isSucccess = false;
    private String message = "";

    @Before
    public void setUp() throws Exception {
        mContext = ApplicationProvider.getApplicationContext();
        poLogin = new AccountAuthentication(mContext);
        poConfig = new GuanzonAppConfig(mContext);
        poConfig.setTestCase(true);
    }

    @Test
    public void test01credentialsValidationTest() {
        poCredentials = new AccountAuthentication.LoginCredentials("", "");
        assertFalse(poCredentials.isDataValid());
        assertEquals("Please enter email", poCredentials.getMessage());
    }

    @Test
    public void test02credentialsValidationTestNoEmail() {
        poCredentials = new AccountAuthentication.LoginCredentials("", "123456");
        assertFalse(poCredentials.isDataValid());
        assertEquals("Please enter email", poCredentials.getMessage());
    }

    @Test
    public void test03credentialsValidationTestNoPassword() {
        poCredentials = new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "");
        assertFalse(poCredentials.isDataValid());
        assertEquals("Please enter password", poCredentials.getMessage());
    }

    @Test
    public void test04credentialsValidationTestInvalidMobileNo() {
        poCredentials = new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "123456");
        assertFalse(poCredentials.isDataValid());
        assertEquals("Mobile number must start with '09'", poCredentials.getMessage());
    }

    @Test
    public void test05credentialsValidationTestLessMobileNo() {
        poCredentials = new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "123456");
        assertFalse(poCredentials.isDataValid());
        assertEquals("Mobile number must be 11 characters", poCredentials.getMessage());
    }


    @Test
    public void test06LoginAccountTest() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
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
    public void test07LoginAccountTestIncorrectPassword() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "qwerty123"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSucccess = true;
            }

            @Override
            public void OnFailedLogin(String fsMessage) {
                isSucccess = false;
                message = fsMessage;
            }
            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        assertFalse(isSucccess);
        assertEquals("Invalid password detected.", message);
    }

    @Test
    public void test08LoginAccountTest() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("garciamike8748@gmail.com", "12345678"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSucccess = true;
            }

            @Override
            public void OnFailedLogin(String fsMessage) {
                isSucccess = false;
            }
            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        assertTrue(isSucccess);
    }

    @Test
    public void test09SessionManageAfterLoginTest() throws Exception{
        AccountInfo loSession = new AccountInfo(mContext);
        String lsUserIDx = loSession.getUserID();
//        String lsEmailxx = loSession.getEmailAdd();
        String lsUserNme = loSession.getFullName();
//        String lsMobilex = loSession.getMobileNo();

        assertEquals("GAP0190554", lsUserIDx);
//        assertEquals("garciamike8748@gmail.com", lsEmailxx);
        assertEquals("Doe, Garcia Michael ", lsUserNme);
//        assertEquals("09270359402", lsMobilex);
    }

    @Test
    public void test10LoginAccountTestInvalidEmail() throws Exception{
        AccountAuthentication loAuth = new AccountAuthentication(mContext);
        loAuth.LoginAccount(new AccountAuthentication.LoginCredentials("mikegarcia87@gmail.com", "qwerty123"), new AccountAuthentication.OnLoginCallback() {
            @Override
            public void OnSuccessLogin(String message) {
                isSucccess = true;
            }

            @Override
            public void OnFailedLogin(String fsMessage) {
                isSucccess = false;
                message = fsMessage;
            }
            @Override
            public void OnAccountVerification(String args, String args1) {

            }
        });

        assertFalse(isSucccess);
        assertEquals("Invalid email detected.", message);
    }
}
