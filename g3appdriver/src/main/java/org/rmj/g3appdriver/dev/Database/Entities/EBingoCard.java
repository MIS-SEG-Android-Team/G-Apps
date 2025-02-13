package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Bingo_Card", primaryKeys = {"cardIDxx", "rowIndex"})
public class EBingoCard {

    @NonNull
    @ColumnInfo(name = "cardIDxx")
    private String cardIDxx;

    @ColumnInfo(name = "colList")
    private String colList;

    @NonNull
    @ColumnInfo(name = "rowIndex")
    private String rowIndex;

    public void setCardIDxx(@NonNull String cardIDxx) {
        this.cardIDxx = cardIDxx;
    }

    @NonNull
    public String getCardIDxx() {
        return cardIDxx;
    }

    public void setColList(String colList) {
        this.colList = colList;
    }

    public String getColList() {
        return colList;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    @NonNull
    public String getRowIndex() {
        return rowIndex;
    }
}
