package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Account.AccountVerification;
import org.rmj.g3appdriver.lib.Account.Obj.UserIdentification;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import java.util.List;

public class VMIDVerification extends AndroidViewModel {
    private static final String TAG = AndroidViewModel.class.getSimpleName();
    private final AccountVerification poSys;
    private final ConnectionUtil poConn;
    private String message;

    public VMIDVerification(@NonNull Application application) {
        super(application);
        this.poSys = new AccountVerification(application);
        this.poConn = new ConnectionUtil(application);
    }

    public void ImportIDTypes(OnImportIDTypeListener listener){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnImportIDType("Loan Application", "Initializing valid ids. Please wait...");
            }

            @Override
            public Object DoInBackground(Object args) {
                if(!poConn.isDeviceConnected()){
                    message = "Unable to connect.";
                    return null;
                }

                List<UserIdentification> loList = poSys.ImportIDCode();

                if(loList == null){
                    message = poSys.getMessage();
                    return null;
                }

                return loList;
            }
            @Override
            public void OnPostExecute(Object object) {
                List<UserIdentification> result = (List<UserIdentification>) object;
                if(result == null){
                    listener.OnFailed(message);
                } else {
                    listener.OnSuccess(result);
                }
            }
        });
    }
}
