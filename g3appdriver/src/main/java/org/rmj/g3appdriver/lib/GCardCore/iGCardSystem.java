package org.rmj.g3appdriver.lib.GCardCore;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;
import org.rmj.g3appdriver.lib.GCardCore.Obj.CartItem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;

import java.util.HashMap;
import java.util.List;

public interface iGCardSystem {
    LiveData<List<EGcardApp>> GetGCardList();
    LiveData<EGcardApp> hasNoGcard();
    LiveData<List<EGcardApp>> hasUnCheckGCard();
    List<EGcardApp> hasActiveGcard();
    LiveData<EGcardApp> getGCardInfo();
    LiveData<List<EPointsRequest>> GetPointsRqsts();

    LiveData<List<Double>> GetRedeemablePointsFilter();
    LiveData<List<ERedeemablesInfo>> GetRedeemablesList();
    LiveData<List<ERedeemablesInfo>> GetRedeemablesList(String fsVal);

    LiveData<List<DRedeemItemInfo.GCardCartItem>> GetCartItems();
    List<EBranchInfo> GetMCBranchesForRedemption();
    LiveData<Integer> GetGcardCartItemCount();
    LiveData<Double> GetGCardCartItemTotalPoints();

    LiveData<List<EGCardTransactionLedger>> GetGcardTransactions();
    LiveData<List<EGCardTransactionLedger>> GetPointsEntryTransactions();
    LiveData<List<EGCardTransactionLedger>> GetRedemptionTransactions();

    LiveData<List<EBranchInfo>> GetMobileBranchList();
    LiveData<List<EBranchInfo>> GetMotorcycleBranchList();

    LiveData<List<EPromo>> GetPromotions();

    LiveData<List<EEvents>> GetNewsEvents();

    String GetMessage();

    Boolean ValidateQR(String sUserIDxx, String sMobileNoxx);
    Boolean ValidateGCardInfo(String sFrstnm, String sLstnm, String sMdnm, String sSuffix, String dBirthdt, String sGCardNox);

    void AddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception;
    void updateGCardActiveStatus(String GCardNmbr);
    double getRemainingActiveCardPoints();
    void updateGCardDeactiveStatus();
    void SavePointsRqst(EPointsRequest loRqst);
    void UpdateSendPointsRqst(String sTransNoxx);

    void AddGCardQrCode(String GcardNo, GCardSystem.GCardSystemCallback callback) throws Exception;
    void ConfirmAddGCard(GcardCredentials gcardInfo, GCardSystem.GCardSystemCallback callback) throws Exception;
    void DownloadGcardNumbers(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveGCardInfo(JSONObject detail) throws Exception;
    void ActivateGcard(String GcardNo) throws Exception;
    Bitmap GenerateGCardQrCode() throws Exception;
    void ParseQrCode(String val, GCardSystem.ParseQrCodeCallback callback) throws Exception;

    HashMap<String, String> ScanTDS();

    Boolean DownloadGcardPoints(HashMap<String, String> params);

    void DownloadRedeemables(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveRedeemables(JSONObject detail) throws Exception;

    void AddToCart(CartItem item, GCardSystem.GCardSystemCallback callback);
    void UpdateCartItem(CartItem item, GCardSystem.GCardSystemCallback callback) throws Exception;
    void DeleteItemCart(String fsVal);
    void PlaceOrder(List<DRedeemItemInfo.GCardCartItem> redeemables, String BranchCD, GCardSystem.GCardSystemCallback callback) throws Exception;
    Bitmap GenerateGCardOrderQrCode(String BatchNox) throws Exception;

    void DownloadTransactions(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveTransactions(JSONObject detail) throws Exception;

    void DownloadMCServiceInfo(GCardSystem.GCardSystemCallback callback) throws Exception;
    void DownloadRegistrationInfo(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveMcServiceInfo(JSONObject detail) throws Exception;
    void SaveRegistrationInfo(JSONObject detail) throws Exception;
    void ScheduleNextServiceDate(String date, GCardSystem.GCardSystemCallback callback) throws Exception;

    void DownloadBranchesList(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveBranchesList(JSONObject detail) throws Exception;

    void DownloadPromotions(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SavePromotions(JSONObject detail) throws Exception;
    EPromo CheckPromo();

    void DownloadNewsEvents(GCardSystem.GCardSystemCallback callback) throws Exception;
    void SaveNewsEvents(JSONObject detail) throws Exception;
    EEvents CheckEvents();
}


