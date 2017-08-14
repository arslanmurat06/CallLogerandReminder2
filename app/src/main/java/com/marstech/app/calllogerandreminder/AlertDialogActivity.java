package com.marstech.app.calllogerandreminder;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AlertDialogActivity extends AppCompatActivity {

    String isim,numara,mesaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle b = intent.getExtras();
        try{
            isim= b.getString("isim");
            numara=b.getString("numara");
            mesaj=b.getString("mesaj");}

        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(getString(R.string.reminder_alert_title)+isim+" / "+numara)
                .setMessage(getString(R.string.reminder_alert_message)+mesaj)
                .setCancelable(false)
                .setPositiveButton(R.string.reminder_alert_button, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numara));
                        try {
                            startActivity(in);
                            finish();
                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
