package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import java.util.List;

public class VMProductList extends AndroidViewModel {
    private static final String TAG = VMProductList.class.getSimpleName();
    private final RProduct poProdct;
    private String message;

    public VMProductList(@NonNull Application application) {
        super(application);
        this.poProdct = new RProduct(application);
    }

    public LiveData<RProduct.oFilterx> GetFilterType(){
        return poProdct.GetFilterType();
    }
    public LiveData<List<DProduct.oProduct>> GetBrandProductList(int fnIndex, FilterType foVal, String args){
        return poProdct.GetProductsList(fnIndex, foVal, args, null);
    }
    public LiveData<List<DProduct.oProduct>> GetProductsOnBrand(String fsArgs, String fsArgs1){
        return poProdct.GetProductsOnBrand(fsArgs, fsArgs1);
    }

    public void SetProductFilter(RProduct.oFilterx foVal){
        poProdct.setFilterType(foVal);
    }
    public void SearchItem(String args, OnSearchItemListener listener){
        TaskExecutor.Execute(args, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnSearch("Guanzon App", "Searching item. Please wait...");
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(!poProdct.SearchProduct(args.toString())){
                        message = poProdct.getMessage();
                        return false;
                    }

                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                    message = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                listener.OnSearch();
            }
        });
    }
    public interface OnSearchItemListener{
        void OnSearch(String title, String message);
        void OnSearch();
    }
}
