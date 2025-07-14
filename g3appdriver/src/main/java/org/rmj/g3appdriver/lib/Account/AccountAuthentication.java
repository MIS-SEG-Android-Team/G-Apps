package org.rmj.g3appdriver.lib.Account;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Version.AppVersion;

public class AccountAuthentication {
    private static final String TAG = AccountAuthentication.class.getSimpleName();
    private final Context mContext;
    private final GuanzonAppConfig poConfig;
    private final ServerAPIs poApi;
    private final HttpHeaders poHeaders;
    private final RClientInfo poClient;
    private final AppVersion poVersion;

    public AccountAuthentication(Context context) {
        this.mContext = context;
        this.poConfig = new GuanzonAppConfig(mContext);
        this.poApi = new ServerAPIs(poConfig.getTestCase());
        this.poHeaders = new HttpHeaders(mContext);
        this.poClient = new RClientInfo(mContext);
        this.poVersion = new AppVersion(mContext);
    }

    public static class LoginCredentials{
        private String sUserName = "";
        private String sPassword = "";
        private String sMobileNo = "";

        private String message;

        public LoginCredentials(String username, String password){
            this.sUserName = username;
            this.sPassword = password;
        }

        public String getMessage() {
            return message;
        }

        public boolean isDataValid(){
            if(sUserName.isEmpty()){
                message = "Please enter email";
                return false;
            } else if(sPassword.isEmpty()){
                message = "Please enter password";
                return false;
            }

            return true;
        }

        public String getJSONParameters() throws Exception{
            JSONObject params = new JSONObject();
            params.put("user", sUserName);
            params.put("pswd", sPassword);
            params.put("nmbr", sMobileNo);

            return params.toString();
        }
    }
    public static class AccountCredentials{
        private String sUserName = "";
        private String sEmailAdd = "";
        private String sPassword = "";
        private String sPasswrd2 = "";
        private String sMobileNo = "";
        private String cAgreeTnC = "";

        private String message;

        public AccountCredentials() {
        }

        public String getMessage() {
            return message;
        }

        public String getsUserName() {
            return sUserName;
        }

        public void setUserName(String sLastName) {
            this.sUserName = sLastName;
        }

        public String getsEmailAdd() {
            return sEmailAdd;
        }

        public void setEmailAdd(String sEmailAdd) {
            this.sEmailAdd = sEmailAdd;
        }

        public String getsPassword() {
            return sPassword;
        }

        public void setPassword(String sPassword) {
            this.sPassword = sPassword;
        }

        public String getsMobileNo() {
            return sMobileNo;
        }

        public void setMobileNo(String sMobileNo) {
            this.sMobileNo = sMobileNo;
        }

        public String getsPasswrd2() {
            return sPasswrd2;
        }

        public void setPasswrd2(String sPasswrd2) {
            this.sPasswrd2 = sPasswrd2;
        }

        public String getcAgreeTnC() {
            return cAgreeTnC;
        }

        public void setcAgreeTnC(String cAgreeTnC) {
            this.cAgreeTnC = cAgreeTnC;
        }

        public boolean isDataValid(){
            if(sUserName.isEmpty()){
                message = "Please enter username";
                return false;
            } else if(sEmailAdd.isEmpty()){
                message = "Please enter email address";
                return false;
            } else if(sMobileNo.isEmpty()){
                message = "Please enter mobile no";
                return false;
            } else if(!sMobileNo.substring(0, 2).equalsIgnoreCase("09")){
                message = "Mobile number must start with '09'";
                return false;
            } else if(sMobileNo.length() != 11){
                message = "Mobile number must be 11 characters";
                return false;
            } else if(sPassword.isEmpty()){
                message = "Please enter password";
                return false;
            }  else if(sPassword.length() < 6){
                message = "Password is too short";
                return false;
            } else if(sPasswrd2.isEmpty()) {
                message = "Please re-type password";
                return false;
            } else if(!sPassword.equals(sPasswrd2)){
                message = "Passwords does not match";
                return false;
            } else if(!cAgreeTnC.equalsIgnoreCase("1")){
                message = "Please indicate that you have agree to the Terms & Conditions and Privacy Policy";
                return false;
            } else {
                return true;
            }
        }

