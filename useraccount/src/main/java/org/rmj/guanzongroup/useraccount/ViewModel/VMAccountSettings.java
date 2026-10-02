package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;

public class VMAccountSettings extends AndroidViewModel {
    private final RClientInfo poClientx;
    public VMAccountSettings(@NonNull Application application) {
        super(application);
        this.poClientx = new RClientInfo(application);
    }

    public LiveData<EClientInfo> getClientInfo() {
        return poClientx.getClientInfo();
    }

}