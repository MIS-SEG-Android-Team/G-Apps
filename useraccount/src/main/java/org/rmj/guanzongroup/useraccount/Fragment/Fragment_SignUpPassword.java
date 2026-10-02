package org.rmj.guanzongroup.useraccount.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.Model.SignUpInfoModel;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountAuthentication;

import java.util.Objects;

public class Fragment_SignUpPassword extends Fragment {

    private VMAccountAuthentication mViewModel;
    private Dialog_Loading poLoading;
    private SignUpInfoModel poSignUpM;
    private TextView lblTerms;
    private TextInputEditText tiePasswd, tieRetype;
    private MaterialButton btnSignUp, btnBack;

    public Fragment_SignUpPassword() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_password, container, false);
        initViews(view);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMAccountAuthentication.class);
        try {
        } catch (Exception e)  {
            e.printStackTrace();
        }

        btnSignUp.setOnClickListener(v -> registerAccount());

        return view;
    }

    private void initViews(View v) {
        tiePasswd = v.findViewById(R.id.tie_password);
        tieRetype = v.findViewById(R.id.tie_retype);
        lblTerms = v.findViewById(R.id.tvTerms);
        btnSignUp = v.findViewById(R.id.btnSignUp);
        btnBack = v.findViewById(R.id.btnBack);

        lblTerms.setOnClickListener(view -> { });
    }

    private void registerAccount() {
        String lsPasswrd = Objects.requireNonNull(tiePasswd.getText().toString());
        String lsRetypeP = Objects.requireNonNull(tieRetype.getText().toString());

        poSignUpM.setPassword(lsPasswrd);
        poSignUpM.setPasswrd2(lsRetypeP);
    }

}