        public String getParameters() throws Exception{
            JSONObject params = new JSONObject();
            params.put("name", sUserName);
            params.put("mail", sEmailAdd);
            params.put("pswd", sPassword);
            params.put("mobile", sMobileNo);
            params.put("cAgreeTnC", cAgreeTnC);
            return params.toString();
        }
    }

    public void LoginAccount(LoginCredentials credentials, OnLoginCallback callback) throws Exception{
         if (!credentials.isDataValid()) {
            callback.OnFailedLogin(credentials.getMessage());
        } else {
            String lsResponse = WebClient.httpsPostJSon(poApi.getSIGN_IN(), credentials.getJSONParameters(), poHeaders.getHeaders());
            if (lsResponse == null) {
                callback.OnFailedLogin("Unable to login account. Server no response.");
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if (lsResult.equalsIgnoreCase("success")) {

                    AccountInfo loInfo = new AccountInfo(mContext);
                    loInfo.setUserID(loResponse.getString("sUserIDxx"));
                    loInfo.setFullName(loResponse.getString("sUserName"));
                    loInfo.setMobileNo(credentials.sMobileNo);
                    loInfo.setLoginStatus(true);

                    EClientInfo loClient = new EClientInfo();
                    loClient.setDateMmbr(loResponse.getString("dCreatedx"));
                    loClient.setEmailAdd(loResponse.getString("sEmailAdd"));
                    loClient.setUserName(loResponse.getString("sUserName"));
                    loClient.setMobileNo(loResponse.getString("sMobileNo"));
                    loClient.setUserIDxx(loResponse.getString("sUserIDxx"));

                    poClient.insert(loClient);

                    //TODO: update user app version
                    Thread.sleep(1000);
                    poVersion.SubmitUserAppVersion();

                    callback.OnSuccessLogin("Login success.");
                } else {
                    JSONObject loError = loResponse.getJSONObject("error");
                    String lsMessage = loError.getString("message");
                    String lsCode = loError.getString("code");
                    if(lsCode.equalsIgnoreCase("40003")){
                        String lsOtp = loResponse.getString("otp");
                        String lsVfy = loResponse.getString("verify");
                        callback.OnAccountVerification(lsOtp, lsVfy);
                    } else {
                        callback.OnFailedLogin(lsMessage);
                    }
                }
            }
        }
    }
    public void RegisterAccount(AccountCredentials credentials, OnCreateAccountCallback callback) throws Exception{
        if(!credentials.isDataValid()){
            callback.OnFailedRegister(credentials.getMessage());
        } else {
            String lsAddress = poApi.getRegisterAcountAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, credentials.getParameters(), poHeaders.getHeaders());
            if(lsResponse == null){
                callback.OnFailedRegister("Server no response.");
            } else {
                Log.d(TAG, lsResponse);
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(lsResult.equalsIgnoreCase("success")){
                    callback.OnSuccessRegister("Please check your email for account verification.");
                } else {
                    JSONObject loError = loResponse.getJSONObject("error");
                    String lsMessage = loError.getString("message");
                    callback.OnFailedRegister(lsMessage);
                }
            }
        }
    }
    public void RetrievePassword(String email, OnRetrievePasswordCallback callback) throws Exception{
        if(email.isEmpty()){
            callback.OnFailedRetrieve("Please enter email.");
        } else {
            JSONObject params = new JSONObject();
            params.put("email", email);

            String lsResponse = WebClient.httpsPostJSon(poApi.getRetrievePasswordAPI(), params.toString(), poHeaders.getHeaders());
            if(lsResponse == null){
                callback.OnFailedRetrieve("Server no response.");
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(lsResult.equalsIgnoreCase("success")){
                    callback.OnSuccessRetrieve("Please check your email for your password info.");
                } else {
                    JSONObject loError = loResponse.getJSONObject("error");
                    String lsMessage = loError.getString("message");
                    callback.OnFailedRetrieve(lsMessage);
                }
            }
        }
    }

    public interface OnLoginCallback{
        void OnSuccessLogin(String message);
        void OnFailedLogin(String message);
        void OnAccountVerification(String args, String args1);
    }
    public interface OnCreateAccountCallback{
        void OnSuccessRegister(String message);
        void OnFailedRegister(String message);
    }
    public interface OnRetrievePasswordCallback{
        void OnSuccessRetrieve(String message);
        void OnFailedRetrieve(String message);
    }
}
