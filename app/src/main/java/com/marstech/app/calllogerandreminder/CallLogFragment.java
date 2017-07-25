package com.marstech.app.calllogerandreminder;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marstech.app.calllogerandreminder.Adapter.MyAdapter;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterComWithFragment;
import com.marstech.app.calllogerandreminder.Database.CallDetails;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.util.ArrayList;

public class CallLogFragment extends Fragment {



    public ArrayList<CalLog> mDataList= new ArrayList<CalLog>();
    RecyclerView recyclerView;
    MyAdapterComWithFragment myAdapterComWithFragment;
    DBManager dbManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_calllog, container, false);


        dbManager = new DBManager(getActivity());

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        CallDetails callDetails= new CallDetails(getActivity(),dbManager);
        callDetails.getCallDetails();
        mDataList = dbManager.loadData(null,null);//DBMANAger Load Data hem statisticsFragmentten hem de Callogfragmenttan çağrıldığı için null gönderdik
        myAdapterComWithFragment = new MyAdapterComWithFragment(getActivity(), mDataList,"");
        recyclerView.setAdapter(myAdapterComWithFragment);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getActivity());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagert);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Call Logs");
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
