package org.rmj.guanzongroup.gconnect.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Repositories.RCandidates;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;

import java.util.List;

public class VMPoll extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private iGCardSystem poSystem;
    private final RCandidates poCandidates;

    public VMPoll(@NonNull Application application) {
        super(application);

        this.mContext = application;
        poCandidates = new RCandidates(application);
    }

    public LiveData<List<EEvents>> getEvents() {
        poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
        return poSystem.GetNewsEvents();
    }

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return poCandidates.GetCandidates(categoryID);
    }

    public LiveData<DCandidates.LatestVote> ObserveVoteCounts(String categoryID){
        return poCandidates.ObserveVoteCounts(categoryID);
    }

    public DCandidates.LatestVote CountCategoryVotesOfTheDay(String categoryID){
        return poCandidates.GetVoteCount(categoryID);
    }

    public void SubmitVote(String pageantID, String categoryID){
        poCandidates.SubmitVote(pageantID, categoryID);
    }
}
