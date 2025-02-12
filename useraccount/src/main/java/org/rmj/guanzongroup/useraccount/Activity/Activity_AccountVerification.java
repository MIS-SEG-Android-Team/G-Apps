package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountAuthentication;

import java.util.Objects;

public class Activity_AccountVerification extends AppCompatActivity {
    private static final String TAG = Activity_AccountVerification.class.getSimpleName();

    private VMAccountAuthentication mViewModel;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;

    private String lsOtpxxx = "", lsVerify = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_verification);

        mViewModel = new ViewModelProvider(Activity_AccountVerification.this).get(VMAccountAuthentication.class);
        poDialogx = new MessageBox(Activity_AccountVerification.this);

        poDialogx.initDialog();

        if(getIntent().hasExtra("otp")){
            lsOtpxxx = getIntent().getStringExtra("otp");
            lsVerify = getIntent().getStringExtra("verify");
        }
        poLoading = new Dialog_Loading(Activity_AccountVerification.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Activate Account");
        MaterialButton btnResend = findViewById(R.id.btn_resend);
        TextInputEditText txtOtp = findViewById(R.id.tie_otp);
        MaterialButton btnSubmit = findViewById(R.id.btn_Submit);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnSubmit.setOnClickListener(v -> {
            String lsEntry = Objects.requireNonNull(txtOtp.getText()).toString();
            mViewModel.ActivateAccount(lsEntry,lsOtpxxx, lsVerify, new VMAccountAuthentication.AuthTransactionCallback() {
                @Override
                public void onLoad() {
                    poLoading.initDialog("Activating Account", "Please wait...");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                    poDialogx.setTitle("Activate Account");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();

                            Intent loIntent = new Intent();
                            loIntent.putExtra("result", "success");
                            setResult(111, loIntent);
                            finish();
                        }
                    });


                    poDialogx.show();
                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.baseline_error_24);
                    poDialogx.setTitle("Activate Account");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });

                    poDialogx.show();
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}