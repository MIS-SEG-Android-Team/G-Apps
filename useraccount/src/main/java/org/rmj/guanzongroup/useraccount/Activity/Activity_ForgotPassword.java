package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.Etc.LogType;
import org.rmj.guanzongroup.useraccount.Model.ForgotPasswordInfoModel;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountAuthentication;

import java.util.Objects;

public class Activity_ForgotPassword extends AppCompatActivity {

    private VMAccountAuthentication mViewModel;
    private MaterialToolbar toolbar;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;
    private TextInputEditText tieEmail;
    private MaterialButton btnResend;

    public boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        mViewModel = new ViewModelProvider(Activity_ForgotPassword.this)
                .get(VMAccountAuthentication.class);

        initViews();
        setUpToolbar();

        poDialogx.initDialog();

        btnResend.setOnClickListener(v -> {
            if(!isClicked) {
                isClicked = true;
                retrievePassword();
            } else {
                Toast.makeText(Activity_ForgotPassword.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });
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

    // Initialize this first before anything else.
    private void initViews() {

        poDialogx = new MessageBox(Activity_ForgotPassword.this);

        toolbar = findViewById(R.id.toolbar);

        tieEmail = findViewById(R.id.tie_email);
        btnResend = findViewById(R.id.btnResend);
    }

    // Initialize initViews() before this method.
    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
    }

    private void retrievePassword() {
        String lsEmailxx = Objects.requireNonNull(tieEmail.getText().toString().trim());
        ForgotPasswordInfoModel infoModel = new ForgotPasswordInfoModel(LogType.EMAIL, lsEmailxx);

        if(infoModel.isDataNotEmpty()) {
            try {
                mViewModel.RetrievePassword(infoModel.getLogUser(), new VMAccountAuthentication.AuthTransactionCallback() {
                    @Override
                    public void onLoad() {
                        poLoading = new Dialog_Loading(Activity_ForgotPassword.this);
                        poLoading.initDialog("Resending Password", "Please wait while re-sending your password to your email.");
                        poLoading.show();
                    }

                    @Override
                    public void onSuccess(String fsMessage) {
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                        poDialogx.setTitle("Forgot Password");
                        poDialogx.setMessage(fsMessage);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                isClicked = false;
                                dialog.dismiss();
                                finish();
                            }
                        });

                        poDialogx.show();
                    }

                    @Override
                    public void onFailed(String fsMessage) {
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.baseline_error_24);
                        poDialogx.setTitle("Forgot Password");
                        poDialogx.setMessage(fsMessage);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                isClicked = false;
                                dialog.dismiss();
                            }
                        });

                        poDialogx.show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                isClicked = false;
            }
        } else {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Forgot Password");
            poDialogx.setMessage(infoModel.getMessage());
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    isClicked = false;
                    dialog.dismiss();
                }
            });

            poDialogx.show();

        }
    }

}