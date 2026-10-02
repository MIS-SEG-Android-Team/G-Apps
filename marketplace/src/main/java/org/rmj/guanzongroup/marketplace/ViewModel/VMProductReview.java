package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMProductReview extends AndroidViewModel {
    private final Application application;
    private final RProduct poProduct;
    private ConnectionUtil poConnect;
    private JSONObject poData;
    private Boolean isSuccess;
    private String lomessage;
    public VMProductReview(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poProduct = new RProduct(application);
        this.poConnect = new ConnectionUtil(application);
    }

    public LiveData<EProducts> getProductInfo(String fsListID){
        return poProduct.GetProductInfo(fsListID);
    }

    public void ImportReviews(String fsVal, OnInquiryReviewsImportCallback callback){
        TaskExecutor.Execute(fsVal, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                if(poConnect.isDeviceConnected()) {
                    if (poProduct.GetProductRatings(args.toString())) {
                        poData = poProduct.getData();

                        lomessage = "Product reviews imported";
                        isSuccess = true;
                    } else {
                        lomessage = poProduct.getMessage();
                        isSuccess = false;
                    }
                } else {
                    lomessage = "Server no response";
                    isSuccess = false;
                }

                return isSuccess;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callback.OnImport(poData.toString());
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }
    public interface OnInquiryReviewsImportCallback{
        void OnImport(String args);
        void OnFailed(String message);
    }

}
