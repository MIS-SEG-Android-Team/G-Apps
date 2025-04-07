package org.rmj.g3appdriver.lib.GCardCore;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONObject;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGcardApp;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DPointsRequest;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EMCSerialRegistration;
import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EServiceInfo;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import org.rmj.g3appdriver.dev.Repositories.RGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Repositories.RMCSerialRegistration;
import org.rmj.g3appdriver.dev.Repositories.RServiceInfo;
import org.rmj.g3appdriver.dev.ServerRequest.ServerAPIs;
import org.rmj.g3appdriver.dev.ServerRequest.HttpHeaders;
import org.rmj.g3appdriver.dev.ServerRequest.WebClient;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.etc.Telephony;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.lib.GCardCore.Obj.CartItem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;

import java.util.HashMap;
import java.util.List;

public class GCardManager implements iGCardSystem{
    private static final String TAG = GCardManager.class.getSimpleName();

    private final Context mContext;

    private final HttpHeaders poHeaders;
    private final CodeGenerator poCode;
    private final DGcardApp poGCard;
    private final AccountInfo poSession;
    private final Telephony poDevicex;
    private final RServiceInfo poService;
    private final RMCSerialRegistration poMcReg;
    private final RGCardTransactionLedger poLedger;
    private final GuanzonAppConfig poConfig;
    private final ServerAPIs poAPI;
    private final AccountInfo loAccount;
    private final DPointsRequest loPointsRequest;
    private String message;

