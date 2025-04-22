package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import org.rmj.g3appdriver.dev.Database.Entities.ESub_Events;

import java.util.List;

@Dao
public interface DSubEvents {

    @Upsert
    void save(ESub_Events subEvents);

    @Query("DELETE FROM Sub_Events")
    void deleteall();

    @Query("SELECT * FROM Sub_Events ORDER BY  sSubEventIDxx ASC")
    LiveData<List<ESub_Events>> getEventCategories();
}
