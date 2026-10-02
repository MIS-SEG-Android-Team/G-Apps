package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EGCardPoints;

@Dao
public interface DGCard_Points {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Save(EGCardPoints egCardPoints);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void Update(EGCardPoints egCardPoints);
    @Query("UPDATE EGCardPoints SET cTranStat = :cTranStat")
    void UpdateVerified(String cTranStat);
}
