package org.guanzongroup.com.creditapp.ViewModel;

import android.app.Application;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.guanzongroup.com.creditapp.Model.PurchaseInfo;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DAddress;
import org.rmj.g3appdriver.dev.Database.Entities.EBarangayInfo;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.CreditApp.CreditApplication;
import org.rmj.g3appdriver.lib.CreditApp.model.MpCreditApp;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import java.util.List;

public class VMApplicantInfo extends AndroidViewModel {
    private static final String TAG = VMApplicantInfo.class.getSimpleName();

    private final CreditApplication poApp;
    private final ConnectionUtil poConn;
    private final PurchaseInfo poModel;

    private final MutableLiveData<String> psTownID = new MutableLiveData<>();

    private String message;

    public interface OnInitializeInfo{
        void OnLoad(String title, String message);
        void OnSuccess(MpCreditApp args);
        void OnFailed(String message);
    }

    public VMApplicantInfo(@NonNull Application application) {
        super(application);
        this.poApp = new CreditApplication(application);
        this.poConn = new ConnectionUtil(application);
        this.poModel = new PurchaseInfo();
        this.psTownID.setValue("");
    }

    public void InitIntentData(Intent foVal){
        poModel.setLsListngID(foVal.getStringExtra("sListngID"));
        poModel.setLsUnitAppl(foVal.getStringExtra("sUnitAppl"));
        poModel.setLsModelIDx(foVal.getStringExtra("sModelIDx"));
        poModel.setLsDiscount(foVal.getStringExtra("nDiscount"));
        poModel.setLsAmortztn(foVal.getStringExtra("nMonAmort"));
        poModel.setLsDownPaym(foVal.getStringExtra("sDownPaym"));
        poModel.setLsMiscExps(foVal.getStringExtra("nMiscExps"));
        poModel.setLsInstallx(foVal.getStringExtra("sLoanTerm"));
    }

    public LiveData<String> GetTownID(){
        return psTownID;
    }
    public LiveData<List<EBarangayInfo>> GetBarangayList(String TownID) {
        return poApp.GetBarangayList(TownID);
    }
    public LiveData<List<DAddress.oTownObj>> GetTownList() {
        return poApp.GetTownList();
    }

    public PurchaseInfo getModel(){
        return poModel;
    }
    public void setTownID(String val){
        this.psTownID.setValue(val);
    }
    public void InitializeApplicantInfo(OnInitializeInfo listener){
        TaskExecutor.Execute(poModel, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnLoad("Apply For A Loan", "Initializing applicant info");
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if(!poConn.isDeviceConnected()){
                        message = "Unable to connect.";
                        return null;
                    }

                    String lsResult = poApp.GetOtherApplicationInfo();

                    if(lsResult == null){
                        message = poApp.getMessage();
                        return null;
                    }

                    PurchaseInfo info = (PurchaseInfo) args;

                    JSONObject loJson = new JSONObject(lsResult);

                    String lsUnitAppl = info.getLsUnitAppl();
                    String lsModelIDx = info.getLsModelIDx();
                    String lsDiscount = info.getLsDiscount();
                    String lsAmortztn = info.getLsAmortztn();
                    String lsDownPaym = info.getLsDownPaym();
                    String lsMiscExps = info.getLsMiscExps();
                    String lsInstallx = info.getLsInstallx();

                    MpCreditApp loApp = poApp.ParseData(loJson);

                    loApp.setUnitApplied(lsUnitAppl);
                    loApp.setModel(lsModelIDx);
                    loApp.setUnitType("1");
                    loApp.setDiscount(lsDiscount);
                    loApp.setDownpayment(lsDownPaym);
                    loApp.setAmortization(lsAmortztn);
                    loApp.setMiscellaneousExpense(lsMiscExps);
                    loApp.setInstallmentTerm(lsInstallx.split(" ")[0]);

                    return loApp;
                } catch (Exception e){
                    e.printStackTrace();
                    message = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                MpCreditApp result = (MpCreditApp) object;

                if(result == null){
                    listener.OnFailed(message);
                } else {
                    listener.OnSuccess(result);
                }
            }
        });
    }
}
