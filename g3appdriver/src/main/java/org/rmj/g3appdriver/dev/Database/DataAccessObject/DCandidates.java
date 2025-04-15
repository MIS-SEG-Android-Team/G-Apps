package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;

import java.util.List;

@Dao
public interface DCandidates {

    @Insert
    void insert(ECandidates candidates);

    @Query("DELETE FROM Guanzon_Candidates")
    void deleteAll();

    @Query("SELECT * " +
            "FROM Guanzon_Candidates " +
            "WHERE sEvntIDxx = :categoryID ORDER BY sGroupIDx ASC")
    LiveData<List<ECandidates>> GetAllCandidates(String categoryID);

}
