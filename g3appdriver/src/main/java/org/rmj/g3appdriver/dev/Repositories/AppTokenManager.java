/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.g3appdriver
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.g3appdriver.dev.Repositories;
import android.app.Application;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRawDao;
import org.rmj.g3appdriver.dev.Database.Entities.ETokenInfo;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;


public class AppTokenManager {
    private final DRawDao dao;
    public AppTokenManager(Application instance){
        GGC_GuanzonAppDB db = GGC_GuanzonAppDB.getInstance(instance);
        this.dao = db.RawDao();
    }
    public void setTokenInfo(ETokenInfo tokenInfo){
        if(dao.getTokenInfo() != null) {
            if(dao.getTokenInfo().equalsIgnoreCase("sAppToken")) {
                dao.clearTokenInfo();
                dao.insertTokenInfo(tokenInfo);
            }
        } else {
            dao.insertTokenInfo(tokenInfo);
        }
    }
}
