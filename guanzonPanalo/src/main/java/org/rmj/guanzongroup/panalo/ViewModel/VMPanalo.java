package org.rmj.guanzongroup.panalo.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EPanaloReward;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Panalo.GPanalo;
import org.rmj.g3appdriver.lib.Panalo.PanaloRewards;
import org.rmj.g3appdriver.lib.Promotions.GPromos;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMPanalo extends AndroidViewModel {
    private final GPromos poPromos;
    private final GPanalo poSys;
    private final ConnectionUtil poConn;
    private String lomessage;

    public VMPanalo(@NonNull Application application) {
        super(application);
        this.poPromos = new GPromos(application);
        this.poSys = new GPanalo(application);
        this.poConn = new ConnectionUtil(application);
    }
    public LiveData<List<EPromo>> getPromotions(){
        return poPromos.GetPromotions();
    }
    public LiveData<EPanaloReward> GetPanaloNotice(){
        return poSys.GetPanaloNotice();
    }

    public void ImportPanaloRewards(String args, OnImportPanaloRewards listener){
        TaskExecutor.Execute(args, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnImport();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = "Unable to connect";
                        return null;
                    }

                    List<PanaloRewards> loRewards = poSys.GetRewards(args.toString());
                    if(loRewards == null){
                        lomessage = poSys.getMessage();
                        return null;
                    }

                    return loRewards;
                } catch (Exception e){
                    lomessage = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                List<PanaloRewards> result = (List<PanaloRewards>) object;
                if(result == null){
                    listener.OnFailed(lomessage);
                } else {
                    listener.OnSuccess(result);
                }
            }
        });
    }
    public interface OnImportPanaloRewards{
        void OnImport();
        void OnSuccess(List<PanaloRewards> rewards);
        void OnFailed(String message);
    }
}