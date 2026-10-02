package org.rmj.guanzongroup.ganado.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.Entities.EMcModel;
import org.rmj.g3appdriver.lib.Ganado.Obj.ProductInquiry;

import java.util.List;

public class VMProductSelection extends AndroidViewModel {

    private final ProductInquiry poSys;

    public VMProductSelection(@NonNull Application application) {
        super(application);

        poSys = new ProductInquiry(application);
    }

    public LiveData<List<EMcModel>> GetModelsList(String lsValue){
        return poSys.GetModelsList(lsValue);
    }

    public String GetGcashNox(){
        return poSys.GetGcashNo();
    }
    
}
