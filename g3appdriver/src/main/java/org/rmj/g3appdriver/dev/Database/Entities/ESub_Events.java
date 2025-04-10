package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Sub_Events", primaryKeys = "sSubEventIDxx")
public class ESub_Events {

    @NonNull
    @ColumnInfo(name = "sSubEventIDxx")
    public String sSubEventIDxx;

    @ColumnInfo(name = "sDescript")
    public String sDescript;

    @ColumnInfo(name = "sImageURL")
    public String sImageURL;

    @ColumnInfo(name = "sEventIDx")
    public String sEventIDx;

    @ColumnInfo(name = "nEntryNox")
    public String nEntryNox;

    @ColumnInfo(name = "cOnlineVt")
    public String cOnlineVt;

    @NonNull
    public String getsSubEventIDxx() {
        return sSubEventIDxx;
    }

    public void setsSubEventIDxx(@NonNull String sSubEventIDxx) {
        this.sSubEventIDxx = sSubEventIDxx;
    }

    public String getsDescript() {
        return sDescript;
    }

    public void setsDescript(String sDescript) {
        this.sDescript = sDescript;
    }

    public String getsImageURL() {
        return sImageURL;
    }

    public void setsImageURL(String sImageURL) {
        this.sImageURL = sImageURL;
    }

    public String getsEventIDx() {
        return sEventIDx;
    }

    public void setsEventIDx(String sEventIDx) {
        this.sEventIDx = sEventIDx;
    }

    public String getnEntryNox() {
        return nEntryNox;
    }

    public void setnEntryNox(String nEntryNox) {
        this.nEntryNox = nEntryNox;
    }

    public String getcOnlineVt() {
        return cOnlineVt;
    }

    public void setcOnlineVt(String cOnlineVt) {
        this.cOnlineVt = cOnlineVt;
    }
}
