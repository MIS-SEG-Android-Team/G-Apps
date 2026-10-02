package org.guanzongroup.com.creditapp.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EProducts;
import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.CreditApp.CreditApplication;
import org.rmj.g3appdriver.lib.CreditApp.model.LoanTerm;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import java.util.List;

public class VMLoanTerm extends AndroidViewModel {
    private static final String TAG = VMLoanTerm.class.getSimpleName();
    private final CreditApplication poApp;
    private final RProduct poProdct;
    private final ConnectionUtil poConn;
    private String message;

    public interface OnDownloadInstallmentPlan{
        void OnDownload(String title, String message);
        void OnDownload(List<LoanTerm> args);
        void OnFailed(String message);
    }

    public VMLoanTerm(@NonNull Application application) {
        super(application);
        this.poApp = new CreditApplication(application);
        this.poProdct = new RProduct(application);
        this.poConn = new ConnectionUtil(application);
    }

    public LiveData<EProducts> GetProductInfo(String args){
        return poProdct.GetProductInfo(args);
    }
    public void ImportInstallmentPlans(String args, OnDownloadInstallmentPlan listener){
        TaskExecutor.Execute(args, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                listener.OnDownload("Apply For A Loan", "Checking installment plans. Please wait...");
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        message = "Unable to connect.";
                        return null;
                    }

                    List<LoanTerm> loDetail = poApp.GetLoanTerms(args.toString());
                    if(loDetail == null){
                        message = poApp.getMessage();
                        return null;
                    }

                    return loDetail;
                } catch (Exception e){
                    e.printStackTrace();
                    message = e.getMessage();
                    return null;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                List<LoanTerm> result = (List<LoanTerm>) object;
                if(result == null){
                    listener.OnFailed(message);
                } else {
                    listener.OnDownload(result);
                }
            }
        });
    }
}
