package org.rmj.guanzongroup.digitalgcard.ViewModel;

import static org.rmj.g3appdriver.utils.CallbackJson.CallbackStatus.FAILED;
import static org.rmj.g3appdriver.utils.CallbackJson.CallbackStatus.SUCCESS;
import static org.rmj.g3appdriver.utils.CallbackJson.parse;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.lib.GCardCore.CodeGenerator;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.CartItem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;
import org.rmj.g3appdriver.lib.GCardCore.RedemptionManager;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.HashMap;
import java.util.List;

public class VMGCardSystem extends AndroidViewModel {

    private static final String TAG = VMGCardSystem.class.getSimpleName();
    private final Context mContext;
    private final GCardSystem poGcrdSys;
    private final ConnectionUtil poConnect;
    private iGCardSystem mGcardSys;
    private String lomessage;
    private Boolean result;
    private AccountInfo loAccount;
    private RClientInfo loClientInfo;

    public VMGCardSystem(@NonNull Application application) {
        super(application);

        this.mContext = application;
        this.poGcrdSys = new GCardSystem(application);
        this.poConnect = new ConnectionUtil(application);
        this.loAccount = new AccountInfo(application);
        this.loClientInfo = new RClientInfo(application);
    }

    /** Initialize this method whenever it is used to return a GCard function instance to be used. */
    public void setmContext(GCardSystem.CoreFunctions foCore) {
        mGcardSys = poGcrdSys.getInstance(foCore);
    }

