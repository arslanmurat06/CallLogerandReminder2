package com.marstech.app.calllogerandreminder;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Adapter.MyAdapter;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    TextView stsIncoming,stsOutgoing,stsMissing,stsRejected,InDuration,OutDuration,totalDuration;
    RecyclerView recyclerViewDetails;
    MyAdapter myAdapter;
    public ArrayList<CalLog> mDataList= new ArrayList<CalLog>();
    DBManager dbManager;


    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_statistics, container, false);

        dbManager=new DBManager(getActivity());
        recyclerViewDetails=(RecyclerView) view.findViewById(R.id.recyclerViewDetails);
        recyclerViewDetails.setNestedScrollingEnabled(false);


        stsIncoming=(TextView) view.findViewById(R.id.stsIncoming);
        stsOutgoing=(TextView) view.findViewById(R.id.stsOutgoing);
        stsMissing=(TextView) view.findViewById(R.id.stsMissing);
        stsRejected=(TextView) view.findViewById(R.id.stsRejected);
        InDuration=(TextView) view.findViewById(R.id.InDuration);
        OutDuration=(TextView) view.findViewById(R.id.OutDuration);
        totalDuration=(TextView) view.findViewById(R.id.totalDuration);



        String isim= getArguments().getString("isim");
        String numara=getArguments().getString("numara");
        String callType=getArguments().getString("tipi");


        mDataList = dbManager.loadData(isim,numara);
        myAdapter = new MyAdapter(getActivity(), mDataList,"sts");
        recyclerViewDetails.setAdapter(myAdapter);

        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getActivity());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDetails.setLayoutManager(mLinearLayoutManagert);


        stsIncoming.setText(String.valueOf(dbManager.count(numara,isim,"INCOMING")));
        stsOutgoing.setText(String.valueOf(dbManager.count(numara,isim,"OUTGOING")));
        stsMissing.setText(String.valueOf(dbManager.count(numara,isim,"MISSED")));
        stsRejected.setText(String.valueOf(dbManager.count(numara,isim,"REJECTED")));




        InDuration.setText(sureDonustur(dbManager.sum(numara,isim,"INCOMING")));
        OutDuration.setText(sureDonustur(dbManager.sum(numara,isim,"OUTGOING")));
        totalDuration.setText(sureDonustur(dbManager.sum(numara,isim,"INCOMING")+dbManager.sum(numara,isim,"OUTGOING")));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(isim+" / "+numara );
        return view;
    }

    public String sureDonustur(int saniye){
        String sure="";

        int artanSaniye=(saniye%3600)%60;
        int dakika=(saniye%3600)/60;
        int saat=saniye/3600;

        if(saat!=0)
        {
            sure=saat+"h:"+dakika+"m "+artanSaniye+"s";
        }
        else
        {
            sure=dakika+"m "+artanSaniye+"s";
        }

        return sure;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }




}