    public GCardManager(Context context) {
        this.mContext = context;
        this.poHeaders = new HttpHeaders(mContext);
        this.poCode = new CodeGenerator();
        this.poGCard = GGC_GuanzonAppDB.getInstance(mContext).EGcardAppDao();
        this.poSession = new AccountInfo(mContext);
        this.poDevicex = new Telephony(mContext);
        this.poService = new RServiceInfo(mContext);
        this.poMcReg = new RMCSerialRegistration(mContext);
        this.poLedger = new RGCardTransactionLedger(mContext);
        this.poConfig = new GuanzonAppConfig(mContext);
        this.poAPI = new ServerAPIs(poConfig.getTestCase());
        this.loAccount = new AccountInfo(mContext);
        this.loPointsRequest = GGC_GuanzonAppDB.getInstance(mContext).GPointsRqstDao();
    }
    @Override
    public LiveData<List<EGcardApp>> GetGCardList() {
        return poGCard.getAllGCardInfo();
    }
    @Override
    public LiveData<EGcardApp> hasNoGcard() {
        return poGCard.hasNoGcard();
    }
    @Override
    public LiveData<List<EGcardApp>> hasUnCheckGCard() {
        return poGCard.hasUnCheckGCard();
    }
    @Override
    public LiveData<EGcardApp> getGCardInfo() {
        return poGCard.getGCardInfo();
    }
    @Override
    public LiveData<List<EPointsRequest>> GetPointsRqsts() {
        return loPointsRequest.GetRequests(loAccount.getUserID());
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
    public LiveData<List<DRedeemItemInfo.GCardCartItem>> GetCartItems() {
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
    public LiveData<List<EGCardTransactionLedger>> GetGcardTransactions() {
        return poLedger.getAllTransactionsList();
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> GetPointsEntryTransactions() {
        return poLedger.getPointsEntryTransactionsList();
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> GetRedemptionTransactions() {
        return poLedger.getRedemptionTransactionsList();
    }
    @Override
    public LiveData<List<EBranchInfo>> GetMobileBranchList() {
        return null;
    }
    @Override
    public LiveData<List<EBranchInfo>> GetMotorcycleBranchList() {
        return null;
    }
    @Override
    public LiveData<List<EPromo>> GetPromotions() {
        return null;
    }
    @Override
    public LiveData<List<EEvents>> GetNewsEvents() {
        return null;
    }

    @Override
    public List<EGcardApp> hasActiveGcard() {
        return poGCard.hasActiveGcard();
    }
    @Override
    public List<EBranchInfo> GetMCBranchesForRedemption() {
        return null;
    }

    @Override
    public String GetMessage() {
        return message;
    }

    @Override
    public void AddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception{
        if(!gcardInfo.isDataValid()){
            callback.OnFailed(gcardInfo.getMessage());
        } else {
            String lsResponse = WebClient.httpsPostJSon(poAPI.getAddNewGCardAPI(), gcardInfo.getJSONParameters(), poHeaders.getHeaders());
            if(lsResponse == null){
                callback.OnFailed("No server response.");
            } else {
                JSONObject loResponse = new JSONObject(lsResponse);

                String lsResult = loResponse.getString("result");
                if(lsResult.equalsIgnoreCase("success")){
                    callback.OnSuccess(lsResponse);
                } else {
                    JSONObject loError = loResponse.getJSONObject("error");
                    String lsCode = loError.getString("code");
                    if(lsCode.equalsIgnoreCase("CNF")){
                        callback.OnFailed(loError.toString());
                    } else {
                        String lsMessage = loError.getString("message");
                        callback.OnFailed(lsMessage);
                    }
                }
            }
        }
    }
    @Override
    public void updateGCardActiveStatus(String GCardNmbr) {
        poGCard.updateGCardActiveStatus(GCardNmbr);
    }

    @Override
    public Boolean DownloadGcardPoints(HashMap<String, String> loParams) {
        try {
            JSONObject params = new JSONObject();
            params.put("sGCardNox", loParams.get("sGCardNox"));
            params.put("dTransact", loParams.get("dTransact"));
            params.put("sBranchCD", loParams.get("sBranchCD"));
            params.put("sReferNox", loParams.get("sReferNox"));
            params.put("sSourceCd", loParams.get("sSourceCd"));
            params.put("sOTPasswd", loParams.get("sOTPasswd"));

            String lsResponse = WebClient.httpsPostJSon(poAPI.getUrlImportGcardPoints(), params.toString(), poHeaders.getHeaders());
            if (lsResponse.isEmpty()){
                message = "Server no response";
                return false;
            }

            JSONObject loResponse = new JSONObject(lsResponse);

            String lsResult = loResponse.getString("result");
            
            if (lsResult.equalsIgnoreCase("success")) {
                message = loResponse.getString("message");

                //TODO: NO API RESULT RETURNED FOR LEDGER. FUTURE ADJUSTMENT -GUILLIER
                /*RGCardPoints loGPoints = new RGCardPoints(mContext);
                EGCardPoints egCardPoints = new EGCardPoints();

                egCardPoints.setsUserIDxx(loAccount.getUserID());
                egCardPoints.setsGCardNox(loParams.get("sGCardNox"));
                egCardPoints.setdTransact(loParams.get("dTransact"));
                egCardPoints.setsBranchCD(loParams.get("sBranchCD"));
                egCardPoints.setsSourceCD(loParams.get("sSourceCd"));
                egCardPoints.setsSourceDs(loParams.get("sSourceDs"));
                egCardPoints.setsReferNox(loParams.get("sReferNox"));
                egCardPoints.setnTranAmtx(Double.parseDouble(loParams.get("nTranAmtx")));
                egCardPoints.setnPointsxx(Double.parseDouble(loParams.get("nPointsxx")));
                egCardPoints.setsOTPasswd(loParams.get("sOTPasswd"));
                egCardPoints.setcSendStat("1");
                egCardPoints.setcTranStat("0");
                egCardPoints.setdTimeStmp(DateTimeToday());

                loGPoints.Save(egCardPoints);*/
                return true;
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                message = loError.getString("message");
                return false;
            }
        }catch (Exception e){
            message = e.getMessage();
            return false;
        }
    }

    @Override
    public double getRemainingActiveCardPoints() {
        return 0;
    }

    @Override
    public void updateGCardDeactiveStatus() {
        poGCard.updateGCardDeactiveStatus();
    }
    @Override
    public void SavePointsRqst(EPointsRequest loRqst) {
        loPointsRequest.SaveRequest(loRqst);
    }
    @Override
    public void UpdateSendPointsRqst(String sTransNoxx) {
        loPointsRequest.SendPointsRequest(sTransNoxx);
    }

    @Override
    public void AddGCardQrCode(String GCardNumber, GCardSystem.GCardSystemCallback callback) throws Exception {
        JSONObject params = new JSONObject();
        params.put("secureno", poCode.generateSecureNo(GCardNumber));

        String lsAddress = poAPI.getAddNewGCardAPI();
        String lsResponse = WebClient.httpsPostJSon(lsAddress, params.toString(), poHeaders.getHeaders());

        if(lsResponse == null){
            callback.OnFailed("No server response.");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("success")){
                callback.OnSuccess("New gcard has been added successfully.");
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
            }
        }
    }
    @Override
    public void ConfirmAddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception {
        gcardInfo.setsConfirmx("1");
        String lsResponse = WebClient.httpsPostJSon(poAPI.getAddNewGCardAPI(), gcardInfo.getJSONParameters(), poHeaders.getHeaders());
        if(lsResponse == null){
            callback.OnFailed("No server response.");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("success")){
                callback.OnSuccess(lsResponse);
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
            }
        }
    }
    @Override
    public void DownloadGcardNumbers(GCardSystem.GCardSystemCallback callback) throws Exception {
        JSONObject param = new JSONObject();
        param.put("user_id", poCode.generateSecureNo(poSession.getUserID()));

        String lsResponse = WebClient.httpsPostJSon(poAPI.getImportGCardAPI(), param.toString(), poHeaders.getHeaders());
        if(lsResponse == null){
            callback.OnFailed("Server no response.");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if(lsResult.equalsIgnoreCase("success")){
                callback.OnSuccess(loResponse.toString());
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
            }
        }
    }
    @Override
    public void SaveGCardInfo(JSONObject detail) throws Exception {
        poGCard.updateGCardDeactiveStatus();

        if(!detail.has("detail")){
            EGcardApp loGCard = new EGcardApp();
            loGCard.setGCardNox(detail.getString("sGCardNox"));
            loGCard.setCardNmbr(detail.getString("sCardNmbr"));
            loGCard.setUserIDxx(poSession.getUserID());
            loGCard.setNmOnCard(detail.getString("sNmOnCard"));
            loGCard.setMemberxx(detail.getString("dMemberxx"));
            loGCard.setCardType(detail.getString("cCardType"));
            loGCard.setAvlPoint(detail.getString("nAvlPoint"));
            loGCard.setTotPoint(detail.getString("nTotPoint"));
            loGCard.setTranStat(detail.getString("cCardStat"));
            loGCard.setActvStat("0");
            loGCard.setNotified("1");
            poGCard.insert(loGCard);
        } else {
            JSONArray laJson = detail.getJSONArray("detail");
            for(int x = 0; x < laJson.length(); x++) {
                JSONObject loJson = laJson.getJSONObject(x);
                EGcardApp loGCard = new EGcardApp();
                loGCard.setGCardNox(loJson.getString("sGCardNox"));
                loGCard.setCardNmbr(loJson.getString("sCardNmbr"));
                loGCard.setUserIDxx(poSession.getUserID());
                loGCard.setNmOnCard(loJson.getString("sNmOnCard"));
                loGCard.setMemberxx(loJson.getString("dMemberxx"));
                loGCard.setCardType(loJson.getString("cCardType"));
                loGCard.setAvlPoint(loJson.getString("nAvlPoint"));
                loGCard.setTotPoint(loJson.getString("nTotPoint"));
                loGCard.setTranStat(loJson.getString("cCardStat"));
                loGCard.setActvStat("0");
                loGCard.setNotified("1");
                poGCard.insert(loGCard);
            }
        }
        poGCard.updateGCardAppWithHighestPoints();
    }
    @Override
    public Boolean ValidateGCardInfo(String sFrstnm, String sLstnm, String sMdnm, String sSuffix, String dBirthdt, String sGCardNox) {
        if (sGCardNox.isEmpty()) {
            message = "GCard number scanned empty from QR.";
            return false;
        }else if (sFrstnm.isEmpty()){
            message = "Firstname scanned empty from QR";
            return false;
        } else if (sLstnm.isEmpty()) {
            message = "Lastname scanned empty from QR";
            return false;
        } else if (dBirthdt.isEmpty()) {
            message = "Birthdate scanned empty from QR";
            return false;
        }

        if (!sGCardNox.equalsIgnoreCase(poGCard.getCardNox())){
            message = "GCard number not registered on this account. Please verify to complete your account.";
            return false;
        } else if (!sLstnm.equalsIgnoreCase(loAccount.getLastName())) {
            message = "Firstname do not match the logged account. Please verify to complete your account.";
            return false;
        } else if (!sFrstnm.equalsIgnoreCase(loAccount.getFirstName())) {
            message = "Lastname do not match the logged account. Please verify to complete your account.";
            return false;
        } else if (!sMdnm.equalsIgnoreCase(loAccount.getMiddleName())) {
            message = "Middlename do not match the logged account. Please verify to complete your account.";
            return false;
        } else if (!sSuffix.equalsIgnoreCase(loAccount.getSuffix())) {
            message = "Suffix name do not match the logged account. Please verify to complete your account.";
            return false;
        } else if (!dBirthdt.equalsIgnoreCase(loAccount.getBirthdate())) {
            message = "Birthdate do not match the logged account. Please verify to complete your account.";
            return false;
        }

        return true;
    }

    @Override
    public Bitmap GenerateGCardQrCode() {
        String lsSource = "CARD";
        String lsDevcID = poDevicex.getDeviceID();
        String lsCardNo = poGCard.getCardNo();
        String lsUserID = poSession.getUserID();
        String lsMobNox = poDevicex.getMobilNumbers();
        String lsDateTm = new AppConstants().GCARD_DATE_TIME;

        double lsCardPt;
        if(poGCard.getRedeemItemPoints() > 0){
            lsCardPt = Math.abs(poGCard.getAvailableGcardPoints() - poGCard.getRedeemItemPoints());
        } else {
            lsCardPt = poGCard.getAvailableGcardPoints();
        }

        String lsModelx = Build.MODEL;
        String lsTransN = "";

        return poCode.generateGCardCodex(lsSource,
                lsDevcID,
                lsCardNo,
                lsUserID,
                lsMobNox,
                lsDateTm,
                lsCardPt,
                lsModelx,
                lsTransN);
    }
    @Override
    public void ParseQrCode(String val, GCardSystem.ParseQrCodeCallback callback){
        poCode.setEncryptedQrCode(val);

        String src = poCode.getTransSource();

        String lsUserIDxx = loAccount.getUserID();
        String lsMobileNo = loAccount.getMobileNo();

        if(poConfig.getTestCase() && lsMobileNo.isEmpty()){
            lsMobileNo = "09171870011";
        }

        if (ValidateQR(lsUserIDxx, lsMobileNo)){
            if (poCode.isQrCodeTransaction()){
                //TODO: GET ANY ACTIVE GCARD NUMBERS ON ACCOUNT
                String lsGcardNox = poGCard.getCardNo();

                //TODO: VALIDATE ACTIVE GCARD NUMBER
                if(!lsGcardNox.isEmpty()){
                    //TODO: VALIDATE AND DISPLAY OTP
                    if (src.equalsIgnoreCase("OTP")){
                        if (poGCard.getCardNmbr(poCode.GetOTPCardNmbr()) == null || poGCard.getCardNmbr(poCode.GetOTPCardNmbr()).isEmpty()){
                            callback.OnFailed("GCard Number is not registered on this account.");
                        }else {
                            callback.TransactionResult(src, poCode.GetOTP());
                        }
                    }else {
                        if(poCode.isTransactionVoid()){
                            //TODO: POINTS SHOULD BE EMPTY
                            callback.TransactionResult(src, poCode.getTransactionPIN());
                        } else if(poCode.isDeviceValid(lsMobileNo, lsGcardNox)) {
                            //TODO: GCARD AND MOBILE, SHOULD BE SAME WITH USER ACCOUNT
                            callback.TransactionResult(src, poCode.getTransactionPIN());
                        } else {
                            callback.OnFailed("Mobile Number or Account is not valid to confirm this transaction");
                        }
                    }

                }else {
                    callback.OnFailed("No GCard number is active in this account. Please make sure a GCard is active.");
                }
            } else {
                if (src.equals("TDS")){
                    HashMap<String, String> params = ScanTDS();
                    if (ValidateGCardInfo(params.get("sFrstName"), params.get("sLastName"), params.get("sMiddName"),
                            params.get("sSuffixNm"), params.get("dBirthDte"), params.get("sGCardNox"))){

                        callback.ApplicationResult(src, params);
                    }else {
                        callback.OnFailed(message);
                    }
                }else {
                    callback.ApplicationResult(src, poCode.getGCardNumber());
                }
            }
        }else {
            callback.OnFailed(message);
        }
    }

    @Override
    public HashMap<String, String> ScanTDS() {
        String decryptval = poCode.decryptedQrCodeValue();

        HashMap<String, String> params = new HashMap<>();

        params.put("sBranchCD", poCode.getKeyValueOf(decryptval, 1));
        params.put("sReferNox", poCode.getKeyValueOf(decryptval, 2));
        params.put("sSourceCd", poCode.getKeyValueOf(decryptval, 3));
        params.put("dTransact", poCode.getKeyValueOf(decryptval, 4));
        params.put("nTranAmtx", poCode.getKeyValueOf(decryptval, 5));
        params.put("nPointsxx", poCode.getKeyValueOf(decryptval, 6));
        params.put("sOTPasswd", poCode.getKeyValueOf(decryptval, 7));
        params.put("sLastName", poCode.getKeyValueOf(decryptval, 8));
        params.put("sFrstName", poCode.getKeyValueOf(decryptval, 9));
        params.put("sMiddName", poCode.getKeyValueOf(decryptval, 10));
        params.put("sSuffixNm", poCode.getKeyValueOf(decryptval, 11));
        params.put("dBirthDte", poCode.getKeyValueOf(decryptval, 12));
        params.put("sBirthPlc", poCode.getKeyValueOf(decryptval, 13));
        params.put("sSourceDs", poCode.getKeyValueOf(decryptval, 14));
        params.put("sGCardNox", poCode.getKeyValueOf(decryptval, 15));

        return params;
    }

    @Override
    public Boolean ValidateQR(String sUserIDxx, String sMobileNoxx) {
        if(poGCard.getCardNox() == null){ //Validate GCARD NO
            message = "No Gcard number detected";
            return false;
        }else if(!poCode.isCodeValid()) { //Validate QR Code
            message = "Invalid Qr Code";
            return false;
        }if(sUserIDxx.isEmpty()){
            message = "No user account detected. Please make sure you login account before proceeding.";
            return false;
        } else if(sMobileNoxx.isEmpty()){
            message = "Unable to retrieve device mobile no. Please make sure your device has mobile no.";
            return false;
        }

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
    public void AddToCart(CartItem item, GCardSystem.GCardSystemCallback callback) {
        throw new NullPointerException();
    }
    @Override
    public void UpdateCartItem(CartItem item, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
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
    public void DownloadTransactions(GCardSystem.GCardSystemCallback callback) {
        try {
            JSONObject params = new JSONObject();
            params.put("secureno", poCode.generateSecureNo(poGCard.getCardNo()));

            String[] Ledger_Address = {poAPI.getImportOfflineTransAPI(),
                    poAPI.getImportOnlineTransAPI(),
                    poAPI.getImportPreOrderAPI(),
                    poAPI.getImportReedemptionsAPI()};

            for (String ledger_address : Ledger_Address) {
                String lsResponse = WebClient.httpsPostJSon(ledger_address, params.toString(), poHeaders.getHeaders());
                if (lsResponse == null) {
                    callback.OnFailed("Server no response");
                } else {
                    JSONObject loResponse = new JSONObject(lsResponse);
                    String lsResult = loResponse.getString("result");
                    if (lsResult.equalsIgnoreCase("success")) {
                        SaveTransactions(loResponse);
                    } else {
                        JSONObject loError = loResponse.getJSONObject("error");
                        String lsMessage = loError.getString("message");
                        callback.OnFailed(lsMessage);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            callback.OnFailed("Failed downloading transactions. " + e.getMessage());
        }
    }
    @Override
    public void SaveTransactions(JSONObject detail) throws Exception {
        RGCardTransactionLedger loLedger = new RGCardTransactionLedger(mContext);
        String[] Import_Type = {
                "ONLINE",
                "OFFLINE",
                "PREORDER",
                "REDEMPTION"};
        JSONArray laDetail = detail.getJSONArray("detail");
        for(int x = 0; x < laDetail.length(); x++){
            JSONObject loJson = laDetail.getJSONObject(x);
            EGCardTransactionLedger info = new EGCardTransactionLedger();
            info.setGCardNox(loJson.getString("sGCardNox"));
            info.setTransact(loJson.getString("dTransact"));
            info.setSourceDs(Import_Type[x]);
            info.setReferNox(loJson.getString("sReferNox"));
            info.setTranType(loJson.getString("sTranType"));
            info.setSourceNo(loJson.getString("sSourceNo"));
            info.setPointsxx(Double.parseDouble(loJson.getString("nPointsxx")));
            loLedger.Save(info);
        }
    }

    @Override
    public void DownloadMCServiceInfo(GCardSystem.GCardSystemCallback callback) throws Exception {
        String lsGcardNo = poGCard.getCardNo();
        String lsSecureNo = new CodeGenerator().generateSecureNo(lsGcardNo);

        JSONObject params = new JSONObject();
        params.put("secureno", lsSecureNo);

        String lsResponse = WebClient.httpsPostJSon(poAPI.getServiceInfoAPI(), params.toString(), poHeaders.getHeaders());
        if (lsResponse == null) {
            callback.OnFailed("Server no response");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");

            if (lsResult.equalsIgnoreCase("success")) {
                callback.OnSuccess(loResponse.toString());
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
            }
        }
    }
    @Override
    public void DownloadRegistrationInfo(GCardSystem.GCardSystemCallback callback) throws Exception {
        String lsSecureNo = new CodeGenerator().generateSecureNo(poGCard.getCardNo());

        JSONObject params = new JSONObject();
        params.put("secureno", lsSecureNo);

        String lsResponse = WebClient.httpsPostJSon(poAPI.getMCRegistrationAPI(), params.toString(), poHeaders.getHeaders());
        if (lsResponse == null) {
            callback.OnFailed("Server no response");
        } else {
            JSONObject loResponse = new JSONObject(lsResponse);
            String lsResult = loResponse.getString("result");
            if (lsResult.equalsIgnoreCase("success")) {
                callback.OnSuccess(loResponse.toString());
            } else {
                JSONObject loError = loResponse.getJSONObject("error");
                String lsMessage = loError.getString("message");
                callback.OnFailed(lsMessage);
            }
        }
    }

    @Override
    public void SaveMcServiceInfo(JSONObject detail) throws Exception {
        JSONArray laJson = detail.getJSONArray("detail");
        if(laJson.length() > 0) {
            for (int x = 0; x < laJson.length(); x++) {
                JSONObject loJson = laJson.getJSONObject(x);
                EServiceInfo loService = new EServiceInfo();

                loService.setGCardNox(poGCard.getCardNox());
                loService.setSerialID(loJson.getString("sSerialID"));
                loService.setEngineNo(loJson.getString("sEngineNo"));
                loService.setFrameNox(loJson.getString("sFrameNox"));
                loService.setModelNme(loJson.getString("sModelNme"));
                loService.setFSEPStat(loJson.getString("cFSEPStat"));
                loService.setLastSrvc(loJson.getString("dLastSrvc"));
                loService.setPurchase(loJson.getString("dPurchase"));
                loService.setYellowxx(loJson.getInt("nYellowxx"));
                loService.setWhitexxx(loJson.getInt("nWhitexxx"));
                loService.setMIlAgexx(loJson.getInt("nMilagexx"));
                loService.setNxtRmnds(loJson.getString("dNxtRmndS"));

                poService.insert(loService);
            }
        }
    }
    @Override
    public void SaveRegistrationInfo(JSONObject detail) throws Exception{
        JSONArray laJson = detail.getJSONArray("detail");
        if(laJson.length() > 0) {
            for (int x = 0; x < laJson.length(); x++) {
                JSONObject loJson = laJson.getJSONObject(x);
                EMCSerialRegistration loMcSerial = new EMCSerialRegistration();
                loMcSerial.setGCardNox(poGCard.getCardNox());
                loMcSerial.setSerialID(loJson.getString("sSerialID"));
                loMcSerial.setEngineNo(loJson.getString("sEngineNo"));
                loMcSerial.setFrameNox(loJson.getString("sFrameNox"));
                loMcSerial.setModelNme(loJson.getString("sModelNme"));
                loMcSerial.setFSEPStat(loJson.getString("cFSEPStat"));
                loMcSerial.setRegStatx(loJson.getString("cRegStatx"));
                poMcReg.insert(loMcSerial);
            }
        }
    }

    @Override
    public void ScheduleNextServiceDate(String date, GCardSystem.GCardSystemCallback callback) {
    }
    @Override
    public void DownloadBranchesList(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveBranchesList(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }

    @Override
    public void DownloadPromotions(GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SavePromotions(JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public EPromo CheckPromo() {
        return null;
    }

    @Override
    public void DownloadNewsEvents(Boolean isPageant, GCardSystem.GCardSystemCallback callback) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public void SaveNewsEvents(Boolean isPageant, JSONObject detail) throws Exception {
        throw new NullPointerException();
    }
    @Override
    public List<EEvents> CheckEvents() {
        return null;
    }

    @Override
    public void ActivateGcard(String GcardNo) throws Exception {
    }
}
