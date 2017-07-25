package com.marstech.app.calllogerandreminder;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.SetReminder.MyBroadcastReceiver;
import com.marstech.app.calllogerandreminder.SetReminder.PopDate;
import com.marstech.app.calllogerandreminder.SetReminder.PopTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class BildirimFragment extends Fragment implements View.OnClickListener {

    TextView txtIsim,txtNumara,txtDate,txtTime;
    ImageView imgPopDate,imgPopTime;
    String gun,ay,yil;
    String saat,dakika;
    String reminderDateandTime;
    Button btnSetReminder;
    public static final int DATEPICKER_FRAGMENT = 1;
    public static final int TIMEPICKER_FRAGMENT = 2;
    SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    DBManager dbManager;

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

        imgPopDate=(ImageView) view.findViewById(R.id.imgPopDate);
        imgPopTime=(ImageView) view.findViewById(R.id.imgPopTime);
        btnSetReminder=(Button) view.findViewById(R.id.btnSetReminder);
        imgPopDate.setOnClickListener(this);
        imgPopTime.setOnClickListener(this);
        btnSetReminder.setOnClickListener(this);

        String isim= getArguments().getString("isim");
        String numara=getArguments().getString("numara");

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

                setReminderMethod(gun,ay,yil,saat,dakika);
                Toast.makeText(getActivity(), "Alarm :"+reminderDateandTime+" tarihe kuruldu", Toast.LENGTH_SHORT).show();

                ContactsFragment contactsFragment=new ContactsFragment();
                android.app.FragmentManager manager = ((Activity) getActivity()).getFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.contentContainer, contactsFragment)
                        .commit();}


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



    public void setReminderMethod(String gun,String ay,String yil,String saat,String dakika)
    {

            Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(gun));
        cal.set(Calendar.MONTH,Integer.parseInt(ay));
        cal.set(Calendar.YEAR,Integer.parseInt(yil));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(saat));
        cal.set(Calendar.MINUTE,Integer.parseInt(dakika));
        cal.set(Calendar.SECOND,0);

        AlarmManager am = (AlarmManager)getActivity().getSystemService  (Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
        intent.setAction("com.marstech.app.calllogerandreminder");
        intent.putExtra("MyMessage","Alarmdan merhaba");
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY , pi);







    }
}
