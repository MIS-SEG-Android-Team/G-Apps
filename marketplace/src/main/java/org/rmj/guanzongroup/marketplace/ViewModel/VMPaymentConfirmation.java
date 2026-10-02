package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.PaymentMethod;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMPaymentConfirmation extends AndroidViewModel {
    private final ROrder poOrder;
    private final ConnectionUtil poConn;
    private String lomessage;

    public VMPaymentConfirmation(@NonNull Application application) {
        super(application);
        this.poOrder = new ROrder(application);
        this.poConn = new ConnectionUtil(application);
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
                    lomessage = e.getMessage();
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
    public void payOrder(String fsTransNo, PaymentMethod foPayMeth, String fsReferNo,
                         OnTransactionsCallback foCallBck) {
        String[] params = {fsTransNo, fsReferNo};
        TaskExecutor.Execute(params, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = "Server no response";
                        return false;
                    }

                    String[] params = (String[]) args;

                    String lsTransNo = params[0];
                    String lsReferNo = params[1];

                    boolean isSuccess = poOrder.PayOrder(lsTransNo, foPayMeth, lsReferNo);
                    lomessage = (isSuccess) ? "Order Completed. \nThank you for your purchase." : poOrder.getMessage();

                    return isSuccess;
                } catch (Exception e){
                    e.printStackTrace();

                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if(isSuccess) {
                    foCallBck.onSuccess(lomessage);
                } else {
                    foCallBck.onFailed(lomessage);
                }
            }
        });
    }
}
