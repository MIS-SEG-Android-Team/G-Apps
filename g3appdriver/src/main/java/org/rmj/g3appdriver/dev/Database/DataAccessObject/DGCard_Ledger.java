package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EGCard_Ledger;

@Dao
public interface DGCard_Ledger {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Save(EGCard_Ledger ledger);
    @Update
    void Update(EGCard_Ledger ledger);
}
