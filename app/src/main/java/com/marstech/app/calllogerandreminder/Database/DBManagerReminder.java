package com.marstech.app.calllogerandreminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.Model.ContactReminder;

import java.util.ArrayList;

/**
 * Created by Marestech 12.07.2017.
 */

public class DBManagerReminder {
    private  SQLiteDatabase sqlDB;
    public static String DBNAME="Reminders";
    public static String TABLENAME="Reminder_Recs";

    public static String COLID="id";
    public static String COLISIM="cagriIsım";
    public static String COLNUMARA="cagriNumara";
    public static String COLBILDIRIMGUN="bildirimGun";
    public static String COLBILDIRIMAY="bildirimAy";
    public static String COLBILDIRIMYIL="bildirimYil";
    public static String COLBILDIRIMSAAT="bildirimSaat";
    public static String COLBILDIRIMDAKIKA="bildirimDakika";
    public static String COLBILDIRIMMESAJ="bildirimMesaj";
    public static String COLBILDIRIMZAMAN="bildirimZaman";


    static final int DBVERSION=1;

    static final String CreateTable="CREATE TABLE IF NOT EXISTS "
            +TABLENAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLISIM+" TEXT,"
            + COLNUMARA +" TEXT,"
            + COLBILDIRIMGUN +" TEXT,"
            + COLBILDIRIMAY +" TEXT,"
            + COLBILDIRIMYIL +" TEXT,"
            + COLBILDIRIMSAAT +" TEXT,"
            + COLBILDIRIMDAKIKA +" TEXT,"
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

    public long update(String numara,ContentValues values) {
        String[] selectionArgs = { numara };

        long ID=sqlDB.update(DBManagerReminder.TABLENAME,values,DBManagerReminder.COLNUMARA + "=?",selectionArgs);

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
    public ArrayList<ContactReminder> loadData() {

       ArrayList<ContactReminder> mDataList= new ArrayList<>();
        Cursor cursor=null;

             cursor=query(null,null,null,null);


       if(cursor.moveToFirst()){

           do {

               ContactReminder contactReminder= new ContactReminder();

               contactReminder.setReminderIsim(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLISIM)));
               contactReminder.setReminderNumara(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLNUMARA)));
               contactReminder.setReminderMesaj(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMMESAJ)));
               contactReminder.setReminderGun(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMGUN)));
               contactReminder.setReminderAy(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMAY)));
               contactReminder.setReminderYil(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMYIL)));
               contactReminder.setReminderSaat(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMSAAT)));
               contactReminder.setReminderDakika(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMDAKIKA)));
               contactReminder.setReminderZaman(cursor.getString(cursor.getColumnIndex(DBManagerReminder.COLBILDIRIMZAMAN)));
               contactReminder.setReminderBroadcastId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));



               mDataList.add(contactReminder);

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


