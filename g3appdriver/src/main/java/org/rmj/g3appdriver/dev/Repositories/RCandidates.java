package org.rmj.g3appdriver.dev.Repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
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
    private final AccountInfo loAccount;
    private String baseUrl;

    public RCandidates(Context context){
        this.daoCandidates = GGC_GuanzonAppDB.getInstance(context).CandidatesDao();
        this.loAccount = new AccountInfo(context);
    }

    private void initBaseDir(String category){

        switch (category) {

            case "dreamboy":
                baseUrl = "http://192.165.10.65/candidatesimage/dreamboy/";
                break;

            case "campusprincess":
                baseUrl = "http://192.165.10.65/candidatesimage/campusprincess/";


            case "bikerbake":
                baseUrl = "http://192.165.10.65/candidatesimage/bikerbabe/";
                break;

            case "guanzonbulilit":
                baseUrl = "http://192.165.10.65/candidatesimage/guanzonbulilit/";
                break;
        }

    }

    private String GetDTimeStmp(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        }else {

            Date current = Calendar.getInstance().getTime();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return dtFormat.format(current);

        }

    }

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return daoCandidates.GetAllCandidates(categoryID);
    }

    public LiveData<DCandidates.LatestVote> ObserveVoteCounts(String categoryID){
        return daoCandidates.ObserveVoteCounts(categoryID, loAccount.getUserID());
    }

    public DCandidates.LatestVote GetVoteCount(String categoryID){
        return daoCandidates.GetVoteCount(categoryID, loAccount.getUserID());
    }

    public void SubmitVote(String pageantID, String categoryID){
        daoCandidates.SubmitVote(GetDTimeStmp(), pageantID, categoryID);
    }
}
