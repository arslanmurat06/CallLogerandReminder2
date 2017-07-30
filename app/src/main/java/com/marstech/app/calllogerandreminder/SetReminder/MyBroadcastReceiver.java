package com.marstech.app.calllogerandreminder.SetReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyBroadcastReceiver extends BroadcastReceiver {

    DBManagerReminder dbManagerReminder;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("com.marstech.app.calllogerandreminder")) {

            Bundle b = intent.getExtras();
            dbManagerReminder=new DBManagerReminder(context);
            ContentValues values = new ContentValues();

            if(dbManagerReminder.Exists(b.getString("numara"))) {




            Toast.makeText(context, b.getString("MyMessage"), Toast.LENGTH_LONG).show();
            Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null)
                vibrator.vibrate(400);

          ///  Toast.makeText(context, "Broadcast receiverden geliyoırum count "+count, Toast.LENGTH_SHORT).show();

            values.put(DBManagerReminder.COLBILDIRIMDURUM, "pasif");
            dbManagerReminder.update(b.getString("numara"),values);

            }



        } else if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {


            ArrayList<ContactReminder> mDataList = new ArrayList<>();
            dbManagerReminder = new DBManagerReminder(context);
            mDataList = dbManagerReminder.loadData();
            ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();


            for (int i = 0; i < mDataList.size(); i++) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mDataList.get(i).getReminderGun()));
                cal.set(Calendar.MONTH, Integer.parseInt(mDataList.get(i).getReminderAy()));
                cal.set(Calendar.YEAR, Integer.parseInt(mDataList.get(i).getReminderYil()));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mDataList.get(i).getReminderSaat()));
                cal.set(Calendar.MINUTE, Integer.parseInt(mDataList.get(i).getReminderDakika()));
                cal.set(Calendar.SECOND, 0);


                if (cal.getTimeInMillis() >= System.currentTimeMillis())
                {

                    Intent intentim = new Intent(context, MyBroadcastReceiver.class);
                    intentim.setAction("com.marstech.app.calllogerandreminder");
                    intentim.putExtra("MyMessage", mDataList.get(i).getReminderMesaj());
                    intentim.putExtra("numara", mDataList.get(i).getReminderNumara());
                    PendingIntent pi = PendingIntent.getBroadcast(context, mDataList.get(i).getReminderBroadcastId(), intentim,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    cancelAlarmIfExists(context,mDataList.get(i).getReminderBroadcastId(),intentim);
                    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                            pi);

                }

                else {
                    ContentValues values = new ContentValues();
                    values.put(DBManagerReminder.COLBILDIRIMDURUM, "pasif");
                    dbManagerReminder.update(mDataList.get(i).getReminderNumara(),values);

                }

            }
        }

    }

    // alarm önceden varsa onu iptal ediyor ve yenisi yukarı mettodda kuruluyor
    public void cancelAlarmIfExists(Context mContext,int requestCode,Intent intent){
        try{

           // Toast.makeText(mContext, "BRID'li "+requestCode+" oldugu icin iptal edildi", Toast.LENGTH_SHORT).show();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent,0);
            AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
