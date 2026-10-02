package org.rmj.guanzongroup.ganado.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

//import org.rmj.g3appdriver.GCircle.room.Entities.EGanadoOnline;
//import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
//import org.rmj.g3appdriver.utils.ConnectionUtil;
import org.rmj.g3appdriver.dev.Database.Entities.EGanadoOnline;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Ganado.Obj.ProductInquiry;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMInquiry extends AndroidViewModel {
    private static final String TAG = VMInquiry.class.getSimpleName();

    private final Ganado poSys;
    private final ConnectionUtil poConn;

    private String message;

    public interface OnTaskExecute{
        void OnExecute();
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMInquiry(@NonNull Application application) {
        super(application);

        poSys = new Ganado(application);
        poConn = new ConnectionUtil(application);
    }
    public EGanadoOnline GetInQuiry(String TransNox){
        return poSys.GetInquiry(TransNox);
    }

    public void RemoveInquiry(){
        poSys.RemoveInquiry();
    }
    public LiveData<List<EGanadoOnline>> GetInquiries(){
        return poSys.GetInquiries();
    }

    public void ImportCriteria(OnTaskExecute listener){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnExecute();
            }

            @Override
            public Object DoInBackground(Object args) {
                if (!poConn.isDeviceConnected()){
                    message = poConn.getMessage();
                    return false;
                }

                if(!poSys.ImportInquiries()){
                    message = poSys.getMessage();
                    return false;
                }

                return true;
            }

            @Override
            public void OnPostExecute(Object object) {
                boolean isSuccess = (boolean) object;
                if(!isSuccess){
                    listener.OnFailed(message);
                    return;
                }

                listener.OnSuccess();
            }
        });
    }
}