    public LiveData<EGcardApp> getActiveGcard() {
        mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.GCARD);
        return mGcardSys.hasNoGcard();
    }
    public LiveData<List<EGcardApp>> getInactiveGCard() {
        return mGcardSys.hasUnCheckGCard();
    }

    public LiveData<List<EGcardApp>> GetGCardList() {
        return mGcardSys.GetGCardList();
    }
    public LiveData<List<ERedeemablesInfo>> GetRedeemablesList() {
        return mGcardSys.GetRedeemablesList();
    }

    public LiveData<List<EGCardTransactionLedger>> GetGcardTransactions() {
        return mGcardSys.GetGcardTransactions();
    }
    public LiveData<List<EGCardTransactionLedger>> GetPointsEntryTransactions() {
        return mGcardSys.GetPointsEntryTransactions();
    }
    public LiveData<List<EGCardTransactionLedger>> GetRedemptionTransactions() {
        return  mGcardSys.GetRedemptionTransactions();
    }

    public LiveData<List<DRedeemItemInfo.GCardCartItem>> GetCartItems(){
        mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        return mGcardSys.GetCartItems();
    }

    public LiveData<List<Double>> GetRedeemablePointsFilter(){
        mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        return mGcardSys.GetRedeemablePointsFilter();
    }
    public LiveData<List<ERedeemablesInfo>> GetRedeemablePointsFilter(String fsVal){
        mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        return mGcardSys.GetRedeemablesList(fsVal);
    }

    public LiveData<Integer> GetGcardCartItemCount(){
        iGCardSystem loSys = poGcrdSys.getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        return loSys.GetGcardCartItemCount();
    }

    public LiveData<Integer> GetMarketplaceItemCartCount(){
        ROrder loOrder = new ROrder(mContext);
        return loOrder.GetMartketplaceCartItemCount();
    }

    public LiveData<Double> GetGCardCartItemTotalPoints(){
        return mGcardSys.GetGCardCartItemTotalPoints();
    }

    public void addGcard(GcardCredentials foCardDta, GcardTransactionCallback foCallBck) {

        TaskExecutor.Execute(foCardDta, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    GcardCredentials loGcardxx = (GcardCredentials) args;
                    mGcardSys.AddGCard(loGcardxx, new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            try {
                                mGcardSys.SaveGCardInfo(new JSONObject(args));
                                mGcardSys.DownloadMCServiceInfo(new GCardSystem.GCardSystemCallback() {
                                    @Override
                                    public void OnSuccess(String args) {
                                        try {
                                            JSONObject loDetail = new JSONObject(args);
                                            mGcardSys.SaveMcServiceInfo(loDetail);

                                            lomessage = parse(SUCCESS, "MC Service downloaded");
                                        } catch (Exception e) {
                                            lomessage = parse(FAILED, e.getMessage());
                                        }
                                    }
                                    @Override
                                    public void OnFailed(String message) {
                                        lomessage = parse(FAILED, message);
                                    }
                                });

                                if (loAccount.getVerificationStatus() <= 0){

                                    //TODO: import complete info after saving gcard number
                                    Thread.sleep(1000);
                                    if (loClientInfo.ImportAccountInfo()){
                                        lomessage = parse(SUCCESS, "Account Completed Successfully");
                                    }else {
                                        lomessage = parse(FAILED, loClientInfo.getMessage());
                                    }

                                }
                            }catch (Exception e) {
                                lomessage = parse(FAILED, e.getMessage());
                            }
                        }
                        @Override
                        public void OnFailed(String message) {
                            lomessage = parse(FAILED, message);
                        }
                    });
                }catch (Exception e){
                    lomessage = parse(FAILED, e.getMessage());
                }

                return lomessage;
            }
            @Override
            public void OnPostExecute(Object object) {
                String res = object.toString();
                setCallBack(res, foCallBck);
            }
        });
    }
    public void addScannedGcard(String args, GcardTransactionCallback foCallBck) {
        TaskExecutor.Execute(args, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String params = args.toString();

                    CodeGenerator loCode = new CodeGenerator();
                    loCode.setEncryptedQrCode(params);

                    String lsArgs = loCode.getGCardNumber();

                    mGcardSys.AddGCardQrCode(lsArgs, new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            try {
                                Thread.sleep(1000);
                                mGcardSys.DownloadGcardNumbers(new GCardSystem.GCardSystemCallback() {
                                    @Override
                                    public void OnSuccess(String args) {
                                        try {
                                            mGcardSys.SaveGCardInfo(new JSONObject(args));
                                            lomessage = parse(SUCCESS, "GCard numbers downloaded");
                                        } catch (Exception e) {
                                            lomessage = parse(FAILED, e.getMessage());
                                        }
                                    }
                                    @Override
                                    public void OnFailed(String message) {
                                        lomessage = parse(FAILED, message);
                                    }
                                });

                                Thread.sleep(1000);
                                mGcardSys.DownloadMCServiceInfo(new GCardSystem.GCardSystemCallback() {
                                    @Override
                                    public void OnSuccess(String args) {
                                        try {
                                            mGcardSys.SaveMcServiceInfo(new JSONObject(args));
                                            lomessage = parse(SUCCESS, "MC Service downloaded");
                                        } catch (Exception e) {
                                            lomessage = parse(FAILED, e.getMessage());
                                        }
                                    }
                                    @Override
                                    public void OnFailed(String message) {
                                        lomessage = parse(FAILED, message);
                                    }
                                });

                                if (loAccount.getVerificationStatus() <= 0){

                                    //TODO: import complete info after saving gcard number
                                    Thread.sleep(1000);
                                    if (loClientInfo.ImportAccountInfo()){
                                        lomessage = parse(SUCCESS, "Account Completed Successfully");
                                    }else {
                                        lomessage = parse(FAILED, loClientInfo.getMessage());
                                    }

                                }
                            } catch (Exception e) {
                                lomessage = parse(FAILED, e.getMessage());
                            }
                        }
                        @Override
                        public void OnFailed(String message) {
                            lomessage = parse(FAILED, message);
                        }

                    });
                } catch(Exception e) {
                    lomessage = parse(FAILED, e.getMessage());
                }

                return lomessage;
            }
            @Override
            public void OnPostExecute(Object object) {
                String res = (String) object;
                setCallBack(res, foCallBck);
            }
        });
    }
    public void addGCardQrCode(GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        //TODO: Add QR Scanner Method here to provide
                        mGcardSys.AddGCardQrCode("", new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                try {
                                    mGcardSys.SaveGCardInfo(new JSONObject(args));

                                    result = true;
                                    lomessage = args;
                                }catch (Exception e) {
                                    result = false;
                                    lomessage = TAG + e.getMessage();
                                }
                            }
                            @Override
                            public void OnFailed(String message) {
                                result = false;
                                lomessage = message;
                            }
                        });
                    } else {
                        result = false;
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                    }
                } catch(Exception e) {
                    result = false;
                    lomessage = TAG + e.getMessage();
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void confirmAddGCard(GcardCredentials foCardDta, GcardTransactionCallback foCallBck) {
        TaskExecutor.Execute(foCardDta, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    GcardCredentials loGcardxx = (GcardCredentials) args;
                    mGcardSys.ConfirmAddGCard(loGcardxx, new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            try {
                                mGcardSys.SaveGCardInfo(new JSONObject(args));
                                mGcardSys.DownloadMCServiceInfo(new GCardSystem.GCardSystemCallback() {
                                    @Override
                                    public void OnSuccess(String args) {
                                        try {
                                            JSONObject loDetail = new JSONObject(args);
                                            mGcardSys.SaveMcServiceInfo(loDetail);

                                            lomessage = parse(SUCCESS, "MC Service downloaded");
                                        } catch (Exception e) {
                                            lomessage = parse(FAILED, e.getMessage());
                                        }
                                    }
                                    @Override
                                    public void OnFailed(String message) {
                                        lomessage = parse(FAILED, message);
                                    }
                                });
                            } catch (Exception e) {
                                lomessage = parse(FAILED, e.getMessage());
                            }
                        }
                        @Override
                        public void OnFailed(String message) {
                            lomessage = parse(FAILED, message);
                        }

                    });
                } catch(Exception e) {
                    lomessage = parse(FAILED, e.getMessage());
                }

                return lomessage;
            }
            @Override
            public void OnPostExecute(Object object) {
                String res = (String) object;
                setCallBack(res, foCallBck);
            }
        });
    }
    public void downloadGcardNumbers(GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(!poConnect.isDeviceConnected()) {
                        lomessage = "SERVER NO RESPONSE";
                        return false;
                    }else {
                        mGcardSys.DownloadGcardNumbers(new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                try {
                                    mGcardSys.SaveGCardInfo(new JSONObject(args));
                                    lomessage = "GCard added successfully";
                                    result = true;
                                } catch (Exception e) {
                                    lomessage = e.getMessage();
                                    result = false;
                                }
                            }
                            @Override
                            public void OnFailed(String fsmessage) {
                                lomessage = fsmessage;
                                result = false;
                            }

                        });
                    }

                    return result;
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if(!res){
                    callback.onFailed(lomessage);
                }else {
                    callback.onSuccess(lomessage);
                }
            }
        });
    }
    public void activateGcard(String foGcardNo,GcardTransactionCallback callBack) {
        TaskExecutor.Execute(foGcardNo, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                String lsGcardNo = (String) args;
                try {
                    if(poConnect.isDeviceConnected()) {
                        //TODO: Add QR Scanner Method here to provide
                        mGcardSys.ActivateGcard(lsGcardNo);
                        lomessage = "GCard activated successfully";
                        return true;
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        return false;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callBack.onSuccess(lomessage);
                }else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void generateGCardQrCode(GcardTransactionCallback callBack) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        //TODO: Add QR Scanner Method here to provide
                        lomessage = "QR Code generated successfully";
                        return mGcardSys.GenerateGCardQrCode();
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        return null;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Bitmap bitmap = (Bitmap) object;
                if (bitmap != null){
                    callBack.onQrGenerate(bitmap);
                    callBack.onSuccess(lomessage);
                }else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void downloadRedeemables(GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.DownloadRedeemables(new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                // TODO: Call the saving of redeemables to local database
                                lomessage = args;
                                result = true;
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void addToCart(CartItem item, GcardTransactionCallback callback) {
        TaskExecutor.Execute(item, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    CartItem loCartItm = (CartItem) args;
                    mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.REDEMPTION);
                    mGcardSys.AddToCart(loCartItm, new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            // TODO: Call the saving of add to cart item to local database
                            lomessage = args;
                            result = true;
                        }

                        @Override
                        public void OnFailed(String message) {
                            lomessage = message;
                            result = false;
                        }
                    });

                    return result;
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void UpdateCartItem(CartItem item) {
        TaskExecutor.Execute(item, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {

            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    CartItem loCartItm = (CartItem) args;
                    mGcardSys.UpdateCartItem(loCartItm, new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            // TODO: Call the update of cart to local database
                            lomessage = args;
                            result = true;
                        }

                        @Override
                        public void OnFailed(String message) {
                            lomessage = message;
                            result = false;
                        }
                    });
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){

                }else {

                }
            }
        });
    }
    public void GetMCBranchesForRedemption(GetBranchCallback callback){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                RedemptionManager loredemp = new RedemptionManager(mContext);
                List<EBranchInfo> branchInfos = loredemp.GetMCBranchesForRedemption();

                return branchInfos;
            }
            @Override
            public void OnPostExecute(Object object) {
                List<EBranchInfo> branchInfos = (List<EBranchInfo>) object;
                callback.onSuccess(branchInfos);
            }
        });
    }
    public void PlaceOrder(List<DRedeemItemInfo.GCardCartItem> redeemables, String BranchCD, GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.PlaceOrder(redeemables, BranchCD, new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                // TODO: Call the update of cart to local database
                                lomessage = args;
                                result = true;
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.NO_INTERNET();
                        result = false;
                    }
                } catch(Exception e) {
                   lomessage = e.getMessage();
                   result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void generateGCardOrderQrCode(GcardTransactionCallback callBack) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        //TODO: Add QR Scanner Method here to provide
                        lomessage = "QR Order Generated";
                        return mGcardSys.GenerateGCardOrderQrCode("");
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        return null;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Bitmap bitmap = (Bitmap) object;
                if (bitmap != null){
                    callBack.onQrGenerate(bitmap);
                    callBack.onSuccess(lomessage);
                }else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void downloadTransactions(GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.DownloadTransactions(new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                // TODO: Save downloaded transactions to local database.
                                lomessage = args;
                                result = true;
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    result =  false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void saveTransactions(JSONObject detail, GcardTransactionCallback callBack) {
        TaskExecutor.Execute(detail, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        JSONObject jsonObjects = (JSONObject) args;

                        mGcardSys.SaveTransactions(jsonObjects);
                        lomessage = "Saved Successfully";
                        result = true;
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callBack.onSuccess(lomessage);
                }else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void downloadMCServiceInfo(GcardTransactionCallback callBack) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.DownloadMCServiceInfo(new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                try {
                                    mGcardSys.SaveMcServiceInfo(new JSONObject(args));
                                    lomessage = args;
                                    result = true;
                                }catch (Exception e) {
                                    lomessage = e.getMessage();
                                    result = false;
                                }
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }

                    return result;
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callBack.onSuccess(lomessage);
                }else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void downloadRegistrationInfo(GcardTransactionCallback callback) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.DownloadRegistrationInfo(new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                // TODO: Save registration info to local
                                try {
                                    mGcardSys.SaveRegistrationInfo(new JSONObject(args));
                                    lomessage = args;
                                    result = true;
                                }catch (Exception e) {
                                    lomessage = e.getMessage();
                                    result = false;
                                }
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }
                } catch(Exception e) {
                    lomessage = e.getMessage();
                    result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void scheduleNextServiceDate(String date, GcardTransactionCallback callback) {
        TaskExecutor.Execute(date, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        mGcardSys.ScheduleNextServiceDate(args.toString(), new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                // TODO: Save service scched to local
                                lomessage = args;
                                result = true;
                            }
                            @Override
                            public void OnFailed(String message) {
                                lomessage = message;
                                result = false;
                            }
                        });
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        result = false;
                    }
                } catch(Exception e) {
                   lomessage = e.getMessage();
                   result = false;
                }

                return result;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    callback.onSuccess(lomessage);
                }else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
    public void setActiveGcard(String fsCardNox, GcardTransactionCallback foCallBck) {
        TaskExecutor.Execute(fsCardNox, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                String lsCardNox = args.toString();

                mGcardSys.updateGCardDeactiveStatus();
                mGcardSys.updateGCardActiveStatus(lsCardNox);

                return parse(SUCCESS, "Gcard activated!");
            }
            @Override
            public void OnPostExecute(Object object) {
                String res = object.toString();
                setCallBack(res, foCallBck);
            }
        });
    }
    public void DeleteCartItem(String fsVal){
        iGCardSystem loSys = poGcrdSys.getInstance(GCardSystem.CoreFunctions.REDEMPTION);
        loSys.DeleteItemCart(fsVal);
    }
    public void ViewGCardQrCode(OnViewGCardQrCode callback){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    mGcardSys = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.GCARD);
                    return mGcardSys.GenerateGCardQrCode();
                } catch (Exception e) {
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                callback.OnView((Bitmap) object);
            }
        });
    }

    private static void setCallBack(String fsResultx, GcardTransactionCallback foCallBck) {
        Log.d(TAG, fsResultx);
        try {
            JSONObject loJson = new JSONObject(fsResultx);

            String lsStatus =String.valueOf(loJson.get("status"));
            String lsMessage = loJson.getString("message");

            if(lsStatus.equals(SUCCESS.toString())) {
                foCallBck.onSuccess(lsMessage);
            } else if(lsStatus.equals(FAILED.toString())) {
                foCallBck.onFailed(lsMessage);
            }
        } catch (JSONException e) {
            foCallBck.onFailed(e.getMessage());
        }
    }

    public interface OnViewGCardQrCode{
        void OnView(Bitmap foVal);
    }
    public interface GetBranchCallback {
        void onSuccess(List<EBranchInfo> branchInfos);
    }
    public interface GcardTransactionCallback {
        void onLoad();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
        void onQrGenerate(Bitmap foBitmap);
    }
}
