package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

import java.util.List;

public class RCandidates {

    private final DCandidates daoCandidates;
    private String baseUrl;

    public RCandidates(Context context){
        this.daoCandidates = GGC_GuanzonAppDB.getInstance(context).CandidatesDao();
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

    public LiveData<List<ECandidates>> GetCandidates(String categoryID){
        return daoCandidates.GetAllCandidates(categoryID);
    }
}
