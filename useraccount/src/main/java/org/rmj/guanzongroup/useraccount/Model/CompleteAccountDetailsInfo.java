package org.rmj.guanzongroup.useraccount.Model;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CompleteAccountDetailsInfo {
    private EClientInfo poClientx;
    private String sLastName = "";
    private String sFirstNme = "";
    private String sMiddName = "-";
    private String sSuffixNm = "";
    private String sBirthDte = "";
    private String sBirthPlc = "";
    private String sGenderxx = "";
    private String sCivilStx = "";
    private String sCitizenx = "";
    private String sGCashNox = "";
    private String sTaxIdNox = "";
    private String sHouseNox = "";
    private String sAddressx = "";
    private String sTownCity = "";
    private String sBarangay = "";
    private String sMessagex = "";

    public CompleteAccountDetailsInfo() {
        this.poClientx = new EClientInfo();
    }

    public String getMessage() {
        return sMessagex;
    }

    public String getLastName() {
        return sLastName;
    }

    public void setLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getFirstName() {
        return sFirstNme;
    }

    public void setFirstName(String sFirstNme) {
        this.sFirstNme = sFirstNme;
    }

    public String getMiddName() {
        return sMiddName;
    }
    public String getsGCashNox() {
        return sGCashNox;
    }
    public void setsGCashNox(String sGCashNox) {
        this.sGCashNox = sGCashNox;
    }
    public void setMiddName(String sMiddName) {
        this.sMiddName = sMiddName;
    }

    public String getSuffixName() {
        return sSuffixNm;
    }

    public void setSuffixName(String sSuffixNm) {
        this.sSuffixNm = sSuffixNm;
    }

    public String getBirthDate() {
        return sBirthDte;
    }

    public void setBirthDate(String sBirthDte) {
        this.sBirthDte = sBirthDte;
    }

    public String getBirthPlace() {
        return sBirthPlc;
    }

    public void setBirthPlace(String sBirthPlc) {
        this.sBirthPlc = sBirthPlc;
    }

    public String getGender() {
        return sGenderxx;
    }

    public void setGender(String sGenderxx) {
        this.sGenderxx = sGenderxx;
    }

    public String getCivilStat() {
        return sCivilStx;
    }

    public void setCivilStat(String sCivilStx) {
        this.sCivilStx = sCivilStx;
    }

    public String getCitizenship() {
        return sCitizenx;
    }

    public void setCitizenship(String sCitizenx) {
        this.sCitizenx = sCitizenx;
    }

    public String getTaxIdNumber() {
        return sTaxIdNox;
    }

    public void setTaxIdNumber(String sTaxIdNox) {
        this.sTaxIdNox = sTaxIdNox;
    }

    public String getHouseNumber() {
        return sHouseNox;
    }

    public void setHouseNumber(String sHouseNox) {
        this.sHouseNox = sHouseNox;
    }

    public String getAddress() {
        return sAddressx;
    }

    public void setAddress(String sAddressx) {
        this.sAddressx = sAddressx;
    }

    public String getTownCity() {
        return sTownCity;
    }

    public void setTownCity(String sTownCity) {
        this.sTownCity = sTownCity;
    }

    public String getBarangay() {
        return sBarangay;
    }
    public void setBarangay(String sBarangay) {
        this.sBarangay = sBarangay;
    }

    public boolean isDataValid() {
        if(sLastName.trim().isEmpty()) {
            sMessagex = "Please enter Last Name";
            return false;
        } else if(sFirstNme.trim().isEmpty()) {
            sMessagex = "Please enter First Name";
            return false;
        } else if(sMiddName.trim().isEmpty()) {
            sMessagex = "Please enter Middle Name";
            return false;
        } else if(sGenderxx.trim().isEmpty()) {
            sMessagex = "Please select a Gender";
            return false;
        } else if(sCivilStx.trim().isEmpty()) {
            sMessagex = "Please select a Civil Status";
            return false;
        } else if(sTownCity.trim().isEmpty()) {
            sMessagex = "Please enter Town or City Address";
            return false;
        } else if(sBarangay.trim().isEmpty()) {
            sMessagex = "Please enter Barangay Address";
            return false;
        } else {
            return true;
        }
    }

    public EClientInfo getClientEntityValues() {
        if(isDataValid()) {
            poClientx.setLastName(getLastName());
            poClientx.setFrstName(getFirstName());
            poClientx.setMiddName(getMiddName());
            poClientx.setSuffixNm(getSuffixName());
            poClientx.setBirthDte(getBirthDate());
            poClientx.setBirthPlc(getBirthPlace());
            poClientx.setGenderCd(getGender());
            poClientx.setCvilStat(getCivilStat());
            poClientx.setCitizenx(getCitizenship());
            poClientx.setGCashNo(getsGCashNox());
            poClientx.setTaxIDNox(getTaxIdNumber());
            poClientx.setHouseNo1(getHouseNumber());
            poClientx.setAddress1(getAddress());
            poClientx.setTownIDx1(getTownCity());
            poClientx.setBrgyIDx1(getBarangay());
            poClientx.setHouseNo2(getHouseNumber());
            poClientx.setAddress2(getAddress());
            poClientx.setTownIDx2(getTownCity());
            poClientx.setBrgyIDx2(getBarangay());
            poClientx.setVerified(1);
            return poClientx;
        } else {
            return null;
        }
    }

}
