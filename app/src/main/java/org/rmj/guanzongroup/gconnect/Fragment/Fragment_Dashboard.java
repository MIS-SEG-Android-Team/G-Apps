package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.FragmentAdapter;
import org.rmj.guanzongroup.gconnect.Activity.Activity_Dashboard;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.notifications.Fragment.Fragment_Promotion;

public class Fragment_Dashboard extends Fragment {
    private static final String TAG = Fragment_Dashboard.class.getSimpleName();

    private View view;

    //todo objects displayed after login
    public MaterialCardView mcv_BotNav;
    public BottomNavigationView botNav;
    public ViewPager2 viewPager;
    public Fragment_Poll loPoll = new Fragment_Poll();

    //todo objects displayed before login
    private ConstraintLayout layout_intro;
    private ShapeableImageView logo;
    private ShapeableImageView btnNext;
    private MaterialCardView mcv_intro;
    private MaterialTextView mtv_text;
    private ShapeableImageView siv_icon;

    private ShapeableImageView end_logo;
    private MaterialTextView mtv_instruct;

    private MutableLiveData<Integer> pageCount = new MutableLiveData<>();

    private RClientInfo loClient;

    public static Fragment_Dashboard newInstance() {
        return new Fragment_Dashboard();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        loClient = new RClientInfo(requireActivity());

        initViews(); //todo init views
        initListener(); //todo init listener
        initObservables(); //todo init observables

        return view;
    }
    private void initViews() {

        //todo objects displayed before login
        viewPager = view.findViewById(R.id.viewpager);
        mcv_BotNav = view.findViewById(R.id.coordinatorLayout);
        botNav = view.findViewById(R.id.bottom_navigation);

        //todo objects displayed after login
        layout_intro = view.findViewById(R.id.layout_intro);
        logo = view.findViewById(R.id.logo);
        btnNext = view.findViewById(R.id.btnNext);
        mcv_intro = view.findViewById(R.id.mcv_intro);
        mtv_text = view.findViewById(R.id.mtv_text);
        siv_icon = view.findViewById(R.id.siv_icon);

        end_logo = view.findViewById(R.id.end_logo);
        mtv_instruct = view.findViewById(R.id.mtv_instruct);

        botNav.setBackground(null);
    }
    private void initListener() {

        botNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                viewPager.setCurrentItem(0);
            }  else if(item.getItemId() == R.id.nav_promos){
                viewPager.setCurrentItem(1);
            }  else if(item.getItemId() == R.id.nav_Poll){
                viewPager.setCurrentItem(3);
            }
            return true;
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pageCount.isInitialized()){
                    pageCount.setValue(1);
                }else {
                    pageCount.setValue(pageCount.getValue() + 1);
                }
            }
        });
    }
    private void initObservables() {

        loClient.getClientInfo().observe(getViewLifecycleOwner(), new Observer<EClientInfo>() {
            @Override
            public void onChanged(EClientInfo eClientInfo) {

                if (eClientInfo != null){
                    //botNav.getMenu().findItem(R.id.nav_Bingo).setVisible(true); todo: for future use, postponed
                    mcv_BotNav.setVisibility(View.VISIBLE);
                    botNav.getMenu().findItem(R.id.nav_Poll).setVisible(true);

                    layout_intro.setVisibility(View.GONE);

                    setupPages(); // todo init pages

                }else {
                    //botNav.getMenu().findItem(R.id.nav_Bingo).setVisible(false); todo: for future use, postponed
                    mcv_BotNav.setVisibility(View.GONE);
                    botNav.getMenu().findItem(R.id.nav_Poll).setVisible(false);

                    viewPager.setCurrentItem(0);

                    layout_intro.setVisibility(View.VISIBLE);
                    btnNext.setOutlineProvider(null);

                    pageCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            initIntroPages(integer);
                        }
                    });

                }
            }
        });
    }
    private void initIntroPages(Integer page){

        Log.d("Count", String.valueOf(page));

        switch (page){

            case 1:
                logo.setVisibility(View.VISIBLE);
                mcv_intro.setVisibility(View.VISIBLE);
                btnNext.setImageResource(R.drawable.arrow2);

                mtv_text.setText("Browse your future RIDE");
                siv_icon.setImageResource(R.drawable.kay1);
                break;

            case 2:
                mtv_text.setText("Selfies with your new PHONE");
                siv_icon.setImageResource(R.drawable.kay2);
                break;

            case 3:
                mtv_text.setText("Be updated to our Promos and Events");
                siv_icon.setImageResource(R.drawable.kay3);
                break;

            case 4:
                mtv_text.setText("Start\nGuanzon Connect");
                siv_icon.setImageResource(R.drawable.kay4);
                break;

            case 5:
                logo.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                mcv_intro.setVisibility(View.GONE);

                end_logo.setVisibility(View.VISIBLE);
                mtv_instruct.setVisibility(View.VISIBLE);

                break;

            default:
                mcv_intro.setVisibility(View.VISIBLE);
                mtv_text.setText("Browse your future RIDE");
                siv_icon.setImageResource(R.drawable.kay1);
                break;
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setupPages(){
        Fragment[] loFragments = new Fragment[]{
                new Fragment_Home(),
                new Fragment_Promotion(),
                loPoll};

        FragmentAdapter loAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        loAdapter.initFragments(loFragments);

        loAdapter.notifyDataSetChanged();

        viewPager.setAdapter(loAdapter);
        viewPager.setUserInputEnabled(false);
    }
}