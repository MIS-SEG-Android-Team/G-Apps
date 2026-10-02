package org.rmj.guanzongroup.gconnect.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;

public class VMDashboard extends AndroidViewModel {
    private final RClientInfo poClient;
    public VMDashboard(@NonNull Application application) {
        super(application);
        this.poClient = new RClientInfo(application);
    }
    public LiveData<EClientInfo> getClientInfo() {
        return poClient.getClientInfo();
    }
}
