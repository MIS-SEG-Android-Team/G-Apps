package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DItemCart;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;

import java.util.List;

public class VMPlaceOrder extends AndroidViewModel {
    private final Application application;
    private final ConnectionUtil poConnect;
    private final RClientInfo poClientx;
    private final ROrder poItmCart;
    private final RAddressMobile poAddress;
    private String lomessage;

    public VMPlaceOrder(@NonNull Application application) {
        super(application);
        this.application = application;
        this.poConnect = new ConnectionUtil(application);
        this.poClientx = new RClientInfo(application);
        this.poItmCart = new ROrder(application);
        this.poAddress = new RAddressMobile(application);
    }

    public LiveData<EClientInfo> getClientInfo(){
        return poClientx.getClientInfo();
    }
    public LiveData<String> getFullAddress(String fsBrgyID) {
        return poAddress.GetFullAddressName(fsBrgyID);
    }
    public LiveData<List<DItemCart.oMarketplaceCartItem>> getCheckoutItems(boolean cBuyNowxx) {
        return poItmCart.GetCheckoutItems(cBuyNowxx);
    }
    public LiveData<DClientInfo.ClientBSAddress> getClientBSAddress(){
        return poClientx.getClientBSAddress();
    }
    public LiveData<Double> GetSelectedItemCartTotalPrice(){
        return poItmCart.GetSelectedItemCartTotalPrice();
    }
    public LiveData<Double> GetShippingFee(){
        MutableLiveData<Double> lnShipFee = new MutableLiveData();
        lnShipFee.setValue(100.0);
        return lnShipFee;
    }

    public void cancelBuyNow(boolean fromBuyNow, OnTransactionsCallback foCallBck) {
        if(fromBuyNow) {
            TaskExecutor.Execute(null, new OnTaskExecuteListener() {
                @Override
                public void OnPreExecute() {
                    foCallBck.onLoading();
                }
                @Override
                public Object DoInBackground(Object args) {
                    try {
                        if(poConnect.isDeviceConnected()) {
                            if (poItmCart.CancelBuyNow()){
                                lomessage = "Purchase cancelled";
                                return true;
                            }else {
                                lomessage = poItmCart.getMessage();
                                return true;
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
        } else {
            foCallBck.onLoading();
            foCallBck.onSuccess("");
        }
    }
    public void placeOrder(List<DItemCart.oMarketplaceCartItem> foItemLst, boolean fcDirectxx,
                           OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(foItemLst, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    List<DItemCart.oMarketplaceCartItem> loProdcts = (List<DItemCart.oMarketplaceCartItem>) args;
                    if(!poConnect.isDeviceConnected()) {
                        lomessage = "Server no response";
                        return null;
                    }else {
                        double lnTotalPrce = poItmCart.GetPlacedOrderTotalPrice();

                        String result = poItmCart.PlaceOrder(loProdcts, fcDirectxx);
                        if(result == null) {
                            lomessage = poItmCart.getMessage();
                            return null;
                        }

                        poItmCart.ImportPurchases();

                        JSONObject loResult = new JSONObject();
                        loResult.put("sTransNox", result);
                        loResult.put("nTrantotl", lnTotalPrce + 100);

                        return loResult.toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lomessage = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                String result = object.toString();
                if(result.isEmpty()) {
                    foCallBck.onFailed(lomessage);
                } else {
                    foCallBck.onSuccess(result);
                }
            }
        });
    }
}
