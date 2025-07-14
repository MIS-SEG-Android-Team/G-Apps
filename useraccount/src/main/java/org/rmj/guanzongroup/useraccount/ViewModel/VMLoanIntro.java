package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Account.AccountVerification;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMLoanIntro extends AndroidViewModel {
    private static final String TAG = VMLoanIntro.class.getSimpleName();
    private final AccountVerification poVerify;
    private final ConnectionUtil poConn;
    private String lomessage;

    public VMLoanIntro(@NonNull Application application) {
        super(application);
        this.poVerify = new AccountVerification(application);
        this.poConn = new ConnectionUtil(application);
    }

    public void GetMobileNo(OnGetMobileNo listener){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try{
                    return poVerify.GetMobileNo();
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                String mobileno = object.toString();
                listener.OnRetrieve(mobileno);
            }
        });
    }

    public void SendOtp(String fsVal, OnSendOtpListener listener){
        TaskExecutor.Execute(fsVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnRequest("Loan Application", "Sending OTP. Please wait...");
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = "Unable to connect.";
                        return null;
                    }

                    String result = poVerify.SendOTP(args.toString());
                    if(result == null){
                        lomessage = poVerify.getMessage();
                        return null;
                    }

                    return result;
                } catch (Exception e){
                    e.printStackTrace();
                    lomessage = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                String result = object.toString();
                if(result == null){
                    listener.OnFailed(lomessage);
                } else {
                    listener.OnSuccess(result);
                }
            }
        });
    }

    public interface OnGetMobileNo{
        void OnRetrieve(String args);
    }

    public interface OnSendOtpListener{
        void OnRequest(String title, String message);
        void OnSuccess(String args);
        void OnFailed(String message);
    }
}
