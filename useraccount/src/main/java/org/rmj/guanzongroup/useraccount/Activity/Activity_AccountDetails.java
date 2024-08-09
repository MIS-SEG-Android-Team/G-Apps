package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.kofigyan.stateprogressbar.StateProgressBar;
import org.rmj.g3appdriver.dev.Database.Entities.EMobileInfo;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_SingleButton;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountDetails;
import java.util.Objects;
import org.rmj.guanzongroup.useraccount.R;


public class Activity_AccountDetails extends AppCompatActivity {
    private static final String TAG = Activity_AccountDetails.class.getSimpleName();
    private static final String[] descriptionData = {"Basic Account", "Account Verified", "Fully Verified"};

    private VMAccountDetails mViewModel;
    private Dialog_Loading poLoading;
    private Dialog_SingleButton poDialogx;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_details);

        mViewModel = new ViewModelProvider(Activity_AccountDetails.this)
                .get(VMAccountDetails.class);

        initViews();
        setUpToolbar();
        importAccountInfo();
        setAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        poDialogx = new Dialog_SingleButton(Activity_AccountDetails.this);

        StateProgressBar progressBar = findViewById(R.id.your_state_progress_bar_id);
        progressBar.setStateDescriptionData(descriptionData);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Details");
    }

    private void setAdapter() {
        try {

            mViewModel.GetClientDetailForPreview().observe(this, clientInfo -> {
                try {
                    TextView lblUserNm = findViewById(R.id.lbl_username);
                    TextView lblInfoVerify= findViewById(R.id.lbl_Info_verify);
                    lblUserNm.setText(clientInfo.sUserName);

                    if(clientInfo.cVerified.equalsIgnoreCase("1")) {
                        lblInfoVerify.setVisibility(View.GONE);
                        Drawable img = Activity_AccountDetails.this.getResources().getDrawable(R.drawable.ic_baseline_verified_user_24);
                        img.setBounds(0, 0, 60, 60);
                        lblUserNm.setCompoundDrawables(null, null, img, null);
                    }

                    TextView lblUserID = findViewById(R.id.lbl_appID);
                    lblUserID.setText(clientInfo.sUserIDxx);

                    TextView lblFullNm = findViewById(R.id.lbl_fullName);
                    String lsFullNme = clientInfo.sLastName + ", " + clientInfo.sFrstName;

                    if(!clientInfo.sMiddName.trim().isEmpty()){
                        lsFullNme = lsFullNme + " " + clientInfo.sMiddName;
                    }

                    if(!clientInfo.sSuffixNm.trim().isEmpty()){
                        lsFullNme = lsFullNme + ", " + clientInfo.sSuffixNm;
                    }
                    lblFullNm.setText(lsFullNme);

                    TextView lblGender = findViewById(R.id.lbl_gender);
                    String lsGenderx = mViewModel.getGenderList().get(Integer.parseInt(clientInfo.cGenderCd));
                    lblGender.setText(lsGenderx);

                    TextView lblCvilSt = findViewById(R.id.lbl_civilStatus);
                    String lsCivilSt = mViewModel.getCivilStatusList().get(Integer.parseInt(clientInfo.cCvilStat));
                    lblCvilSt.setText(lsCivilSt);

                    TextView lblBirthP = findViewById(R.id.lbl_birthPlace);
                    lblBirthP.setText(clientInfo.sBirthPlc);

                    TextView lblBirthD = findViewById(R.id.lbl_birthDate);
                    String lsBirthDt = mViewModel.getDate(clientInfo.dBirthDte);
                    lblBirthD.setText(lsBirthDt);

                    TextView lblEmailx = findViewById(R.id.lbl_email);
                    lblEmailx.setText(clientInfo.sEmailAdd);

                    mViewModel.GetEmailInfo(clientInfo.sEmailAdd, args -> {
                        if(args != null){
                            if(args.getIsVerifd() == 1){
                                Drawable img = Activity_AccountDetails.this.getResources().getDrawable(R.drawable.ic_baseline_verified_24);
                                img.setBounds(0, 0, 60, 60);
                                lblEmailx.setCompoundDrawables(null, null, img, null);
                            }
                        }
                    });

                    findViewById(R.id.lbl_editEmail).setOnClickListener(v -> {
                        Intent loIntent = new Intent(Activity_AccountDetails.this, Activity_AccountUpdate.class);
                        loIntent.putExtra("sUpdatexx", 0);
                        startActivity(loIntent);
                    });

                    TextView lblMobile = findViewById(R.id.lbl_mobile);
                    lblMobile.setText(clientInfo.sMobileNo);

                    mViewModel.GetMobileInfo(clientInfo.sMobileNo, new VMAccountDetails.OnRetrieveMobileInfo() {
                        @Override
                        public void OnRetrieve(EMobileInfo args) {
                            if(args != null){
                                if(args.getVerified().equalsIgnoreCase("1")){
                                    Drawable img = Activity_AccountDetails.this.getResources().getDrawable(R.drawable.ic_baseline_verified_24);
                                    img.setBounds(0, 0, 60, 60);
                                    lblMobile.setCompoundDrawables(null, null, img, null);
                                }
                            }
                        }
                    });

                    findViewById(R.id.lbl_editMobileNo).setOnClickListener(v -> {
                        Intent loIntent = new Intent(Activity_AccountDetails.this, Activity_AccountUpdate.class);
                        loIntent.putExtra("sUpdatexx", 1);
                        startActivity(loIntent);
                    });

                    TextView lblGcash = findViewById(R.id.lbl_gcash);
                    lblGcash.setText(clientInfo.sGCashNox);

                    findViewById(R.id.lbl_editGcashNo).setOnClickListener(v -> {
                        Intent loIntent = new Intent(Activity_AccountDetails.this, Activity_AccountUpdate.class);
                        loIntent.putExtra("sUpdatexx", 2);
                        startActivity(loIntent);
                    });

                    findViewById(R.id.lbl_editPassword).setOnClickListener(v -> {
                        Intent loIntent = new Intent(Activity_AccountDetails.this, Activity_AccountUpdate.class);
                        loIntent.putExtra("sUpdatexx", 3);
                        startActivity(loIntent);
                    });

                    findViewById(R.id.btnVerify).setOnClickListener(v -> {
                        Intent loIntent = new Intent(Activity_AccountDetails.this, Activity_ProfileVerification.class);
                        loIntent.putExtra("cSkippedx", false);
                        startActivity(loIntent);
                    });

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void importAccountInfo() {
        try {
            mViewModel.importAccountInfo(new VMAccountDetails.OnTransactionCallBack() {
                @Override
                public void onLoading() {
                    poLoading = new Dialog_Loading(Activity_AccountDetails.this);
                    poLoading.initDialog("Account Details", "Importing Account Data. Please wait.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();
                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();
                    poDialogx.setButtonText("Okay");
                    poDialogx.initDialog("Account Details", fsMessage, () -> {
                        poDialogx.dismiss();
                        finish();
                    });
                    poDialogx.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}