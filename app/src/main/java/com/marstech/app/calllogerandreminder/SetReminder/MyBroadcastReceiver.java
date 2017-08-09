package com.marstech.app.calllogerandreminder.SetReminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.AlertDialogActivity;
import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.marstech.app.calllogerandreminder.MainActivity;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;
import com.marstech.app.calllogerandreminder.ReminderListFragment;
import com.marstech.app.calllogerandreminder.ReminderNotification;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyBroadcastReceiver extends BroadcastReceiver {

    DBManagerReminder dbManagerReminder;
    SharedPreferences ShredRef;
    String isim,numara,mesaj;
    int count=0;
    Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("com.marstech.app.calllogerandreminder")) {

            Bundle b = intent.getExtras();
            dbManagerReminder=new DBManagerReminder(context);
            ContentValues values = new ContentValues();
          final  Ringtone ringtone;

            isim=b.getString("isim");
            numara=b.getString("numara");
            mesaj=b.getString("MyMessage");


            if(dbManagerReminder.Exists(numara)) {

              Intent i = new Intent(context, AlertDialogActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("isim",isim);
                i.putExtra("numara",numara);
                i.putExtra("mesaj",mesaj);
                context.startActivity(i);




                ShredRef=context.getSharedPreferences("myRef", Context.MODE_PRIVATE);

                if(ShredRef.getBoolean("switchTone",false)) {

                    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    if (alarmUri == null) {
                        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                     ringtone = RingtoneManager.getRingtone(context, alarmUri);
                    ringtone.play();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ringtone.stop();
                        }
                    }, 1000 * 5);

                }


                if(ShredRef.getBoolean("switchVibrate",false)) {

                    vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);


                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (vibrator != null)
                                vibrator.vibrate(400);
                        }
                    }, 1000 * 5);


                }

                Intent intentNotify;
                if(ShredRef.getBoolean("switchNotify",false)) {

                 /*   NotificationManager mNotifyMgr =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                            .setContentTitle("Call Reminder")
                            .setContentText(mesaj)
                            .setOngoing(false)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
                    intentNotify = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    intentNotify,
                                    PendingIntent.FLAG_ONE_SHOT
                            );
                    // example for blinking LED
                    mBuilder.setLights(0xFFb71c1c, 1000, 2000);
                    mBuilder.setContentIntent(pendingIntent);
                    mNotifyMgr.notify(12345, mBuilder.build());*/

                    ReminderNotification reminderNotification=new ReminderNotification();
                    reminderNotification.notify(context,isim,numara,mesaj,count);
                    count++;
                }


          ///  Toast.makeText(context, "Broadcast receiverden geliyoırum count "+count, Toast.LENGTH_SHORT).show();

            values.put(DBManagerReminder.COLBILDIRIMDURUM, "pasif");
            dbManagerReminder.update(numara,values);

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
