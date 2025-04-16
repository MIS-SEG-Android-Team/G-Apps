package org.rmj.guanzongroup.gconnect.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DVoteLogs;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.ESub_Events;
import org.rmj.g3appdriver.dev.Repositories.RCandidates;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

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

    public EEvents getEventByID(String eventID) {
        poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
        return poSystem.GetEventByID(eventID);
    }

    public LiveData<List<ESub_Events>> GetCategories(){
        poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
        return poSystem.GetEventCategories();
    }

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return poCandidates.GetCandidates(categoryID);
    }

    public LiveData<DVoteLogs.LatestVote> ObserveVoteCounts(String categoryID){
        return poCandidates.ObserveVoteCounts(categoryID);
    }

    public DVoteLogs.LatestVote CountCategoryVotes(String categoryID){
        return poCandidates.GetVoteCount(categoryID);
    }
    public int GetCandidateVotes(String sGroupIDx, String categoryID){
        return poCandidates.GetCandidateVotes(sGroupIDx, categoryID);
    }

    public void UpdateTimeStmp(String sGroupIDx, String sEvntIDxx, String dTimeStmp){
        poCandidates.UpdateTimeStmp(sGroupIDx, sEvntIDxx, dTimeStmp);
    }

    public void SubmitVote(String sGroupIDxx, onSubmitVote callback){

        TaskExecutor.Execute(sGroupIDxx, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoad();
            }

            @Override
            public Object DoInBackground(Object args) {

                try{

                    poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
                    return poSystem.SubmitVote((String) args);

                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

            }

            @Override
            public void OnPostExecute(Object object) {

                callback.onResult((Boolean) object);
            }
        });
    }

    public interface onSubmitVote{
        void onLoad();
        void onResult(Boolean result);
    }

}
