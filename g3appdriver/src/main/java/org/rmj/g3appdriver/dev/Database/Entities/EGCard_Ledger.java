package org.rmj.g3appdriver.dev.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "EGCardLedger", primaryKeys = {"sGCardNox", "sSourceDs", "sReferNox"})
public class EGCard_Ledger {
    @ColumnInfo(name = "sUserIDxx")
    public String sUserIDxx;
    @NonNull
    @ColumnInfo(name = "sGCardNox")
    public String sGCardNox;
    @ColumnInfo(name = "dTransact")
    public String dTransact;
    @NonNull
    @ColumnInfo(name = "sSourceDs")
    public String sSourceDs;
    @NonNull
    @ColumnInfo(name = "sReferNox")
    public String sReferNox;
    @ColumnInfo(name = "nDebitAmt")
    public Double nDebitAmt = 0.00;
    @ColumnInfo(name = "nCreditAmt")
    public Double nCreditAmt = 0.00;
    @ColumnInfo(name = "nTotalPnt")
    public Double nTotalPnt = 0.00;
    @ColumnInfo(name = "sBranchCD")
    public Double sBranchCD;
    @ColumnInfo(name = "dTimeStmp")
    public Double dTimeStmp;

    public String getsUserIDxx() {
        return sUserIDxx;
    }
    public void setsUserIDxx(String sUserIDxx) {
        this.sUserIDxx = sUserIDxx;
    }
    public String getsGCardNox() {
        return sGCardNox;
    }
    public void setsGCardNox(@NonNull String sGCardNox) {
        this.sGCardNox = sGCardNox;
    }
    public String getdTransact() {
        return dTransact;
    }
    public void setdTransact(String dTransact) {
        this.dTransact = dTransact;
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
    public void setsReferNox(@NonNull String sReferNox) {
        this.sReferNox = sReferNox;
    }
    public Double getnDebitAmt() {
        return nDebitAmt;
    }
    public void setnDebitAmt(Double nDebitAmt) {
        this.nDebitAmt = nDebitAmt;
    }
    public Double getnCreditAmt() {
        return nCreditAmt;
    }
    public void setnCreditAmt(Double nCreditAmt) {
        this.nCreditAmt = nCreditAmt;
    }
    public Double getnTotalPnt() {
        return nTotalPnt;
    }
    public void setnTotalPnt(Double nTotalPnt) {
        this.nTotalPnt = nTotalPnt;
    }
    public Double getsBranchCD() {
        return sBranchCD;
    }
    public void setsBranchCD(Double sBranchCD) {
        this.sBranchCD = sBranchCD;
    }
    public Double getdTimeStmp() {
        return dTimeStmp;
    }
    public void setdTimeStmp(Double dTimeStmp) {
        this.dTimeStmp = dTimeStmp;
    }
}
