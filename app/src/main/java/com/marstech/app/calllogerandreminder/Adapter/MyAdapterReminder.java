package com.marstech.app.calllogerandreminder.Adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.marstech.app.calllogerandreminder.BildirimFragment;
import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;
import com.marstech.app.calllogerandreminder.Model.Contacts;
import com.marstech.app.calllogerandreminder.R;
import com.marstech.app.calllogerandreminder.SetReminder.MyBroadcastReceiver;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;
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

        holder.rootCardViewReminderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BildirimFragment bildirimFragment=new BildirimFragment();
                Bundle bundle=new Bundle();
                bundle.putString("isim",tiklaninanKayit.getReminderIsim());
                bundle.putString("numara",tiklaninanKayit.getReminderNumara());
                bundle.putString("mesaj",tiklaninanKayit.getReminderMesaj());
                bundle.putString("gün",tiklaninanKayit.getReminderGun());
                bundle.putString("gün",tiklaninanKayit.getReminderGun());
                bundle.putString("ay",tiklaninanKayit.getReminderAy());
                bundle.putString("yıl",tiklaninanKayit.getReminderYil());
                bundle.putString("saat",tiklaninanKayit.getReminderSaat());
                bundle.putString("dakika",tiklaninanKayit.getReminderDakika());
                //     bundle.putString("tipi",tiklaninanKayit.getCagriTipi());
                bildirimFragment.setArguments(bundle);



//herhangi bir cardview tıklanınca Statistics fragmentının açılmasını sağladık
                android.app.FragmentManager manager = ((Activity) context).getFragmentManager();

                manager.beginTransaction()
                        .replace(R.id.contentContainer, bildirimFragment)
                        .addToBackStack("tag3")
                        .commit();



            }
        });



    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView reminderIsim,reminderNumara,reminderTarih,reminderSaat,reminderDurum;
        ImageView imgReminder,imgReminderDelete;
        CardView rootCardViewReminderList;

        public MyViewHolder(View itemView) {
            super(itemView);



            reminderIsim=(TextView)itemView.findViewById(R.id.reminderIsim);
            reminderNumara=(TextView)itemView.findViewById(R.id.reminderNumara);
            reminderTarih=(TextView)itemView.findViewById(R.id.reminderTarih);

            reminderDurum=(TextView)itemView.findViewById(R.id.reminderDurum);

            imgReminder=(ImageView) itemView.findViewById(R.id.imgReminder);
            imgReminderDelete=(ImageView) itemView.findViewById(R.id.imgReminderDelete);
            rootCardViewReminderList=(CardView) itemView.findViewById(R.id.rootCardViewReminderList);

        }

        public void setData(final ContactReminder tiklaninanKayit, final int position) {


            this.reminderIsim.setText(tiklaninanKayit.getReminderIsim());
            this.reminderNumara.setText(tiklaninanKayit.getReminderNumara());
            this.reminderTarih.setText(tiklaninanKayit.getReminderZaman());
            Random rnd = new Random();
            imgReminder.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

            if(tiklaninanKayit.getReminderDurum().equals("aktif")) {
                this.reminderDurum.setText("Aktif");

            }

            else {
                this.reminderDurum.setText("Pasif");
                this.reminderDurum.setTextColor(Color.RED);

            }

            imgReminderDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(false);
                    dialog.setTitle("Deleting Reminder");
                    dialog.setMessage("Are you sure* you want to delete this reminder?" );
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                           /* try{
                                Intent intent = new Intent(context, MyBroadcastReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tiklaninanKayit.getReminderBroadcastId(), intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                ((AlarmManager)context.getSystemService(context.ALARM_SERVICE)).cancel(pendingIntent);

                            }catch (Exception e){
                                e.printStackTrace();
                            }*/
                            DBManagerReminder dbManagerReminder=new DBManagerReminder(context);
                            dbManagerReminder.delete(mDataList.get(position).getReminderIsim());




                            mDataList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,mDataList.size());


                            //reminder listten silinen reminderın alarmını iptal etmek gerekiyor. buna bak



                            Toast.makeText(context,"Reminder removed",Toast.LENGTH_SHORT).show();

                        }
                    })
                            .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    final AlertDialog alert = dialog.create();
                    alert.show();

                }
            });



        }
    }






}
