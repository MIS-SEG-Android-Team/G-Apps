package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Vote_History")
public class EVoteLogs {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sTransNoxx")
    public String sTransNoxx;

    @ColumnInfo(name = "dVoted")
    public String dVoted;

    @ColumnInfo(name = "sSubEventIDxx")
    public String sSubEventIDxx;

    @ColumnInfo(name = "sGroupIDx")
    public String sGroupIDx;

    @ColumnInfo(name = "sUserIDxx")
    public String sUserIDxx;

    @ColumnInfo(name = "nNoVotesx")
    public String nNoVotesx;

    @NonNull
    public String getsTransNoxx() {
        return sTransNoxx;
    }

    public void setsTransNoxx(@NonNull String sTransNoxx) {
        this.sTransNoxx = sTransNoxx;
    }

    public String getdVoted() {
        return dVoted;
    }

    public void setdVoted(String dVoted) {
        this.dVoted = dVoted;
    }

    public String getsSubEventIDxx() {
        return sSubEventIDxx;
    }

    public void setsSubEventIDxx(String sSubEventIDxx) {
        this.sSubEventIDxx = sSubEventIDxx;
    }

    public String getsGroupIDx() {
        return sGroupIDx;
    }

    public void setsGroupIDx(String sGroupIDx) {
        this.sGroupIDx = sGroupIDx;
    }

    public String getsUserIDxx() {
        return sUserIDxx;
    }

    public void setsUserIDxx(String sUserIDxx) {
        this.sUserIDxx = sUserIDxx;
    }

    public String getnNoVotesx() {
        return nNoVotesx;
    }

    public void setnNoVotesx(String nNoVotesx) {
        this.nNoVotesx = nNoVotesx;
    }
}
