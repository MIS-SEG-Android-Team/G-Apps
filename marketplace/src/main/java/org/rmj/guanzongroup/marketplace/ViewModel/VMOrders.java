package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DOrderDetail;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DOrderMaster;
import org.rmj.g3appdriver.dev.Database.Entities.EOrderMaster;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMOrders extends AndroidViewModel {
    private static final String TAG = VMOrders.class.getSimpleName();
    private final Context mContext;
    private final ConnectionUtil loConnect;
    private JSONObject loData;
    private final ROrder poORder;
    private final MutableLiveData<String> poStatus = new MutableLiveData<>();
    private String lomessage;

    public VMOrders(@NonNull Application application) {
        super(application);
        this.mContext = application;
        this.loConnect = new ConnectionUtil(mContext);
        this.poORder = new ROrder(application);
        this.poStatus.setValue("");
    }

    public LiveData<String> GetOrderStatusFilter(){
        return poStatus;
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetOrderHistoryList(){
        return poORder.GetOrderHistoryList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetProcessingOrdersList(){
        return poORder.GetProcessingOrdersList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetToShipOrderList(){
        return poORder.GetToShipOrdersList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetShippedOrderList(){
        return poORder.GetShippedOrderList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetCancelledOrderList(){
        return poORder.GetCancelledOrdersList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetDeliveredOrderList(){
        return poORder.GetDeliveredOrdersList();
    }
    public LiveData<List<DOrderMaster.OrderHistory>> GetToPayOrderList(){
        return poORder.GetToPayOrderList();
    }
    public LiveData<EOrderMaster> GetOrderMaster(String fsVal){
        return poORder.GetOrderMasterInfo(fsVal);
    }
    public LiveData<List<DOrderDetail.OrderedItemsInfo>> GetOrderedItemsList(String fsVal){
        return poORder.GetOrderedItems(fsVal);
    }
    public LiveData<Integer> GetToPayOrdersCount(){
        return poORder.GetToPayOrdersCount();
    }
    public LiveData<Integer> GetProcessingOrdersCount(){
        return poORder.GetProcessingOrdersCount();
    }
    public LiveData<Integer> GetToShipOrdersCount(){
        return poORder.GetToShipOrdersCount();
    }
    public LiveData<Integer> GetDeliveredOrdersCount(){
        return poORder.GetDeliveredOrdersCount();
    }
    public LiveData<Integer> GetCancelledOrdersCount(){
        return poORder.GetCancelledOrdersCount();
    }
    public LiveData<DOrderMaster.DetailedOrderHistory> GetDetailOrderHistory(String fsVal){
        return poORder.GetDetailOrderHistory(fsVal);
    }

    public void setOrderStatusFilter(String fsVal){
        this.poStatus.setValue(fsVal);
    }
    public void ImportOrdersTask(){
        if(loConnect.isDeviceConnected()) {
            poORder.ImportPurchases();
        }
    }
    public void CheckCancellationDetail(String fsArgs, OnCheckCancellationCallback callback){
        TaskExecutor.Execute(fsArgs, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                if(loConnect.isDeviceConnected()) {
                    if (poORder.ImportCancellationDetail(args.toString())) {
                        loData = poORder.getData();

                        lomessage = "Cancellation details downloaded";
                        return true;
                    } else {
                        lomessage = poORder.getMessage();
                        return false;
                    }
                } else {
                    lomessage = "Server no response";
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean) {
                    try {
                        String dTransact = loData.getString("dTransact");
                        String sClientNm = loData.getString("sModified");
                        String sRemarksx = loData.getString("sRemarksx");

                        callback.OnCheck(dTransact, sClientNm, sRemarksx);
                    } catch (Exception e){
                        e.printStackTrace();
                        callback.OnFailed(e.getMessage());
                    }
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }
    public void CancelOrder(String fsOrderID, String fsRemarks, OnCancelCallback callback){
        TaskExecutor.Execute(fsRemarks, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.OnLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(poORder.CancelOrder(fsOrderID, args.toString())){
                        lomessage = "Order cancelled";
                        return true;
                    } else {
                        lomessage = poORder.getMessage();
                        return false;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callback.OnSuccess("Order cancelled");
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }

    public interface OnCheckCancellationCallback {
        void OnCheck(String dTransact, String sClientNm, String sRemarksx);
        void OnFailed(String message);
    }
    public interface OnCancelCallback{
        void OnLoad();
        void OnSuccess(String args);
        void OnFailed(String message);
    }
}