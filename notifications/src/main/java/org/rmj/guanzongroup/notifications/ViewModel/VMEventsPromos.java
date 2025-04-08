package org.rmj.guanzongroup.notifications.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;

import java.util.List;

public class VMEventsPromos extends AndroidViewModel {
    private String TAG = getClass().getSimpleName();
    private final iGCardSystem poSystem;
    private final Application instance;
    private String lomessage;

    public VMEventsPromos(@NonNull Application application) {
        super(application);
        this.instance = application;
        this.poSystem = new GCardSystem(application).getInstance(GCardSystem.CoreFunctions.EXTRAS);
    }

    public LiveData<List<EEvents>> getEvents() {
        return poSystem.GetNewsEvents();
    }
    public LiveData<List<EPromo>> getPromos() {
        return poSystem.GetPromotions();
    }

    public void DownloadPromos(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    iGCardSystem loGcard = new GCardSystem(instance).getInstance(GCardSystem.CoreFunctions.EXTRAS);
                    loGcard.DownloadPromotions(new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            lomessage = args;
                        }
                        @Override
                        public void OnFailed(String message) {
                            lomessage = message;
                        }
                    });
                } catch (Exception e) {
                    lomessage = e.getMessage();
                }

                return lomessage;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, object.toString());
            }
        });
    }
    public void DownloadEvents(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    iGCardSystem loGcard = new GCardSystem(instance).getInstance(GCardSystem.CoreFunctions.EXTRAS);
                    loGcard.DownloadNewsEvents(new GCardSystem.GCardSystemCallback() {
                        @Override
                        public void OnSuccess(String args) {
                            lomessage = args;
                        }

                        @Override
                        public void OnFailed(String message) {
                            lomessage = message;

                        }
                    });
                } catch (Exception e) {
                    lomessage = e.getMessage();
                }

                return lomessage;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, object.toString());
            }
        });
    }
}