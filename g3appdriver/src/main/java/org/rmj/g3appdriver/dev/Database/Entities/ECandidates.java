package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Guanzon_Candidates", primaryKeys = {"sGroupIDx", "sEvntIDxx"})
public class ECandidates {

    @NonNull
    @ColumnInfo(name = "sEvntIDxx")
    public String sEvntIDxx;

    @NonNull
    @ColumnInfo(name = "sGroupIDx")
    public String sGroupIDx;

    @ColumnInfo(name = "sEntryNme")
    public String sEntryNme;

    @ColumnInfo(name = "sSchoolNm")
    public String sSchoolNm;

    @ColumnInfo(name = "sUserIDxx")
    public String sUserIDxx;

    @ColumnInfo(name = "urlImgs")
    public String urlImgs;

    @ColumnInfo(name = "nVotes")
    public int nVotes = 0;

    @ColumnInfo(name = "dVoted")
    public String dVoted;

    @NonNull
    public String getsEvntIDxx() {
        return sEvntIDxx;
    }

    public void setsEvntIDxx(@NonNull String sEvntIDxx) {
        this.sEvntIDxx = sEvntIDxx;
    }

    @NonNull
    public String getsGroupIDx() {
        return sGroupIDx;
    }

    public void setsGroupIDx(@NonNull String sGroupIDx) {
        this.sGroupIDx = sGroupIDx;
    }

    public String getsEntryNme() {
        return sEntryNme;
    }

    public void setsEntryNme(String sEntryNme) {
        this.sEntryNme = sEntryNme;
    }

    public String getsSchoolNm() {
        return sSchoolNm;
    }

    public void setsSchoolNm(String sSchoolNm) {
        this.sSchoolNm = sSchoolNm;
    }

    public String getsUserIDxx() {
        return sUserIDxx;
    }

    public void setsUserIDxx(String sUserIDxx) {
        this.sUserIDxx = sUserIDxx;
    }

    public String getUrlImgs() {
        return urlImgs;
    }

    public void setUrlImgs(String urlImgs) {
        this.urlImgs = urlImgs;
    }

    public int getVotes() {
        return nVotes;
    }

    public void setVotes(int nVotes) {
        this.nVotes = nVotes;
    }

    public String getdVoted() {
        return dVoted;
    }

    public void setdVoted(String dVoted) {
        this.dVoted = dVoted;
    }
}
