package org.rmj.guanzongroup.notifications.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;

import org.rmj.guanzongroup.notifications.Adapter.Adapter_Fragment;
import org.rmj.guanzongroup.notifications.Adapter.Adapter_Notifications;
import org.rmj.guanzongroup.notifications.Fragment.Fragment_Notification;
import org.rmj.guanzongroup.notifications.Fragment.Fragment_Panalo;
import org.rmj.guanzongroup.notifications.Fragment.Fragment_Promotion;
import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMNotifications;

public class Activity_NotificationList extends AppCompatActivity {
    private static final String TAG = Activity_NotificationList.class.getSimpleName();
    private Adapter_Fragment mAdapter;
    private VMNotifications mViewModel;
    private TabLayout tabLayout_NotifList;
    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private TextView noNotif;
    private RecyclerView recyclerView;
    private Adapter_Notifications adapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        initViews();

        mViewModel = new ViewModelProvider(Activity_NotificationList.this).get(VMNotifications.class);
        toolbar.setTitle("Notifications");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new Adapter_Fragment(getSupportFragmentManager(), getLifecycle());

        mAdapter.addFragment(new Fragment_Notification());
        mAdapter.addFragment(new Fragment_Promotion());
        mAdapter.addFragment(new Fragment_Panalo());

        tabLayout_NotifList.addTab(tabLayout_NotifList.newTab().setText("Notification"));
        tabLayout_NotifList.addTab(tabLayout_NotifList.newTab().setText("Promotions"));
        tabLayout_NotifList.addTab(tabLayout_NotifList.newTab().setText("Guanzon Panalo"));

        viewPager.setAdapter(mAdapter);
        setTabListener();

        mViewModel.getUnreadNotificationsCounts().observe(Activity_NotificationList.this, count -> {
            try {
                if(count > 0) {
                    tabLayout_NotifList.getTabAt(0).getOrCreateBadge().setNumber(count);
                    tabLayout_NotifList.getTabAt(0).getOrCreateBadge().setVisible(true);
                } else {
                    tabLayout_NotifList.getTabAt(0).getOrCreateBadge().setVisible(false);
                    supportInvalidateOptionsMenu();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        mViewModel.getUnreadPromotionsNotifications().observe(Activity_NotificationList.this, count -> {
            try {
                if(count > 0) {
                    tabLayout_NotifList.getTabAt(1).getOrCreateBadge().setNumber(count);
                    tabLayout_NotifList.getTabAt(1).getOrCreateBadge().setVisible(true);
                } else {
                    tabLayout_NotifList.getTabAt(1).getOrCreateBadge().setVisible(false);
                    supportInvalidateOptionsMenu();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        mViewModel.getUnreadMessagesPanaloCount().observe(Activity_NotificationList.this, count -> {
            try {
                if(count > 0) {
                    tabLayout_NotifList.getTabAt(2).getOrCreateBadge().setNumber(count);
                    tabLayout_NotifList.getTabAt(2).getOrCreateBadge().setVisible(true);
                } else {
                    tabLayout_NotifList.getTabAt(2).getOrCreateBadge().setVisible(false);
                    supportInvalidateOptionsMenu();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(Activity_NotificationList.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(Activity_NotificationList.this, DividerItemDecoration.VERTICAL));

        mViewModel.GetClientNotificationList().observe(Activity_NotificationList.this, notif -> {
            if (notif.size() > 0) {
                noNotif.setVisibility(View.GONE);
                adapter = new Adapter_Notifications(notif, (fsMesgIDxx, lsCreated, fsMesgType, lsDataSent) -> {
                    if (!fsMesgType.equalsIgnoreCase("00000")) {
                        Intent loIntent = new Intent(Activity_NotificationList.this, Activity_ViewNotification.class);
                        loIntent.putExtra("sMsgIDxxx", fsMesgIDxx);
                        loIntent.putExtra("sMsgTypex", fsMesgType);
                        loIntent.putExtra("sDataSent", lsDataSent);
                        startActivity(loIntent);
                    } else {
                        Intent loIntent = new Intent(Activity_NotificationList.this, Activity_PreviewMessage.class);
                        loIntent.putExtra("sCreatedx", lsCreated);
                        startActivity(loIntent);
                    }
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                noNotif.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_notification);
        noNotif = findViewById(R.id.lbl_no_notifications);
        recyclerView = findViewById(R.id.recycler_view_notifications);
        tabLayout_NotifList = findViewById(R.id.tablayout_notifList);
        viewPager = findViewById(R.id.Notif_viewPager);
    }
    private void setTabListener(){
        tabLayout_NotifList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}