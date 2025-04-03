package org.rmj.guanzongroup.gconnect.Fragment;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DCandidates;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.guanzongroup.gconnect.Activity.Activity_Dashboard;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Candidates;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMPoll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fragment_Poll extends Fragment {

    private VMPoll mViewModel;

    private TabLayout tab_candidates;
    private TextInputEditText tv_search;
    private RecyclerView rv_candidates;
    private Adapter_Candidates adapter_candidates;

    private Bundle argsParams;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poll, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMPoll.class);

        initViews(view);
        initListener();
        initArguments(); //todo: trigger on first view initialization
        initObservables();
        initDataArgs();

        return view;

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        argsParams = args;

        initArguments(); //todo: trigger on every argument passed

    }

    private void initViews(View view){
        tab_candidates = view.findViewById(R.id.tab_candidates);
        tv_search = view.findViewById(R.id.tv_search);
        rv_candidates = view.findViewById(R.id.rv_candidates);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initListener(){

        //TODO: CHANGE TAB INDICATOR ON SELECTION
        tab_candidates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        initCandidates("M00120000001");
                        break;
                    case 1:
                        initCandidates("M00120000002");
                        break;
                    case 2:
                        initCandidates("M00120000003");
                        break;
                    case 3:
                        initCandidates("M00120000004");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                try {

                    adapter_candidates.getFilter().filter(s.toString());
                    adapter_candidates.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    adapter_candidates.getFilter().filter(s.toString());
                    adapter_candidates.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initArguments(){

        if (tab_candidates != null){

            if (argsParams != null){

                if (argsParams.containsKey("eventID")){

                    String eventID = argsParams.getString("eventID");

                    switch (eventID){

                        case "M00120000001": //dreamboy
                            tab_candidates.selectTab(tab_candidates.getTabAt(0), true);
                            break;

                        case "M00120000002": //campus princess
                            tab_candidates.selectTab(tab_candidates.getTabAt(1), true);
                            break;

                        case "M00120000003": //biker babe
                            tab_candidates.selectTab(tab_candidates.getTabAt(2), true);
                            break;

                        case "M00120000004": //guanzon bulilit
                            tab_candidates.selectTab(tab_candidates.getTabAt(3), true);
                            break;
                    }
                }

            }

        }

    }

    private void initDataArgs(){

        if (argsParams != null){
            initCandidates(argsParams.getString("eventID"));
        }

    }

    private void initObservables(){

        mViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<EEvents>>() {
            @Override
            public void onChanged(List<EEvents> eEvents) {

                try {

                    if (eEvents == null) {
                        return;
                    }

                    if (eEvents.size() <= 0){
                        return;
                    }

                    List<EEvents> laEvents = new ArrayList<>();
                    for (EEvents loEvent : eEvents){

                        //todo if event is not valid, disable tab display
                        if (!isEventValid(loEvent.getEvntFrom(), loEvent.getEvntThru())){
                            disableEventTab(loEvent.getTransNox()); //todo allow event tab
                        }else {
                            laEvents.add(loEvent); //todo add to active events
                        }

                    }

                    if (argsParams != null) { //todo do not reload data if arguments are passed (triggered from event list)
                        return;
                    }

                    if (argsParams.containsKey("eventID")) { //todo do not reload data if arguments are passed (triggered from event list)
                        return;
                    }

                    //todo initialize candidates for the first event on list
                    initCandidates(laEvents.get(0).getTransNox());

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void initCandidates(String eventIDxx){

        mViewModel.GetCandidates(eventIDxx).observe(getViewLifecycleOwner(), new Observer<List<ECandidates>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ECandidates> eCandidates) {

                try {

                    if (eCandidates == null){

                        //todo display error message on toolbar, if no candidates found
                        Activity_Dashboard loParent = (Activity_Dashboard) getActivity();

                        if (loParent != null){
                            loParent.initToolbarMessage("Oops! Sorry, No candidates found for this event. \n\nCheck your connection or try refreshing the app");
                        }

                        return;
                    }

                    //todo if not empty, initialize adapter
                    adapter_candidates = new Adapter_Candidates(requireContext(), eCandidates, mViewModel);

                    adapter_candidates.notifyDataSetChanged();

                    rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                    rv_candidates.setAdapter(adapter_candidates);

                    if (eCandidates.size() <= 0){

                        //todo display error message on toolbar, if no candidates found
                        Activity_Dashboard loParent = (Activity_Dashboard) getActivity();

                        if (loParent != null){
                            loParent.initToolbarMessage("Oops!\n\n Sorry, no candidates found for this event. \n\nCheck your connection or try refreshing the app");
                        }

                        return;
                    }

                    //todo observe vote counts, after loading candidates
                    mViewModel.ObserveVoteCounts(eventIDxx).observe(getViewLifecycleOwner(), new Observer<DCandidates.LatestVote>() {
                        @Override
                        public void onChanged(DCandidates.LatestVote lastVote) {

                            try {

                                if (lastVote != null){

                                    //todo if not empty, initialize toolbar message displaying vote balance
                                    Activity_Dashboard loParent = (Activity_Dashboard) getActivity();
                                    if (loParent != null){

                                        if (hasVotedToday(lastVote)){ //todo if voted today, notify user to choose another event on toolbar
                                            loParent.initToolbarMessage("You have consumed your vote(s) on this day.\n\nChoose another event.");
                                        } else { //todo if not, display vote balance on toolbar
                                            loParent.initToolbarMessage("Hi! You only have " + 1 + " vote for this event.\n\nChoose your candidate wisely.");
                                        }

                                    }
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private Boolean isEventValid(String dtFrom, String dtThru) throws ParseException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            LocalDate currentDt = LocalDateTime.now().toLocalDate();
            LocalDate eventFrom = LocalDate.parse(dtFrom);
            LocalDate eventThru = LocalDate.parse(dtThru);

            //todo: current date is equal to event date
            if (currentDt.isEqual(eventFrom) || currentDt.isEqual(eventThru)){
                return true;
            }else {

                //todo: current date is between event date
                if (currentDt.isAfter(eventFrom) && currentDt.isBefore(eventThru)){
                    return true;
                }else {
                    return false;
                }
            }

        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date loToday = dtFormat.parse(dtFormat.format(Calendar.getInstance().getTime()));
            Date loEvntFrom = dtFormat.parse(dtFormat.format(dtFormat.parse(dtFrom)));
            Date loEvntThru = dtFormat.parse(dtFormat.format(dtFormat.parse(dtThru)));

            if (loToday.equals(loEvntFrom) || loToday.equals(loEvntThru)){
                return true;
            }else {

                if (loToday.after(loEvntFrom) && loToday.before(loEvntThru)){
                    return true;
                }else {
                    return false;
                }
            }
        }

    }

    private void disableEventTab(String eventID){

        switch (eventID){

            case "M00120000001":
                tab_candidates.getTabAt(0).view.setEnabled(false);
                break;
            case "M00120000002":
                tab_candidates.getTabAt(1).view.setEnabled(false);
                break;
            case "M00120000003":
                tab_candidates.getTabAt(2).view.setEnabled(false);
                break;
            case "M00120000004":
                tab_candidates.getTabAt(3).view.setEnabled(false);
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private Boolean hasVotedToday(DCandidates.LatestVote lastVote) throws ParseException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (lastVote.getdTimeStmp() != null){

                LocalDate loLastVote = LocalDateTime.parse(lastVote.getdTimeStmp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
                LocalDate loToday = LocalDateTime.now().toLocalDate();

                return loLastVote.isEqual(loToday) && lastVote.getTotal() > 0;

            }else {
                return false;
            }

        }else {

            if (lastVote.getdTimeStmp() != null){

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date loLastVote = dtFormat.parse(dtFormat.format(dtFormat.parse(lastVote.getdTimeStmp())));
                Date loToday = dtFormat.parse(dtFormat.format(Calendar.getInstance().getTime()));

                return loLastVote.equals(loToday) && lastVote.getTotal() > 0;

            }else {
                return false;
            }

        }

    }

}
