package com.marstech.app.calllogerandreminder;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Adapter.MyAdapter;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public ArrayList<CalLog> mDataList= new ArrayList<CalLog>();
    SimpleDateFormat formatterTarih = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatterSaat = new SimpleDateFormat("hh:mm:ss");
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    DBManager dbManager;
    Boolean izin=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckUserPermsions();//danger pernissions
        dbManager = new DBManager(this);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);



        if(izin) {
            getCallDetails(this);
            mDataList = dbManager.loadData(null);
            myAdapter = new MyAdapter(this, mDataList,"");
            recyclerView.setAdapter(myAdapter);
            LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(this);
            mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLinearLayoutManagert);



        }


    }


    public  void getCallDetails(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int isim=cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {

            String cagriIsım=cursor.getString(isim);
            String cagriNumara = cursor.getString(number);
            String cagriTipi = cursor.getString(type);
            String cagriTarih = cursor.getString(date);
            Date cagriZamani = new Date(Long.valueOf(cagriTarih));
            String cagriSuresi = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(cagriTipi);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;

                case CallLog.Calls.REJECTED_TYPE:
                    dir = "REJECTED";
                    break;
            }





            if(cagriIsım==null)

            {
                cagriIsım="İsimsiz";

            }
            /*
             CalLog cagriKaydi= new CalLog();
            cagriKaydi.setCagriIsim(cagriIsım);
            cagriKaydi.setCagriNumara(cagriNumara);
            cagriKaydi.setCagriSure(cagriSuresi);
            cagriKaydi.setCagriTipi(dir);

            String formatTarih = formatterTarih.format(cagriZamani);
            cagriKaydi.setCagriTarih(formatTarih);

            String formatSaat = formatterSaat.format(cagriZamani);
            cagriKaydi.setCagriSaat(formatSaat);

            mDataList.add(cagriKaydi);*/

            String formatTarih = formatterTarih.format(cagriZamani);
            String formatSaat = formatterSaat.format(cagriZamani);

                if(! dbManager.Exists(formatSaat,formatTarih)) {

                    ContentValues values = new ContentValues();
                    values.put(DBManager.COLISIM, cagriIsım);
                    values.put(DBManager.COLNUMARA, cagriNumara);
                    values.put(DBManager.COLSURE, cagriSuresi);
                    values.put(DBManager.COLTIP, dir);
                    values.put(DBManager.COLTARIH, formatTarih);
                    values.put(DBManager.COLSAAT, formatSaat);
                    values.put(DBManager.COLBILDIRIM, "Yok");

                    dbManager.Insert(values);

                }

        }
        cursor.close();

    }


    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.READ_CALL_LOG},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }
       izin=true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(i);
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Rehbere erişim izni alınamadı" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





}
