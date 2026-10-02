package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DOrderMaster;
import org.rmj.g3appdriver.dev.Database.Entities.EOrderMaster;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.PaymentMethod;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMPayOrder extends AndroidViewModel {
    private final Application application;
    private final ROrder poOrder;
    private final MutableLiveData<PaymentMethod> poPayMeth = new MutableLiveData<>();
    private final ConnectionUtil poConnect;
    private String lomessage;

    public VMPayOrder(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poOrder = new ROrder(application);
        this.poConnect = new ConnectionUtil(application);
    }

    public LiveData<DOrderMaster.DetailedOrderHistory> getOrderDetail(String args){
        return poOrder.GetDetailOrderHistory(args);
    }
    public LiveData<Double> GetSelectedItemCartTotalPrice(){
        return poOrder.GetSelectedItemCartTotalPrice();
    }
    public LiveData<EOrderMaster> GetOrderMaster(String args){
        return poOrder.GetOrderMasterInfo(args);
    }
    public LiveData<String> GetOrderAmount(String args){
        return poOrder.GetOrderAmount(args);
    }
    public LiveData<PaymentMethod> getPaymentMethod() {
        return poPayMeth;
    }

    public void setPaymentMethod(PaymentMethod foPayMeth) {
        this.poPayMeth.setValue(foPayMeth);
    }

    public void payOrder(String fsTransNo, PaymentMethod foPayMeth, String fsReferNo,
                         OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(fsTransNo, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String lsTransNo = args.toString();
                    if(poConnect.isDeviceConnected()) {
                        boolean isSuccess = poOrder.PayOrder(lsTransNo, foPayMeth, fsReferNo);
                        lomessage = (isSuccess) ? "Order Completed. \nThank you for your purchase." : poOrder.getMessage();

                        return isSuccess;
                    } else {
                        lomessage = "Unable to connect.";
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

    public void CheckPaymentMethods(OnTransactionsCallback callback){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(poOrder.DownloadBankAccounts()){
                        lomessage = poOrder.getData().toString();
                        return true;
                    } else {
                        lomessage = poOrder.getMessage();
                        return false;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callback.onSuccess(lomessage);
                } else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
}
