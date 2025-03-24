package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMSearchItem extends AndroidViewModel {
    private static final String TAG = VMSearchItem.class.getSimpleName();
    private final Context mContext;
    private final RProduct poProdct;
    private iGCardSystem poSystem;

    public VMSearchItem(@NonNull Application application) {
        super(application);
        this.mContext = application;
        this.poProdct = new RProduct(mContext);
        this.poSystem = new GCardSystem(mContext).getInstance(GCardSystem.CoreFunctions.EXTRAS);
    }

    public LiveData<List<DProduct.oProduct>> getProductList(int fnIndex) {
        return poProdct.GetProductsList(fnIndex, FilterType.DEFAULT, null, null);
    }
    public LiveData<List<DProduct.oProduct>> GetSearchProductList(String fsVal){
        return poProdct.SearchProducts(fsVal);
    }
    public LiveData<List<EPromo>> GetPromoList(){
        return poSystem.GetPromotions();
    }

    public void RequestProductSearch(String fsVal, OnSearchCallback callback){
        TaskExecutor.Execute(fsVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.OnSearch();
            }
            @Override
            public Object DoInBackground(Object args) {
                if(!poProdct.SearchProduct(args.toString())){
                    Log.e(TAG, poProdct.getMessage());
                }

                return null;
            }
            @Override
            public void OnPostExecute(Object object) {
                callback.OnSearchFinish();
            }
        });
    }

    public interface OnSearchCallback{
        void OnSearch();
        void OnSearchFinish();
    }
}
