package org.rmj.guanzongroup.marketplace.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DItemCart;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DProduct;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.guanzongroup.marketplace.Model.ItemCartModel;

import java.util.ArrayList;
import java.util.List;

public class VMMPItemCart extends AndroidViewModel {
    private static final String TAG = VMMPItemCart.class.getSimpleName();
    private final MutableLiveData<List<ItemCartModel>> poItemCart = new MutableLiveData<>();
    private final ConnectionUtil poConnection;
    private final ROrder poOrder;
    private final RProduct poProdct;
    private String lomessage;

    public VMMPItemCart(@NonNull Application application) {
        super(application);
        this.poConnection = new ConnectionUtil(application);
        this.poOrder = new ROrder(application);
        this.poProdct = new RProduct(application);
    }
    public LiveData<List<DProduct.oProduct>> getProductList(int fnIndex) {
        return poProdct.GetProductsList(fnIndex, FilterType.DEFAULT, null, null);
    }
    public LiveData<List<DItemCart.oMarketplaceCartItem>> GetCartItemsList(){
        return poOrder.GetItemCartList();
    }
    public LiveData<Double> GetSelectedItemTotalPrice(){
        return poOrder.GetSelectedItemCartTotalPrice();
    }
    public LiveData<Integer> GetSelectedItemCartTotalCount(){
        return poOrder.GetSelectedItemCartTotalCount();
    }
    public LiveData<Integer> GetMpItemCartCount(){
        return poOrder.GetMartketplaceCartItemCount();
    }
    public LiveData<List<ItemCartModel>> getMarketPlaceItemCart(){
        return poItemCart;
    }

    public List<ItemCartModel> ParseDataForAdapter(List<DItemCart.oMarketplaceCartItem> foVal) {
        ArrayList<ItemCartModel> list = new ArrayList<>();
        for(int x = 0; x < foVal.size(); x++){
            ItemCartModel loDetail = new ItemCartModel();
            loDetail.setMarket(true);
            loDetail.setListingId(foVal.get(x).sListIDxx);
            loDetail.setItemName(foVal.get(x).xModelNme);
            loDetail.setItemPrice(foVal.get(x).nUnitPrce);
            loDetail.setItemQty(foVal.get(x).nQuantity);
            loDetail.setItemImage(foVal.get(x).sImagesxx);
            loDetail.setcMktCheck(foVal.get(x).cCheckOut.equalsIgnoreCase("1"));
            list.add(loDetail);
        }
        return list;
    }

    public void SelectAllItemOnCart(boolean isSelected){
        poOrder.SelectAll(isSelected);
    }
    public void DeleteAllSelected(OnTransactionsCallback foCallBck){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                if(!poOrder.DeleteAll()){
                    lomessage = poOrder.getMessage();
                    return false;
                }

                return true;
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean res = (Boolean) object;
                if (res){
                    foCallBck.onSuccess(lomessage);
                }else {
                    foCallBck.onFailed(lomessage);
                }
            }
        });
    }

    public void addUpdateCart(String fsListId, int fnItemQty, boolean QtyUpdate, OnTransactionsCallback foCallBck) {
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
    public void forCheckOut(String fsListIdx) {
        String lsListIdx = fsListIdx;
        poOrder.ForCheckOut(lsListIdx);
    }
    public void removeForCheckOut(String fsListIdx) {
        String lsListIdx = fsListIdx;
        poOrder.RemoveForCheckOut(lsListIdx);
    }
    public void checkCartItemsForCheckOut(OnTransactionsCallback foCallBck) {
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                if(poOrder.CheckCartItemsForCheckOut()) {
                    return true;
                } else {
                    lomessage = poOrder.getMessage();
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
    public void RemoveItemOnCart(String fsVal, OnTransactionsCallback callback){
        TaskExecutor.Execute(fsVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                String lsListID = args.toString();
                if(poConnection.isDeviceConnected()) {
                    if (poOrder.RemoveCartItem(lsListID)) {
                        return true;
                    } else {
                        lomessage = poOrder.getMessage();
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
                if(aBoolean){
                    callback.onSuccess("Item removed!");
                } else {
                    callback.onFailed(lomessage);
                }
            }
        });
    }
}