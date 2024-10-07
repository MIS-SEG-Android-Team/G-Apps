package org.rmj.guanzongroup.gconnect.ViewModel;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnLoadApplicationListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class VMSplashScreen extends AndroidViewModel {
    private static final String TAG = VMSplashScreen.class.getSimpleName();
    private final Context mContext;
    private final GuanzonAppConfig loConfig;
    private final RAddressMobile loAddress;
    private final RProduct loProduct;
    private final AccountInfo loAccount;
    private final RNotificationInfo loNotif;
    private final ROrder loOrder;
    private iGCardSystem loGcard;
    public VMSplashScreen(@NonNull Application application) {
        super(application);
        this.mContext = application;

        loConfig = new GuanzonAppConfig(mContext);
        loAddress = new RAddressMobile(mContext);
        loProduct = new RProduct(mContext);
        loAccount = new AccountInfo(mContext);
        loNotif = new RNotificationInfo(mContext);
        loOrder = new ROrder(mContext);

        loConfig.setProductID("GuanzonApp");
        loConfig.setClientID(AppConstants.APP_CLIENT);
        loConfig.setIfPermissionsGranted(true);

        loConfig.setTestCase(false);
    }
    public void setsAppVrsnCd(String sAppVrsnCd){
        loConfig.setAppversionCode(sAppVrsnCd);
    }
    public void setsAppVrsnNm(String sAppVrsnNm){
        loConfig.setAppversionName(sAppVrsnNm);
    }
    public List<String> GetPermissions(){
        List<String> laPermissions = new ArrayList<>();
        laPermissions.add(Manifest.permission.POST_NOTIFICATIONS);
        laPermissions.add(Manifest.permission.INTERNET);
        laPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        laPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        laPermissions.add(Manifest.permission.READ_PHONE_STATE);
        laPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        laPermissions.add(Manifest.permission.GET_ACCOUNTS);
        laPermissions.add(Manifest.permission.CAMERA);

        return laPermissions;
    }
    public void InitializeData(OnInitializeData listener){
        GCardSystem.GCardSystemCallback poCallback = new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                Log.d(TAG, "Import Success...");
            }
            @Override
            public void OnFailed(String message) {
                Log.e(TAG, "Import failed... " + message);
            }
        };

        TaskExecutor loTask = new TaskExecutor();
        loTask.setOnLoadApplicationListener(new OnLoadApplicationListener() {
            @Override
            public Object DoInBackground() {
                try {
                    //TODO: IMPORT DATA NEEDED FOR ADDRESS
                    loTask.publishProgress(1);
                    if (!loAddress.ImportBarangayList()){
                        Log.d(TAG, "Unable to Import Baranggay Info");
                    }

                    loTask.publishProgress(2);
                    Thread.sleep(500);
                    if (!loAddress.ImportTownList()){
                        Log.d(TAG, "Unable to Import Town Info");
                    }

                    loTask.publishProgress(3);
                    Thread.sleep(500);
                    if (!loAddress.ImportProvinceList()){
                        Log.d(TAG, "Unable to Import Province Info");
                    }

                    loTask.publishProgress(4);
                    Thread.sleep(500);
                    if (!loAddress.ImportCountryList()){
                        Log.d(TAG, "Unable to Import Country Info");
                    }

                    //TODO: IMPORT NEW PRODUCTS
                    loTask.publishProgress(5);
                    Thread.sleep(500);
                    if (loProduct.ImportProductList()) {
                        Log.d(TAG, "Product Sales imported successfully...");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            public void OnProgress(int progress) {
                switch (progress){
                    case 1:
                        listener.OnProgress("Importing Barangays", progress);
                        break;
                    case 2:
                        listener.OnProgress("Importing Towns", progress);
                        break;
                    case 3:
                        listener.OnProgress("Importing Province", progress);
                        break;
                    case 4:
                        listener.OnProgress("Importing Country", progress);
                        break;
                    case 5:
                            listener.OnProgress("Importing Products and Services", progress);
                            break;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                listener.OnFinished("Finished loading data");
            }
        });
        loTask.Execute();
    }
    public interface OnInitializeData{
        void OnLoad(String args);
        void OnProgress(String args, int progress);
        void OnFinished(String args);
    }

}
