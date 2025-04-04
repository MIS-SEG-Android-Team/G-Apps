package org.rmj.guanzongroup.gconnect.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.rmj.guanzongroup.ganado.Dialog.DialogDisclosure;
import org.rmj.guanzongroup.gconnect.BuildConfig;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.Service.GMessagingService;
import org.rmj.guanzongroup.gconnect.ViewModel.VMSplashScreen;

public class Activity_SplashScreen extends AppCompatActivity {

    private static final String TAG = Activity_SplashScreen.class.getSimpleName();
    private VMSplashScreen mViewModel;
    private DialogDisclosure poDialog;
    private ActivityResultLauncher<String[]> poRequest;
    private TextView txt_Status;
    private TextView txt_Version;
    private LinearProgressIndicator progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        //TODO: INITIALIZE VARIABLES
        mViewModel = new ViewModelProvider(this).get(VMSplashScreen.class);
        poDialog = new DialogDisclosure(Activity_SplashScreen.this);

        txt_Status = findViewById(R.id.txt_Status);
        txt_Version = findViewById(R.id.txt_Version);
        progbar = findViewById(R.id.progbar);

        txt_Version.setText(BuildConfig.VERSION_NAME);

        if (!isMyServiceRunning()) {
            startService(new Intent(Activity_SplashScreen.this, GMessagingService.class));
        }

        InitializeAppContent();
    }
    private void InitializeAppContent(){
        poRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            InitializeData();
        });

        String lsMessage = "Guanzon Connect collects and stores your phone number to ensure security on every transaction made within the mobile app. " +
                "This collection enables authentication and verification processes, protecting your account from unauthorized access.";

        poDialog.initDialog(new DialogDisclosure.onDisclosure() {
            @Override
            public void onAccept() {
                poDialog.dismiss();
                poRequest.launch(mViewModel.GetPermissions().toArray(new String[0]));
            }

            @Override
            public void onDecline() {
                poDialog.dismiss();
                finish();
            }
        });

        poDialog.setMessage(lsMessage);
        poDialog.show();
    }

    private void InitializeData(){
        mViewModel.setsAppVrsnCd(String.valueOf(BuildConfig.VERSION_CODE));
        mViewModel.setsAppVrsnNm(BuildConfig.VERSION_NAME);
        mViewModel.InitializeData(new VMSplashScreen.OnInitializeData() {
            @Override
            public void OnLoad(String args) {
                txt_Status.setText("Loading Data");
            }
            @Override
            public void OnProgress(String args, int progress) {
                txt_Status.setText(args);
                progbar.setProgress(progress);
            }
            @Override
            public void OnFinished(String args) {
                Intent loIntent = new Intent(Activity_SplashScreen.this, Activity_Dashboard.class);
                if(getIntent().hasExtra("notification")){
                    String lsArgs = getIntent().getStringExtra("notification");
                    switch (lsArgs){
                        case "regular":
                            break;
                        case "mp_order":
                            String lsOrderIDx = getIntent().getStringExtra("sOrderIDx");
                            loIntent.putExtra("notification", "mp_order");
                            loIntent.putExtra("sOrderIDx", lsOrderIDx);
                            break;
                        case "panalo":
                            String lsPanaloxx = getIntent().getStringExtra("panalo");
                            String lsReferNox = getIntent().getStringExtra("sReferNox");
                            loIntent.putExtra("notification", "panalo");
                            loIntent.putExtra("panalo", lsPanaloxx);
                            loIntent.putExtra("sReferNox", lsReferNox);
                            break;
                        case "promo":
                            String lsArgument = getIntent().getStringExtra("args");
                            String lsUrlLinkx = getIntent().getStringExtra("url_link");
                            loIntent.putExtra("notification", "promo");
                            loIntent.putExtra("args", lsArgument);
                            loIntent.putExtra("url_link", lsUrlLinkx);
                            break;
                        case "review":
                            String lsListngID = getIntent().getStringExtra("sListngId");
                            String lnEntryNox = getIntent().getStringExtra("nEntryNox");
                            loIntent.putExtra("notification", "review");
                            loIntent.putExtra("sListngId", lsListngID);
                            loIntent.putExtra("nEntryNox", lnEntryNox);
                            break;
                    }
                }
                startActivity(loIntent);
                finish();
            }
        });
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (GMessagingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}