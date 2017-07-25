package com.marstech.app.calllogerandreminder.SetReminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("com.marstech.app.calllogerandreminder")) {


            Bundle b=intent.getExtras();

            Toast.makeText(context,b.getString("MyMessage"),Toast.LENGTH_LONG).show();

        }
    }
}
