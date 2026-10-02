package org.rmj.g3appdriver.dev.Database.DataAccessObject;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.rmj.g3appdriver.dev.Database.Entities.EAddressInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ECountryInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEmailInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEmployeeInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGanadoOnline;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EGuanzonPanalo;
import org.rmj.g3appdriver.dev.Database.Entities.EItemCart;
import org.rmj.g3appdriver.dev.Database.Entities.EMCColor;
import org.rmj.g3appdriver.dev.Database.Entities.EMCModelCashPrice;
import org.rmj.g3appdriver.dev.Database.Entities.EMCSerialRegistration;
import org.rmj.g3appdriver.dev.Database.Entities.EMcBrand;
import org.rmj.g3appdriver.dev.Database.Entities.EMcCategory;
import org.rmj.g3appdriver.dev.Database.Entities.EMcModel;
import org.rmj.g3appdriver.dev.Database.Entities.EMcModelPrice;
import org.rmj.g3appdriver.dev.Database.Entities.EMcTermCategory;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.dev.Database.Entities.ENotificationRecipient;
import org.rmj.g3appdriver.dev.Database.Entities.ENotificationUser;
import org.rmj.g3appdriver.dev.Database.Entities.EOrderDetail;
import org.rmj.g3appdriver.dev.Database.Entities.EOrderMaster;
import org.rmj.g3appdriver.dev.Database.Entities.EPanaloReward;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.dev.Database.Entities.EProvinceInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemItemInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERedeemablesInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ERelation;
import org.rmj.g3appdriver.dev.Database.Entities.ESearchLog;
import org.rmj.g3appdriver.dev.Database.Entities.EServiceInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ETokenInfo;
import org.rmj.g3appdriver.dev.Database.Entities.ETownInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EUserInfo;

@Database(entities = {
        EEvents.class,
        EBranchInfo.class,
        EClientInfo.class,
        EGcardApp.class,
        EGCardTransactionLedger.class,
        EMCSerialRegistration.class,
        EPromo.class,
        ENotificationMaster.class,
        ENotificationRecipient.class,
        ENotificationUser.class,
        ERedeemablesInfo.class,
        ERedeemItemInfo.class,
        EServiceInfo.class,
        EEmployeeInfo.class,
        EUserInfo.class,
        ETokenInfo.class,
        EBarangayInfo.class,
        ETownInfo.class,
        EProvinceInfo.class,
        ECountryInfo.class,
        EMobileInfo.class,
        EAddressInfo.class,
        EProducts.class,
        EItemCart.class,
        EOrderDetail.class,
        EOrderMaster.class,
        ESearchLog.class,
        EEmailInfo.class,
        EPanaloReward.class,
        EGuanzonPanalo.class,
        ERelation.class,
        EGanadoOnline.class,
        EMCColor.class,
        EMcBrand.class,
        EMcModel.class,
        EMcModelPrice.class,
        EMCModelCashPrice.class,
        EMcCategory.class,
        EMcTermCategory.class}, version = 3, exportSchema = false)
public abstract class GGC_GuanzonAppDB extends RoomDatabase {
    private static final String TAG = "GuanzonApp_DB_Manager";
    private static GGC_GuanzonAppDB instance;


//    public abstract DAppEventInfo EAppEventDao();
    public abstract DBranchInfo EBranchDao();
    public abstract DClientInfo EClientDao();
    public abstract DGcardApp EGcardAppDao();
    public abstract DGCardTransactionLedger EGCardTransactionLedgerDao();
    public abstract DMCSerialRegistration EMCSerialRegistrationDao();
    public abstract DPromo EPromoDao();
    public abstract DRedeemablesInfo ERedeemablesDao();
    public abstract DRedeemItemInfo ERedeemItemDao();
    public abstract DServiceInfo EServiceDao();
    public abstract DUserInfo EUserInfoDao();
    public abstract DRawDao RawDao();
    public abstract DEmployeeInfo EmployeeDao();
    public abstract DNotifications NotificationDao();
    public abstract DEvents EventDao();
    public abstract DAddress AddDao();
    public abstract DMobileAddressInfo mobAddDao();
    public abstract DProduct prodctDao();
    public abstract DItemCart itemCartDao();
    public abstract DOrderMaster orderMasterDao();
    public abstract DOrderDetail orderDetailDao();
    public abstract DSearchLog searchDao();
    public abstract DPanalo panaloDao();
    public abstract DGanadoOnline ganadoDao();
    public abstract DRelation relDao();
    public abstract DMcBrand McBrandDao();
    public abstract DMcModel McModelDao();
    public abstract DMcModelPrice McModelPriceDao();
    public abstract DMcCategory McCategoryDao();

    public static synchronized GGC_GuanzonAppDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GGC_GuanzonAppDB.class, "GGC_ISysDBF.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_V1)
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.e(TAG, "Local database has been created.");
        }
    };

    private static final Migration MIGRATION_V1 = new Migration(0, 1){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE " +
                    "MarketPlace_Cart_New(sUserIDxx TEXT NOT NULL, sListIDxx TEXT NOT NULL, cBuyNowxx TEXT NOT NULL, " +
                    "nQuantity INT, nAvlQtyxx INT, dCreatedx TEXT, cTranStat TEXT, cCheckOut TEXT, dTimeStmp TEXT)");

            database.execSQL("INSERT INTO MarketPlace_Cart_New(" +
                    "sUserIDxx, sListIDxx, cBuyNowxx, nQuantity, nAvlQtyxx, dCreatedx, cTranStat, cCheckOut, dTimeStmp) " +
                    "SELECT sUserIDxx, sListIDxx, cBuyNowxx, nQuantity, nAvlQtyxx, dCreatedx, cTranStat, cCheckOut, dTimeStmp " +
                    "FROM MarketPlace_Cart");

            database.execSQL("DROP TABLE MarketPlace_Cart");
            database.execSQL("ALTER TABLE MarketPlace_Cart_New RENAME TO MarketPlace_Cart");

        }
    };
}
