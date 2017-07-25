package com.marstech.app.calllogerandreminder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.marstech.app.calllogerandreminder.FragmentCommunication;
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.R;
import com.marstech.app.calllogerandreminder.Statistics;
import com.marstech.app.calllogerandreminder.StatisticsFragment;

import java.util.ArrayList;

/**
 * Created by HP-PC on 11.07.2017.
 */

public class MyAdapterComWithFragment extends RecyclerView.Adapter<MyAdapterComWithFragment.MyViewHolder> {


    ArrayList<CalLog> mDataList;
    LayoutInflater layoutInflater;
    Context context;
    String flag="";
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;


    public MyAdapterComWithFragment (Context context, ArrayList<CalLog> data, String flag) {

        layoutInflater=LayoutInflater.from(context);
        this.mDataList=data;
        this.context=context;
        this.flag=flag;


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v=layoutInflater.inflate(R.layout.list_item,parent,false);

        MyViewHolder holder= new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final CalLog tiklaninanKayit=mDataList.get(position);
        holder.setData (tiklaninanKayit,position);

        if(!flag.equals("sts")) {

            holder.rootCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Statistics fragmentına veri gönderme işlemimi gerçekleştirdik

                    StatisticsFragment statisticsFragment=new StatisticsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("isim",tiklaninanKayit.getCagriIsim());
                    bundle.putString("numara",tiklaninanKayit.getCagriNumara());
                    bundle.putString("tipi",tiklaninanKayit.getCagriTipi());
                    statisticsFragment.setArguments(bundle);



//herhangi bir cardview tıklanınca Statistics fragmentının açılmasını sağladık
                   android.app.FragmentManager manager = ((Activity) context).getFragmentManager();

                    manager.beginTransaction()
                            .replace(R.id.contentContainer, statisticsFragment)
                            .addToBackStack(null)
                            .commit();





                }
            });}


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cagriIsim,cagriNumara,cagriTarih,cagriSaat,cagriSure;
        ImageView imgCagriTipi,imgKisiResim;
        CardView rootCardView;
        FragmentCommunication mComminication;

        public MyViewHolder(View itemView) {
            super(itemView);



            cagriIsim=(TextView)itemView.findViewById(R.id.cagriIsım);
            cagriNumara=(TextView)itemView.findViewById(R.id.cagriNumara);
            cagriTarih=(TextView)itemView.findViewById(R.id.cagriTarih);
            cagriSaat=(TextView)itemView.findViewById(R.id.cagriSaat);
            cagriSure=(TextView)itemView.findViewById(R.id.cagriSure);


            imgCagriTipi=(ImageView) itemView.findViewById(R.id.imgCagriTipi);
            imgKisiResim=(ImageView) itemView.findViewById(R.id.imgKisiResim);
            rootCardView=(CardView) itemView.findViewById(R.id.rootCardView);

          //  mComminication=Communicator;

           // mComminication.respond(mDataList.get(getAdapterPosition()).getCagriIsim(),mDataList.get(getAdapterPosition()).getCagriNumara(),mDataList.get(getAdapterPosition()).getCagriTipi());





        }

        public void setData(CalLog tiklaninanKayit, int position) {

            if(tiklaninanKayit.getCagriIsim()==null)

            {
                tiklaninanKayit.setCagriIsim("İsimsiz");

            }


            this.cagriIsim.setText(tiklaninanKayit.getCagriIsim());
            this.cagriNumara.setText(tiklaninanKayit.getCagriNumara());
            this.cagriTarih.setText(tiklaninanKayit.getCagriTarih());
            this.cagriSaat.setText(tiklaninanKayit.getCagriSaat());

            int saniye=Integer.parseInt(tiklaninanKayit.getCagriSure());

            int artanSaniye=saniye%60;
            int dakika=saniye/60;
            int saat=dakika/60;

            if(saat!=0)
            {
                this.cagriSure.setText(saat+"h:"+dakika+"m "+artanSaniye+"s");
            }
            else
            {
                this.cagriSure.setText(dakika+"m "+artanSaniye+"s");
            }



            if(tiklaninanKayit.getCagriTipi().equals("MISSED"))

            {
                this.imgCagriTipi.setImageResource(R.mipmap.gelen_cagri);
                imgCagriTipi.setColorFilter(ContextCompat.getColor(context,R.color.missed));
            }

            else if(tiklaninanKayit.getCagriTipi().equals("OUTGOING"))

            {

                this.imgCagriTipi.setImageResource(R.mipmap.giden_cagri);
                imgCagriTipi.setColorFilter(ContextCompat.getColor(context,R.color.outgoing));

            }

            else if (tiklaninanKayit.getCagriTipi().equals("INCOMING"))
            {
                this.imgCagriTipi.setImageResource(R.mipmap.gelen_cagri);

                imgCagriTipi.setColorFilter(ContextCompat.getColor(context,R.color.incoming));

            }

            else

            {
                this.imgCagriTipi.setImageResource(R.mipmap.iptal_cagri);


            }

            //Special thanks to : https://github.com/amulyakhare/TextDrawable

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter(tiklaninanKayit.getCagriIsim().toString()).toString(), mColorGenerator.getColor(tiklaninanKayit.getCagriIsim().toString()));

            this.imgKisiResim.setImageDrawable(drawable);


        }
    }


    public String firstLetter(String isim) {

        String firstLetter;
        firstLetter=String.valueOf(isim.charAt(0));

        return firstLetter;
    }





}
