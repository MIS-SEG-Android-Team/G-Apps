package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import java.util.List;

public class VMProductOverview extends AndroidViewModel {
    private final Application application;
    private final ConnectionUtil poConnection;
    private final RProduct poProdcts;
    private final ROrder poOrder;
    private String lomessage;
    private Boolean isSuccess;
    JSONObject poData;

    public VMProductOverview(@NonNull Application application) {
        super(application);

        this.poConnection = new ConnectionUtil(application);
        this.poOrder = new ROrder(application);
        this.application = application;
        this.poProdcts = new RProduct(application);
    }

    public LiveData<EProducts> getProductInfo(String fsListID){
        return poProdcts.GetProductInfo(fsListID);
    }
    public LiveData<Integer> GetCartItemCount(){
        return poOrder.GetCartItemCount();
    }
    public LiveData<List<DProduct.oProduct>> getProductList(int fnIndex, String fsArgs) {
        return poProdcts.GetProductsList(fnIndex, FilterType.BRAND_NAME, fsArgs, null);
    }

    public void addUpdateCart(String fsListId, int fnItemQty, OnTransactionsCallback foCallBck) {
        Boolean QtyUpdate = false;
        TaskExecutor.Execute(fsListId, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String lsListIdx = args.toString();
                    if(poConnection.isDeviceConnected()) {
                        if(QtyUpdate){
                            if(poOrder.UpdateCartQuantity(lsListIdx, fnItemQty)) {
                                lomessage = "Item added successfully";
                                return true;
                            } else {
                                lomessage = poOrder.getMessage();
                                return false;
                            }
                        } else {
                            if(poOrder.AddUpdateCart(lsListIdx, fnItemQty)) {
                                lomessage = "Item added successfully";
                                return true;
                            } else {
                                lomessage = poOrder.getMessage();
                                return false;
                            }
                        }
                    } else {
                        lomessage = "Server no response";
                        return false;
                    }
                } catch (Exception e) {
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
    public void buyNow(String fsLstngID, int fnItemQty, OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(fsLstngID, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String lsItemIdx = args.toString();
                    if(poConnection.isDeviceConnected()) {
                        boolean isSuccess = poOrder.BuyNow(lsItemIdx, fnItemQty);
                        lomessage = "Item purchased!";
                        return isSuccess;
                    } else {
                        lomessage = AppConstants.SERVER_NO_RESPONSE();
                        return false;
                    }
                } catch (Exception e) {
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
    public void ImportInquiries(String fsVal, OnInquiryReviewsImportCallback callback){
        TaskExecutor.Execute(fsVal, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {

                if(poConnection.isDeviceConnected()) {
                    if (poProdcts.GetQuestionsAndAnswers(args.toString())) {
                        poData = poProdcts.getData();

                        lomessage = "Products Imported Successfully";
                        isSuccess = true;
                    } else {
                        lomessage = poProdcts.getMessage();
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
                Boolean res = (Boolean) object;
                if (res){
                    callback.OnImport(poData.toString());
                }else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }
    public void ImportReviews(String fsVal, OnInquiryReviewsImportCallback callback){
        TaskExecutor.Execute(fsVal, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                if(poConnection.isDeviceConnected()) {
                    if (poProdcts.GetProductRatings(args.toString())) {
                        poData = poProdcts.getData();

                        lomessage = "Reviews imported successfully";
                        isSuccess = true;
                    } else {
                        lomessage = poProdcts.getMessage();
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
