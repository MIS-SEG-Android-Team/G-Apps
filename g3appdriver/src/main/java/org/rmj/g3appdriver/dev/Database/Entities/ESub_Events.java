package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Sub_Events", primaryKeys = "sSubEventIDxx")
public class ESub_Events {

    @NonNull
    @ColumnInfo(name = "sSubEventIDxx")
    private String sSubEventIDxx;

    @ColumnInfo(name = "sDescript")
    private String sDescript;

    @ColumnInfo(name = "sImageURL")
    private String sImageURL;

    @ColumnInfo(name = "sEventIDx")
    private String sEventIDx;

    @ColumnInfo(name = "nEntryNox")
    private String nEntryNox;

    @ColumnInfo(name = "cOnlineVt")
    private String cOnlineVt;

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
