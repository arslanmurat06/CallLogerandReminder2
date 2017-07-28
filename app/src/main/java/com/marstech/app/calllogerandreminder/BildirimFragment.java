package com.marstech.app.calllogerandreminder;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;
import com.marstech.app.calllogerandreminder.Model.Contacts;
import com.marstech.app.calllogerandreminder.SetReminder.MyBroadcastReceiver;
import com.marstech.app.calllogerandreminder.SetReminder.PopDate;
import com.marstech.app.calllogerandreminder.SetReminder.PopTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class BildirimFragment extends Fragment implements View.OnClickListener {

    TextView txtIsim,txtNumara,txtDate,txtTime;
    ImageView imgPopDate,imgPopTime;
    EditText edtReminderMesaj;

    String isim,numara;
    String gun,ay,yil;
    String saat,dakika;
    String reminderMesaj;
    String reminderDateandTime;
    Button btnSetReminder;
    public static final int DATEPICKER_FRAGMENT = 1;
    public static final int TIMEPICKER_FRAGMENT = 2;
    SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    DBManagerReminder dbManagerReminder;
    Contacts contacts;




    public BildirimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_bildirim, container, false);

        txtIsim=(TextView) view.findViewById(R.id.txtIsım);
        txtNumara=(TextView) view.findViewById(R.id.txtNumara);
        txtDate=(TextView) view.findViewById(R.id.txtDate);
        txtTime=(TextView) view.findViewById(R.id.txtTime);
        edtReminderMesaj=(EditText) view.findViewById(R.id.edtReminderMesaj);

        imgPopDate=(ImageView) view.findViewById(R.id.imgPopDate);
        imgPopTime=(ImageView) view.findViewById(R.id.imgPopTime);
        btnSetReminder=(Button) view.findViewById(R.id.btnSetReminder);
        imgPopDate.setOnClickListener(this);
        imgPopTime.setOnClickListener(this);
        btnSetReminder.setOnClickListener(this);


        isim= getArguments().getString("isim");
        numara=getArguments().getString("numara");

        txtIsim.setText(isim);
        txtNumara.setText(numara);



        return view;
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id) {

            case R.id.imgPopDate:

                PopDate popDate=new PopDate();

                popDate.setTargetFragment(this, DATEPICKER_FRAGMENT);
                popDate.show(getFragmentManager().beginTransaction(),"popDate");

                break;
            case R.id.imgPopTime:

                PopTime popTime=new PopTime();

                popTime.setTargetFragment(this, TIMEPICKER_FRAGMENT);
                popTime.show(getFragmentManager().beginTransaction(),"popTime");


                break;

            case R.id.btnSetReminder:

                reminderDateandTime= gun+"-"+ay+"-"+yil+" "+saat+":"+dakika;

                if(gun==null || dakika==null)

                {

                    Toast.makeText(getActivity(), "Lütfen bir tarih ve saat seçin", Toast.LENGTH_SHORT).show();

                }

                else {



                Toast.makeText(getActivity(), "Alarm :"+reminderDateandTime+" tarihe "+numara+" için kuruldu", Toast.LENGTH_SHORT).show();

                    reminderMesaj=edtReminderMesaj.getText().toString();

                    dbManagerReminder=new DBManagerReminder(getActivity());
                    ContentValues values = new ContentValues();

                    if(! dbManagerReminder.Exists(numara)) {


                        values.put(DBManagerReminder.COLISIM, isim);
                        values.put(DBManagerReminder.COLNUMARA, numara);
                        values.put(DBManagerReminder.COLBILDIRIMMESAJ, reminderMesaj);
                        values.put(DBManagerReminder.COLBILDIRIMGUN, gun);
                        values.put(DBManagerReminder.COLBILDIRIMAY, ay);
                        values.put(DBManagerReminder.COLBILDIRIMYIL, yil);
                        values.put(DBManagerReminder.COLBILDIRIMSAAT, saat);
                        values.put(DBManagerReminder.COLBILDIRIMDAKIKA, dakika);
                        values.put(DBManagerReminder.COLBILDIRIMZAMAN, reminderDateandTime);


                        dbManagerReminder.Insert(values);

                    }

                    else {
                        values.put(DBManagerReminder.COLBILDIRIMMESAJ, reminderMesaj);
                        values.put(DBManagerReminder.COLBILDIRIMGUN, gun);
                        values.put(DBManagerReminder.COLBILDIRIMAY, ay);
                        values.put(DBManagerReminder.COLBILDIRIMYIL, yil);
                        values.put(DBManagerReminder.COLBILDIRIMSAAT, saat);
                        values.put(DBManagerReminder.COLBILDIRIMDAKIKA, dakika);
                        values.put(DBManagerReminder.COLBILDIRIMZAMAN, reminderDateandTime);

                        Toast.makeText(getActivity(), isim+"'in mesajı "+reminderMesaj+" olarak değiştirildi", Toast.LENGTH_SHORT).show();

                        dbManagerReminder.update(numara,values);

                    }

                    setReminder(getActivity());

                    ContactsFragment contactsFragment=new ContactsFragment();
                android.app.FragmentManager manager = ((Activity) getActivity()).getFragmentManager();
                    manager.beginTransaction()
                        .replace(R.id.contentContainer, contactsFragment)
                        .commit();


                }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DATEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    gun = bundle.getString("gün");
                    ay = bundle.getString("ay");
                    yil = bundle.getString("yil");

                    txtDate.setText(gun+"-"+ay+"-"+yil);

                    break;
                }
            case TIMEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    saat = bundle.getString("saat");
                    dakika = bundle.getString("dakika");


                    txtTime.setText(saat+":"+dakika);

                    break;
                }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setReminder(Context context)

    {
        ArrayList<ContactReminder> mDataList= new ArrayList<>();
        DBManagerReminder dbManagerReminder= new DBManagerReminder(context);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        mDataList= dbManagerReminder.loadData();


        for(int i=0;i<mDataList.size();i++) {

            Calendar cal=Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(mDataList.get(i).getReminderGun()));
            cal.set(Calendar.MONTH,Integer.parseInt(mDataList.get(i).getReminderAy()));
            cal.set(Calendar.YEAR,Integer.parseInt(mDataList.get(i).getReminderYil()));
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(mDataList.get(i).getReminderSaat()));
            cal.set(Calendar.MINUTE,Integer.parseInt(mDataList.get(i).getReminderDakika()));
            cal.set(Calendar.SECOND,0);

            if (cal.getTimeInMillis() >= System.currentTimeMillis())
            {

               // Toast.makeText(context, "setReminder fonksiyonuna gelen yeni zaman vee alarm mesajı : "+cal.getTime()+" "+mDataList.get(i).getReminderMesaj(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, "Br IDsi "+mDataList.get(i).getReminderBroadcastId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MyBroadcastReceiver.class);
                intent.setAction("com.marstech.app.calllogerandreminder");
                intent.putExtra("MyMessage", mDataList.get(i).getReminderMesaj());
                PendingIntent pi = PendingIntent.getBroadcast(context, mDataList.get(i).getReminderBroadcastId(), intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                cancelAlarmIfExists(context,mDataList.get(i).getReminderBroadcastId(),intent);
                AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pi);

                intentArray.add(pi);

               // Toast.makeText(context, mDataList.get(i).getReminderMesaj() + " BRID " + mDataList.get(i).getReminderBroadcastId() + "Alarmın milisaniye olarak " + cal.getTimeInMillis(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, "Sistem ile alar arasındaki milisaniye farkı "+(cal.getTimeInMillis()-System.currentTimeMillis()), Toast.LENGTH_SHORT).show();
            }


        }


    }
// alarm önceden varsa onu iptal ediyor ve yenisi yukarı mettodda kuruluyor
    public void cancelAlarmIfExists(Context mContext,int requestCode,Intent intent){
        try{

          //  Toast.makeText(mContext, "BRID'li "+requestCode+" oldugu icin iptal edildi", Toast.LENGTH_SHORT).show();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent,0);
            AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
