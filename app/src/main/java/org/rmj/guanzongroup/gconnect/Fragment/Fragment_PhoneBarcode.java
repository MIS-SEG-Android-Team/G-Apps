package org.rmj.guanzongroup.gconnect.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.digitalgcard.Activity.Activity_QrCodeScanner;
import org.rmj.guanzongroup.gconnect.R;

public class Fragment_PhoneBarcode extends Fragment {

    private ListView lstProducts;
    private FloatingActionButton fabScan;
    private MaterialTextView mtv_tapme;

    private final ActivityResultLauncher<Intent> poArl =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("PhoneBarcode", result.getData().getStringExtra("result"));
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barcode, container, false);

        lstProducts = view.findViewById(R.id.lst_products);
        fabScan = view.findViewById(R.id.fab_scan);
        mtv_tapme = view.findViewById(R.id.mtv_tapme);

        mtv_tapme.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce_animation));
        //mtv_tapme.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_intent_slide_in_right));

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loIntent = new Intent(requireActivity(), Activity_QrCodeScanner.class);
                poArl.launch(loIntent);
            }
        });

        return view;
    }
}
