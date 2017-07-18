package com.marstech.app.calllogerandreminder.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.R;
import com.marstech.app.calllogerandreminder.Statistics;

import java.util.ArrayList;

/**
 * Created by HP-PC on 11.07.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    ArrayList<CalLog> mDataList;
    LayoutInflater layoutInflater;
    Context context;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public MyAdapter (Context context,ArrayList<CalLog> data) {

        layoutInflater=LayoutInflater.from(context);
        this.mDataList=data;
        this.context=context;

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

        holder.rootCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              Intent i = new Intent(v.getContext(), Statistics.class);
                i.putExtra("isim",tiklaninanKayit.getCagriIsim()) ;
                i.putExtra("numara",tiklaninanKayit.getCagriNumara());
                v.getContext().startActivity(i);


            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView cagriIsim,cagriNumara,cagriTarih,cagriSaat,cagriSure;
        ImageView imgCagriTipi,imgKisiResim;
        CardView rootCardView;


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



          if(tiklaninanKayit.getCagriTipi()=="MISSING")

          {
              this.imgCagriTipi.setImageResource(R.mipmap.gelen_cagri);
              imgCagriTipi.setColorFilter(ContextCompat.getColor(context,R.color.missed));
          }

             else if(tiklaninanKayit.getCagriTipi()=="OUTGOING")

            {

              this.imgCagriTipi.setImageResource(R.mipmap.giden_cagri);
                imgCagriTipi.setColorFilter(ContextCompat.getColor(context,R.color.outgoing));

            }

                    else if (tiklaninanKayit.getCagriTipi()=="INCOMING")
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
