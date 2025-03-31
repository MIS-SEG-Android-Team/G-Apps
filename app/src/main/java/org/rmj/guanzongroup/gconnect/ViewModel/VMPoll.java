package org.rmj.guanzongroup.gconnect.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Repositories.RCandidates;

import java.util.List;

public class VMPoll extends AndroidViewModel {

    private final RCandidates poCandidates;

    public VMPoll(@NonNull Application application) {
        super(application);

        poCandidates = new RCandidates(application);
    }

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return poCandidates.GetCandidates(categoryID);
    }
}
