package com.marstech.app.calllogerandreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Adapter.MyAdapter;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Model.CalLog;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    TextView stsIncoming,stsOutgoing,stsMissing,stsRejected,InDuration,OutDuration,totalDuration;
    RecyclerView recyclerViewDetails;
    MyAdapter myAdapter;
    public ArrayList<CalLog> mDataList= new ArrayList<CalLog>();
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbManager=new DBManager(this);
        recyclerViewDetails=(RecyclerView) findViewById(R.id.recyclerViewDetails);


        stsIncoming=(TextView) findViewById(R.id.stsIncoming);
        stsOutgoing=(TextView) findViewById(R.id.stsOutgoing);
        stsMissing=(TextView) findViewById(R.id.stsMissing);
        stsRejected=(TextView) findViewById(R.id.stsRejected);
        InDuration=(TextView) findViewById(R.id.InDuration);
        OutDuration=(TextView) findViewById(R.id.OutDuration);
        totalDuration=(TextView) findViewById(R.id.totalDuration);



        String isim= getIntent().getStringExtra("isim");
        String numara=getIntent().getStringExtra("numara");
        String callType=getIntent().getStringExtra("tipi");

        mDataList = dbManager.loadData(numara);
        myAdapter = new MyAdapter(this, mDataList,"sts");
        recyclerViewDetails.setAdapter(myAdapter);

        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(this);
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDetails.setLayoutManager(mLinearLayoutManagert);


        stsIncoming.setText(String.valueOf(dbManager.count(numara,"INCOMING")));
        stsOutgoing.setText(String.valueOf(dbManager.count(numara,"OUTGOING")));
        stsMissing.setText(String.valueOf(dbManager.count(numara,"MISSED")));
        stsRejected.setText(String.valueOf(dbManager.count(numara,"REJECTED")));




        InDuration.setText(sureDonustur(dbManager.sum(numara,"INCOMING")));
        OutDuration.setText(sureDonustur(dbManager.sum(numara,"OUTGOING")));
        totalDuration.setText(sureDonustur(dbManager.sum(numara,"INCOMING")+dbManager.sum(numara,"OUTGOING")));



        getSupportActionBar().setTitle(isim+" / "+numara);





    }

    public String sureDonustur(int saniye){
        String sure="";

        int artanSaniye=saniye%60;
        int dakika=saniye/60;
        int saat=dakika/60;

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
}
