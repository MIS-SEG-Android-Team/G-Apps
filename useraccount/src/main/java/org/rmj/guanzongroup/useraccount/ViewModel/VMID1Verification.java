package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.SessionManager;
import org.rmj.g3appdriver.lib.Account.AccountVerification;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.useraccount.Etc.IDDetail;

public class VMID1Verification extends AndroidViewModel {
    private final AccountVerification poSys;
    private final ConnectionUtil poConn;
    private final SessionManager poSession;
    private String lomessage;

    public VMID1Verification(@NonNull Application application) {
        super(application);
        this.poSys = new AccountVerification(application);
        this.poConn = new ConnectionUtil(application);
        this.poSession = new SessionManager(application);
    }

    public String getUserID(){
        return poSession.getUserID();
    }
    public void SubmitIDPicture(IDDetail foVal, OnSubmitIDPictureListener listener){
        TaskExecutor.Execute(foVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnSubmit("Loan Application", "Uploading id picture. Please wait...");
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = "Unable to connect.";
                        return false;
                    }

                    IDDetail loDetail = (IDDetail) args;
                    String lsFilePath = loDetail.getsFrntPath();
                    String lsFileName = loDetail.getsFrontImg();

                    if(!poSys.SubmitIDVerification(lsFilePath, lsFileName)){
                        lomessage = poSys.getMessage();
                        return false;
                    }

                    if(!loDetail.getcHasBackx().equalsIgnoreCase("1")){
                        return true;
                    }

                    Thread.sleep(1000);

                    lsFilePath = loDetail.getsBackPath();
                    lsFileName = loDetail.getsBackImgx();
                    if(!poSys.SubmitIDVerification(lsFilePath, lsFileName)){
                        lomessage = poSys.getMessage();
                        return false;
                    }

                    return true;
                } catch (Exception e){
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if(!isSuccess){
                    listener.OnFailed(lomessage);
                } else {
                    listener.OnSuccess("Id picture uploaded successfully.");
                }
            }
        });
    }
}