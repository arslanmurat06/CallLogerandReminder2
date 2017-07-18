package com.marstech.app.calllogerandreminder.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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

    static final String CreateTable="CREATE TABLE IF NOT EXISTS "+TABLENAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLISIM+" TEXT,"+
            COLNUMARA +" TEXT,"+ COLTARIH +" TEXT,"+ COLSAAT +" TEXT,"+ COLTIP +" TEXT,"+ COLSURE +" TEXT,"+ COLBILDIRIM +" TEXT);";

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

    public DBManager(Context context) {

        DatabaseHelperUser db= new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }

    public long Insert (ContentValues values) {

        long ID= sqlDB.insert(DBManager.TABLENAME,null,values);

        return ID;
    }

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

}
