package com.marstech.app.calllogerandreminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marestech 12.07.2017.
 */

public class DBManager {
    private  SQLiteDatabase sqlDB;
    public static String DBNAME="Callogs1";
    public static String TABLENAME="Cagri_Kaydi";

    public static String COLID="id";
    public static String COLISIM="cagriIsım";
    public static String COLNUMARA="cagriNumara";
    public static String COLTARIH="cagriTarih";
    public static String COLSAAT="cagriSaat";
    public static String COLSURE="cagriSure";
    public static String COLTIP="cagriTipi";
    public static String COLBILDIRIM="bildirim";



    static final int DBVERSION=1;

    static final String CreateTable="CREATE TABLE IF NOT EXISTS "
            +TABLENAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLISIM+" TEXT,"
            + COLNUMARA +" TEXT,"
            + COLTARIH +" TEXT,"
            + COLSAAT +" TEXT,"
            + COLTIP +" TEXT,"
            + COLSURE +" TEXT,"
            + COLBILDIRIM +" TEXT);";

    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;
         DatabaseHelperUser (Context context) {

            super(context,DBNAME,null,DBVERSION);
            this.context=context;

        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CreateTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("Drop table IF EXISTS "+TABLENAME);
            onCreate(db);
        }
    }

    public DBManager(Context context) {

        DatabaseHelperUser db= new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }


//veri tabanına ekleme işlemini yapar.
    public long Insert (ContentValues values) {

        long ID= sqlDB.insert(DBManager.TABLENAME,null,values);

        return ID;
    }

//kayıt mevcutmu diye kontrol eder
    public boolean Exists(String formatSaat, String formatTarih) {

        String[] columns = { DBManager.COLSAAT,DBManager.COLTARIH };
        String selection = DBManager.COLSAAT + " =?" + " AND "+ DBManager.COLTARIH + " =?" ;
        String[] selectionArgs = { formatSaat,formatTarih };
        String limit = "1";

        Cursor cursor = sqlDB.query(DBManager.TABLENAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


//şartlı sorgu yapabileceğimiz method
    public Cursor query  (String[] projection,String selection,String[] selectionArgs,String sortOrder) {

        SQLiteQueryBuilder qb= new SQLiteQueryBuilder();
        qb.setTables(TABLENAME);

        Cursor cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder);

        return cursor;

    }
   public String isNameExists (String numara) {
       String name;
       Cursor cursor=null;
       String selection=DBManager.COLNUMARA+ " =?";
       String[] selectionArgs = { numara };
       cursor=query(null,selection,selectionArgs,null);
       if(cursor.moveToFirst()) {

           do {

               name = cursor.getString(cursor.getColumnIndex(DBManager.COLISIM));

           }

           while(cursor.moveToNext()); }



       else {name=null;}
       return name;
   }


 //veritabanından verileri çeker
    public ArrayList<CalLog> loadData(String CallIsım,String CallNumara) {

       ArrayList<CalLog> mDataList= new ArrayList<>();
        Cursor cursor=null;

        if(CallIsım==null && CallNumara==null)//null ise Callogfragmettan geliyor değilse statisticfragmenttan geliyor
        {
             cursor=query(null,null,null,DBManager.COLTARIH+" DESC");
        }


        else if(CallIsım.equals("İsimsiz") ) {

            String selection=DBManager.COLNUMARA+ " =?";
            String[] selectionArgs = { CallNumara };


            cursor=query(null,selection,selectionArgs,DBManager.COLTARIH+" DESC");

        }

        else {
            String selection=DBManager.COLISIM + " =?";
            String[] selectionArgs = { CallIsım };


            cursor=query(null,selection,selectionArgs,DBManager.COLTARIH+" DESC");
        }

       if(cursor.moveToFirst()){

           do {

               CalLog cagriKaydi= new CalLog();

               cagriKaydi.setCagriID(cursor.getInt(cursor.getColumnIndex("ID")));
               cagriKaydi.setCagriIsim(cursor.getString(cursor.getColumnIndex(DBManager.COLISIM)));
               cagriKaydi.setCagriNumara(cursor.getString(cursor.getColumnIndex(DBManager.COLNUMARA)));
               cagriKaydi.setCagriSure(cursor.getString(cursor.getColumnIndex(DBManager.COLSURE)));
               cagriKaydi.setCagriTipi(cursor.getString(cursor.getColumnIndex(DBManager.COLTIP)));
               cagriKaydi.setCagriTarih(cursor.getString(cursor.getColumnIndex(DBManager.COLTARIH)));
               cagriKaydi.setCagriSaat(cursor.getString(cursor.getColumnIndex(DBManager.COLSAAT)));

               mDataList.add(cagriKaydi);

           }

           while(cursor.moveToNext()); }
                return mDataList;

           }

public int count(String callNumber,String callIsım,String callType) {
    Cursor cursor=null;
    int count=0;
    String selection;
    String[] selectionArgs;

    if(callIsım.equals("İsimsiz"))
    {
    selection=DBManager.COLNUMARA + " =?" + " AND " + DBManager.COLTIP + " =?";
    selectionArgs =new String[] { callNumber,callType };

    }

    else
        {
            selection=DBManager.COLISIM + " =?" + " AND " + DBManager.COLTIP + " =?";
            selectionArgs =new String[] { callIsım,callType };

        }

    cursor=query(null,selection,selectionArgs,null);
    count=cursor.getCount();

    cursor.close();

    return count;

}

public int sum(String callNumber,String callIsim,String callType) {
    int sum =0;
    String selection;
    String[] selectionArgs;


    if(callIsim.equals("İsimsiz")) {

      selection=DBManager.COLNUMARA + " =?" + " AND " + DBManager.COLTIP + " =?";
      selectionArgs = new String[] { callNumber,callType };
  }


    else {

      selection=DBManager.COLISIM + " =?" + " AND " + DBManager.COLTIP + " =?";
     selectionArgs =new String[] { callIsim,callType };

  }

    Cursor cursor=query(null,selection,selectionArgs,null);

    if(cursor.moveToFirst()){

        do {

          sum+= Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.COLSURE)));

        }

        while(cursor.moveToNext()); }


    return sum;

}


}


