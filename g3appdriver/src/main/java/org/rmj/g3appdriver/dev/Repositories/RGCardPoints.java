package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGCard_Points;
import org.rmj.g3appdriver.dev.Database.Entities.EGCardPoints;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

public class RGCardPoints implements DGCard_Points {
    Context context;
    final DGCard_Points DB_App;
    public RGCardPoints(Context context){
        this.context = context;
        this.DB_App = GGC_GuanzonAppDB.getInstance(context).GPointsDao();
    }
    @Override
    public void Save(EGCardPoints egCardPoints) {
        DB_App.Save(egCardPoints);
    }
    @Override
    public void Update(EGCardPoints egCardPoints) {
        DB_App.Update(egCardPoints);
    }
    @Override
    public void UpdateVerified(String cTranStat) {
        DB_App.UpdateVerified(cTranStat);
    }
}
