package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rmj.g3appdriver.dev.Database.Entities.EBingoCard;

import java.util.List;

@Dao
public interface DBingoCard {

    @Insert
    void insert(EBingoCard bingoCard);

    @Query("SELECT COUNT(*) FROM Bingo_Card")
    int count();

    @Query("SELECT * FROM Bingo_Card")
    LiveData<List<EBingoCard>> getCard();

}
