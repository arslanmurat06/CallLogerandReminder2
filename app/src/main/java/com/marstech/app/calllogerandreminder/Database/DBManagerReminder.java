package com.marstech.app.calllogerandreminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.util.ArrayList;

/**
 * Created by Marestech 12.07.2017.
 */

public class DBManagerReminder {
    private  SQLiteDatabase sqlDB;
    public static String DBNAME="Reminder";
    public static String TABLENAME="Reminder_Recs";

    public static String COLID="id";
    public static String COLISIM="cagriIsım";
    public static String COLNUMARA="cagriNumara";
    public static String COLBILDIRIMZAMAN="bildirimZaman";
    public static String COLBILDIRIMMESAJ="bildirimMesaj";


    static final int DBVERSION=1;

    static final String CreateTable="CREATE TABLE IF NOT EXISTS "
            +TABLENAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLISIM+" TEXT,"
            + COLNUMARA +" TEXT,"
            + COLBILDIRIMZAMAN +" TEXT,"
            + COLBILDIRIMMESAJ +" TEXT);";

    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;
         DatabaseHelperUser (Context context) {

            super(context,DBNAME,null,DBVERSION);
            this.context=context;

        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CreateTable);
            Toast.makeText(context, "Table is created", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("Drop table IF EXISTS "+TABLENAME);
            onCreate(db);
        }
    }

    public DBManagerReminder(Context context) {

        DatabaseHelperUser db= new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }


//veri tabanına ekleme işlemini yapar.
    public long Insert (ContentValues values) {

        long ID= sqlDB.insert(DBManagerReminder.TABLENAME,null,values);

        return ID;
    }


//kayıt mevcutmu diye kontrol eder
    public boolean Exists(String numaraReminder) {


        String[] columns = { DBManagerReminder.COLNUMARA };
        String selection = DBManagerReminder.COLNUMARA + " =?" ;
        String[] selectionArgs = { numaraReminder };
        String limit = "1";

        Cursor cursor = sqlDB.query(DBManagerReminder.TABLENAME, columns, selection, selectionArgs, null, null, null, limit);
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

 //veritabanından verileri çeker
    public ArrayList<CalLog> loadData() {

       ArrayList<CalLog> mDataList= new ArrayList<>();
        Cursor cursor=null;

             cursor=query(null,null,null, DBManagerReminder.COLBILDIRIMZAMAN+" DESC");


       if(cursor.moveToFirst()){

           do {

               CalLog cagriKaydi= new CalLog();

               cagriKaydi.setCagriIsim(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLISIM)));
               cagriKaydi.setCagriNumara(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLNUMARA)));
               cagriKaydi.setCagriSure(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMMESAJ)));
               cagriKaydi.setCagriTipi(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMZAMAN)));


               mDataList.add(cagriKaydi);

           }


           while(cursor.moveToNext()); }




                return mDataList;

           }

public int count(String callNumber) {
    Cursor cursor=null;
    int count=0;
    String selection;
    String[] selectionArgs;


    selection= DBManagerReminder.COLNUMARA + " =?";
    selectionArgs =new String[] { callNumber};

    cursor=query(null,selection,selectionArgs,null);
    count=cursor.getCount();

    cursor.close();

    return count;

}



}


