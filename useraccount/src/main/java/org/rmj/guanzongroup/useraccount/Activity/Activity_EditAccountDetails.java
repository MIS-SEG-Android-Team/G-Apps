package org.rmj.guanzongroup.useraccount.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.rmj.g3appdriver.etc.FragmentAdapter;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_EditAccountInfo;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_EditAddress;
import org.rmj.guanzongroup.useraccount.Fragment.Fragment_EditPersonalInfo;
import org.rmj.guanzongroup.useraccount.R;

import java.util.Objects;

public class Activity_EditAccountDetails extends AppCompatActivity {

    private static Activity_EditAccountDetails instance;
    private Toolbar toolbar;
    private ViewPager2 viewPager;
    private MessageBox poDialogx;
    private int index;

    private Fragment[] poPages = new Fragment[] {
            new Fragment_EditPersonalInfo(),
            new Fragment_EditAddress(),
            new Fragment_EditAccountInfo()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acount_details);

        instance = Activity_EditAccountDetails.this;
        index = getIntent().getIntExtra("index", 0);

        initViews();
        setUpToolbar();
        moveToPageNumber(index);

        poDialogx.initDialog();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            popUpCloseConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        popUpCloseConfirmationDialog();
    }

    public static Activity_EditAccountDetails getInstance() {
        return instance;
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager_signup);
        poDialogx = new MessageBox(Activity_EditAccountDetails.this);

        FragmentAdapter loAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        loAdapter.initFragments(poPages);

        viewPager.setAdapter(loAdapter);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Details");
    }

    public void moveToPageNumber(int fnPageNum){
        viewPager.setCurrentItem(fnPageNum);
    }

    private void popUpCloseConfirmationDialog() {

        poDialogx.setIcon(R.drawable.baseline_contact_support_24);
        poDialogx.setTitle("Edit Account Details");
        poDialogx.setMessage("Are you sure you want to cancel editing?");

        poDialogx.setPositiveButton("Yes", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                finish();
            }
        });

        poDialogx.setNegativeButton("No", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        poDialogx.show();

    }

}