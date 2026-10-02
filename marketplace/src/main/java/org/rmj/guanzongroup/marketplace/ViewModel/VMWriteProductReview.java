package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMWriteProductReview extends AndroidViewModel {
    private final Context poApplctn;
    private final RProduct poProdcts;
    private final ROrder poOrder;
    private final ConnectionUtil loConnect;
    private String lomessage;

    public VMWriteProductReview(@NonNull Application application) {
        super(application);
        this.poApplctn = application;
        this.loConnect = new ConnectionUtil(application);
        this.poProdcts = new RProduct(application);
        this.poOrder = new ROrder(application);
    }

    public LiveData<EProducts> getProductInfo(String fsListID){
        return poProdcts.GetProductInfo(fsListID);
    }

    public void saveReview(String OrderID,
                            String fsListID,
                           int fnRating,
                           String fsReview,
                           OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(fsListID, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String lsListIdx = args.toString();
                    if(loConnect.isDeviceConnected()) {
                        // TODO: Initialize write review here.
                        if(poProdcts.SendProductReview(OrderID, lsListIdx, fnRating, fsReview)){
                            lomessage = "Thank you for rating this product";
                            poOrder.UpdateReviewedItem(OrderID, lsListIdx);
                            return true;
                        } else {
                            lomessage = poProdcts.getMessage();
                            return false;
                        }
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean) {
                    foCallBck.onSuccess(lomessage);
                } else {
                    foCallBck.onFailed(lomessage);
                }
            }
        });
    }
}
