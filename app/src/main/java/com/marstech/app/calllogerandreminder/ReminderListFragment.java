package com.marstech.app.calllogerandreminder;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marstech.app.calllogerandreminder.Adapter.ContactsAdapter;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterComWithFragment;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterReminder;
import com.marstech.app.calllogerandreminder.Database.CallDetails;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderListFragment extends Fragment {


    public ArrayList<ContactReminder> mDataList= new ArrayList<ContactReminder>();
    RecyclerView recyclerView;
    MyAdapterReminder myAdapterReminder;
    DBManagerReminder dbManagerReminder;


    public ReminderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     View view=inflater.inflate(R.layout.fragment_reminder_list, container, false);

        dbManagerReminder = new DBManagerReminder(getActivity());

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewReminders);
        recyclerView.setNestedScrollingEnabled(false);

        mDataList = dbManagerReminder.loadData();//DBMANAger Load Data hem statisticsFragmentten hem de Callogfragmenttan çağrıldığı için null gönderdik
        myAdapterReminder = new MyAdapterReminder(getActivity(), mDataList);
        recyclerView.setAdapter(myAdapterReminder);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getActivity());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagert);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reminder Details");
        return view;


    }

}
