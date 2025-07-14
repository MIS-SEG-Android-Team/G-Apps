package org.rmj.guanzongroup.useraccount.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.etc.InputFieldController;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountDetails;
import org.rmj.guanzongroup.useraccount.databinding.FragmentEditPersonalInfoBinding;

import java.util.ArrayList;

public class Fragment_EditPersonalInfo extends Fragment {

    private FragmentEditPersonalInfoBinding mBinding;
    private VMAccountDetails mViewModel;
    private MessageBox poDialog;
    private Dialog_Loading poLoading;
    private EClientInfo poClientx;
    private String psErrMesg = "";

    public Fragment_EditPersonalInfo() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(VMAccountDetails.class);
        mBinding =  FragmentEditPersonalInfoBinding.inflate(inflater, container, false);
        poClientx = new EClientInfo();

        poDialog = new MessageBox(requireActivity());
        poDialog.initDialog();

        initSelector();
        setDefaultValues();

        mBinding.btnUpdate.setOnClickListener(v -> updatePersonalInfo());

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        poClientx = null;
    }

    private void initSelector() {
        mBinding.txtGender.setAdapter(
                InputFieldController.getAutoCompleteData(
                        requireActivity(), mViewModel.getGenderList()
                ));

        mBinding.txtCivilS.setAdapter(
                InputFieldController.getAutoCompleteData(
                        requireActivity(), mViewModel.getCivilStatusList()
                ));

        mViewModel.getCountryList().observe(getViewLifecycleOwner(), countries -> {
            try {
                ArrayList<String> lsCountry = mViewModel.getCountryForInput(countries);
                mBinding.txtCtizen.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                requireActivity(),
                                lsCountry
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mBinding.txtCtizen.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getCountryList().observe(requireActivity(), countries -> {
                try {
                    for(int x = 0; x < countries.size(); x++) {
                        if(countries.get(x).getNational() != null && !countries.get(x).getNational().isEmpty()) {
                            if(countries.get(x).getNational().equalsIgnoreCase(mBinding.txtCtizen.getText().toString().trim())) {
                                poClientx.setCitizenx(countries.get(x).getCntryCde());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        mBinding.txtGender.setOnItemClickListener((adapterView, view, i, l)
                -> poClientx.setGenderCd(String.valueOf(i)));
        mBinding.txtCivilS.setOnItemClickListener((adapterView, view, i, l)
                -> poClientx.setCvilStat(String.valueOf(i)));
    }

    private void setDefaultValues() {
        mViewModel.getClientInfo().observe(getViewLifecycleOwner(), eClientInfo -> {
            try {
                mBinding.txtGender.setListSelection(Integer.parseInt(eClientInfo.getGenderCd()));
                mBinding.txtGender.setHint(mViewModel.getGenderList()
                        .get(Integer.parseInt(eClientInfo.getGenderCd())));
                mBinding.txtCivilS.setListSelection(Integer.parseInt(eClientInfo.getCvilStat()));
                mBinding.txtCivilS.setHint(mViewModel.getCivilStatusList()
                        .get(Integer.parseInt(eClientInfo.getCvilStat())));
                mViewModel.getCountryList().observe(getViewLifecycleOwner(), eCountryInfos -> {
                    try {
                         for(int x = 0; x < eCountryInfos.size(); x++) {
                             if(eClientInfo.getCitizenx().equalsIgnoreCase(eCountryInfos.get(x).getCntryCde())) {
                                 mBinding.txtCtizen.setText(eCountryInfos.get(x).getNational());
                                 break;
                             } else {
                                 continue;
                             }
                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                mBinding.txtTaxIdN.setText(eClientInfo.getTaxIDNox());

                poClientx.setGenderCd(eClientInfo.getGenderCd());
                poClientx.setCvilStat(eClientInfo.getCvilStat());
                poClientx.setCitizenx(eClientInfo.getCitizenx());
                poClientx.setTaxIDNox(eClientInfo.getTaxIDNox());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updatePersonalInfo() {
        poClientx.setTaxIDNox(mBinding.txtTaxIdN.getText().toString().trim());
        if(isFormClear()) {
            mViewModel.updateAccountInfo(poClientx, new VMAccountDetails.OnTransactionCallBack() {
                @Override
                public void onLoading() {
                    poLoading = new Dialog_Loading(requireActivity());
                    poLoading.initDialog("Account Details",
                            "Updating personal information. Please wait.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.ic_baseline_message_24);
                    poDialog.setTitle("Account Details");
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                            requireActivity().finish();
                        }
                    });


                    poDialog.show();

                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.baseline_error_24);
                    poDialog.setTitle("Account Details");
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });

                    poDialog.show();

                }
            });
        } else {

            poDialog.setIcon(R.drawable.baseline_error_24);
            poDialog.setTitle("Account Details");
            poDialog.setMessage(psErrMesg);
            poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                }
            });

            poDialog.show();

        }
    }

    private boolean isFormClear() {
        if(poClientx.getGenderCd().trim().isEmpty()) {
            psErrMesg = "Please select gender.";
            return false;
        } else if (poClientx.getCvilStat().trim().isEmpty()) {
            psErrMesg = "Please select civil status.";
            return false;
        } else if (poClientx.getCitizenx().isEmpty()) {
            psErrMesg = "Please enter citizenship.";
            return false;
        } else {
            return true;
        }
    }
}