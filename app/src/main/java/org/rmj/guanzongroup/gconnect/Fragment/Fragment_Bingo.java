package org.rmj.guanzongroup.gconnect.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.rmj.g3appdriver.dev.Database.Entities.EBingoCard;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_CardNumbers;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMBingo;
import java.util.List;

public class Fragment_Bingo extends Fragment {

    private VMBingo mviewModel;
    private Dialog_Loading poDialog;
    private MessageBox poMessage;
    private MaterialButton btnGenerate;
    private MaterialCardView mcv_bingocard;
    private RecyclerView rvBingoCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bingo, container, false);

        mviewModel = new ViewModelProvider(this).get(VMBingo.class);
        poDialog = new Dialog_Loading(requireContext());
        poMessage = new MessageBox(requireContext());
        btnGenerate = view.findViewById(R.id.btn_generate);
        mcv_bingocard = view.findViewById(R.id.mcv_bingocard);
        rvBingoCard = view.findViewById(R.id.bingo_card);

        poDialog.initDialog("Bingo", "Generating Bingo Card Numbers");
        poMessage.initDialog();

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mviewModel.SaveCardNumbers(new VMBingo.OnSaveCardNumbers() {
                    @Override
                    public void onGenerate() {
                        poDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        poDialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        poDialog.dismiss();

                        poMessage.setMessage(message);
                        poMessage.setIcon(R.drawable.baseline_error_24);
                        poMessage.setPositiveButton("Close", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        poMessage.show();
                    }
                });

            }
        });

        mviewModel.GetCardNumbers().observe(getViewLifecycleOwner(), new Observer<List<EBingoCard>>() {
            @Override
            public void onChanged(List<EBingoCard> eBingoCards) {

                if (eBingoCards == null){

                    btnGenerate.setVisibility(View.VISIBLE);
                    mcv_bingocard.setVisibility(View.INVISIBLE);
                    rvBingoCard.setVisibility(View.INVISIBLE);

                }else {

                    if (eBingoCards.size() > 0){

                        btnGenerate.setVisibility(View.INVISIBLE);
                        mcv_bingocard.setVisibility(View.VISIBLE);
                        rvBingoCard.setVisibility(View.VISIBLE);

                        rvBingoCard.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                        rvBingoCard.setAdapter(new Adapter_CardNumbers(eBingoCards));
                    }else {

                        btnGenerate.setVisibility(View.VISIBLE);
                        mcv_bingocard.setVisibility(View.INVISIBLE);
                        rvBingoCard.setVisibility(View.INVISIBLE);

                    }

                }

            }
        });

        return view;
    }
}
