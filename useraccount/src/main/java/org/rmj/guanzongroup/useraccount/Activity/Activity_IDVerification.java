package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.rmj.g3appdriver.etc.FragmentAdapter;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.Obj.UserIdentification;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_ID1;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_ID2;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.OnImportIDTypeListener;
import org.rmj.guanzongroup.useraccount.ViewModel.VMIDVerification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_IDVerification extends AppCompatActivity {
    private static final String TAG = Activity_IDVerification.class.getSimpleName();
    private static Activity_IDVerification instance;

    private VMIDVerification mViewModel;
    private ViewPager2 viewPager;
    private List<UserIdentification> poList = new ArrayList<>();
    private MessageBox poDialogx;
    private Dialog_Loading poLoad;
    private String sIDCodexx = "";
    public String getsIDCodexx() {
        return sIDCodexx;
    }

    public void setsIDCodexx(String sIDCodexx) {
        this.sIDCodexx = sIDCodexx;
    }
    public List<UserIdentification> getIDList(){
        return poList;
    }
    public static Activity_IDVerification getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_id_verification);

        viewPager = findViewById(R.id.viewpager);
        instance = this;
        mViewModel = new ViewModelProvider(Activity_IDVerification.this).get(VMIDVerification.class);
        poDialogx = new MessageBox(Activity_IDVerification.this);
        poLoad = new Dialog_Loading(Activity_IDVerification.this);

        poDialogx.initDialog();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ID Verification");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager.setOffscreenPageLimit(1);
        mViewModel.ImportIDTypes(new OnImportIDTypeListener() {
            @Override
            public void OnImportIDType(String title, String message) {
                poLoad.initDialog(title, message);
                poLoad.show();
            }
            @Override
            public void OnSuccess(List<UserIdentification> args) {
                poLoad.dismiss();
                poList = args;

                FragmentAdapter loAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
                loAdapter.initFragments(new Fragment[]{new Fragment_ID1(), new Fragment_ID2()});

                viewPager.setAdapter(loAdapter);
            }
            @Override
            public void OnFailed(String message) {
                poLoad.dismiss();

                poDialogx.setIcon(R.drawable.baseline_error_24);
                poDialogx.setTitle("Loan Application");
                poDialogx.setMessage(message);
                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                poDialogx.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(viewPager.getCurrentItem() == 1){
                viewPager.setCurrentItem(0);
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void moveToNext(int fnPageNum){
        viewPager.setCurrentItem(fnPageNum);
    }
}