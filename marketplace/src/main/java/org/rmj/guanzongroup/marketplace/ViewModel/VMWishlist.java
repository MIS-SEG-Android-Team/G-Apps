package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

public class VMWishlist extends AndroidViewModel {
    private Context application;
    private ConnectionUtil poConnect;
    private final ROrder poOrdersx;
    private String lomessage;

    public VMWishlist(@NonNull Application application) {
        super(application);

        this.application = application;
        this.poConnect = new ConnectionUtil(application);
        this.poOrdersx = new ROrder(application);
    }
    public void addUpdateCart(String fsListId, int fnItemQty, boolean QtyUpdate,OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(fsListId, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    String lsListIdx = (String) args;
                    if(poConnect.isDeviceConnected()) {
                        if(QtyUpdate){
                            if(poOrdersx.UpdateCartQuantity(lsListIdx, fnItemQty)) {
                                lomessage = "Item added successfully";
                                return true;
                            } else {
                                lomessage = poOrdersx.getMessage();
                                return false;
                            }
                        } else {
                            if(poOrdersx.AddUpdateCart(lsListIdx, fnItemQty)) {
                                lomessage = "Item added successfully";
                                return true;
                            } else {
                                lomessage = poOrdersx.getMessage();
                                return false;
                            }
                        }
                    } else {
                        lomessage = poConnect.getMessage();
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
}