package com.marstech.app.calllogerandreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        String isim= getIntent().getStringExtra("isim");
        String numara=getIntent().getStringExtra("numara");


        getSupportActionBar().setTitle(isim+" / "+numara);

    }
}
