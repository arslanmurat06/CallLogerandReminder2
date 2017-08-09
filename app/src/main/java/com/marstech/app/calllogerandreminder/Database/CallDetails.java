package com.marstech.app.calllogerandreminder.Database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.OpenableColumns.DISPLAY_NAME;

/**
 * Created by HP-PC on 19.07.2017.
 */

public class CallDetails {

    Context context;
    DBManager dbManager;
    SimpleDateFormat formatterTarih = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatterSaat = new SimpleDateFormat("kk:mm:ss");

        public  CallDetails (Context context,DBManager dbManager) {

            this.context=context;
            this.dbManager=dbManager;

        }


        public void getCallDetails() {
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    null, null, null, CallLog.Calls.DATE + " DESC");
            int isim=cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            while (cursor.moveToNext()) {

                String cagriIsım=cursor.getString(isim);
                String cagriNumara = cursor.getString(number);
                String cagriTipi = cursor.getString(type);
                String cagriTarih = cursor.getString(date);
                Date cagriZamani = new Date(Long.valueOf(cagriTarih));
                String cagriSuresi = cursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(cagriTipi);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                    case CallLog.Calls.REJECTED_TYPE:
                        dir = "REJECTED";
                        break;
                }

                if(cagriIsım==null)

                {
                    String nameControl=getContactNameFromNumber(cagriNumara);

                   if(nameControl!=null) {

                       cagriIsım=nameControl;
                   }

                   else {
                       cagriIsım = "İsimsiz";
                   }
                }

                String formatTarih = formatterTarih.format(cagriZamani);
                String formatSaat = formatterSaat.format(cagriZamani);

                if(! dbManager.Exists(formatSaat,formatTarih)) {

                    ContentValues values = new ContentValues();
                    values.put(DBManager.COLISIM, cagriIsım);
                    values.put(DBManager.COLNUMARA, cagriNumara);
                    values.put(DBManager.COLSURE, cagriSuresi);
                    values.put(DBManager.COLTIP, dir);
                    values.put(DBManager.COLTARIH, formatTarih);
                    values.put(DBManager.COLSAAT, formatSaat);
                    values.put(DBManager.COLBILDIRIM, "Yok");


                    dbManager.Insert(values);

                }

            }
            cursor.close();

        }

        //bazen CallLog.Calls.CACHED_NAME null değeri dönebiliyor dolayısıyla bunu numaradan kontrol edip ismini alamk için kullanıyoruz
    public String getContactNameFromNumber(String number)
    {


        String contactName = null;
        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
        String selection = "REPLACE (" + ContactsContract.CommonDataKinds.Phone.NUMBER + ", \" \" , \"\" ) = ?";
        String[] selectionArgs = { number };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);

        if (cursor.moveToFirst())
        {

            contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
            cursor.close();
            return contactName;
        }
        else
        {
            cursor.close();
            return null;
        }
    }
}
