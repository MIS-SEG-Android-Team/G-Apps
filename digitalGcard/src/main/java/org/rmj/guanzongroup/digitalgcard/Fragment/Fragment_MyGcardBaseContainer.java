package org.rmj.guanzongroup.digitalgcard.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.rmj.guanzongroup.digitalgcard.R;

public class Fragment_MyGcardBaseContainer extends Fragment {

    public static Fragment_MyGcardBaseContainer newInstance() {
        return new Fragment_MyGcardBaseContainer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_gcard_base_container, container, false);
        setViewPager(view);
        if(getArguments() != null){
            int lnArgs = getArguments().getInt("gcardInstance");
            if(lnArgs == 1){

            } else if(lnArgs == 2){

            }
        }
        return view;
    }

    private void setViewPager(View view) {
        ViewPager2 loViewPgr = view.findViewById(R.id.vp_gcard);
        loViewPgr.setAdapter(new FragmentAdapter(getChildFragmentManager(), getLifecycle()));
    }

    private static class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new Fragment_MyGcard();
        }
        @Override
        public int getItemCount() {
            return 1;
        }
    }

}