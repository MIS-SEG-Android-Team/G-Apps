package org.rmj.guanzongroup.panalo.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EPanaloReward;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.lib.Panalo.GPanalo;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMPanaloDashboard extends AndroidViewModel {
    private static final String TAG = VMPanaloDashboard.class.getSimpleName();
    private final GPanalo poSys;
    private iGCardSystem poSystem;
    private final ConnectionUtil poConn;
    char cResult;
    String tranSrc;
    String message;
    Boolean isSuccess;

    public interface OnActionCallback{
        void OnLoad();
        void OnSuccess(String args);
        void OnFailed(String args);
    }

    public VMPanaloDashboard(@NonNull Application application) {
        super(application);
        this.poSystem = new GCardSystem(application).getInstance(GCardSystem.CoreFunctions.GCARD);
        this.poSys = new GPanalo(application);
        this.poConn = new ConnectionUtil(application);
    }

    public LiveData<EPanaloReward> GetPanaloNotice(){
        return poSys.GetPanaloNotice();
    }

    public void ParseQrCode(String fsArgs, GCardSystem.ParseQrCodeCallback callback){
        TaskExecutor.Execute(fsArgs, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try{
                    String params = (String) args;
                    poSystem.ParseQrCode(params, new GCardSystem.ParseQrCodeCallback() {
                        @Override
                        public void ApplicationResult(String src, Object args) {
                            cResult = '0';
                            tranSrc = src;
                            message = args.toString();
                        }
                        @Override
                        public void TransactionResult(String src, Object args) {
                            cResult = '1';
                            tranSrc = src;
                            message = args.toString();
                        }
                        @Override
                        public void OnFailed(String args) {
                            cResult = '2';
                            message = args;
                        }
                    });
                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                    message = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean) {
                    switch (cResult) {
                        case '0':
                            callback.ApplicationResult(tranSrc, message);
                            break;
                        case '1':
                            callback.TransactionResult(tranSrc, message);
                            break;
                        default:
                            callback.OnFailed(message);
                            break;
                    }
                } else {
                    callback.OnFailed(message);
                }
            }
        });
    }
    public void AddNewGCard(String fsVal, OnActionCallback callback){
        TaskExecutor.Execute(fsVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.OnLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String params = (String) args;
                    if(poConn.isDeviceConnected()) {
                        poSystem.AddGCardQrCode(params, new GCardSystem.GCardSystemCallback() {
                            @Override
                            public void OnSuccess(String args) {
                                isSuccess = true;
                                message = args;
                            }
                            @Override
                            public void OnFailed(String fsMsg) {
                                isSuccess = false;
                                message = fsMsg;
                            }
                        });
                    } else {
                        isSuccess = false;
                        message = "Unable to connect";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                    message = e.getMessage();
                }
                return isSuccess;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if(res){
                    callback.OnSuccess(message);
                } else {
                    callback.OnFailed(message);
                }
            }
        });
    }
}