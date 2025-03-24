package org.rmj.guanzongroup.gconnect.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.rmj.g3appdriver.dev.Database.Entities.ENotificationMaster;
import org.rmj.g3appdriver.dev.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.dev.Repositories.RRawData;
import org.rmj.g3appdriver.etc.GuanzonAppConfig;
import org.rmj.g3appdriver.lib.Notifications.NMM;
import org.rmj.g3appdriver.lib.Notifications.NOTIFICATION_STATUS;
import org.rmj.g3appdriver.lib.Notifications.RemoteMessageParser;
import org.rmj.g3appdriver.lib.Notifications.iNotification;
import org.rmj.guanzongroup.notifications.Etc.NotificationUI;
import org.rmj.guanzongroup.notifications.Etc.iNotificationUI;

public class GMessagingService extends FirebaseMessagingService {
    private static final String TAG = GMessagingService.class.getSimpleName();
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        new RRawData(GMessagingService.this).SaveNewToken(token);
        new GuanzonAppConfig(GMessagingService.this).setAppToken(token);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.e(TAG, "Message received!");

        String lsSysMon = new RemoteMessageParser(message).getValueOf("msgmon");
        iNotification loSys = new NMM(GMessagingService.this).getInstance(lsSysMon);
        String lsResult = loSys.Save(message);
        if(lsResult == null){
            Log.e(TAG, loSys.getMessage());
            return;
        }

        ENotificationMaster loMaster = loSys.SendResponse(lsResult, NOTIFICATION_STATUS.RECEIVED);
        if(loMaster == null){
            Log.e(TAG, loSys.getMessage());
            return;
        }

        iNotificationUI loNotif = new NotificationUI(GMessagingService.this).getInstance(loMaster);
        loNotif.CreateNotification();
    }
}
