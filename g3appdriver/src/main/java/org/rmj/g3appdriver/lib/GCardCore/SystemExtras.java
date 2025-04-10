package org.rmj.g3appdriver.lib.GCardCore;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DBranchInfo;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DEvents;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DPromo;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DSubEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ESub_Events;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.GCardCore.Obj.CartItem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemExtras implements iGCardSystem{
    private static final String TAG = SystemExtras.class.getSimpleName();

    private final Context mContext;

    private final DBranchInfo poBranch;
    private final DPromo poPromo;
    private final DEvents poEvents;
    private final DSubEvents poSubEvnts;
    private final DCandidates poCandidates;
    private final HttpHeaders poHeaders;
    private final GuanzonAppConfig poConfig;
    private final ServerAPIs poAPI;

    public SystemExtras(Context context) {
        this.mContext = context;
        this.poBranch = GGC_GuanzonAppDB.getInstance(mContext).EBranchDao();
        this.poPromo = GGC_GuanzonAppDB.getInstance(mContext).EPromoDao();
        this.poEvents = GGC_GuanzonAppDB.getInstance(mContext).EventDao();
        this.poSubEvnts = GGC_GuanzonAppDB.getInstance(mContext).SubEvntsDao();
        this.poCandidates = GGC_GuanzonAppDB.getInstance(mContext).CandidatesDao();

        this.poHeaders = new HttpHeaders(mContext);
        this.poConfig = new GuanzonAppConfig(mContext);
        this.poAPI = new ServerAPIs(poConfig.getTestCase());
    }

    @Override
    public void AddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }

    @Override
    public List<EGcardApp> hasActiveGcard() {
        return null;
    }

    @Override
    public LiveData<List<EGcardApp>> GetGCardList() {
        return null;
    }
    @Override
    public LiveData<EGcardApp> hasNoGcard() {
        return null;
    }
    @Override
    public LiveData<List<EGcardApp>> hasUnCheckGCard() {
        return null;
    }
    @Override
    public LiveData<EGcardApp> getGCardInfo() {
        return null;
    }
    @Override
    public LiveData<List<EPointsRequest>> GetPointsRqsts() {
        return null;
    }

    @Override
    public void updateGCardActiveStatus(String GCardNmbr) {
        throw new NullPointerException();
    }

    @Override
    public double getRemainingActiveCardPoints() {
        return 0;
    }
    @Override
    public void updateGCardDeactiveStatus() {
        throw new NullPointerException();
    }
    @Override
    public void SavePointsRqst(EPointsRequest loRqst) {
    }
    @Override
    public void UpdateSendPointsRqst(String sTransNoxx) {
    }

    @Override
    public void AddGCardQrCode(String GcardNo, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void ConfirmAddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void DownloadGcardNumbers(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveGCardInfo(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void ActivateGcard(String GcardNo) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public Bitmap GenerateGCardQrCode() throws Exception {
        return null;
    }
    @Override
    public void ParseQrCode(String val, GCardSystem.ParseQrCodeCallback callback) throws Exception {

    }
    @Override
    public HashMap<String, String> ScanTDS() {
        return null;
    }
    @Override
    public Boolean DownloadGcardPoints(HashMap<String, String> params) {
        return true;
    }
    @Override
    public void DownloadRedeemables(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveRedeemables(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public LiveData<List<Double>> GetRedeemablePointsFilter() {
        return null;
    }
    @Override
    public LiveData<List<ERedeemablesInfo>> GetRedeemablesList() {
        return null;
    }
    @Override
    public LiveData<List<ERedeemablesInfo>> GetRedeemablesList(String fsVal) {
        return null;
    }
    @Override
    public void AddToCart(CartItem item, GCardSystem.GCardSystemCallback callback) {
        throw new NullPointerException();
    }
    @Override
    public void UpdateCartItem(CartItem item, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public LiveData<List<DRedeemItemInfo.GCardCartItem>> GetCartItems() {
        return null;
    }
    @Override
    public List<EBranchInfo> GetMCBranchesForRedemption() {
        return null;
    }
    @Override
    public LiveData<Integer> GetGcardCartItemCount() {
        return null;
    }
    @Override
    public LiveData<Double> GetGCardCartItemTotalPoints() {
        return null;
    }
    @Override
    public void DeleteItemCart(String fsVal) {
        throw new NullPointerException();
    }
    @Override
    public void PlaceOrder(List<DRedeemItemInfo.GCardCartItem> redeemables, String BranchCD, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public Bitmap GenerateGCardOrderQrCode(String BatchNox) throws Exception {
        return null;
    }
    @Override
    public void DownloadTransactions(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveTransactions(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> GetGcardTransactions() {
        return null;
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> GetPointsEntryTransactions() {
        return null;
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> GetRedemptionTransactions() {
        return null;
    }
    @Override
    public void DownloadMCServiceInfo(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void DownloadRegistrationInfo(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveMcServiceInfo(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveRegistrationInfo(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void ScheduleNextServiceDate(String date, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void DownloadBranchesList(GCardSystem.GCardSystemCallback callback) throws Exception {
        JSONObject params = new JSONObject();
        String lsResponse = WebClient.httpsPostJSon(poAPI.getImportBranchesAPI(), params.toString(), poHeaders.getHeaders());
        if(lsResponse == null){
            callback.OnFailed("Server no response.");
            Log.d(TAG, "Unable to retrieve data from server. Server no response.");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("success")){
                callback.OnSuccess(loResponse.toString());
                SaveBranchesList(loResponse);
                Log.d(TAG, "Branch records retrieve successfully.");
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
                Log.d(TAG, "Unable to retrieve records from server. Message : " + lsMessage);
            }
        }
    }
    @Override
    public void SaveBranchesList(JSONObject detail) throws Exception {
        JSONArray laDetail = detail.getJSONArray("detail");

        for(int x = 0; x < laDetail.length(); x++){
            JSONObject loJson = laDetail.getJSONObject(x);

            EBranchInfo loBranch = poBranch.getBranchIfExist(loJson.getString("sBranchCD"));
            if(loBranch == null){
                //check the records from API, if record status is not equal to 1, record is inactive, do not insert
                    // insert saving method inside...
                    EBranchInfo info = new EBranchInfo();
                    info.setBranchCd(loJson.getString("sBranchCD"));
                    info.setBranchNm(loJson.getString("sBranchNm"));
                    info.setDescript(loJson.getString("sDescript"));
                    info.setAddressx(loJson.getString("sAddressx"));
                    info.setTownIDxx(loJson.getString("sTownIDxx"));

                    if(loJson.has("nLatitude") &&
                    loJson.has("nLongtude") &&
                    !loJson.getString("nLatitude").isEmpty() &&
                    !loJson.getString("nLongtude").isEmpty()) {
                        info.setLatitude(Double.parseDouble(loJson.getString("nLatitude")));
                        info.setLongtude(Double.parseDouble(loJson.getString("nLongtude")));
                    }

                    info.setTelNumbr(loJson.getString("sTelNumbr"));
                    info.setEmailAdd(loJson.getString("sEMailAdd"));
                    poBranch.insert(info);
                    Log.d(TAG, "New record save!");
            }
        }
    }

    @Override
    public LiveData<List<EBranchInfo>> GetMobileBranchList() {
        return poBranch.getMobileBranches();
    }
    @Override
    public LiveData<List<EBranchInfo>> GetMotorcycleBranchList() {
        return poBranch.getMotorBranches();
    }

    @Override
    public void DownloadPromotions(GCardSystem.GCardSystemCallback callback) throws Exception {

        JSONObject params = new JSONObject();
        String lsResponse = WebClient.httpsPostJSon(poAPI.getImportPromosAPI(), params.toString(), poHeaders.getHeaders());

        if(lsResponse == null){
            callback.OnFailed("Server no response.");
            Log.d(TAG, "Unable to retrieve data from server. Server no response.");
        } else {

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if(lsResult.equalsIgnoreCase("success")){

                callback.OnSuccess(loResponse.toString());
                SavePromotions(loResponse);
                Log.d(TAG, "Promo records retrieve successfully.");
            } else {

                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
                Log.d(TAG, "Unable to retrieve records from server. Message : " + lsMessage);
            }
        }
    }

    @Override
    public void SavePromotions(JSONObject detail) throws Exception {

        JSONArray laDetail = detail.getJSONArray("detail");

        for(int x = 0; x < laDetail.length(); x++){
            JSONObject loJson = laDetail.getJSONObject(x);

            EPromo loPromo = poPromo.getPromoInfoIfExist(loJson.getString("sTransNox"));
            if(loPromo == null) {
                //check the records from API, if record status is not equal to 1, record is inactive, do not insert
                    // insert saving method inside...
                    EPromo info = new EPromo();
                    info.setTransNox(loJson.getString("sTransNox"));
                    info.setDivision(loJson.getInt("cDivision"));
                    info.setTransact(loJson.getString("dTransact"));
                    info.setImageUrl(loJson.getString("sImageURL"));
                    info.setImageSld(loJson.getString("sImageNme"));
                    info.setPromoUrl(loJson.getString("sPromoURL"));
                    info.setCaptionx(loJson.getString("sCaptionx"));
                    info.setDateFrom(loJson.getString("dDateFrom"));
                    info.setDateThru(loJson.getString("dDateThru"));

                    poPromo.insert(info);
                    Log.d(TAG, "New record save!");
            }
        }
    }

    @Override
    public LiveData<List<EPromo>> GetPromotions() {
        return poPromo.getAllPromo();
    }

    @Override
    public EPromo CheckPromo() {
        return poPromo.CheckPromo();
    }

    @Override
    public void DownloadNewsEvents(GCardSystem.GCardSystemCallback callback) throws Exception {

        try {

            JSONObject params = new JSONObject();
            List<String> laResponse = List.of(
                    poAPI.getImportEventsAPI(),
                    poAPI.getImportSubEvents()
            );

            for (String url: laResponse){

                String lsResponse = WebClient.httpsPostJSon(url, params.toString(), poHeaders.getHeaders());

                if(lsResponse == null){
                    callback.OnFailed("Server no response.");
                } else {

                    JSONObject loResponse = new JSONObject(lsResponse);
                    String lsResult = loResponse.getString("result");

                    if(lsResult.equalsIgnoreCase("success")){
                        callback.OnSuccess(loResponse.toString());
                        SaveNewsEvents(loResponse);
                    } else {
                        JSONObject loError = loResponse.getJSONObject("error");
                        String lsMessage = loError.getString("message");
                        callback.OnFailed(lsMessage);
                    }
                }

                Thread.sleep(1000);

            }

            ImportSubEvents();

            Thread.sleep(1000);

            ImportCandidates();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void SaveNewsEvents(JSONObject detail) throws Exception {

        JSONArray laDetail = detail.getJSONArray("detail");

        poEvents.deleteAll();

        for(int x = 0; x < laDetail.length(); x++){

            JSONObject loJson = laDetail.getJSONObject(x);

            EEvents info = new EEvents();
            info.setTransNox(loJson.getString("sTransNox"));
            info.setEventTle(loJson.getString("sEventTle"));
            info.setEvntFrom(loJson.getString("dEvntFrom"));
            info.setEvntThru(loJson.getString("dEvntThru"));
            info.setBranchNm(loJson.getString("sBranchNm"));
            info.setAddressx(loJson.getString("sAddressx"));
            info.setEventURL(loJson.getString("sEventURL"));
            info.setImageURL(loJson.getString("sImageURL"));
            info.setNotified("0");
            info.setModified(new AppConstants().DATE_MODIFIED);
            info.setDirectoryFolder("Events");

            poEvents.insert(info);
        }
    }

    @Override
    public void ImportSubEvents() throws Exception {

        JSONObject params = new JSONObject();

        String lsResponse = WebClient.httpsPostJSon(poAPI.getImportTabulationEventsAPI(), params.toString(), poHeaders.getHeaders());

        if (lsResponse != null){

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if(lsResult.equalsIgnoreCase("success")){

                JSONArray laArr = loResponse.getJSONArray("payload");

                poSubEvnts.deleteall();

                for (int i = 0; i < laArr.length(); i++){

                    JSONObject loJson = laArr.getJSONObject(i);

                    ESub_Events loData = new ESub_Events();
                    loData.setsSubEventIDxx(loJson.getString("sContstID"));
                    loData.setsDescript(loJson.getString("sDescript"));
                    loData.setsImageURL(loJson.getString("sImageURL"));
                    loData.setsEventIDx(loJson.getString("sEventIDx"));
                    loData.setnEntryNox(loJson.getString("nEntryNox"));
                    loData.setcOnlineVt(loJson.getString("cOnlineVt"));

                    poSubEvnts.save(loData);
                }

            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");

                Log.d(TAG, lsMessage);
            }

        }
    }

    @Override
    public void ImportCandidates() throws Exception {

        JSONObject params = new JSONObject();

        String lsResponse = WebClient.httpsPostJSon(poAPI.getImportTabulationEventsAPI(), params.toString(), poHeaders.getHeaders());

        if (lsResponse != null){

            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if(lsResult.equalsIgnoreCase("success")){

                JSONArray laArr = loResponse.getJSONArray("payload");

                for (int i = 0; i < laArr.length(); i++){

                    JSONObject loJson = laArr.getJSONObject(i);

                    ECandidates candidates = new ECandidates();
                    candidates.setsGroupIDx(loJson.getString("sGroupIDx"));
                    candidates.setsEvntIDxx(loJson.getString("contstIDxx"));

                    JSONObject loDetails = laArr.getJSONObject(i).getJSONObject("sDetails");

                    candidates.setsEntryNme(loDetails.getString("00001"));
                    candidates.setsSchoolNm(loDetails.getString("00002"));

                    //todo convert to list of string the urls for image details
                    List<String> laDetails = new ArrayList<>();

                    //todo iterate based on retrieved size of api result
                    for (int x = 0; x < loDetails.length() - 3; x++) {
                        String imgKey = "0000" + String.valueOf(3 + x);
                        laDetails.add('"'+loDetails.get(imgKey).toString()+'"');
                    }

                    //todo put all urls to json object
                    JSONObject loImg = new JSONObject();
                    loImg.put("master", loDetails.get("00003"));
                    loImg.put("details", laDetails);

                    candidates.setUrlImgs(loImg.toString());
                    candidates.setVotes(0);

                    //todo save to local
                    poCandidates.insert(candidates);
                }

            }else {

                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");

                Log.d(TAG, lsMessage);

            }

        }
    }

    @Override
    public LiveData<List<EEvents>> GetNewsEvents() {
        return poEvents.getAllEvents();
    }

    @Override
    public String GetMessage() {
        return null;
    }

    @Override
    public Boolean ValidateQR(String sUserIDxx, String sMobileNoxx) {
        return null;
    }

    @Override
    public Boolean ValidateGCardInfo(String sFrstnm, String sLstnm, String sMdnm, String sSuffix, String dBirthdt, String sGCardNox) {
        return null;
    }

    @Override
    public List<EEvents> CheckEvents() {
        return poEvents.CheckEvent();
    }

}
