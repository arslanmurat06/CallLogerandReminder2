package com.marstech.app.calllogerandreminder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.marstech.app.calllogerandreminder.Model.Contacts;
import com.marstech.app.calllogerandreminder.R;

import java.util.ArrayList;

/**
 * Created by HP-PC on 11.07.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {


    ArrayList<Contacts> mDataList;
    LayoutInflater layoutInflater;
    Context context;
    String flag="";
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    DBManagerReminder dbManagerReminder;


    public ContactsAdapter (Context context,ArrayList<Contacts> data,String flag) {

        layoutInflater=LayoutInflater.from(context);
        this.mDataList=data;
        this.context=context;
        this.flag=flag;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v=layoutInflater.inflate(R.layout.list_contacts_item,parent,false);

        MyViewHolder holder= new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Contacts tiklaninanKayit=mDataList.get(position);
        holder.setData (tiklaninanKayit,position);

        if(!flag.equals("sts")) {

            holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BildirimFragment bildirimFragment=new BildirimFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("isim",tiklaninanKayit.getContactName());
                    bundle.putString("numara",tiklaninanKayit.getContactNumber());
               //     bundle.putString("tipi",tiklaninanKayit.getCagriTipi());
                    bildirimFragment.setArguments(bundle);

//herhangi bir cardview tıklanınca Statistics fragmentının açılmasını sağladık
                    android.app.FragmentManager manager = ((Activity) context).getFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.contentContainer, bildirimFragment)
                            .addToBackStack("tag")
                            .commit();


                }
            });}

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView contactName,contactNumber,contactNormalizedNumber;
        ImageView imgKisiResim,imgIsReminderSet,imgCall;
        CardView rootCardView;


        public MyViewHolder(View itemView) {
            super(itemView);



            contactName=(TextView)itemView.findViewById(R.id.contactName);
            contactNumber=(TextView)itemView.findViewById(R.id.contactNumber);

            imgKisiResim=(ImageView) itemView.findViewById(R.id.imgKisiResim);
            imgIsReminderSet=(ImageView)itemView.findViewById(R.id.imgIsReminderSet) ;
            rootCardView=(CardView) itemView.findViewById(R.id.rootCardView);
            imgCall=(ImageView) itemView.findViewById(R.id.imgCall);

        }

        public void setData(final Contacts tiklaninanKayit, int position) {

            if(tiklaninanKayit.getContactName()==null)

            {
                tiklaninanKayit.setContactName("İsimsiz");

            }


            this.contactName.setText(tiklaninanKayit.getContactName());
            this.contactNumber.setText(tiklaninanKayit.getContactNumber());



            //Special thanks to : https://github.com/amulyakhare/TextDrawable

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter(tiklaninanKayit.getContactName().toString()).toString(), mColorGenerator.getColor(tiklaninanKayit.getContactName().toString()));

            this.imgKisiResim.setImageDrawable(drawable);

            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tiklaninanKayit.getContactNumber().toString()));
                    try {
                        context.startActivity(in);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            dbManagerReminder=new DBManagerReminder(context);
            if(dbManagerReminder.isReminderSet(mDataList.get(position).getContactNumber(),"aktif")){

                imgIsReminderSet.setVisibility(View.VISIBLE);


            }

            else { imgIsReminderSet.setVisibility(View.INVISIBLE);}

        }
    }


    public String firstLetter(String isim) {

        String firstLetter;
        firstLetter=String.valueOf(isim.charAt(0));

        return firstLetter;
    }

    public void setFilter(ArrayList<Contacts> contactsSearch) {
        mDataList = new ArrayList<>();
        mDataList.addAll(contactsSearch);
        notifyDataSetChanged();
    }



}
