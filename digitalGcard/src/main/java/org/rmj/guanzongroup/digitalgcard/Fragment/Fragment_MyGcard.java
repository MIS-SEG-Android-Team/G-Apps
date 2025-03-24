package org.rmj.guanzongroup.digitalgcard.Fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_UserInfo;
import org.rmj.guanzongroup.digitalgcard.Activity.Activity_AddGcard;
import org.rmj.guanzongroup.digitalgcard.Activity.Activity_ManageGcard;
import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardSystem;

import java.util.Objects;

public class Fragment_MyGcard extends Fragment{
    private VMGCardSystem mViewModel;
    private View view;
    private ConstraintLayout vAddGcard, vMyGcardx;
    private TextView txtManage, txtUserNm, txtCardNo, txtPoints;
    private MaterialButton btnAddCrd;
    private ImageButton btn_refresh;
    private Dialog_Loading poLoading;
    private MessageBox poDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_gcard, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(VMGCardSystem.class);

        poDialog = new MessageBox(requireActivity());
        poDialog.initDialog();
        poDialog.setTitle("GCard Information");

        mViewModel.setmContext(GCardSystem.CoreFunctions.GCARD);

        initViews();
        initMyGcard();

        return view;
    }

    private void initViews() {
        vAddGcard = view.findViewById(R.id.layout_add_gcard);
        vMyGcardx = view.findViewById(R.id.layout_my_gcard);
        txtManage = view.findViewById(R.id.lblManageGcard);
        txtUserNm = view.findViewById(R.id.lbl_gcard_user);
        txtCardNo = view.findViewById(R.id.lbl_card_number);
        txtPoints = view.findViewById(R.id.lbl_gcard_points);
        btnAddCrd = view.findViewById(R.id.btnAddGcard);
        btn_refresh = view.findViewById(R.id.btn_refresh);
    }

    private void initGCardInfo(){
        mViewModel.downloadGcardNumbers(new VMGCardSystem.GcardTransactionCallback() {
            @Override
            public void onLoad() {
                poLoading = new Dialog_Loading(requireActivity());
                poLoading.initDialog("GCard Information", "Please wait for a while.");
                poLoading.show();
            }
            @Override
            public void onSuccess(String fsMessage) {
                poLoading.dismiss();
                initMyGcard();
            }
            @Override
            public void onFailed(String fsMessage) {
                poLoading.dismiss();

                poDialog.setIcon(R.drawable.baseline_error_24);
                poDialog.setMessage(fsMessage);
                poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                poDialog.show();
            }
            @Override
            public void onQrGenerate(Bitmap foBitmap) {

            }
        });
    }
    private void initMyGcard() {
        mViewModel.getActiveGcard().observe(getViewLifecycleOwner(), eGcardApp -> {
            try {
                if(eGcardApp == null) {
                    vAddGcard.setVisibility(View.VISIBLE);
                    vMyGcardx.setVisibility(View.GONE);
                    btnAddCrd.setOnClickListener(v -> {
                        Intent loIntent = new Intent(requireActivity(), Activity_AddGcard.class);
                        startActivity(loIntent);
                    });
                } else {
                    vAddGcard.setVisibility(View.GONE);
                    vMyGcardx.setVisibility(View.VISIBLE);

                    displayGcardInfo(eGcardApp);

                    txtManage.setOnClickListener(v -> {
                        Intent loIntent = new Intent(requireActivity(), Activity_ManageGcard.class);
                        startActivity(loIntent);
                    });

                    view.findViewById(R.id.cvGcard).setOnClickListener(v -> {
                        mViewModel.ViewGCardQrCode(bitmap -> {
                            try{
                                final Dialog_UserInfo loDialog = new Dialog_UserInfo(requireActivity());
                                loDialog.initDialog(eGcardApp, bitmap);
                                loDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void displayGcardInfo(EGcardApp foGcardxx) {
        if(foGcardxx.getNmOnCard() == null || foGcardxx.getNmOnCard().equalsIgnoreCase("null")) {
            txtUserNm.setText("");
        } else {
            txtUserNm.setText(Objects.requireNonNull(foGcardxx.getNmOnCard()));
        }

        txtCardNo.setText(Objects.requireNonNull(foGcardxx.getCardNmbr()));
        txtPoints.setText(Objects.requireNonNull(foGcardxx.getAvlPoint()));

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGCardInfo();
            }
        });
    }
}