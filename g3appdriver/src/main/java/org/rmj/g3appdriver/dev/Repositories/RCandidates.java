package org.rmj.g3appdriver.dev.Repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DVoteLogs;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.lib.Account.AccountInfo;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RCandidates {

    private final DCandidates daoCandidates;
    private final DVoteLogs daoVoteLogs;
    private final AccountInfo loAccount;

    public RCandidates(Context context){
        this.daoCandidates = GGC_GuanzonAppDB.getInstance(context).CandidatesDao();
        this.daoVoteLogs = GGC_GuanzonAppDB.getInstance(context).VoteLogsDao();
        this.loAccount = new AccountInfo(context);
    }

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return daoCandidates.GetAllCandidates(categoryID);
    }

    public LiveData<DVoteLogs.LatestVote> ObserveVoteCounts(String categoryID){
        return daoVoteLogs.ObserveVoteCounts(categoryID, loAccount.getUserID());
    }

    public DVoteLogs.LatestVote GetVoteCount(String categoryID){
        return daoVoteLogs.GetVoteCounts(categoryID, loAccount.getUserID());
    }

    public int GetCandidateVotes(String sGroupIDx, String categoryID){
        return daoVoteLogs.GetCandidateVotes(sGroupIDx, categoryID, loAccount.getUserID());
    }

    public void UpdateTimeStmp(String sGroupIDx, String sEvntIDxx, String dTimeStmp){
        daoCandidates.UpdateTimeStmp(sGroupIDx, sEvntIDxx, dTimeStmp);
    }

}
