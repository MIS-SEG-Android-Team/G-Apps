package org.rmj.guanzongroup.useraccount.ViewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.lib.Account.Obj.PhotoDetail;
import org.rmj.g3appdriver.utils.Task.OnLoadApplicationListener;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class VMUserVerification extends AndroidViewModel {
    private static final String TAG = VMUserVerification.class.getSimpleName();
    private final Context mContext;
    private final RClientInfo poClient;
    private final ConnectionUtil poConn;
    private JSONObject poData;
    private String lomessage;

    public VMUserVerification(@NonNull Application application) {
        super(application);
        this.mContext = application;
        this.poConn = new ConnectionUtil(mContext);
        this.poClient = new RClientInfo(mContext);
    }

    public void UploadForVerification(PhotoDetail foVal, OnSelfieVerificationSubmitCallback callback){
        TaskExecutor.Execute(foVal, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.OnLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                PhotoDetail loDetail = (PhotoDetail) args;
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = poConn.getMessage();
                        return false;
                    } else {
                        if(!poClient.UploadVerificationImage(loDetail)){
                            lomessage = poClient.getMessage();
                            return false;
                        } else {
                            String lsDtrn = loDetail.getCaptured();
                            String lsName = loDetail.getImageNme();
                            String lsHash = loDetail.getMD5Hashx();
                            String lsPath = loDetail.getFileLoct();


                            String lsDate = loDetail.getCaptured();
                            Log.d(TAG, "Selfie verification parameters: + \n" +
                                    "dTransact: " + lsDtrn + "\n" +
                                    "sImageNme: " + lsName + "\n" +
                                    "sMD5Hashx: " + lsHash + "\n" +
                                    "sImagePth: " + lsPath + "\n" +
                                    "dImgeDate: " + lsDate + "\n");

                            boolean isSuccess = poClient.SubmitSelfieVerification(lsDtrn, lsName, lsHash, lsPath, lsDate);
                            if(!isSuccess){
                                lomessage = poClient.getMessage();
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                } catch (Exception e){
                    lomessage= e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if (aBoolean){
                    callback.OnSuccess("Request for verification has been submitted.");
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }
    public void ImportValidIDCode(OnImportIDCode callback){
        TaskExecutor.Execute(null, new OnTaskExecuteListener() {
            @Override
            public void OnPreExecute() {
                callback.OnLoad();
            }
            @Override
            public Object DoInBackground(Object args) {
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = poConn.getMessage();
                        return false;
                    } else {
                        boolean isSuccess = poClient.ImportIDCode();
                        if(!isSuccess){
                            lomessage = poClient.getMessage();
                            return false;
                        } else {
                            poData = poClient.getData();
                            return true;
                        }
                    }
                } catch (Exception e){
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    try{
                        JSONArray laJson = poData.getJSONArray("detail");
                        List<JSONObject> idCode = new ArrayList<>();
                        for(int x = 0; x < laJson.length(); x++){
                            JSONObject loDetail = laJson.getJSONObject(x);
                            idCode.add(loDetail);
                        }
                        callback.OnSuccess(idCode);
                    } catch (Exception e){
                        e.printStackTrace();
                        callback.OnFailed(e.getMessage());
                    }
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
    }
    public void UploadIDVerification(List<PhotoDetail> foVal, JSONObject params, OnIDVerificationSubmitCallback callback){
        TaskExecutor loTask = new TaskExecutor();
        loTask.setOnLoadApplicationListener(new OnLoadApplicationListener() {
            @Override
            public Object DoInBackground() {
                List<PhotoDetail> loDetail = foVal;
                try{
                    if(!poConn.isDeviceConnected()){
                        lomessage = poConn.getMessage();
                        return false;
                    } else {
                        for(int x = 0; x < loDetail.size(); x++){
                            PhotoDetail loPhoto = loDetail.get(x);
                            lomessage = "Uploading ID Capture. " + x;

                            boolean isSuccess = poClient.UploadVerificationImage(loPhoto);
                            if(isSuccess){
                                lomessage = "Upload success!";
                            } else {
                                lomessage = poClient.getMessage();
                                return false;
                            }
                            loTask.publishProgress(x);
                        }

                        Thread.sleep(1000);
                        boolean isSuccess = poClient.SubmitIDVerification(params);
                        if(!isSuccess){
                            lomessage = poClient.getMessage();
                            return false;
                        } else {
                            lomessage = "Your request for account verification has been submitted.";
                            return true;
                        }
                    }
                } catch (Exception e){
                    lomessage = e.getMessage();
                    return false;
                }
            }
            @Override
            public void OnProgress(int progress) {
                callback.OnProgress(progress);
            }
            @Override
            public void OnPostExecute(Object object) {
                Boolean aBoolean = (Boolean) object;
                if(aBoolean){
                    callback.OnSuccess(lomessage);
                } else {
                    callback.OnFailed(lomessage);
                }
            }
        });
        loTask.Execute();
    }

    public interface OnSelfieVerificationSubmitCallback {
        void OnLoad();
        void OnSuccess(String message);
        void OnFailed(String message);
    }
    public interface OnIDVerificationSubmitCallback{
        void OnProgress(int progress);
        void OnSuccess(String message);
        void OnFailed(String message);
    }
    public interface OnImportIDCode{
        void OnLoad();
        void OnSuccess(List<JSONObject> idCode);
        void OnFailed(String message);
    }
}
