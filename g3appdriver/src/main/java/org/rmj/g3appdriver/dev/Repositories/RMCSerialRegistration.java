package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DMCSerialRegistration;
import org.rmj.g3appdriver.dev.Database.Entities.EMCSerialRegistration;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;
import java.util.List;

public class RMCSerialRegistration implements DMCSerialRegistration {
    private static final String TAG = "RAppEventInfo";
    private final Context mContext;
    private final DMCSerialRegistration mcDao;
    public RMCSerialRegistration(Context instance){
        GGC_GuanzonAppDB database = GGC_GuanzonAppDB.getInstance(instance);
        this.mContext = instance;
        this.mcDao = database.EMCSerialRegistrationDao();
    }
    @Override
    public void insert(EMCSerialRegistration emcSerialRegistration) {
        mcDao.insert(emcSerialRegistration);
    }
    @Override
    public void insertBulkData(List<EMCSerialRegistration> emcSerialRegistrationList) {
        mcDao.insertBulkData(emcSerialRegistrationList);
    }
    @Override
    public void update(EMCSerialRegistration emcSerialRegistration) {
        mcDao.update(emcSerialRegistration);
    }
    @Override
    public void deleteMC() {
        mcDao.deleteMC();
    }
}
