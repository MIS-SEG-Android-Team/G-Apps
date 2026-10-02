package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DServiceInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EServiceInfo;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

import java.util.List;

public class RServiceInfo implements DServiceInfo {
    private static final String TAG = "RServiceInfo";
    private final Context mContext;
    private final DServiceInfo serviceDao;
    public RServiceInfo(Context context){
        this.mContext = context;
        GGC_GuanzonAppDB database = GGC_GuanzonAppDB.getInstance(mContext);
        this.serviceDao = database.EServiceDao();
    }

    @Override
    public LiveData<EServiceInfo> getActiveServiceInfo() {
        return serviceDao.getActiveServiceInfo();
    }

    @Override
    public void insert(EServiceInfo eServiceInfo) {
        serviceDao.insert(eServiceInfo);
    }
    @Override
    public void insertBulkData(List<EServiceInfo> foServices) {
        serviceDao.insertBulkData(foServices);
    }
    @Override
    public void update(EServiceInfo eServiceInforInfo) {
        serviceDao.update(eServiceInforInfo);
    }

}
