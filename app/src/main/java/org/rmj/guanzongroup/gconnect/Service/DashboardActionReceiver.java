package org.rmj.guanzongroup.gconnect.Service;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RMcModel;
import org.rmj.g3appdriver.dev.Repositories.RNotificationInfo;
import org.rmj.g3appdriver.dev.Repositories.ROrder;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportBrand;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportBrandModel;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportCategory;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportMcModelPrice;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportMcTermCategory;
import org.rmj.g3appdriver.lib.Ganado.Obj.ImportTown;
import org.rmj.g3appdriver.lib.Ganado.Obj.Import_McColors;
import org.rmj.g3appdriver.lib.Ganado.Obj.Import_Relation;
import org.rmj.g3appdriver.lib.Ganado.model.ImportDataCallback;
import org.rmj.g3appdriver.lib.Ganado.model.ImportInstance;
import org.rmj.g3appdriver.utils.Task.OnDoBackgroundTaskListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.notifications.Activity.Activity_Browser;
import org.rmj.guanzongroup.notifications.Activity.Activity_GuanzonPanalo;
import org.rmj.guanzongroup.useraccount.Activity.Activity_ProfileVerification;

public class DashboardActionReceiver extends BroadcastReceiver {
    private static final String TAG = DashboardActionReceiver.class.getSimpleName();
    private char cImportxx;
    private Context loContext;
    private Application loApplication;
    private iGCardSystem loGcard;
    private iGCardSystem loGcardExtra;
    private iGCardSystem loGcardRedeemables;
    private RClientInfo loClient;
    private ROrder loPurchase;
    private RNotificationInfo loNotif;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            loContext = context;
            loApplication = (Application) context.getApplicationContext();
            loClient = new RClientInfo(context);
            loPurchase = new ROrder(context);
            loNotif = new RNotificationInfo(context);
            loGcard = new GCardSystem(context).getInstance(GCardSystem.CoreFunctions.GCARD);
            loGcardExtra = new GCardSystem(context).getInstance(GCardSystem.CoreFunctions.EXTRAS);
            loGcardRedeemables = new GCardSystem(context).getInstance(GCardSystem.CoreFunctions.REDEMPTION);

