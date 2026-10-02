package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "EGCardPoints", primaryKeys = {"sGCardNox", "sSourceDs", "sReferNox"})
public class EGCardPoints {
    @ColumnInfo(name = "sUserIDxx")
    public String sUserIDxx;
    @NonNull
    @ColumnInfo(name = "sGCardNox")
    public String sGCardNox;
    @ColumnInfo(name = "dTransact")
    public String dTransact;
    @ColumnInfo(name = "sBranchCD")
    public String sBranchCD;
    @ColumnInfo(name = "sSourceCD")
    public String sSourceCD;
    @NonNull
    @ColumnInfo(name = "sSourceDs")
    public String sSourceDs;
    @NonNull
    @ColumnInfo(name = "sReferNox")
    public String sReferNox;
    @ColumnInfo(name = "nTranAmtx")
    public Double nTranAmtx = 0.00;
    @ColumnInfo(name = "nPointsxx")
    public Double nPointsxx = 0.00;
    @ColumnInfo(name = "sOTPasswd")
    public String sOTPasswd;
    @ColumnInfo(name = "cSendStat")
    public String cSendStat;
    @ColumnInfo(name = "cTranStat")
    public String cTranStat;
    @ColumnInfo(name = "dTimeStmp")
    public String dTimeStmp;

    public String getsUserIDxx() {
        return sUserIDxx;
    }
    public void setsUserIDxx(String sUserIDxx) {
        this.sUserIDxx = sUserIDxx;
    }
    public String getsGCardNox() {
        return sGCardNox;
    }
    public void setsGCardNox(String sGCardNox) {
        this.sGCardNox = sGCardNox;
    }
    public String getdTransact() {
        return dTransact;
    }
    public void setdTransact(String dTransact) {
        this.dTransact = dTransact;
    }
    public String getsBranchCD() {
        return sBranchCD;
    }
    public void setsBranchCD(String sBranchCD) {
        this.sBranchCD = sBranchCD;
    }
    public String getsSourceCD() {
        return sSourceCD;
    }
    public void setsSourceCD(String sSourceCD) {
        this.sSourceCD = sSourceCD;
    }
    public String getsSourceDs() {
        return sSourceDs;
    }
    public void setsSourceDs(String sSourceDs) {
        this.sSourceDs = sSourceDs;
    }
    public String getsReferNox() {
        return sReferNox;
    }
    public void setsReferNox(String sReferNox) {
        this.sReferNox = sReferNox;
    }
    public Double getnTranAmtx() {
        return nTranAmtx;
    }
    public void setnTranAmtx(Double nTranAmtx) {
        this.nTranAmtx = nTranAmtx;
    }
    public Double getnPointsxx() {
        return nPointsxx;
    }
    public void setnPointsxx(Double nPointsxx) {
        this.nPointsxx = nPointsxx;
    }
    public String getsOTPasswd() {
        return sOTPasswd;
    }
    public void setsOTPasswd(String sOTPasswd) {
        this.sOTPasswd = sOTPasswd;
    }
    public String getcSendStat() {
        return cSendStat;
    }
    public void setcSendStat(String cSendStat) {
        this.cSendStat = cSendStat;
    }
    public String getcTranStat() {
        return cTranStat;
    }
    public void setcTranStat(String cTranStat) {
        this.cTranStat = cTranStat;
    }
    public String getdTimeStmp() {
        return dTimeStmp;
    }
    public void setdTimeStmp(String dTimeStmp) {
        this.dTimeStmp = dTimeStmp;
    }
}
