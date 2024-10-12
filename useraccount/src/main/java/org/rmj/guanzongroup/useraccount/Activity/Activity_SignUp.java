package org.rmj.guanzongroup.useraccount.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.AccountAuthentication;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountAuthentication;

import java.util.Objects;

public class Activity_SignUp extends AppCompatActivity {
    private VMAccountAuthentication mViewModel;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;
    private MaterialToolbar toolbar;
    private CheckBox cbAgreexx;
    private TextInputEditText txtUserNm, txtEmailx, txtMobile, txtPasswd, txtRetype;
    private TextView txtTermsx, txtPrivcy;
    private MaterialButton btnSignUp;
    private static final int VERIFY = 111;
    public boolean isClicked = false;
    private AccountAuthentication.AccountCredentials loCrednts;

    private final ActivityResultLauncher<Intent> poArl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == VERIFY) {
                    Intent loIntent = result.getData();
                    if (loIntent != null) {
                        if(loIntent.getStringExtra("result").equalsIgnoreCase("success")){
                            LoginAccount();
                        }
                    } else {
                        Toast.makeText(Activity_SignUp.this, "No data result receive.", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mViewModel = new ViewModelProvider(Activity_SignUp.this).get(VMAccountAuthentication.class);

        initViews();
        setUpToolbar();

        poDialogx.initDialog();

        btnSignUp.setOnClickListener(view -> {
            if(!isClicked) {
                isClicked = true;
                signUp();
            } else {
                Toast.makeText(Activity_SignUp.this, "Please wait...", Toast.LENGTH_SHORT).show();
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
        poDialogx = new MessageBox(Activity_SignUp.this);
        toolbar = findViewById(R.id.toolbar);
        cbAgreexx = findViewById(R.id.cbAgree);
        txtUserNm = findViewById(R.id.tie_username);
        txtEmailx = findViewById(R.id.tie_email);
        txtMobile = findViewById(R.id.tie_mobile);
        txtPasswd = findViewById(R.id.tie_password);
        txtRetype = findViewById(R.id.tie_retype);
        txtTermsx = findViewById(R.id.tvTerms);
        txtPrivcy = findViewById(R.id.tvPrivacy);
        btnSignUp = findViewById(R.id.btnSignUp);

        txtTermsx.setOnClickListener(v -> intentTerms(0));
        txtPrivcy.setOnClickListener(v -> intentTerms(1));
    }

    // Initialize initViews() before this method.
    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void intentTerms(int index) {
        Intent loIntent = new Intent(Activity_SignUp.this, Activity_TermsAndConditions.class);
        switch (index) {
            case 0:
                loIntent.putExtra("cTermsDsp", 0);
                startActivity(loIntent);
                break;
            case 1:
                loIntent.putExtra("cTermsDsp", 1);
                startActivity(loIntent);
                break;
        }
    }

    private void signUp() {
        loCrednts = new AccountAuthentication.AccountCredentials();
        loCrednts.setUserName(Objects.requireNonNull(Objects.requireNonNull(txtUserNm.getText()).toString().trim()));
        loCrednts.setEmailAdd(Objects.requireNonNull(Objects.requireNonNull(txtEmailx.getText()).toString().trim()));
        loCrednts.setMobileNo(Objects.requireNonNull(Objects.requireNonNull(txtMobile.getText()).toString().trim()));
        loCrednts.setPassword(Objects.requireNonNull(Objects.requireNonNull(txtPasswd.getText()).toString().trim()));
        loCrednts.setPasswrd2(Objects.requireNonNull(Objects.requireNonNull(txtRetype.getText()).toString().trim()));
        loCrednts.setcAgreeTnC((cbAgreexx.isChecked()) ? "1" : "0");
        try {
            mViewModel.RegisterAccount(loCrednts, new VMAccountAuthentication.AuthTransactionCallback() {
                @Override
                public void onLoad() {
                    poLoading = new Dialog_Loading(Activity_SignUp.this);
                    poLoading.initDialog("Signing Up", "Please wait for a while.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();
                    isClicked = false;
                    LoginAccount();
                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.baseline_error_24);
                    poDialogx.setTitle("Sign Up Failed");
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
    }

    //Automatically login account after successfully signing up to request OTP for activation...
    private void LoginAccount(){
        AccountAuthentication.LoginCredentials loLogin =
                new AccountAuthentication.LoginCredentials(
                        loCrednts.getsEmailAdd(),
                        loCrednts.getsPassword());
        mViewModel.LoginAccount(loLogin, new VMAccountAuthentication.AuthenticationCallback() {
            @Override
            public void onLoad() {
                poLoading = new Dialog_Loading(Activity_SignUp.this);
                poLoading.initDialog("Logging In", "Please wait for a while.");
                poLoading.show();
            }

            @Override
            public void onSuccess(String fsMessage) {
                Intent intent = new Intent("android.intent.action.SUCCESS_LOGIN");
                intent.putExtra("args", "auth");
                sendBroadcast(intent);
                poLoading.dismiss();
                finish();
            }

            @Override
            public void onFailed(String fsMessage) {
                poLoading.dismiss();

                poDialogx.setIcon(R.drawable.baseline_error_24);
                poDialogx.setTitle("Logging In");
                poDialogx.setMessage("Unable to sign in your account. Please try again. " + fsMessage);
                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });


                poDialogx.show();

            }

            @Override
            public void onVerifiy(String args1, String args2) {
                poLoading.dismiss();

                Log.d("Activation OTP", args1);

                Intent loIntent = new Intent(Activity_SignUp.this, Activity_AccountVerification.class);
                loIntent.putExtra("otp", args1);
                loIntent.putExtra("verify", args2);
                loIntent.putExtra("email", loCrednts.getsEmailAdd());
                loIntent.putExtra("passw", loCrednts.getsPassword());

                poArl.launch(loIntent);
            }
        });
    }
}