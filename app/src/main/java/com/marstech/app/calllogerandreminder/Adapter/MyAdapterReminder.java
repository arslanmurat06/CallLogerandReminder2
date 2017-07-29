package com.marstech.app.calllogerandreminder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.marstech.app.calllogerandreminder.BildirimFragment;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;
import com.marstech.app.calllogerandreminder.Model.Contacts;
import com.marstech.app.calllogerandreminder.R;

import java.util.ArrayList;
import java.util.Random;

import static java.security.AccessController.getContext;

/**
 * Created by HP-PC on 29.07.2017.
 */

public class MyAdapterReminder  extends RecyclerView.Adapter<MyAdapterReminder.MyViewHolder>{

    ArrayList<ContactReminder> mDataList;
    LayoutInflater layoutInflater;
    Context context;


    public MyAdapterReminder (Context context,ArrayList<ContactReminder> data) {

        layoutInflater=LayoutInflater.from(context);
        this.mDataList=data;
        this.context=context;

    }



    @Override
    public MyAdapterReminder.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=layoutInflater.inflate(R.layout.list_reminder_item,parent,false);

        MyAdapterReminder.MyViewHolder holder= new MyAdapterReminder.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapterReminder.MyViewHolder holder, int position) {

        final ContactReminder tiklaninanKayit=mDataList.get(position);
        holder.setData (tiklaninanKayit,position);



    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView reminderIsim,reminderNumara,reminderTarih,reminderSaat,reminderDurum;
        ImageView imgReminder,imgReminderDelete;
        CardView rootCardViewReminder;

        public MyViewHolder(View itemView) {
            super(itemView);



            reminderIsim=(TextView)itemView.findViewById(R.id.reminderIsim);
            reminderNumara=(TextView)itemView.findViewById(R.id.reminderNumara);
            reminderTarih=(TextView)itemView.findViewById(R.id.reminderTarih);
            reminderSaat=(TextView)itemView.findViewById(R.id.reminderSaat);
            reminderDurum=(TextView)itemView.findViewById(R.id.reminderDurum);

            imgReminder=(ImageView) itemView.findViewById(R.id.imgReminder);
            imgReminderDelete=(ImageView) itemView.findViewById(R.id.imgReminderDelete);
            rootCardViewReminder=(CardView) itemView.findViewById(R.id.rootCardView);

        }

        public void setData(ContactReminder tiklaninanKayit, int position) {


            this.reminderIsim.setText(tiklaninanKayit.getReminderIsim());
            this.reminderNumara.setText(tiklaninanKayit.getReminderNumara());
            this.reminderTarih.setText(tiklaninanKayit.getReminderZaman());
            Random rnd = new Random();
            imgReminder.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));





        }
    }






}
