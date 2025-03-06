package org.rmj.g3appdriver.etc;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DBarcode;
import org.rmj.g3appdriver.dev.Database.Entities.EBarcode;
import org.rmj.g3appdriver.dev.Database.GGC_GuanzonAppDB;;import java.util.List;

public class Barcode {

    private DBarcode barcodeDao;

    public Barcode(Context context){
        this.barcodeDao = GGC_GuanzonAppDB.getInstance(context).BarcodeDao();
    }

    public void saveBarcode(EBarcode barcode){
        barcodeDao.save(barcode);
    }

    public LiveData<List<EBarcode>> getBarcodeList(){
        return barcodeDao.getBarcodes();
    }

    public int countBarcode(){
        return barcodeDao.getBarcodeCount();
    }

}
