package org.rmj.guanzongroup.gconnect.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;

import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.R;

public class Dialog_ImagePreview {

    private Context context;
    private AlertDialog poDialogx;
    private String urlImg;

    public Dialog_ImagePreview(Context context, String urlImg){
        this.context = context;
        this.urlImg = urlImg;
    }

    public void initDialog(){

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_imgpreview, null, false);

        AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
        loBuilder.setCancelable(true)
                .setView(view);
        poDialogx = loBuilder.create();

        ShapeableImageView loImg = view.findViewById(R.id.previewimg);
        ImageFileManager.LoadImageToView(urlImg, loImg);

        poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
        poDialogx.show();
    }
}
