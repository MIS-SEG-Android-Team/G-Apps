package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMProductQueries extends AndroidViewModel {
    private final Application application;
    private final RProduct poProduct;
    private final ConnectionUtil poConnect;
    private JSONObject poData;
    private String lomessage;
    private Boolean isSuccess;

    public VMProductQueries(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poProduct = new RProduct(application);
        this.poConnect = new ConnectionUtil(application);
    }

    public LiveData<EProducts> getProductInfo(String fsListID){
        return poProduct.GetProductInfo(fsListID);
    }

    public void ImportInquiries(String fsVal, OnInquiryReviewsImportCallback callback){
        TaskExecutor.Execute(fsVal, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                if(poConnect.isDeviceConnected()) {
                    if (poProduct.GetQuestionsAndAnswers(args.toString())) {
                        poData = poProduct.getData();

                        lomessage = "Inquiries imported successfully";
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
    public void sendProductInquiry(String fsItemIdx, String fsInquiry, OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(fsItemIdx, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(poConnect.isDeviceConnected()) {
                        String lsItemIdx = args.toString();
                        if(poProduct.SendProductInquiry(lsItemIdx, fsInquiry)) {
                            Thread.sleep(1000);
                            poProduct.GetQuestionsAndAnswers(lsItemIdx);
                            lomessage = "Product Inquiry Sent.";
                            return true;
                        } else {
                            lomessage = poProduct.getMessage();
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

    public interface OnInquiryReviewsImportCallback{
        void OnImport(String args);
        void OnFailed(String message);
    }
}
