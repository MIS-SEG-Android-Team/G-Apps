package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DClientInfo;
import org.rmj.g3appdriver.etc.InputFieldController;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMShippingAddress;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_AddressUpdate extends AppCompatActivity {

    private TextView lblType;

    private Toolbar toolbar;
    private TextInputEditText tieHouseNo, tieAddress;
    private AutoCompleteTextView tieBrgy, tieTown;
    private MaterialButton btnUpdate;

    private Dialog_Loading poLoading;
    private MessageBox poDialogx;

    private VMShippingAddress mViewModel;

    private DClientInfo.oAddressUpdate poAddress = new DClientInfo.oAddressUpdate();

    public boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_AddressUpdate.this).get(VMShippingAddress.class);
        setContentView(R.layout.activity_address_update);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Address Update");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lblType = findViewById(R.id.lblEditAddress);
        tieHouseNo = findViewById(R.id.tie_houseNo);
        tieAddress = findViewById(R.id.tie_street);
        tieTown = findViewById(R.id.tie_towncity);
        tieBrgy = findViewById(R.id.tie_barangay);
        btnUpdate = findViewById(R.id.btnUpdate);

        poDialogx = new MessageBox(Activity_AddressUpdate.this);
        poLoading = new Dialog_Loading(Activity_AddressUpdate.this);

        poDialogx.initDialog();

        mViewModel.getTownCityList().observe(Activity_AddressUpdate.this, oTownObjs -> {
            try {
                ArrayList<String> lsTownCts = mViewModel.getTownCityForInput(oTownObjs);
                tieTown.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                Activity_AddressUpdate.this, lsTownCts
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tieTown.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getTownCityList().observe(this, townObjs -> {
                try {
                    for (int x = 0; x < townObjs.size(); x++) {
                        String lsTownCt = townObjs.get(x).sTownNm + ", " + townObjs.get(x).sProvNm;
                        if (tieTown.getText().toString().equalsIgnoreCase(lsTownCt)) {
                            poAddress.sTownIDxx = townObjs.get(x).sTownID;
                            mViewModel.getBarangayList(townObjs.get(x).sTownID).observe(this, brgys -> {
                                tieBrgy.setAdapter(
                                        InputFieldController.getAutoCompleteData(
                                                Activity_AddressUpdate.this,
                                                mViewModel.getBarangayForInput(brgys)
                                        )
                                );

                                tieBrgy.setOnItemClickListener((adapterView1, view1, i1, l1) -> {
                                    for(int x1 = 0; x1 < brgys.size(); x1++){
                                        if(brgys.get(x1).getBrgyName().equalsIgnoreCase(tieBrgy.getText().toString().trim())){
                                            poAddress.sBrgyIDxx = brgys.get(x1).getBrgyIDxx();
                                            break;
                                        }
                                    }
                                });
                            });
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        boolean cIsShipp;
        if(getIntent().hasExtra("sAddUpdte")){
            cIsShipp = !getIntent().getStringExtra("sAddUpdte").equalsIgnoreCase("billing");
            if(!cIsShipp){
                lblType.setText("Billing Address");
                mViewModel.GetBillingAddressInfoForUpdate().observe(Activity_AddressUpdate.this, oAddressUpdate -> {
                    try {
                        tieHouseNo.setText(oAddressUpdate.sHouseNox);
                        tieAddress.setText(oAddressUpdate.sAddressx);
                        tieBrgy.setText(oAddressUpdate.sBrgyName);
                        tieTown.setText(oAddressUpdate.sTownName);
                        poAddress.sBrgyIDxx = oAddressUpdate.sBrgyIDxx;
                        poAddress.sTownIDxx = oAddressUpdate.sTownIDxx;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            } else {
                lblType.setText("Shipping Address");
                mViewModel.GetShippingAddressInfoForUpdate().observe(Activity_AddressUpdate.this, oAddressUpdate -> {
                    try {
                        tieHouseNo.setText(oAddressUpdate.sHouseNox);
                        tieAddress.setText(oAddressUpdate.sAddressx);
                        tieBrgy.setText(oAddressUpdate.sBrgyName);
                        tieTown.setText(oAddressUpdate.sTownName);
                        poAddress.sBrgyIDxx = oAddressUpdate.sBrgyIDxx;
                        poAddress.sTownIDxx = oAddressUpdate.sTownIDxx;
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
            btnUpdate.setOnClickListener(v -> {
                if(!isClicked) {
                    isClicked = true;
                    poAddress.sHouseNox = tieHouseNo.getText().toString().trim();
                    poAddress.sAddressx = tieHouseNo.getText().toString().trim();
                    VMShippingAddress.OnTransactionCallBack loCallback = new VMShippingAddress.OnTransactionCallBack() {
                        @Override
                        public void onLoading() {
                            poLoading.initDialog("Address Update", "Sending address update request. Please wait...");
                            poLoading.show();
                        }

                        @Override
                        public void onSuccess(String fsMessage) {
                            poLoading.dismiss();

                            poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                            poDialogx.setTitle("Address Update");
                            poDialogx.setMessage(fsMessage);
                            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                @Override
                                public void OnButtonClick(View view, AlertDialog dialog) {
                                    isClicked = false;
                                    poDialogx.dismiss();
                                }
                            });

                            poDialogx.show();
                        }

                        @Override
                        public void onFailed(String fsMessage) {
                            poLoading.dismiss();

                            poDialogx.setIcon(R.drawable.baseline_error_24);
                            poDialogx.setTitle("Address Update");
                            poDialogx.setMessage(fsMessage);
                            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                @Override
                                public void OnButtonClick(View view, AlertDialog dialog) {
                                    isClicked = false;
                                    poDialogx.dismiss();
                                }
                            });

                            poDialogx.show();
                        }
                    };
                    if (cIsShipp) {
                        mViewModel.UpdateShippingAddress(poAddress, loCallback);
                    } else {
                        mViewModel.UpdateBillingAddress(poAddress, loCallback);
                    }
                } else {
                    Toast.makeText(Activity_AddressUpdate.this, "Please wait...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}