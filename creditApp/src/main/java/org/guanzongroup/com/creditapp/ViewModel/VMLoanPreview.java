package org.guanzongroup.com.creditapp.ViewModel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.guanzongroup.com.creditapp.Model.PurchaseInfo;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.CreditApp.CreditApplication;
import org.rmj.g3appdriver.lib.CreditApp.model.MpCreditApp;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

public class VMLoanPreview extends AndroidViewModel {
    private static final String TAG = VMLoanPreview.class.getSimpleName();

    private final CreditApplication poApp;
    private final ConnectionUtil poConn;
    private final RClientInfo poClient;
    private final PurchaseInfo poModel;
    private final RProduct poProdct;
    private final MpCreditApp poCrdt;

    private String message;
    private String psDetlInf;

    public interface OnInitializeInfo{
        void OnLoad(String title, String message);
        void OnSuccess(MpCreditApp args);
        void OnFailed(String message);
    }

    public interface OnSubmitApplication{
        void OnSubmit(String title, String message);
        void OnSuccess();
        void OnFailed(String message);
    }

    public VMLoanPreview(@NonNull Application application) {
        super(application);
        this.poApp = new CreditApplication(application);
        this.poConn = new ConnectionUtil(application);
        this.poClient = new RClientInfo(application);
        this.poModel = new PurchaseInfo();
        this.poProdct = new RProduct(application);
        this.poCrdt = new MpCreditApp();
    }

    public void InitIntentData(Intent foVal){
        try {
            poModel.setLsListngID(foVal.getStringExtra("sListngID"));
            poModel.setLsUnitAppl(foVal.getStringExtra("sUnitAppl"));
            poModel.setLsModelIDx(foVal.getStringExtra("sModelIDx"));
            poModel.setLsDiscount(foVal.getStringExtra("nDiscount"));
            poModel.setLsAmortztn(foVal.getStringExtra("nMonAmort"));
            poModel.setLsDownPaym(foVal.getStringExtra("sDownPaym"));
            poModel.setLsMiscExps(foVal.getStringExtra("nMiscExps"));
            poModel.setLsInstallx(foVal.getStringExtra("sLoanTerm"));
            poCrdt.setData(foVal.getStringExtra("sDetlInfo"));
            psDetlInf = poCrdt.getData();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public PurchaseInfo getModel(){
        return poModel;
    }

    public LiveData<EProducts> GetProductInfo(){
        return poProdct.GetProductInfo(poModel.getLsListngID());
    }

    public void InitializeApplicantInfo(OnInitializeInfo listener){
        TaskExecutor.Execute(poModel, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnLoad("Apply For A Loan", "Initializing applicant info...");
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

                    JSONObject loJson = new JSONObject(lsResult);
                    String lsMeans = loJson.getString("sMeansInf");
                    String lsOther = loJson.getString("sOtherInf");

                    MpCreditApp loApp = new MpCreditApp();
                    loApp.setDateApplied(new AppConstants().DATE_MODIFIED);
                    loApp.setDateCreated(new AppConstants().DATE_MODIFIED);

                    PurchaseInfo info = (PurchaseInfo) args;

                    String lsUnitAppl = info.getLsUnitAppl();
                    String lsModelIDx = info.getLsModelIDx();
                    String lsDiscount = info.getLsDiscount();
                    String lsAmortztn = info.getLsAmortztn();
                    String lsDownPaym = info.getLsDownPaym();
                    String lsMiscExps = info.getLsMiscExps();
                    String lsInstallx = info.getLsInstallx();

                    loApp.setUnitApplied(lsUnitAppl);
                    loApp.setModel(lsModelIDx);
                    loApp.setUnitType("1");
                    loApp.setDiscount(lsDiscount);
                    loApp.setDownpayment(lsDownPaym);
                    loApp.setAmortization(lsAmortztn);
                    loApp.setMiscellaneousExpense(lsMiscExps);
                    loApp.setInstallmentTerm(lsInstallx.split(" ")[0]);

                    EClientInfo loClient = poClient.GetClientInfo();

                    String lsLastNm = loClient.getLastName();
                    String lsMiddNm = loClient.getMiddName();
                    String lsFrstNm = loClient.getFrstName();
                    String lsGender = loClient.getGenderCd();
                    String lsCvlStt = loClient.getCvilStat();

                    loApp.clientInfo().setLastName(lsLastNm);
                    loApp.clientInfo().setFirstName(lsFrstNm);
                    loApp.clientInfo().setMiddleName(lsMiddNm);
                    loApp.clientInfo().setGender(lsGender);
                    loApp.clientInfo().setCivilStatus(lsCvlStt);

                    loApp.clientInfo().addressInfo().setHouseNo(loClient.getHouseNo1());
                    loApp.clientInfo().addressInfo().setAddress1(loClient.getAddress1());
                    loApp.clientInfo().addressInfo().setBarangayID(loClient.getBrgyIDx1());
                    loApp.clientInfo().addressInfo().setTownID(loClient.getTownIDx1());

                    MpCreditApp loMeans = new MpCreditApp();
                    loMeans.setData(lsMeans);
                    loApp.meansInfo().setData(loMeans.getData());

                    MpCreditApp loOther = new MpCreditApp();
                    loMeans.setData(lsOther);
                    loApp.disbursementInfo().setData(loOther.getData());

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
    public void SubmitApplication(OnSubmitApplication listener){
        TaskExecutor.Execute(psDetlInf, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnSubmit("Apply For A Loan", "Submitting application. Please wait...");
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        message = "Unable to connect";
                        return false;
                    }

                    if(!poApp.SubmitApplication(args.toString())){
                        message = poApp.getMessage();
                        return false;
                    }

                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                    message = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if(!isSuccess){
                    listener.OnFailed(message);
                } else {
                    listener.OnSuccess();
                }
            }
        });
    }
}
