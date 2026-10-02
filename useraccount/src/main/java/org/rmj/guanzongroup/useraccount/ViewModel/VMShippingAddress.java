package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EAddressInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;
import org.rmj.g3appdriver.dev.Repositories.RAddressMobile;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.useraccount.Model.ShippingInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VMShippingAddress extends AndroidViewModel {
    private final RClientInfo poClientx;
    private final RAddressMobile poAddress;
    private final ConnectionUtil poConnect;
    private String lomessage;

    public VMShippingAddress(@NonNull Application application) {
        super(application);
        this.poClientx = new RClientInfo(application);
        this.poAddress = new RAddressMobile(application);
        this.poConnect = new ConnectionUtil(application);
    }

    public LiveData<EClientInfo> getClientInfo() {
        return poClientx.getClientInfo();
    }
    public LiveData<DClientInfo.ClientBSAddress> getClientBSAddress(){
        return poClientx.getClientBSAddress();
    }
    public LiveData<List<DAddress.oTownObj>> getTownCityList(){
        return poAddress.GetTownList();
    }
    public LiveData<List<EBarangayInfo>> getBarangayList(String fsTownID){
        return poAddress.GetBarangayList(fsTownID);
    }
    public LiveData<DClientInfo.oAddressUpdate> GetBillingAddressInfoForUpdate(){
        return poClientx.GetBillingAddressInfoForUpdate();
    }
    public LiveData<DClientInfo.oAddressUpdate> GetShippingAddressInfoForUpdate(){
        return poClientx.GetShippingAddressInfoForUpdate();
    }

    public ArrayList<String> getTownCityForInput(List<DAddress.oTownObj> foList) {
        return poAddress.getTownCityForInput(foList);
    }
    public ArrayList<String> getBarangayForInput(List<EBarangayInfo> foList) {
        return poAddress.getBarangayForInput(foList);
    }

    public void UpdateShippingAddress(DClientInfo.oAddressUpdate foVal, OnTransactionCallBack callBack){
        TaskExecutor.Execute(foVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                DClientInfo.oAddressUpdate loDetail = (DClientInfo.oAddressUpdate) args;
                EClientInfo loInfo = poClientx.GetClientInfo();

                if(loDetail.sTownIDxx.trim().isEmpty()){
                    lomessage = "Please select town/municipality";
                    return false;
                } else if(loDetail.sBrgyIDxx.trim().isEmpty()){
                    lomessage = "Please select barangay";
                    return false;
                } else if(loDetail.sHouseNox.equalsIgnoreCase(loInfo.getHouseNo2()) &&
                        loDetail.sAddressx.equalsIgnoreCase(loInfo.getAddress2()) &&
                        loDetail.sBrgyIDxx.equalsIgnoreCase(loInfo.getBrgyIDx2()) &&
                        loDetail.sTownIDxx.equalsIgnoreCase(loInfo.getTownIDx2())) {

                        lomessage = "No changes were made.";
                        return false;
                } else {
                    loInfo.setHouseNo2(loDetail.sHouseNox);
                    loInfo.setAddress2(loDetail.sAddressx);
                    loInfo.setBrgyIDx2(loDetail.sBrgyIDxx);
                    loInfo.setTownIDx2(loDetail.sTownIDxx);

                    if(poClientx.UpdateAddress(loInfo)){
                        return true;
                    } else {
                        lomessage = poClientx.getMessage();
                        return false;
                    }
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callBack.onSuccess("Shipping address has been updated.");
                } else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void UpdateBillingAddress(DClientInfo.oAddressUpdate foVal, OnTransactionCallBack callBack){
        TaskExecutor.Execute(foVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callBack.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                DClientInfo.oAddressUpdate loDetail = (DClientInfo.oAddressUpdate) args;
                EClientInfo loInfo = poClientx.GetClientInfo();

                if(loDetail.sTownIDxx.trim().isEmpty()){
                    lomessage = "Please select town/municipality";
                    return false;
                } else if(loDetail.sBrgyIDxx.trim().isEmpty()){
                    lomessage = "Please select barangay";
                    return false;
                } else if(loDetail.sHouseNox.equalsIgnoreCase(loInfo.getHouseNo1()) &&
                        loDetail.sAddressx.equalsIgnoreCase(loInfo.getAddress1()) &&
                        loDetail.sBrgyIDxx.equalsIgnoreCase(loInfo.getBrgyIDx1()) &&
                        loDetail.sTownIDxx.equalsIgnoreCase(loInfo.getTownIDx1())) {

                        lomessage = "No changes were made.";
                        return false;
                } else {
                    loInfo.setHouseNo1(loDetail.sHouseNox);
                    loInfo.setAddress1(loDetail.sAddressx);
                    loInfo.setBrgyIDx1(loDetail.sBrgyIDxx);
                    loInfo.setTownIDx1(loDetail.sTownIDxx);

                    if(poClientx.UpdateAddress(loInfo)){
                        return true;
                    } else {
                        lomessage = poClientx.getMessage();
                        return false;
                    }
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callBack.onSuccess("Billing address has been updated.");
                } else {
                    callBack.onFailed(lomessage);
                }
            }
        });
    }
    public void addShippingAddress(ShippingInfoModel foInfoMdl, OnTransactionCallBack foCallBck) {
        TaskExecutor.Execute(foInfoMdl, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                foCallBck.onLoading();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    ShippingInfoModel infoModel = (ShippingInfoModel) args;
                    if(poConnect.isDeviceConnected()) {
                        if(poAddress.AddContactInfo(getMobileInfo(infoModel))) {
                            Thread.sleep(1000);
                            if(poAddress.AddShipAddress(getAddressInfo(infoModel))) {
                                lomessage = "Shipping address added.";
                                return true;
                            } else {
                                lomessage = poAddress.getMessage();
                                return false;
                            }
                        } else {
                            lomessage = poAddress.getMessage();
                            return false;
                        }
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

    private EMobileInfo getMobileInfo(ShippingInfoModel infoModel) {
        EMobileInfo loMobilex = new EMobileInfo();
        loMobilex.setMobileNo(infoModel.getMobileN());

        return loMobilex;
    }
    private EAddressInfo getAddressInfo(ShippingInfoModel infoModel) {
        EAddressInfo loAddress = new EAddressInfo();
        loAddress.setTransNox(String.valueOf(ThreadLocalRandom.current().nextInt()));
        loAddress.setClientID(infoModel.getClientId());
        loAddress.setHouseNox(infoModel.getHouseNo());
        loAddress.setAddressx(infoModel.getAddress());
        loAddress.setTownIDxx(infoModel.getTownCty());
        loAddress.setBrgyIDxx(infoModel.getBarngay());
        loAddress.setReqstCDe("0");
        loAddress.setPrimaryx("0");

        return loAddress;
    }

    public interface OnTransactionCallBack {
        void onLoading();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }
}
