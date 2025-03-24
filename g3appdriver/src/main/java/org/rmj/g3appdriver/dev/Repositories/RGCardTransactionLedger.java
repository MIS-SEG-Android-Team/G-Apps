package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardTransactionLedger;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

import java.util.List;

public class RGCardTransactionLedger implements DGCardTransactionLedger {
    private static final String TAG = "RAppEventInfo";
    private final Context mContext;
    private final DGCardTransactionLedger ledgerDao;
    public RGCardTransactionLedger(Context context){
        GGC_GuanzonAppDB database = GGC_GuanzonAppDB.getInstance(context);
        this.mContext = context;
        this.ledgerDao = database.EGCardTransactionLedgerDao();
    }

    @Override
    public void Save(EGCardTransactionLedger egCardTransactionLedger) {
        ledgerDao.Save(egCardTransactionLedger);
    }
    @Override
    public void insertBulkData(List<EGCardTransactionLedger> egCardTransactionLedgerList) {
        ledgerDao.insertBulkData(egCardTransactionLedgerList);
    }
    @Override
    public void update(EGCardTransactionLedger egCardTransactionLedger) {
        ledgerDao.update(egCardTransactionLedger);
    }
    @Override
    public void deleteGCardTrans() {
        ledgerDao.deleteGCardTrans();
    }

    @Override
    public LiveData<List<EGCardTransactionLedger>> getRedemptionTransactionsList() {
        return ledgerDao.getRedemptionTransactionsList();
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> getAllTransactionsList() {
        return ledgerDao.getAllTransactionsList();
    }
    @Override
    public LiveData<List<EGCardTransactionLedger>> getPointsEntryTransactionsList() {
        return ledgerDao.getPointsEntryTransactionsList();
    }

    @Override
    public EGCardTransactionLedger isTransactionValid(String gcardNo, String srcedsc, String referno, String trandsc, String srcenox, String pointsx) {
        return ledgerDao.isTransactionValid(gcardNo,  srcedsc,  referno,  trandsc,  srcenox,  pointsx);
    }
}
