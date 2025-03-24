package org.rmj.guanzongroup.useraccount.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.rmj.g3appdriver.etc.InputFieldController;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.Activity.Activity_ShippingAddress;
import org.rmj.guanzongroup.useraccount.Model.ShippingInfoModel;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMShippingAddress;
import org.rmj.guanzongroup.useraccount.databinding.FragmentAddressCreateBinding;

import java.util.ArrayList;
import java.util.Objects;

public class Fragment_AddressCreate extends Fragment {

    private VMShippingAddress mViewModel;
    private FragmentAddressCreateBinding mBinding;
    private MessageBox poDialogx;
    private Dialog_Loading poLoading;
    private ShippingInfoModel infoModel;

    public Fragment_AddressCreate() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(VMShippingAddress.class);
        mBinding = FragmentAddressCreateBinding.inflate(inflater, container, false);

        View view = mBinding.getRoot();

        poDialogx = new MessageBox(requireActivity());
        poLoading = new Dialog_Loading(requireActivity());
        infoModel = new ShippingInfoModel(new AccountInfo(requireActivity()).getClientID());

        initInputSelectors();
        poDialogx.initDialog();

        mBinding.btnAddShp.setOnClickListener(v -> addShipping());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initInputSelectors() {
//        mBinding.spnSrcCde.setAdapter(new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, AppConstants.REQUEST_CODE));
        mViewModel.getTownCityList().observe(requireActivity(), townObj ->{
            try {
                ArrayList<String> lsTownCts = mViewModel.getTownCityForInput(townObj);
                mBinding.txtTownCt.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                requireActivity(), lsTownCts
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mBinding.txtTownCt.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getTownCityList().observe(requireActivity(), townObjs -> {
                try {
                    for (int x = 0; x < townObjs.size(); x++) {
                        String lsTownCt = townObjs.get(x).sTownNm + ", " + townObjs.get(x).sProvNm;
                        if (mBinding.txtTownCt.getText().toString().equalsIgnoreCase(lsTownCt)) {
                            infoModel.setTownCty(townObjs.get(x).sTownID);
                            mViewModel.getBarangayList(townObjs.get(x).sTownID)
                                    .observe(requireActivity(), brgys -> {
                                mBinding.txtBrgyxx.setAdapter(
                                        InputFieldController.getAutoCompleteData(
                                                requireActivity(),
                                                mViewModel.getBarangayForInput(brgys)
                                        )
                                );

                                mBinding.txtBrgyxx.setOnItemClickListener((adapterView1, view1, i1, l1) -> {
                                    for(int x1 = 0; x1 < brgys.size(); x1++){
                                        if(brgys.get(x1).getBrgyName().
                                                equalsIgnoreCase(mBinding.txtBrgyxx.getText()
                                                        .toString().trim())){
                                            infoModel.setBarngay(brgys.get(x1).getBrgyIDxx());
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

    }

    private void addShipping() {
        infoModel.setMobileN(Objects.requireNonNull(mBinding.txtMobile.getText()).toString().trim());
        infoModel.setHouseNo(Objects.requireNonNull(mBinding.txtHouseN.getText()).toString().trim());
        infoModel.setAddress(Objects.requireNonNull(mBinding.txtStreet.getText()).toString().trim());
        if(infoModel.isAddressComplete()) {
            mViewModel.addShippingAddress(infoModel, new VMShippingAddress.OnTransactionCallBack() {
                @Override
                public void onLoading() {
                    poLoading.initDialog("Shipping Address", "Adding shipping address. Please wait.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                    poDialogx.setTitle("Shipping Address");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                            Activity_ShippingAddress.getInstance().setFragment(0);
                        }
                    });

                    poDialogx.show();
                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.baseline_error_24);
                    poDialogx.setTitle("Shipping Address");
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
        } else {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Shipping Address");
            poDialogx.setMessage(infoModel.getErrorMessage());
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                }
            });

            poDialogx.show();

        }
    }

}