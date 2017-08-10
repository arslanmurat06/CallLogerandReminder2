package com.marstech.app.calllogerandreminder.SetReminder;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.R;

import java.util.Calendar;

/**
 * Created by HP-PC on 25.07.2017.
 */

public class PopTime extends DialogFragment implements View.OnClickListener {


    TimePicker timePicker;
    Button btnSetTime;
    View view;
    int saat,dakika;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.pop_time,container,false);

        timePicker= (TimePicker) view.findViewById(R.id.timePicker);
        btnSetTime=(Button) view.findViewById(R.id.btnSetTime);
        btnSetTime.setOnClickListener(this);



        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent i = new Intent();





            if ((int) Build.VERSION.SDK_INT >= 23) {
                i
                        .putExtra("saat", String.valueOf(timePicker.getHour()))
                        .putExtra("dakika", String.valueOf(timePicker.getMinute()));

            } else {

                i
                        .putExtra("saat", String.valueOf(timePicker.getCurrentHour()))
                        .putExtra("dakika", String.valueOf(timePicker.getCurrentMinute()));


            }
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            dismiss();

        }





}
