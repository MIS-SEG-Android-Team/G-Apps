package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rmj.g3appdriver.dev.Database.Entities.EPromo;

import java.util.List;

@Dao
public interface DPromo {

    @Insert
    void insert(EPromo ePromo);

    @Query("SELECT * FROM Promo_Link_Info " +
            "WHERE strftime('%Y-%m-%d %H:%H:%S', datetime('now', 'localtime'))  BETWEEN dDateFrom AND dDateThru")
    LiveData<List<EPromo>> getAllPromo();

    @Query("SELECT * FROM PROMO_LINK_INFO WHERE sTransNox =:TransNox")
    EPromo getPromoInfoIfExist(String TransNox);

    @Query("SELECT * FROM Promo_Link_Info ORDER BY dDateFrom DESC LIMIT 1")
    EPromo CheckPromo();

}
