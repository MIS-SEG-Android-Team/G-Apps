package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.appdriver.base.GConnection;
import org.rmj.apprdiver.util.MiscUtil;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DMobileAddressInfo;
import org.rmj.g3appdriver.dev.Database.DbConnection;
import org.rmj.g3appdriver.dev.Database.Entities.EAddressInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ECountryInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EProvinceInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ETownInfo;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RAddressMobile {
    private static final String TAG = RAddressMobile.class.getSimpleName();

    private final Context mContext;
    private final DAddress poDao;
    private String message;

    public RAddressMobile(Context instance) {
        this.mContext = instance;
        this.poDao = GGC_GuanzonAppDB.getInstance(mContext).AddDao();
    }

    public String getMessage() {
        return message;
    }

    private String GenerateNewAddressID(){
        String lsNextCode = "";
        try{
            GConnection loConn = DbConnection.doConnect(mContext);
            lsNextCode = MiscUtil.getNextCode("Address_Update_Request", "sTransNox", true, loConn.getConnection(), "", 12, false);
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
        }
        return lsNextCode;
    }

    private String GenerateNewContactID(){
        String lsNextCode = "";
        try{
            GConnection loConn = DbConnection.doConnect(mContext);
            lsNextCode = MiscUtil.getNextCode("Mobile_Update_Request", "sTransNox", true, loConn.getConnection(), "", 12, false);
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
        }
        return lsNextCode;
    }

    public LiveData<List<EBarangayInfo>> GetBarangayList(String fsTownID){
        return poDao.GetBarangayList(fsTownID);
    }

    public LiveData<List<DAddress.oTownObj>> GetTownList(){
        return poDao.GetTownList();
    }

    public LiveData<List<ECountryInfo>> GetCountryList(){
        return poDao.GetCountryList();
    }

    public LiveData<String> ParseBrgyID(String fsValue){
        return poDao.GetBrgyName(fsValue);
    }

    public LiveData<String> ParseTownID(String fsValue){
        return poDao.GetTownProvName(fsValue);
    }

    public LiveData<String> GetFullAddressName(String fsBrgyID){
        return poDao.GetFullAddressName(fsBrgyID);
    }

    public ArrayList<String> getBarangayForInput(List<EBarangayInfo> foList){
        ArrayList<String> loBrgyDta = new ArrayList<>();
        for(int x = 0; x < foList.size(); x++){
            loBrgyDta.add(foList.get(x).getBrgyName());
        }
        return loBrgyDta;
    }

    public ArrayList<String> getTownCityForInput(List<DAddress.oTownObj> foList){
        String lsTownProv = "";
        ArrayList<String> loTPList = new ArrayList<>();
        for(int x = 0; x < foList.size(); x++){
            lsTownProv = foList.get(x).sTownNm + ", " + foList.get(x).sProvNm;
            loTPList.add(lsTownProv);
        }
        return loTPList;
    }

    public ArrayList<String> getCountryForInput(List<ECountryInfo> foList){
        ArrayList<String> loCountry = new ArrayList<>();
        for(int x = 0; x < foList.size(); x++){
            if(foList.get(x).getNational() != null && !foList.get(x).getNational().isEmpty()) {
                loCountry.add(foList.get(x).getNational());
            }
        }
        return loCountry;
    }


    public boolean ImportBarangayList(){
        try{
            JSONObject param = new JSONObject();
            param.put("bsearch", true);
            param.put("descript", "All");

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());

            String lsAddress = loApis.getImportBarangayAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());

            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {

                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");

                if(!lsResult.equalsIgnoreCase("success")){

                    JSONObject loError = loResponse.getJSONObject("error");

                    message = loError.getString("message");
                    Log.e(TAG, message);

                    return false;
                } else {

                    Log.d(TAG, "Importing barangay info success.");

                    JSONArray laJson = loResponse.getJSONArray("detail");
                    List<EBarangayInfo> laDetail = new ArrayList<>();

                    for(int x = 0; x < laJson.length(); x++){

                        JSONObject loJson = laJson.getJSONObject(x);
                        EBarangayInfo loDetail = new EBarangayInfo();
                        loDetail.setBrgyIDxx(loJson.getString("sBrgyIDxx"));
                        loDetail.setBrgyName(loJson.getString("sBrgyName"));
                        loDetail.setTownIDxx(loJson.getString("sTownIDxx"));
                        loDetail.setHasRoute(loJson.getString("cHasRoute"));
                        loDetail.setBlackLst(loJson.getString("cBlackLst"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        laDetail.add(loDetail);
                    }

                    poDao.SaveBarangay(laDetail);
                    Log.d(TAG, "Saving barangay info success.");

                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean ImportTownList(){
        try{
            JSONObject param = new JSONObject();
            param.put("bsearch", true);
            param.put("descript", "All");

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getImportTownAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "Importing town info success.");
                    JSONArray laJson = loResponse.getJSONArray("detail");
                    List<ETownInfo> laDetail = new ArrayList<>();
                    for(int x = 0; x < laJson.length(); x++) {
                        JSONObject loJson = laJson.getJSONObject(x);
                        ETownInfo loDetail = new ETownInfo();
                        loDetail.setTownIDxx(loJson.getString("sTownIDxx"));
                        loDetail.setTownName(loJson.getString("sTownName"));
                        loDetail.setZippCode(loJson.getString("sZippCode"));
                        loDetail.setProvIDxx(loJson.getString("sProvIDxx"));
                        loDetail.setMuncplCd(loJson.getString("sMuncplCd"));
                        loDetail.setHasRoute(loJson.getString("cHasRoute"));
                        loDetail.setBlackLst(loJson.getString("cBlackLst"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        laDetail.add(loDetail);
                    }
                    poDao.SaveTown(laDetail);
                    Log.d(TAG, "Saving town info success.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean ImportProvinceList(){
        try{
            JSONObject param = new JSONObject();
            param.put("bsearch", true);
            param.put("descript", "All");

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getImportProvinceAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "Importing province info success.");
                    JSONArray laJson = loResponse.getJSONArray("detail");
                    List<EProvinceInfo> laDetail = new ArrayList<>();
                    for(int x = 0; x < laJson.length(); x++){
                        JSONObject loJson = new JSONObject(laJson.getString(x));
                        EProvinceInfo loDetail = new EProvinceInfo();
                        loDetail.setProvIDxx(loJson.getString("sProvIDxx"));
                        loDetail.setProvName(loJson.getString("sProvName"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat").charAt(0));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        laDetail.add(loDetail);
                    }
                    poDao.SaveProvince(laDetail);
                    Log.d(TAG, "Saving province info success.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean ImportCountryList(){
        try{
            JSONObject param = new JSONObject();
            param.put("bsearch", true);
            param.put("descript", "All");

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getImportCountriesAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "Importing country info success.");
                    JSONArray laJson = loResponse.getJSONArray("detail");
                    List<ECountryInfo> laDetail = new ArrayList<>();
                    for(int x = 0; x < laJson.length(); x++){
                        JSONObject loJson = new JSONObject(laJson.getString(x));
                        ECountryInfo loDetail = new ECountryInfo();
                        loDetail.setCntryCde(loJson.getString("sCntryCde"));
                        loDetail.setCntryNme(loJson.getString("sCntryNme"));
                        loDetail.setNational(loJson.getString("sNational"));
                        loDetail.setRecdStat(loJson.getString("cRecdStat"));
                        loDetail.setTimeStmp(loJson.getString("dTimeStmp"));
                        laDetail.add(loDetail);
                    }
                    poDao.SaveCountry(laDetail);
                    Log.d(TAG, "Saving country info success.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean AddShipAddress(EAddressInfo foValue){
        try{
            EClientInfo loClient = GGC_GuanzonAppDB.getInstance(mContext).EClientDao().GetUserInfo();
            String fsTransNo = String.valueOf(ThreadLocalRandom.current().nextInt());
            if(!fsTransNo.isEmpty()) {
                foValue.setTransNox(fsTransNo);
            } else {
                message = "Unable to generate local id for new address. " + getMessage();
                return false;
            }

            //Save new Address Update.
            DMobileAddressInfo loDao = GGC_GuanzonAppDB.getInstance(mContext).mobAddDao();
            foValue.setSourceCD("MPlc");
            foValue.setSourceNo(loClient.getClientID());
            loDao.SaveAddress(foValue);
            Log.d(TAG, "New address info has been save successfully.");

            JSONObject param = new JSONObject();
            param.put("sTransNox", foValue.getTransNox());
            param.put("sClientID", foValue.getClientID());
            param.put("cReqstCDe", foValue.getReqstCDe()); // 0-> Change, 1->Update, 2-> New
            param.put("cAddrssTp", foValue.getAddrssTp()); // 0-> Present, 1-> Permanent
            param.put("sHouseNox", foValue.getHouseNox());
            param.put("sAddressx", foValue.getAddressx());
            param.put("sTownIDxx", foValue.getTownIDxx());
            param.put("sBrgyIDxx", foValue.getBrgyIDxx());
            param.put("cPrimaryx", foValue.getPrimaryx()); //0-> false, 1-> true
            param.put("nLatitude", "0.0");
            param.put("nLongitud", "0.0");
            param.put("sRemarksx", foValue.getRemarksx());
            param.put("sSourceCD", foValue.getSourceCD());
            param.put("sSourceNo", foValue.getSourceNo());

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getAddressUpdateRequestAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "New address info has been uploaded successfully.");
                    String lsTransNo = loResponse.getString("sTransNox");
                    loDao.UpdateNewAddressID(foValue.getTransNox(),lsTransNo);
                    Log.d(TAG, "New address info has been updated successfully.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean AddContactInfo(EMobileInfo foValue){
        try{
            EClientInfo loClient = GGC_GuanzonAppDB.getInstance(mContext).EClientDao().GetUserInfo();
            String fsTransNo = String.valueOf(ThreadLocalRandom.current().nextInt());
            if(!fsTransNo.isEmpty()) {
//                foValue.setTransNox(fsTransNo);
            } else {
                message = "Unable to generate local id for new address. " + getMessage();
                return false;
            }

            //Save new Address Update.
            DMobileAddressInfo loDao = GGC_GuanzonAppDB.getInstance(mContext).mobAddDao();
//            foValue.setSourceCD("MPlc");
//            foValue.setSourceNo(loClient.getClientID());
            loDao.SaveMobile(foValue);
            Log.d(TAG, "New contact info has been save successfully.");

            JSONObject param = new JSONObject();
//            param.put("sTransNox", foValue.getTransNox());
//            param.put("sClientID", foValue.getClientID());
//            param.put("cReqstCDe", foValue.getReqstCDe());
            param.put("sMobileNo", foValue.getMobileNo());
//            param.put("cPrimaryx", foValue.getPrimaryx());
//            param.put("sRemarksx", foValue.getRemarksx());
//            param.put("sSourceCD", foValue.getSourceCD());
//            param.put("sSourceNo", foValue.getSourceNo());

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getMobileUpdateRequestAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                Log.d(TAG, lsResponse);
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "New contact info has been uploaded successfully.");
                    String lsTransNo = loResponse.getString("sTransNox");
//                    loDao.UpdateNewContactID(foValue.getTransNox(),lsTransNo);
                    Log.d(TAG, "New contact info has been updated successfully.");

                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean UpdateShipAddress(EAddressInfo foValue){
        try{
            //Update new Address Update info.
            DMobileAddressInfo loDao = GGC_GuanzonAppDB.getInstance(mContext).mobAddDao();
            loDao.UpdateAddress(foValue);
            Log.d(TAG, "New address info has been updated successfully.");

            JSONObject param = new JSONObject();
            param.put("sTransNox", foValue.getTransNox());
            param.put("sClientID", foValue.getClientID());
            param.put("cReqstCDe", foValue.getReqstCDe());
            param.put("cAddrssTp", foValue.getAddrssTp());
            param.put("sHouseNox", foValue.getHouseNox());
            param.put("sAddressx", foValue.getAddressx());
            param.put("sTownIDxx", foValue.getTownIDxx());
            param.put("sBrgyIDxx", foValue.getBrgyIDxx());
            param.put("cPrimaryx", foValue.getPrimaryx());
            param.put("nLatitude", "");
            param.put("nLongitud", "");
            param.put("sRemarksx", foValue.getRemarksx());
            param.put("sSourceCD", foValue.getSourceCD());
            param.put("sSourceNo", foValue.getSourceNo());

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getImportCountriesAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "New address info has been uploaded successfully.");
                    String lsTransNo = loResponse.getString("sTransNox");
                    loDao.UpdateNewAddressID(foValue.getTransNox(),lsTransNo);
                    Log.d(TAG, "New address info has been updated successfully.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean UpdateContactInfo(EMobileInfo foValue){
        try{
            //Update new Contact Update info.
            DMobileAddressInfo loDao = GGC_GuanzonAppDB.getInstance(mContext).mobAddDao();
            loDao.SaveMobile(foValue);
            Log.d(TAG, "New contact info has been save successfully.");

            JSONObject param = new JSONObject();
//            param.put("sTransNox", foValue.getTransNox());
//            param.put("sClientID", foValue.getClientID());
//            param.put("cReqstCDe", foValue.getReqstCDe());
            param.put("sMobileNo", foValue.getMobileNo());
//            param.put("cPrimaryx", foValue.getPrimaryx());
//            param.put("sRemarksx", foValue.getRemarksx());
//            param.put("sSourceCD", foValue.getSourceCD());
//            param.put("sSourceNo", foValue.getSourceNo());

            ServerAPIs loApis = new ServerAPIs(new GuanzonAppConfig(mContext).getTestCase());
            String lsAddress = loApis.getImportCountriesAPI();
            String lsResponse = WebClient.httpsPostJSon(lsAddress, param.toString(), new HttpHeaders(mContext).getHeaders());
            if(lsResponse == null){
                message = "Server no response";
                return false;
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);
                String lsResult = loResponse.getString("result");
                if(!lsResult.equalsIgnoreCase("success")){
                    JSONObject loError = loResponse.getJSONObject("error");
                    message = loError.getString("message");
                    Log.e(TAG, message);
                    return false;
                } else {
                    Log.d(TAG, "New contact info has been uploaded successfully.");
                    String lsTransNo = loResponse.getString("sTransNox");
//                    loDao.UpdateNewContactID(foValue.getTransNox(),lsTransNo);
                    Log.d(TAG, "New contact info has been updated successfully.");
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }

    public boolean CheckAddressParameters(){
        try{
            if(poDao.CheckProvinceInfo() == 0){
                ImportProvinceList();
            }
            if(poDao.CheckTownInfo() == 0){
                ImportTownList();
            }
            if(poDao.CheckBarangayInfo() == 0){
                ImportBarangayList();
            }
            if(poDao.CheckCountryInfo() == 0){
                ImportCountryList();
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            message = e.getMessage();
            return false;
        }
    }
}
