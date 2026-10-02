package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EAddressInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;

@Dao
public interface DMobileAddressInfo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void SaveAddress(EAddressInfo foVal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void SaveMobile(EMobileInfo foVal);

    @Update
    void UpdateAddress(EAddressInfo foVal);

    @Query("UPDATE Address_Update_Request SET sTransNox =:fsNewTran WHERE sTransNox =:fsOldTran")
    void UpdateNewAddressID(String fsOldTran, String fsNewTran);
}
