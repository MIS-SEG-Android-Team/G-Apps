package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;

import java.util.List;

@Dao
public interface DPointsRequest {
    @Insert
    void SaveRequest(EPointsRequest pointsRequest);
    @Query("SELECT * FROM G_Card_Points_Request WHERE cSendStat = '0' AND sUserIDxx = :sUserIDxx")
    LiveData<List<EPointsRequest>> GetRequests(String sUserIDxx);
    @Query("UPDATE G_Card_Points_Request SET cSendStat = '1' WHERE sTransNox = :sTransNoxx")
    void SendPointsRequest(String sTransNoxx);
}
