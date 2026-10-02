package org.rmj.guanzongroup.marketplace.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.etc.PaymentMethod;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMPayOrder;
import org.rmj.guanzongroup.marketplace.databinding.FragmentPaymentSelectBinding;

public class Fragment_PaymentSelection extends Fragment {

    private VMPayOrder mViewModel;
    private FragmentPaymentSelectBinding binding;
    private MessageBox poDialogx;

    private PaymentMethod poPayMeth;

    public Fragment_PaymentSelection() { }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(VMPayOrder.class);
        binding = FragmentPaymentSelectBinding.inflate(inflater, container, false);
        poDialogx = new MessageBox(requireActivity());

        poDialogx.initDialog();

        binding.btnSelect.setOnClickListener(v -> {
            if(isMethodSelected()) {
                mViewModel.setPaymentMethod(poPayMeth);
//                Activity_PayOrder.getInstance().moveToPageNumber(1);
            }
        });
        binding.crdGcashx.setOnClickListener(v -> {
            binding.rdGcashxx.setChecked(true);
        });
        binding.crdPayMya.setOnClickListener(v -> {
            binding.rdPayMaya.setChecked(true);
        });
        binding.crdCashOD.setOnClickListener(v -> {
            binding.rdCashOnD.setChecked(true);
        });
        binding.rdGcashxx.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                binding.rdGcashxx.setChecked(true);
                binding.rdPayMaya.setChecked(false);
                binding.rdCashOnD.setChecked(false);
                poPayMeth = PaymentMethod.GCash;
            }
        });
        binding.rdPayMaya.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                binding.rdGcashxx.setChecked(false);
                binding.rdPayMaya.setChecked(true);
                binding.rdCashOnD.setChecked(false);
                poPayMeth = PaymentMethod.Maya;
            }
        });
        binding.rdCashOnD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                binding.rdGcashxx.setChecked(false);
                binding.rdPayMaya.setChecked(false);
                binding.rdCashOnD.setChecked(true);
                poPayMeth = PaymentMethod.CashOnDelivery;
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean isMethodSelected() {

        if(poPayMeth == null) {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Pay Order");
            poDialogx.setMessage("Please select payment method for your order.");
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                }
            });

            poDialogx.show();

            return false;
        }
        return true;
    }

}