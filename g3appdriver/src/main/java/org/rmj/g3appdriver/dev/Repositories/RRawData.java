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

import android.content.Context;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DRawDao;
import org.rmj.g3appdriver.dev.Database.Entities.ETokenInfo;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

public class RRawData {

    private final Context mContext;
    private DRawDao poDaw;

    public RRawData(Context context){
        this.mContext = context;
        GGC_GuanzonAppDB GGCGriderDB = GGC_GuanzonAppDB.getInstance(mContext);
        poDaw = GGCGriderDB.RawDao();
    }

    public void SaveNewToken(String val){
        ETokenInfo loToken = new ETokenInfo();
        loToken.setTokenInf(val);

        poDaw.insertTokenInfo(loToken);
    }
}
