package org.rmj.g3appdriver.dev.Repositories;

import android.content.Context;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGCard_Ledger;
import org.rmj.g3appdriver.dev.Database.Entities.EGCard_Ledger;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;

public class RGCardLedger implements DGCard_Ledger {
    Context context;
    final DGCard_Ledger DB_App;
    public RGCardLedger(Context context){
        this.context = context;
        this.DB_App = GGC_GuanzonAppDB.getInstance(context).GLedgerDao();
    }
    @Override
    public void Save(EGCard_Ledger ledger) {
        DB_App.Save(ledger);
    }
    @Override
    public void Update(EGCard_Ledger ledger) {
        DB_App.Update(ledger);
    }
}
