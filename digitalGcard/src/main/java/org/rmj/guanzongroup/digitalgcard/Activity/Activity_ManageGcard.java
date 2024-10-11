package org.rmj.guanzongroup.digitalgcard.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.digitalgcard.Adapter.Adapter_GcardList;
import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardSystem;

import java.util.Objects;

public class Activity_ManageGcard extends AppCompatActivity {
    private VMGCardSystem mViewModel;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;
    private Adapter_GcardList poAdapter;
    private MaterialButton btnAddGcard;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtUserNm, txtCardNo, txtPoints, lblOtherx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gcard);

        mViewModel = new ViewModelProvider(Activity_ManageGcard.this).get(VMGCardSystem.class);
        mViewModel.setmContext(GCardSystem.CoreFunctions.GCARD);

        poDialogx = new MessageBox(Activity_ManageGcard.this);
        poDialogx.initDialog();
        poDialogx.setTitle("GCard Activation");

        initViews();
        setUpToolbar();
        displayGcard();

        btnAddGcard.setOnClickListener(v -> {
            Intent loIntent = new Intent(Activity_ManageGcard.this, Activity_AddGcard.class);
            startActivity(loIntent);
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
        txtUserNm = findViewById(R.id.lbl_gcard_user);
        txtCardNo = findViewById(R.id.lbl_card_number);
        txtPoints = findViewById(R.id.lbl_gcard_points);
        lblOtherx = findViewById(R.id.lblOtherGcard);
        btnAddGcard = findViewById(R.id.btnAddGcard);
        toolbar = findViewById(R.id.toolbar_transaction);
        recyclerView = findViewById(R.id.card_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_ManageGcard.this));
        recyclerView.setHasFixedSize(true);
    }

    // Initialize initViews() before this method.
    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage GCard");
    }

    private void displayGcard() {
        displayActiveGcard();
        displayInactiveGcard();
    }
    private void displayActiveGcard() {
        mViewModel.getActiveGcard().observe(Activity_ManageGcard.this, eGcardApp -> {
            try {
                if(eGcardApp != null) {
                    txtUserNm.setText(Objects.requireNonNull(eGcardApp.getNmOnCard()));
                    txtCardNo.setText(Objects.requireNonNull(eGcardApp.getCardNmbr()));
                    txtPoints.setText(Objects.requireNonNull(eGcardApp.getAvlPoint()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void displayInactiveGcard() {
        mViewModel.getInactiveGCard().observe(Activity_ManageGcard.this, eGcardApps -> {
            try {
                if(eGcardApps.size() > 0) {
                    lblOtherx.setVisibility(View.VISIBLE);
                    poAdapter = new Adapter_GcardList(eGcardApps, fsCardNox -> {
                        mViewModel.setActiveGcard(fsCardNox, new VMGCardSystem.GcardTransactionCallback() {
                            @Override
                            public void onLoad() {
                                poLoading = new Dialog_Loading(Activity_ManageGcard.this);
                                poLoading.initDialog("Activating GCard", "Please wait for a while.");
                                poLoading.show();
                            }

                            @Override
                            public void onSuccess(String fsMessage) {
                                poLoading.dismiss();

                                poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                                poDialogx.setMessage(fsMessage);
                                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                    @Override
                                    public void OnButtonClick(View view, AlertDialog dialog) {
                                        poDialogx.dismiss();
                                    }
                                });

                                poDialogx.show();
                            }

                            @Override
                            public void onFailed(String fsMessage) {
                                poDialogx.setIcon(R.drawable.baseline_error_24);
                                poDialogx.setMessage(fsMessage);
                                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                    @Override
                                    public void OnButtonClick(View view, AlertDialog dialog) {
                                        poDialogx.dismiss();
                                    }
                                });

                                poDialogx.show();
                            }

                            @Override
                            public void onQrGenerate(Bitmap foBitmap) {

                            }
                        });
                    });
                    recyclerView.setAdapter(poAdapter);
                    poAdapter.notifyDataSetChanged();
                } else {
                    lblOtherx.setVisibility(View.GONE);
                }
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

}