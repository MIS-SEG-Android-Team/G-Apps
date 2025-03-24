package org.rmj.guanzongroup.notifications.ViewModel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DNotifications;
import org.rmj.g3appdriver.dev.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.lib.Notifications.NMM;
import org.rmj.g3appdriver.lib.Notifications.NOTIFICATION_STATUS;
import org.rmj.g3appdriver.lib.Notifications.iNotification;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMViewNotification extends AndroidViewModel {
    private static final String TAG = VMViewNotification.class.getSimpleName();
    private final Application instance;
    private final RNotificationInfo poNotif;
    private String lomessage;

    public VMViewNotification(@NonNull Application application) {
        super(application);

        this.instance = application;
        this.poNotif = new RNotificationInfo(application);
    }

    public LiveData<DNotifications.ClientNotificationInfo> GetNotificationInfo(String fsMesgID){
        return poNotif.GetNotificationInfo(fsMesgID);
    }

    public void SendReadResponse(String args, String args1){
        String[] params = {args, args1};
        TaskExecutor.Execute(params, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                String[] paras = (String[]) args;

                String lsMesgID = paras[0];
                String lsMesgTP = paras[1];

                iNotification loSys = new NMM(instance).getInstance(lsMesgTP);
                if(loSys.SendResponse(lsMesgID, NOTIFICATION_STATUS.READ) == null){
                    lomessage = loSys.getMessage();
                    return false;
                }

                lomessage = "Response Sent!";
                return true;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if(!isSuccess){
                    Log.e(TAG, lomessage);
                } else {
                    Log.d(TAG, lomessage);
                }
            }
        });
    }
}