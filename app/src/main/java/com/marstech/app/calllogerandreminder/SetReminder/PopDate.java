package com.marstech.app.calllogerandreminder.SetReminder;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.BildirimFragment;
import com.marstech.app.calllogerandreminder.MainActivity;
import com.marstech.app.calllogerandreminder.R;
import com.marstech.app.calllogerandreminder.StatisticsFragment;

import java.util.Calendar;

/**
 * Created by HP-PC on 25.07.2017.
 */

public class PopDate extends DialogFragment implements View.OnClickListener{

    DatePicker datePicker;
    Button btnSetDate;
    View view;
    int gun,ay,yil;
    Calendar cal = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.pop_date,container,false);

        datePicker= (DatePicker) view.findViewById(R.id.datePicker);
        btnSetDate=(Button) view.findViewById(R.id.btnSetDate);
        btnSetDate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {


        cal.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
        cal.set(Calendar.MONTH,datePicker.getMonth());
        cal.set(Calendar.YEAR,datePicker.getYear());



            Intent i = new Intent()
                    .putExtra("gün", String.valueOf(datePicker.getDayOfMonth()))
                    .putExtra("ay", String.valueOf(datePicker.getMonth()))
                    .putExtra("yil", String.valueOf(datePicker.getYear()));


            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);

            dismiss();


    }
}



