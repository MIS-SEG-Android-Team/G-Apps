package org.rmj.g3appdriver.utils.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.MultiplePulseRing;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.PulseRing;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.rmj.g3appdriver.R;

import java.util.Objects;

public class Dialog_Loading {
    private static final String TAG = Dialog_Loading.class.getSimpleName();

    private AlertDialog poDialogx;
    private final Context poContext;
    private static boolean isShown;

    public Dialog_Loading(Context foContext) {
        this.poContext = foContext;
    }

    public void initDialog(String foTitlexx, String fsMessage){

        View view = LayoutInflater.from(poContext).inflate(R.layout.dialog_loading ,
                null, false);

        AlertDialog.Builder loBuilder = new AlertDialog.Builder(poContext);
        loBuilder.setView(view).setCancelable(false);
        
        poDialogx = loBuilder.create();
        poDialogx.setCancelable(false);

        ProgressBar spinner =  view.findViewById(R.id.loading_spinner);
        TextView lblTitlex = view.findViewById(R.id.lbl_title);
        TextView lblMesgxx = view.findViewById(R.id.lbl_message);

        Sprite drawable = new FadingCircle();
        spinner.setIndeterminateDrawable(drawable);

        lblTitlex.setText(Objects.requireNonNull(foTitlexx));
        lblMesgxx.setText(Objects.requireNonNull(fsMessage));
    }

    public void show() {
        if(!poDialogx.isShowing()) {
            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
        } else {
            poDialogx.dismiss();
            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
        }
        poDialogx.show();
        isShown = true;
    }

    public void dismiss(){
        poDialogx.dismiss();
        isShown = false;
    }
}
