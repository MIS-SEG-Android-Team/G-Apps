package org.rmj.g3appdriver.dev.Database.DataAccessObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemItemInfo;

import java.util.List;

@Dao
public interface DRedeemItemInfo {

    @Insert()
    void insert(ERedeemItemInfo redeemItemInfo);

    @Insert()
    void insertBulkData(List<ERedeemItemInfo> redeemItemInfoList);

    @Update
    void update(ERedeemItemInfo redeemItemInfo);

    @Query("DELETE FROM redeem_item WHERE sTransNox = :fsPromoId")
    void removeItemFromCart(String fsPromoId);

    @Query("SELECT * FROM Redeem_Item WHERE sTransNox =:TransNox AND sPromoIDx=:PromoIDx")
    List<ERedeemItemInfo> getRedeemableIfExist(String TransNox, String PromoIDx);

    @Query("UPDATE Redeem_Item SET " +
            "nItemQtyx =:ItemQty " +
            "WHERE sTransNox=:TransNox AND sTransNox =:PromoIDx")
    void UpdateExistingItemOnCart(String TransNox, String PromoIDx, int ItemQty);

    @Query("SELECT a.sTransNox, " +
            "b.sPromoDsc, " +
            "a.nPointsxx," +
            "a.nItemQtyx, " +
            "b.sImageUrl " +
            "FROM Redeem_Item a " +
            "LEFT JOIN Redeemables b " +
            "ON a.sPromoIDx = b.sPromoCde " +
            "WHERE a.sGCardNox = (SELECT sGCardNox FROM GCard_App_Master WHERE cActvStat = '1')")
    LiveData<List<GCardCartItem>> GetGCardCartItemList();

    @Query("SELECT * FROM BranchInfo WHERE sBranchCd LIKE '%M%'")
    List<EBranchInfo> GetMCBranchesForRedemption();

    @Query("SELECT COUNT(*) FROM Redeem_Item WHERE sGCardNox = (SELECT sGCardNox FROM GCard_App_Master WHERE cActvStat = '1')")
    LiveData<Integer> GetGcardCartItemCount();

    @Query("SELECT SUM(nPointsxx * nItemQtyx) FROM Redeem_Item WHERE sGCardNox = (SELECT sGCardNox FROM GCard_App_Master WHERE cActvStat = '1')")
    LiveData<Double> GetGCardCartItenTotalPoints();

    class GCardCartItem{
        public String sTransNox;
        public String sPromoDsc;
        public String nPointsxx;
        public String nItemQtyx;
        public String sImageUrl;
    }
}
