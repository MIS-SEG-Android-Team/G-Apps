package org.rmj.guanzongroup.gconnect.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Candidates;
import org.rmj.guanzongroup.gconnect.R;

public class Dialog_Candidate_Details {

    private Context context;
    private AlertDialog poDialogx;
    private ECandidates details;

    public Dialog_Candidate_Details(Context context, ECandidates details){
        this.context = context;
        this.details = details;
    }

    public class Dialog_Details{

        private ShapeableImageView loImg;
        private MaterialTextView mtv_name;
        private MaterialTextView mtv_school;
        private MaterialButton btn_vote;

        public void initDialogDetails(){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_candidate_details, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder
                    .setView(view)
                    .setCancelable(true);

            poDialogx = loBuilder.create();

            initViews(view);
            initDetails();

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }

        private void initViews(View view){

            loImg = view.findViewById(R.id.img_candidate);

            mtv_name = view.findViewById(R.id.mtv_name);
            mtv_school = view.findViewById(R.id.mtv_school);

            btn_vote = view.findViewById(R.id.btn_vote);

        }

        private void initDetails(){

            try{

                JSONArray loArr = new JSONArray(details.getUrlImgs());
                ImageFileManager.LoadImageToView(loArr.get(0).toString(), loImg);

                mtv_name.setText(details.getName());
                mtv_school.setText(details.getSchool());

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
