package org.rmj.guanzongroup.digitalgcard.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.GcardCredentials;
import org.rmj.g3appdriver.lib.GCardCore.iGCardSystem;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardSystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Activity_AddGcard extends AppCompatActivity {
    private VMGCardSystem mViewModel;
    private Toolbar toolbar;
    private Dialog_Loading poLoading;
    private MessageBox poDialog;
    private TextInputEditText txtBdatex, txtGcardN;
    private MaterialButton btnAddCrd, btnScanGc;
    private static final int SCAN_GCARD = 1;
    public boolean isClicked = false;

    private final ActivityResultLauncher<Intent> poArl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == SCAN_GCARD) {
                    Intent loIntent = result.getData();
                    if (loIntent != null) {
                        String lsArgs = loIntent.getStringExtra("result");
                        addScannedGcard(lsArgs);
                    } else {
                        Toast.makeText(Activity_AddGcard.this, "No data result receive.", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gcard);

        mViewModel = new ViewModelProvider(Activity_AddGcard.this).get(VMGCardSystem.class);
        mViewModel.setmContext(GCardSystem.CoreFunctions.GCARD);

        poDialog = new MessageBox(Activity_AddGcard.this);
        poDialog.initDialog();
        poDialog.setTitle("Add GCard");

        initViews();
        setUpToolbar();

        txtBdatex.setOnClickListener(v -> datePicker());
        btnAddCrd.setOnClickListener(v -> {
            if(!isClicked) {
                isClicked = true;
                addGcard();
            }  else {
                Toast.makeText(Activity_AddGcard.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });
        btnScanGc.setOnClickListener(v -> scanGcard());

        if(getIntent().hasExtra("args")){
            String lsArgs = getIntent().getStringExtra("args");
            addScannedGcard(lsArgs);
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == SCAN_GCARD){
            iGCardSystem loGcard = new GCardSystem(Activity_AddGcard.this).getInstance(GCardSystem.CoreFunctions.GCARD);
            try {
                String lsVal = Objects.requireNonNull(data).getStringExtra("data");
                loGcard.ParseQrCode(lsVal, new GCardSystem.ParseQrCodeCallback() {
                    @Override
                    public void ApplicationResult(String src, Object args) {
                        //TODO : Add call GCardSystem>AddGCardQrCode()
                    }

                    @Override
                    public void TransactionResult(String src, Object args) {
                        Toast.makeText(Activity_AddGcard.this, args.toString(), Toast.LENGTH_LONG).show();
                        //TODO: Create dialog that will display the PIN. After closing the dialog, call GCardSystem>DownloadTransactions()
                        // Display message that transaction won't affect immediately on GCard Ledger.
                    }

                    @Override
                    public void OnFailed(String message) {
                        //TODO: Display error message dialog
                        Toast.makeText(Activity_AddGcard.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Activity_AddGcard.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        txtGcardN = findViewById(R.id.tie_gcard_number);
        txtBdatex = findViewById(R.id.tie_birth_date);
        btnAddCrd = findViewById(R.id.btnAddGcard);
        btnScanGc = findViewById(R.id.btnScanGcard);
    }
    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
    private void addGcard() {
        String lsGcardNo = Objects.requireNonNull(txtGcardN.getText().toString().trim());
        String lsBrtDate = Objects.requireNonNull(txtBdatex.getText().toString().trim());

        GcardCredentials loGcard = new GcardCredentials(lsGcardNo, lsBrtDate);
        if(loGcard.isDataValid()) {
            try {
                mViewModel.addGcard(loGcard, new VMGCardSystem.GcardTransactionCallback() {
                    @Override
                    public void onLoad() {
                        poLoading = new Dialog_Loading(Activity_AddGcard.this);
                        poLoading.initDialog("Adding GCard", "Please wait for a while.");
                        poLoading.show();
                    }

                    @Override
                    public void onSuccess(String fsMessage) {
                        poLoading.dismiss();

                        poDialog.setIcon(R.drawable.ic_baseline_message_24);
                        poDialog.setMessage(fsMessage);
                        poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                isClicked = false;
                                poDialog.dismiss();
                                finish();
                            }
                        });

                        poDialog.show();
                    }

                    @Override
                    public void onFailed(String fsMessage) {
                        poLoading.dismiss();

                        if(isJSONValid(fsMessage)) {
                            String lsErrCode = "";

                            try {

                                JSONObject loJson = new JSONObject(fsMessage);
                                lsErrCode = loJson.getString("code");

                                if("CNF".equalsIgnoreCase(lsErrCode)) {

                                    poDialog.setIcon(R.drawable.baseline_contact_support_24);
                                    poDialog.setMessage(loJson.getString("message"));
                                    poDialog.setPositiveButton("Confirm", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {

                                            confirmAddGcard(loGcard);
                                            isClicked = false;
                                            dialog.dismiss();

                                            }
                                    });

                                    poDialog.setNegativeButton("Cancel", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {

                                            isClicked = false;
                                            dialog.dismiss();

                                        }
                                    });

                                    poDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            poDialog.setIcon(R.drawable.baseline_error_24);
                            poDialog.setMessage(fsMessage);
                            poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                @Override
                                public void OnButtonClick(View view, AlertDialog dialog) {
                                    isClicked = false;
                                    poDialog.dismiss();
                                    finish();
                                }
                            });

                            poDialog.show();

                        }
                    }

                    @Override
                    public void onQrGenerate(Bitmap foBitmap) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            poDialog.setIcon(R.drawable.baseline_error_24);
            poDialog.setMessage("Add GCard Failed");
            poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    isClicked = false;
                    poDialog.dismiss();
                    finish();
                }
            });

            poDialog.show();
        }
    }
    private void addScannedGcard(String args){
        mViewModel.addScannedGcard(args, new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {
                poLoading = new Dialog_Loading(Activity_AddGcard.this);
                poLoading.initDialog("Adding GCard", "Please wait for a while.");
                poLoading.show();
            }
            @Override
            public void onSuccess(String fsMessage) {
                poLoading.dismiss();

                poDialog.setIcon(R.drawable.ic_baseline_message_24);
                poDialog.setMessage(fsMessage);
                poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        poDialog.dismiss();
                        finish();
                    }
                });

                poDialog.show();
            }
            @Override
            public void onFailed(String fsMessage) {
                poLoading.dismiss();

                if(isJSONValid(fsMessage)) {
                    String lsErrCode = "";
                    try {
                        JSONObject loJson = new JSONObject(fsMessage);
                        lsErrCode = loJson.getString("code");

                        if("CNF".equalsIgnoreCase(lsErrCode)) {

                            poDialog.setIcon(R.drawable.baseline_error_24);
                            poDialog.setMessage("GCard is already registered to other account. Please add GCard number manually and confirm to register the GCard on your account.");
                            poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                @Override
                                public void OnButtonClick(View view, AlertDialog dialog) {
                                    poDialog.dismiss();
                                    finish();
                                }
                            });

                            poDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    poDialog.setIcon(R.drawable.baseline_error_24);
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialog.dismiss();
                        }
                    });

                    poDialog.show();

                }
            }
            @Override
            public void onQrGenerate(Bitmap foBitmap) {
            }
        });
    }
    private void confirmAddGcard(GcardCredentials foGcard) {
        try {
            mViewModel.confirmAddGCard(foGcard, new VMGCardSystem.GcardTransactionCallback() {
                @Override
                public void onLoad() {
                    poLoading = new Dialog_Loading(Activity_AddGcard.this);
                    poLoading.initDialog("Adding GCard", "Please wait for a while.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.ic_baseline_message_24);
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialog.dismiss();
                            finish();
                        }
                    });

                    poDialog.show();

                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.baseline_error_24);
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialog.dismiss();
                        }
                    });

                    poDialog.show();

                }

                @Override
                public void onQrGenerate(Bitmap foBitmap) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void scanGcard() {
        Intent loIntent = new Intent(Activity_AddGcard.this, Activity_QrCodeScanner.class);
        poArl.launch(loIntent);
    }

    private void datePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        final DatePickerDialog dateFrom = new DatePickerDialog(Activity_AddGcard.this, (view, year, month, dayOfMonth) -> {
            try {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                txtBdatex.setText(dateFormatter.format(newDate.getTime()));
            } catch (Exception e){
                e.printStackTrace();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dateFrom.show();
    }
    public boolean isJSONValid(String fsMessage) {
        try {
            new JSONObject(fsMessage);
        } catch (JSONException ex) {
            try {
                new JSONArray(fsMessage);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}