            if (intent.hasExtra("args")) {
                String args = intent.getStringExtra("args");

                Intent loIntent;
                switch (args) {
                    case "auth":
                        Thread.sleep(1000);
                        CheckDataImportTask();
                        break;
                    case "client":
                        Thread.sleep(1000);
                        ImportClientCompleteInfoTask();
                        break;
                    case "promo":
                        Thread.sleep(1000);
                        loIntent = new Intent(context, Activity_Browser.class);
                        loIntent.putExtra("url_link", intent.getStringExtra("url_link"));
                        loIntent.putExtra("args", intent.getStringExtra("browser_args"));
                        loIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(loIntent);
                        break;
                    case "panalo":
                        Thread.sleep(1000);
                        loIntent = new Intent(context, Activity_GuanzonPanalo.class);
                        loIntent.putExtra("url_link", intent.getStringExtra("url_link"));
                        loIntent.putExtra("args", intent.getStringExtra("args"));
                        loIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(loIntent);
                        break;
                    case "purchase":
                        Thread.sleep(1000);
                        ImportClientPurchasesTask();
                        break;
                    case "verify":
                        Thread.sleep(1000);
                        loIntent = new Intent(context, Activity_ProfileVerification.class);
                        loIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(loIntent);
                        break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void CheckDataImportTask(){
        GCardSystem.GCardSystemCallback callback = new GCardSystem.GCardSystemCallback() {
            @Override
            public void OnSuccess(String args) {
                try {
                    switch (cImportxx) {
                        case '0':
                            loGcard.SaveGCardInfo(new JSONObject(args));
                            break;
                        case '1':
                            loGcard.SaveMcServiceInfo(new JSONObject(args));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void OnFailed(String message) {
                Log.e(TAG, message);
            }
        };

        TaskExecutor.Execute(callback, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try{

                    //TODO: IMPORT PROMOS, NEWS AND EVENTS. PRIORITY FOR THIS FOR IMAGE LOADING
                    GCardSystem.GCardSystemCallback callback = (GCardSystem.GCardSystemCallback) args;
                    cImportxx = '0';

                    //TODO: IMPORT ACCOUNT INFO
                    if (loClient.ImportAccountInfo()) {
                        Log.d(TAG, "Client info downloaded successfully.");
                    } else {
                        Log.e(TAG, "Failed to download client info. " + loClient.getMessage());
                    }

                    Thread.sleep(500);
                    loGcardExtra.DownloadPromotions(callback);
                    Log.d(TAG, "Promotions imported successfully...");

                    Thread.sleep(500);
                    loGcardExtra.DownloadNewsEvents(true, callback); //todo import guanzon fest pageant
                    Log.d(TAG, "News events imported successfully...");

                    //TODO: IMPORT MC DATA. SECOND PRIORITY
                    Thread.sleep(500);
                    if (new RMcModel(loContext).ImportCashPrices()){
                        Log.d(TAG, "Cash price imported successfully");
                    }

                    Thread.sleep(500);
                    ImportInstance[]  importInstances = {
                            new Import_Relation(loApplication),
                            new ImportBrand(loApplication),
                            new ImportBrandModel(loApplication),
                            new Import_McColors(loApplication),
                            new ImportCategory(loApplication),
                            new ImportMcModelPrice(loApplication),
                            new ImportTown(loApplication),
                            new ImportMcTermCategory(loApplication)};

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

                        Thread.sleep(500);
                    }

                    //TODO: IMPORT GCARD DATA
                    loGcard.DownloadGcardNumbers(callback);
                    if(loGcard.hasActiveGcard().size() > 0){
                        cImportxx = '1';

                        Thread.sleep(500);
                        loGcard.DownloadMCServiceInfo(callback);

                        Thread.sleep(500);
                        loGcard.DownloadTransactions(callback);
                    }

                    Thread.sleep(500);
                    loGcardExtra.DownloadBranchesList(callback);
                    Log.d(TAG, "Branches imported successfully...");

                    Thread.sleep(500);
                    loGcardRedeemables.DownloadRedeemables(callback);

                    if (loClient.getClientId() != null) {
                        Thread.sleep(500);
                        if (loPurchase.ImportMarketPlaceItemCart()) {
                            Log.d(TAG, "Cart items downloaded successfully.");
                        } else {
                            Log.e(TAG, "Failed to download cart items. " + loPurchase.getMessage());
                        }

                        Thread.sleep(500);
                        if (loPurchase.ImportPurchases()) {
                            Log.d(TAG, "Purchases downloaded successfully.");
                        } else {
                            Log.e(TAG, "Failed to download purchases. " + loPurchase.getMessage());
                        }
                    }

                    Thread.sleep(500);
                    if (loNotif.ImportClientNotifications(0)){
                        Log.d(TAG, "Client notifications downloaded successfully.");
                    }else {
                        Log.d(TAG, loNotif.getMessage());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, "Local imports has finished.");
            }
        });
    }
    private void ImportClientCompleteInfoTask(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if (loClient.ImportAccountInfo()) {
                        Log.d(TAG, "Client info downloaded successfully. " + loClient.getClientId());
                    } else {
                        Log.e(TAG, "Failed to download client info. " + loClient.getMessage());
                    }

                    if (loClient.getClientId() != null) {
                        if (loPurchase.ImportMarketPlaceItemCart()) {
                            Log.d(TAG, "Cart items downloaded successfully.");
                        } else {
                            Log.e(TAG, "Failed to download cart items. " + loPurchase.getMessage());
                        }

                        Thread.sleep(1000);
                        if (loPurchase.ImportPurchases()) {
                            Log.d(TAG, "Purchases downloaded successfully.");
                        } else {
                            Log.e(TAG, "Failed to download purchases. " + loPurchase.getMessage());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, "Local imports for user account has finished.");
            }
        });
    }
    private void ImportClientPurchasesTask(){
        TaskExecutor.Execute(null, new OnDoBackgroundTaskListener() {
            @Override
            public Object DoInBackground(Object args) {
                try {
                    if (loPurchase.ImportPurchases()) {
                        Log.d(TAG, "Purchases downloaded successfully.");
                    } else {
                        Log.e(TAG, "Failed to download purchases. " + loPurchase.getMessage());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            public void OnPostExecute(Object object) {
                Log.d(TAG, "Local imports for purchase has finished.");
            }
        });
    }
}
