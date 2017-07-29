package com.marstech.app.calllogerandreminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity  {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Boolean izin=false;
    public Fragment fr;
    int reminderCount;

    BottomBarTab item1,item2,item3,item4;



    public MainActivity(BottomBarTab item1,BottomBarTab item2, BottomBarTab item3, BottomBarTab item4) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
    }
    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CheckUserPermsions();//danger pernissions
        setContentView(R.layout.activity_main);

        final DBManagerReminder dbManagerReminder= new DBManagerReminder(this);
        reminderCount=dbManagerReminder.count();

        if (izin == true) {

            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

            item1 = bottomBar.getTabWithId(R.id.tab_item1);
           // item1.setBadgeCount(1);

             item2 = bottomBar.getTabWithId(R.id.tab_item2);
         //   item2.setBadgeCount(2);

           item3 = bottomBar.getTabWithId(R.id.tab_item3);
            item3.setBadgeCount(reminderCount);

            item4 = bottomBar.getTabWithId(R.id.tab_item4);
           // item4.setBadgeCount(4);



            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {

                    reminderCount=dbManagerReminder.count();
                    item3.setBadgeCount(reminderCount);

                    switch (tabId) {



                        case R.id.tab_item1:

                            //  item1.removeBadge();

                            fr = new ContactsFragment();
                            setFragment(fr);


                            break;
                        case R.id.tab_item2:

                        //    item2.removeBadge();
                            fr = new CallLogFragment();
                            setFragment(fr);

                            break;
                        case R.id.tab_item3:


                            fr = new ReminderListFragment();
                            setFragment(fr);

                            break;
                        case R.id.tab_item4:
                            break;


                    }

                }
            });
        }

    }
   void CheckUserPermsions(){

       String[] izinler={Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS};
        if ( Build.VERSION.SDK_INT >= 23){

            for(String izin:izinler) {
                if (ActivityCompat.checkSelfPermission(this, izin) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                                    android.Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }
            }
        }
        izin=true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                  Intent i = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(i);
                  izin=true;
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Rehbere erişim izni alınamadı" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void setFragment(Fragment fr){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentContainer, fr)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }

}
