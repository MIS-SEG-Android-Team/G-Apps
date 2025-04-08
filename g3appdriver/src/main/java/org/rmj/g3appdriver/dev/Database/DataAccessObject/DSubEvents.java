package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import org.rmj.g3appdriver.dev.Database.Entities.ESub_Events;

@Dao
public interface DSubEvents {

    @Upsert
    void save(ESub_Events subEvents);

    @Query("DELETE FROM Sub_Events")
    void deleteall();
}
