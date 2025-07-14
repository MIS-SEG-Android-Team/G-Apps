package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "G_Card_Points_Request", primaryKeys = {"sTransNox"})
public class EPointsRequest {

    @NonNull
    @ColumnInfo(name = "sTransNox")
    public String sTransNox;
    @ColumnInfo(name = "sGCardNox")
    public String sGCardNox;
    @ColumnInfo(name = "dTransact")
    public String dTransact;
    @ColumnInfo(name = "sBranchCd")
    public String sBranchCd;
    @ColumnInfo(name = "sReferNox")
    public String sReferNox;
    @ColumnInfo(name = "sSourceCd")
    public String sSourceCd;
    @ColumnInfo(name = "sOTPasswd")
    public String sOTPasswd;
    @ColumnInfo(name = "nTranAmtx")
    public double nTranAmtx;
    @ColumnInfo(name = "nPointsxx")
    public double nPointsxx;
    @ColumnInfo(name = "cTranStat")
    public String cTranStat;
    @ColumnInfo(name = "cSendStat")
    public String cSendStat;
    @ColumnInfo(name = "sUserIDxx")
    public String sUserIDxx;
    @ColumnInfo(name = "sIMEINoxx")
    public String sIMEINoxx;
    @ColumnInfo(name = "dRequestd")
    public String dRequestd;
    @ColumnInfo(name = "dTimeStmp")
    public String dTimeStmp;

    public String getsTransNox() {
        return sTransNox;
    }

    public void setsTransNox(@NonNull String sTransNox) {
        this.sTransNox = sTransNox;
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

    public String getsBranchCd() {
        return sBranchCd;
    }

    public void setsBranchCd(String sBranchCd) {
        this.sBranchCd = sBranchCd;
    }

    public String getsReferNox() {
        return sReferNox;
    }

    public void setsReferNox(String sReferNox) {
        this.sReferNox = sReferNox;
    }

    public String getsSourceCd() {
        return sSourceCd;
    }

    public void setsSourceCd(String sSourceCd) {
        this.sSourceCd = sSourceCd;
    }

    public String getsOTPasswd() {
        return sOTPasswd;
    }

    public void setsOTPasswd(String sOTPasswd) {
        this.sOTPasswd = sOTPasswd;
    }

    public double getnTranAmtx() {
        return nTranAmtx;
    }

    public void setnTranAmtx(double nTranAmtx) {
        this.nTranAmtx = nTranAmtx;
    }

    public double getnPointsxx() {
        return nPointsxx;
    }

    public void setnPointsxx(double nPointsxx) {
        this.nPointsxx = nPointsxx;
    }

    public String getcTranStat() {
        return cTranStat;
    }

    public void setcTranStat(String cTranStat) {
        this.cTranStat = cTranStat;
    }

    public String getcSendStat() {
        return cSendStat;
    }

    public void setcSendStat(String cSendStat) {
        this.cSendStat = cSendStat;
    }

    public String getsUserIDxx() {
        return sUserIDxx;
    }

    public void setsUserIDxx(String sUserIDxx) {
        this.sUserIDxx = sUserIDxx;
    }

    public String getsIMEINoxx() {
        return sIMEINoxx;
    }

    public void setsIMEINoxx(String sIMEINoxx) {
        this.sIMEINoxx = sIMEINoxx;
    }

    public String getdRequestd() {
        return dRequestd;
    }

    public void setdRequestd(String dRequestd) {
        this.dRequestd = dRequestd;
    }

    public String getdTimeStmp() {
        return dTimeStmp;
    }

    public void setdTimeStmp(String dTimeStmp) {
        this.dTimeStmp = dTimeStmp;
    }
}
