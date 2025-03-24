package org.rmj.guanzongroup.ganado.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EMcBrand;
import org.rmj.g3appdriver.dev.Repositories.RMcModel;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportBrand;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportBrandModel;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportCategory;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportMcModelPrice;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportMcTermCategory;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportTown;
import org.rmj.g3appdriver.lib.Ganado.Obj.Import_McColors;
import org.rmj.g3appdriver.lib.Ganado.Obj.Import_Relation;
import org.rmj.g3appdriver.lib.Ganado.Obj.ProductInquiry;
import org.rmj.g3appdriver.lib.Ganado.model.ImportDataCallback;
import org.rmj.g3appdriver.lib.Ganado.model.ImportInstance;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import java.util.List;

public class VMBrandList extends AndroidViewModel {
    private static final String TAG = ConnectionUtil.class.getSimpleName();
    private final ProductInquiry poSys;
    private final ConnectionUtil poConn;
    private final Context mContext;
    private String lomessage;

    public VMBrandList(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        poSys = new ProductInquiry(application);
        poConn = new ConnectionUtil(application);
    }
    public LiveData<List<EMcBrand>> getBrandList(){
        return poSys.GetMotorcycleBrands();
    }
    public void importCriteria(OnDownloadModels loCallback){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                loCallback.onLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if (!poConn.isDeviceConnected()){
                        return poConn.getMessage();
                    }
                    RMcModel rMcModel = new RMcModel(mContext);
                    rMcModel.ImportCashPrices();
                    ImportInstance[]  importInstances = {
                            new Import_Relation(getApplication()),
                            new ImportBrand(getApplication()),
                            new ImportBrandModel(getApplication()),
                            new Import_McColors(getApplication()),
                            new ImportCategory(getApplication()),
                            new ImportMcModelPrice(getApplication()),
                            new ImportTown(getApplication()),
                            new ImportMcTermCategory(getApplication())};
                    new Thread(() -> {
                        for (ImportInstance importInstance : importInstances) {
                            importInstance.ImportData(new ImportDataCallback() {
                                @Override
                                public void OnSuccessImportData() {
                                    Log.e(TAG, importInstance.getClass().getSimpleName() + " import success.");
                                }
                                @Override
                                public void OnFailedImportData(String message) {
                                    Log.e(TAG, importInstance.getClass().getSimpleName() + " import failed. " + message);
                                }
                            });

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    return true;
                }catch (NullPointerException e) {
                    lomessage = e.getMessage();
                    return false;
                } catch (Exception e) {
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean isSuccess = (Boolean) object;
                if(!isSuccess) {
                    loCallback.onFailed(lomessage);
                } else {
                    loCallback.onSuccess("Import finished!");
                }
            }
        });
    }
    public interface OnDownloadModels {
        void onLoad();
        void onSuccess(String fsMessage);
        void onFailed(String fsMessage);
    }
}
