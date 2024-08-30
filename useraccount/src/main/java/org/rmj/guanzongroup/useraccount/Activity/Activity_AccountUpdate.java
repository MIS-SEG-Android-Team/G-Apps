package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_SingleButton;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountDetails;

import java.util.Objects;
import java.util.regex.Pattern;

public class Activity_AccountUpdate extends AppCompatActivity {

    private VMAccountDetails mViewModel;

    private Dialog_Loading poLoading;
    private Dialog_SingleButton poDialogx;

    private Toolbar toolbar;

    private TextView lblUpdate;
    private TextInputEditText tieUpdate, tieOld, tieNew, tieNw1;
    private MaterialButton btnSubmit;

    public boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);
        mViewModel = new ViewModelProvider(Activity_AccountUpdate.this).get(VMAccountDetails.class);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Account Update");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblUpdate = findViewById(R.id.lblAccountUpdate);
        tieUpdate = findViewById(R.id.tie_accountUpdate);
        btnSubmit = findViewById(R.id.btnUpdate);
        tieOld = findViewById(R.id.tie_oldPassword);
        tieNew = findViewById(R.id.tie_newPassword);
        tieNw1 = findViewById(R.id.tie_rtPassword);

        poDialogx = new Dialog_SingleButton(Activity_AccountUpdate.this);
        poLoading = new Dialog_Loading(Activity_AccountUpdate.this);

        if(getIntent().hasExtra("sUpdatexx")){
            switch (getIntent().getIntExtra("sUpdatexx", 0)){
                case 0:
                    findViewById(R.id.constraint_updateAccount).setVisibility(View.VISIBLE);

                    lblUpdate.setText("Enter Email Address");

                    btnSubmit.setOnClickListener(v -> {
                        if(!isClicked){
                            isClicked = true;

                            mViewModel.UpdateEmailAdd(Objects.requireNonNull(tieUpdate.getText()).toString().trim(), new VMAccountDetails.OnTransactionCallBack() {
                                @Override
                                public void onLoading() {
                                    poLoading.initDialog("Account Update", "Sending email update request. Please wait...");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }
                            });
                        } else {
                            Toast.makeText(Activity_AccountUpdate.this, "Please wait...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 1:
                    findViewById(R.id.constraint_updateAccount).setVisibility(View.VISIBLE);

                    lblUpdate.setText("Enter Mobile No");

                    tieUpdate.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    btnSubmit.setOnClickListener(v -> {
                        if(!isClicked){

                            isClicked = true;
                            mViewModel.UpdateMobileNo(Objects.requireNonNull(tieUpdate.getText()).toString().trim(), new VMAccountDetails.OnTransactionCallBack() {
                                @Override
                                public void onLoading() {
                                    poLoading.initDialog("Account Update", "Sending mobile no update request. Please wait...");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }
                            });
                        } else {
                            Toast.makeText(Activity_AccountUpdate.this, "Please wait...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 2:
                    findViewById(R.id.constraint_updateAccount).setVisibility(View.VISIBLE);

                    tieUpdate.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    lblUpdate.setText("Enter Gcash No");

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String gcashno = Objects.requireNonNull(tieUpdate.getText()).toString().trim();

                            if (ValidateGcashNo(gcashno)){

                                EClientInfo clientInfo = mViewModel.getNonliveClientInfo();

                                if (clientInfo != null){

                                    //TODO: UPDATE GCASH NO FROM EXISTING DATA AND SEND TO SERVER FOR UPDATE
                                    clientInfo.setGCashNo(gcashno);

                                    mViewModel.completeClientInfo(clientInfo, new VMAccountDetails.OnTransactionCallBack() {
                                        @Override
                                        public void onLoading() {
                                            poLoading.initDialog("Account Update", "Sending New Gcash No Update");
                                            poLoading.show();
                                        }

                                        @Override
                                        public void onSuccess(String fsMessage) {
                                            poLoading.dismiss();

                                            poDialogx.setButtonText("Dismiss");
                                            poDialogx.initDialog("Account Update", fsMessage, () -> {
                                                isClicked = false;
                                                poDialogx.dismiss();
                                            });
                                            poDialogx.show();
                                        }

                                        @Override
                                        public void onFailed(String fsMessage) {
                                            poLoading.dismiss();

                                            poDialogx.setButtonText("Dismiss");
                                            poDialogx.initDialog("Account Update", fsMessage, () -> {
                                                isClicked = false;
                                                poDialogx.dismiss();
                                            });
                                            poDialogx.show();
                                        }
                                    });

                                }else {

                                    poDialogx.setButtonText("Dismiss");
                                    poDialogx.initDialog("Account Update", "Account detail not found", () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();

                                }

                            }
                        }
                    });
                    break;
                default:
                    findViewById(R.id.constraint_updatePassword).setVisibility(View.VISIBLE);
                    btnSubmit.setText("Submit");
                    btnSubmit.setOnClickListener(v -> {
                        if(!isClicked){
                            isClicked = true;
                            String lsOld = tieOld.getText().toString().trim(),
                            lsNew = tieNew.getText().toString().trim(),
                            lsNw1 = tieNw1.getText().toString().trim();
                            mViewModel.UpdatePassword(lsOld, lsNew, lsNw1, new VMAccountDetails.OnTransactionCallBack() {
                                @Override
                                public void onLoading() {
                                    poLoading.initDialog("Account Update", "Sending mobile no update request. Please wait...");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    poLoading.dismiss();
                                    poDialogx.setButtonText("Okay");
                                    poDialogx.initDialog("Account Update", fsMessage, () -> {
                                        isClicked = false;
                                        poDialogx.dismiss();
                                    });
                                    poDialogx.show();
                                }
                            });
                        } else {
                            Toast.makeText(Activity_AccountUpdate.this, "Please wait...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    public Boolean ValidateGcashNo(String gcashno){

        poDialogx.setButtonText("Dismiss");

        if (gcashno.isEmpty()){

            poDialogx.initDialog("Account Update", "Please enter gcash number", () -> {
                isClicked = false;
                poDialogx.dismiss();
            });

            poDialogx.show();

            return false;
        }else {

            if (!gcashno.substring(0, 2).contentEquals("09")){

                poDialogx.initDialog("Account Update", "Gcash number must start with 09", () -> {
                    isClicked = false;
                    poDialogx.dismiss();
                });

                poDialogx.show();

                return false;

            }else if (gcashno.length() != 11) {

                poDialogx.initDialog("Account Update", "Mobile number must be 11 characters", () -> {
                    isClicked = false;
                    poDialogx.dismiss();
                });
                poDialogx.show();

                return false;
            } else if (!Pattern.matches(String.format("[0-9]{%s}?", gcashno.length()), gcashno)) {

                poDialogx.initDialog("Account Update", "Mobile number must only contain numbers", () -> {
                    isClicked = false;
                    poDialogx.dismiss();
                });
                poDialogx.show();

                return false;

            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}