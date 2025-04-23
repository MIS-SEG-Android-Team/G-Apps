package org.rmj.guanzongroup.gconnect.ViewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarcode;
import org.rmj.g3appdriver.dev.Repositories.RTown;
import org.rmj.g3appdriver.etc.Barcode;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

@SuppressLint("StaticFieldLeak")
public class VMBarcode extends AndroidViewModel {

    private final Context context;
    private final Barcode poBarcode;
    private final RTown poTown;

    public VMBarcode(@NonNull Application application) {
        super(application);

        this.context = application;
        this.poBarcode = new Barcode(context);
        this.poTown = new RTown(context);
    }

    public void saveBarcode(EBarcode barcode){
        poBarcode.saveBarcode(barcode);
    }

    public void deleteBarcode(String bcodeID){
        poBarcode.deleteBarcode(bcodeID);
    }

    public LiveData<List<DTownInfo.TownProvinceInfo>> GetTownProvinceList() {
        return poTown.getTownProvinceInfo();
    }

    public LiveData<List<EBarcode>> getBarcodeList(){
        return poBarcode.getBarcodeList();
    }

    public List<EBarcode> getBarcodeEntries(){
        return poBarcode.getBarcodeEntries();
    }

    public int countBarcode(){
        return poBarcode.countBarcode();
    }

    public void generateQR(JSONObject loData, onGenerateQR callback){

        TaskExecutor.Execute(loData, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onGenerating();
            }

            @Override
            public Object DoInBackground(Object args) {

                try {

                    JSONObject params = (JSONObject) args;

                    Log.d("BARCODE", params.toString());
                    if (poBarcode.generateQR(params) == null){
                        return null;
                    }else {
                        return poBarcode.generateQR(params);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void OnPostExecute(Object object) {

                if (object != null){
                    callback.onQRGenerated((Bitmap) object);
                }else {
                    callback.onQRGenerationFailed("QR failed to generate.");
                }

            }
        });
    }

    public interface onGenerateQR{
        void onGenerating();
        void onQRGenerated(Bitmap bitmap);
        void onQRGenerationFailed(String message);
    }

    public void CheckPermission(onCheckPermission callback){

        TaskExecutor.Execute(context, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.onChecking("Initializing Camera");
            }

            @Override
            public Object DoInBackground(Object args) {
                Context instance = (Context) args;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (instance.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        return true;
                    }else {
                        return false;
                    }
                }else {
                    if (ContextCompat.checkSelfPermission(instance, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        return true;
                    }else {
                        return false;
                    }
                }
            }

            @Override
            public void OnPostExecute(Object object) {
                if ((boolean) object){
                    callback.onPermissionGranted();
                }else {
                    callback.onPermissionDenied("Camera Permission Denied. Please enable from settings.");
                }
            }
        });
    }

    public interface onCheckPermission{
        void onChecking(String message);
        void onPermissionGranted();
        void onPermissionDenied(String message);
    }

}
