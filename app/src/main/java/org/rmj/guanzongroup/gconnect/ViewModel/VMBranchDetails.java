package org.rmj.guanzongroup.gconnect.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RBranchInfo;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMBranchDetails extends AndroidViewModel {
    private final RBranchInfo poBranchx;
    private final RAddressMobile poAddress;
    private final Application instance;
    private final iGCardSystem loGcard;
    private String TAG = getClass().getSimpleName();

    public VMBranchDetails(@NonNull Application application) {
        super(application);
        this.instance = application;
        this.poBranchx = new RBranchInfo(application);
        this.poAddress = new RAddressMobile(application);
        this.loGcard = new GCardSystem(instance).getInstance(GCardSystem.CoreFunctions.EXTRAS);
    }
    public LiveData<List<EBranchInfo>> getMotorBranches() {
        return poBranchx.getMotorBranches();
    }
    public LiveData<List<EBranchInfo>> getMobileBranches() {
        return poBranchx.getMobileBranches();
    }
    public LiveData<List<DAddress.oTownObj>> GetTownList(){
        return poAddress.GetTownList();
    }

    public void DownloadBranches(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    loGcard.DownloadBranchesList(new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            Log.d(TAG, "Branches downloaded");
                        }
                        @Override
                        public void OnFailed(String message) {
                            Log.d(TAG, "Failed to download branches");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, "Finished downloading data.");
            }
        });
    }
}
