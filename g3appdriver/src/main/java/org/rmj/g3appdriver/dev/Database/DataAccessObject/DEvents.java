package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EEvents;

import java.util.List;

@Dao
public interface DEvents {

    @Insert
    void insert(EEvents events);

    @Insert
    void insertBulkData(List<EEvents> events);

    @Update
    void update(EEvents events);

    @Query("SELECT * FROM App_Event_Info ORDER BY sTransNox ASC")
    LiveData<List<EEvents>> getAllEvents();

    @Query("SELECT * FROM App_Event_Info ORDER BY dEvntFrom")
    List<EEvents> CheckEvent();
}
