package org.rmj.guanzongroup.useraccount.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.rmj.guanzongroup.useraccount.R;

public class Fragment_SignUpInfo extends Fragment {

    private TextInputEditText tieEmailx, tieMobile;
    private MaterialButton btnNext;

    public Fragment_SignUpInfo() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up_info, container, false);

        initViews(view);

        btnNext.setOnClickListener(v -> toNextPage());

        return view;
    }

    private void initViews(View v) {
        tieEmailx = v.findViewById(R.id.tie_email);
        tieMobile = v.findViewById(R.id.tie_mobile);
        btnNext = v.findViewById(R.id.btnNext);
    }

    private void toNextPage() {

    }

}