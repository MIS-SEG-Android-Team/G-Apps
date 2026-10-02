package org.rmj.guanzongroup.ganado.ViewModel;

import static org.rmj.g3appdriver.etc.AppConstants.getLocalMessage;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DGanadoOnline;
import org.rmj.g3appdriver.dev.Database.Entities.EGanadoOnline;
import org.rmj.g3appdriver.dev.Database.Entities.EMCColor;
import org.rmj.g3appdriver.dev.Database.Entities.EMcBrand;
import org.rmj.g3appdriver.dev.Database.Entities.EMcModel;
import org.rmj.g3appdriver.lib.Ganado.Obj.Ganado;
import org.rmj.g3appdriver.lib.Ganado.Obj.ProductInquiry;
import org.rmj.g3appdriver.lib.Ganado.pojo.InquiryInfo;
import org.rmj.g3appdriver.lib.Ganado.pojo.InstallmentInfo;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMInstallmentSummary extends AndroidViewModel implements GanadoUI {

    private static final String TAG = VMInstallmentSummary.class.getSimpleName();

    private final ProductInquiry poApp;
    private final Ganado oApp;
    private final InquiryInfo poModel;
    private String TransNox;
    private final MutableLiveData<String> psBrandID = new MutableLiveData<>();
    private final MutableLiveData<String> psModelID = new MutableLiveData<>();

    private String message;

    public VMInstallmentSummary(@NonNull Application instance) {
        super(instance);
        this.poApp = new ProductInquiry(instance);
        this.poModel = new InquiryInfo();
        this.oApp = new Ganado(instance);
        poModel.setGanadoTp("1");
    }

    public InquiryInfo getModel() {
        return poModel;
    }

    public void setModelID(String args) {
        this.psModelID.setValue(args);
    }

    public LiveData<String> GetModelID() {
        return psModelID;
    }

    public DGanadoOnline.CashPrice GetCashInfo(String ModelID){
        return poApp.GetCashInfo(ModelID);
    }

    public LiveData<EGanadoOnline> GetInquiryDetail() {
        return poApp.GetInquiryDetail(TransNox);
    }

    @Override
    public void InitializeApplication(Intent params) {
        TransNox = params.getStringExtra("sTransNox");
    }

    @Override
    public LiveData<EGanadoOnline> GetApplication() {
        return null;
    }

    @Override
    public void ParseData(EGanadoOnline args, OnParseListener listener) {
        Log.d(TAG, "No data to parse on introductory question");
    }

    @Override
    public void Validate(Object args) {
    }

    @Override
    public void SaveData(OnSaveInfoListener listener) {
        TaskExecutor.Execute(listener, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {

            }

            @Override
            public Object DoInBackground(Object args) {
                try {
                    InquiryInfo loDetail = poModel;
                    InquiryInfo.InquiryInfoValidator loValid = new InquiryInfo.InquiryInfoValidator();

                    if (!loValid.isDataValid(loDetail)) {
                        message = loValid.getMessage();
                        return null;
                    }

                    String lsResult = oApp.CreateInquiry(loDetail);

                    if (lsResult == null) {
                        message = poApp.getMessage();
                        return null;
                    }

                    return lsResult;
                } catch (Exception e) {
                    e.printStackTrace();
                    message = getLocalMessage(e);
                    return null;
                }
            }

            @Override
            public void OnPostExecute(Object object) {
                String lsResult = (String) object;
                if (lsResult == null) {
                    listener.OnFailed(message);
                } else {
                    listener.OnSave(lsResult);
                }
            }
        });
    }
}
