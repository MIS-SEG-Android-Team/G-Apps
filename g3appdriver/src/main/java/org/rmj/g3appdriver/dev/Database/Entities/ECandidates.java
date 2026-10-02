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

    @ColumnInfo(name = "urlImgs")
    public String urlImgs;

    @ColumnInfo(name = "dTimeStmp")
    public String dTimeStmp;

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

    public String getUrlImgs() {
        return urlImgs;
    }

    public void setUrlImgs(String urlImgs) {
        this.urlImgs = urlImgs;
    }

    public String getdTimeStmp() {
        return dTimeStmp;
    }

    public void setdTimeStmp(String dTimeStmp) {
        this.dTimeStmp = dTimeStmp;
    }
}
