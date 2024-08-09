package org.rmj.guanzongroup.useraccount.ViewModel;

import static org.rmj.g3appdriver.utils.CallbackJson.CallbackStatus.FAILED;
import static org.rmj.g3appdriver.utils.CallbackJson.CallbackStatus.SUCCESS;
import static org.rmj.g3appdriver.utils.CallbackJson.parse;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ECountryInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEmailInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;
import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.GCardCore.CodeGenerator;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.useraccount.Model.AccountDetailsInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VMAccountDetails extends AndroidViewModel {
    private static final String TAG = VMAccountDetails.class.getSimpleName();
    private final ConnectionUtil poConnect;
    private final RClientInfo poClientx;
    private final RAddressMobile poAddress;
    private final MutableLiveData<List<AccountDetailsInfo>> poAcctInf = new MutableLiveData<>();

    private Boolean isSuccess;
    private Object loResult;

    public VMAccountDetails(@NonNull Application application) {
        super(application);
        this.poConnect = new ConnectionUtil(application);
        this.poClientx = new RClientInfo(application);
        this.poAddress = new RAddressMobile(application);
    }

    public LiveData<EClientInfo> getClientInfo(){
        return poClientx.getClientInfo();
    }

    public LiveData<EClientInfo> getClientDetail(){
        return poClientx.getLoClient();
    }

    public LiveData<DClientInfo.ClientDetail> GetClientDetailForPreview(){
        return poClientx.GetClientDetailForPreview();
    }

    public LiveData<List<EBarangayInfo>> getBarangayList(String fsTownID){
        return poAddress.GetBarangayList(fsTownID);
    }

    public LiveData<String> GetTownProvinceName(String fsTownID){
        return poAddress.ParseTownID(fsTownID);
    }

    public LiveData<String> GetBarangay(String fsBrgyID){
        return poAddress.ParseBrgyID(fsBrgyID);
    }

    public LiveData<List<DAddress.oTownObj>> getTownCityList(){
        return poAddress.GetTownList();
    }

    public LiveData<List<ECountryInfo>> getCountryList(){
        return poAddress.GetCountryList();
    }

    public LiveData<String> getBirthplace(String fsTownIdx) {
        return poAddress.ParseTownID(fsTownIdx);
    }

    public ArrayList<String> getGenderList() {
        return poClientx.getGenderList();
    }

    public ArrayList<String> getCivilStatusList() {
        return poClientx.getCivilStatusList();
    }

    public ArrayList<String> getBarangayForInput(List<EBarangayInfo> foList) {
        return poAddress.getBarangayForInput(foList);
    }

    public ArrayList<String> getTownCityForInput(List<DAddress.oTownObj> foList) {
        return poAddress.getTownCityForInput(foList);
    }

    public ArrayList<String> getCountryForInput(List<ECountryInfo> foList) {
        return poAddress.getCountryForInput(foList);
    }

    public String getDate(String val){
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = null;
        try {
            formattedDate = formatter.format(fromUser.parse(val));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public void importAccountInfo(OnTransactionCallBack foCallBck) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {

                        if(poClientx.ImportAccountInfo()) {
                            loResult = "Account imported successfully";
                            isSuccess = true;
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess = false;
                        }

                    } else {
                        loResult = AppConstants.SERVER_NO_RESPONSE();
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                String res = (String) object;
                if(isSuccess) {
                    foCallBck.onSuccess(res);
                } else {
                    foCallBck.onFailed(res);
                }
            }
        });
    }
    public void importClientInfo(String ClientID, String SourceCD, String SourceNo, OnClientInfoCallBack foCallBck) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        if(poClientx.ImportClientInfo(ClientID, SourceCD, SourceNo)) {
                            loResult = "Client info imported successfully";
                            isSuccess = true;
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess = false;
                        }
                    } else {
                        loResult = AppConstants.SERVER_NO_RESPONSE();
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                String res = (String) object;
                if(isSuccess) {
                    foCallBck.onSuccess(res);
                } else {
                    foCallBck.onFailed(res);
                }
            }
        });
    }
    public void completeClientInfo(EClientInfo foClientx, OnTransactionCallBack foCallBck) {
        TaskExecutor.Execute(foClientx, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                EClientInfo loInfo = (EClientInfo) args;

                try {
                    if(poConnect.isDeviceConnected()) {
                        if(poClientx.CompleteClientInfo(loInfo)) {

                            Thread.sleep(1000);
                            if (!poClientx.ImportAccountInfo()){
                                loResult = poClientx.getMessage();
                                isSuccess = false;
                            }else {
                                loResult = "Client info completion success";
                                isSuccess = true;
                            }
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess = false;
                        }
                    } else {
                        loResult = AppConstants.SERVER_NO_RESPONSE();
                        isSuccess = false;
                    }
                } catch (Exception e) {
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                String res = (String) object;

                if(isSuccess) {
                    foCallBck.onSuccess(res);
                } else {
                    foCallBck.onFailed(res);
                }
            }
        });
    }
    public void updateAccountInfo(EClientInfo foClientx, OnTransactionCallBack foCallBck) {
        TaskExecutor.Execute(foClientx, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        EClientInfo oClientx = (EClientInfo) args;

                        if(poClientx.UpdateAccountInfo(oClientx)) {
                            Thread.sleep(1000);
                            if(poClientx.ImportAccountInfo()) {
                                loResult = "Personal account details updated successfully.";
                                isSuccess = true;
                            } else {
                                loResult = poClientx.getMessage();
                                isSuccess =  false;
                            }
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess =  false;
                        }
                    } else {
                        loResult = AppConstants.SERVER_NO_RESPONSE();
                        isSuccess =  false;
                    }
                } catch (Exception e) {
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if(res) {
                    foCallBck.onSuccess(loResult.toString());
                } else {
                    foCallBck.onFailed(loResult.toString());
                }
            }
        });
    }
    public void UpdateMobileNo(String fsArgs, OnTransactionCallBack foCallBck){
        TaskExecutor.Execute(fsArgs, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                try{

                    String lsMobileNo = (String) args;

                    if(lsMobileNo.trim().isEmpty()){
                        loResult = "Please enter mobile no.";
                        isSuccess =  false;
                    } else {
                        String mobileKey = lsMobileNo.substring(0, 1);

                        if(!mobileKey.equals("09".substring(0,1))){
                            loResult = "Mobile number must start with 09";
                            isSuccess =  false;
                        } else if(lsMobileNo.length() < 11) {
                            loResult = "Mobile number must be 11 characters";
                            isSuccess =  false;
                        } else {
                            if (!poConnect.isDeviceConnected()) {
                                loResult = "Not connected to internet.";
                                isSuccess =  false;
                            } else {
                                if(poClientx.UpdateMobileNo(lsMobileNo)){
                                    loResult = "Mobile Number updated successfully";
                                    isSuccess =  true;
                                } else {
                                    loResult = poClientx.getMessage();
                                    isSuccess =  false;
                                }
                            }
                        }
                    }
                } catch (Exception e){
                    loResult = e.getMessage();
                    isSuccess =  false;
                }

                return isSuccess;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if(res){
                    foCallBck.onSuccess("Your request to update mobile no has been submitted. Please wait for verification.");
                } else {
                    foCallBck.onFailed(loResult.toString());
                }
            }
        });
    }
    public void UpdateEmailAdd(String fsArgs, OnTransactionCallBack foCallBck){

        TaskExecutor.Execute(fsArgs, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }

            @Override
            public Object DoInBackground(Object args) {
                try{
                    String lsEmailAdd = args.toString();

                    if(lsEmailAdd.trim().isEmpty()){
                        loResult = "Please enter email address";
                        isSuccess =  false;
                    } else {
                        if (!poConnect.isDeviceConnected()) {
                            loResult = "Not connected to internet.";
                            isSuccess = false;
                        } else if(poClientx.UpdateEmailAddress(lsEmailAdd)){
                            loResult = "Email updated successfully";
                            isSuccess = true;
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess = false;
                        }
                    }
                } catch (Exception e){
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return isSuccess;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if(res){
                    foCallBck.onSuccess("Your request to update email address has been submitted. Please wait for verification.");
                } else {
                    foCallBck.onFailed(loResult.toString());
                }
            }
        });
    }
    public void UpdatePassword(String fsOld, String fsNew, String fsNew1, OnTransactionCallBack callBack){

        ArrayList<String> lsParams = new ArrayList<>();
        lsParams.add(fsOld);
        lsParams.add(fsNew);
        lsParams.add(fsNew1);

        TaskExecutor.Execute(lsParams, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    ArrayList<String> lsParams = (ArrayList<String>) args;

                    String lsOld = lsParams.get(0);
                    String lsNew = lsParams.get(1);
                    String lsNw1 = lsParams.get(2);

                    if(!poConnect.isDeviceConnected()){
                        loResult = "Not connected to internet.";
                        isSuccess =  false;
                    } else{
                        if(poClientx.ChangePassword(lsOld, lsNew, lsNw1)){
                            loResult = "Password changed successfully";
                            isSuccess =  true;
                        } else {
                            loResult = poClientx.getMessage();
                            isSuccess =  false;
                        }
                    }
                } catch (Exception e){
                    loResult = e.getMessage();
                    isSuccess = false;
                }

                return isSuccess;
            }

            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;

                if(res){
                    callBack.onSuccess("Password change successfully.");
                } else {
                    callBack.onFailed(loResult.toString());
                }
            }
        });
    }

    public void GetEmailInfo(String args, OnRetrieveEmailInfo listener){

        TaskExecutor.Execute(args, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                EEmailInfo loResult = poClientx.GetEmailInfo(args.toString());
                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                listener.OnRetrieve((EEmailInfo) object);
            }
        });
    }
    public void GetMobileInfo(String args, OnRetrieveMobileInfo listener){

        TaskExecutor.Execute(args, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                EMobileInfo loResult = poClientx.GetMobileInfo(args.toString());
                return loResult;
            }

            @Override
            public void OnPostExecute(Object object) {
                listener.OnRetrieve((EMobileInfo) object);
            }
        });
    }

    public interface OnClientInfoCallBack {
        void onLoading();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }
    public interface OnTransactionCallBack {
        void onLoading();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }
    public interface ClientInfoTransactionCallback {
        void onLoad();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }
    public interface OnRetrieveEmailInfo{
        void OnRetrieve(EEmailInfo args);
    }
    public interface OnRetrieveMobileInfo{
        void OnRetrieve(EMobileInfo args);
    }
}
