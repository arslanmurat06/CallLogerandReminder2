package com.marstech.app.calllogerandreminder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.marstech.app.calllogerandreminder.Database.ContactDetails;
import com.marstech.app.calllogerandreminder.Database.DBManagerReminder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity  {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Boolean izin=false;
    int reminderCount;
    String title = "";
    public Fragment fr;
    BottomBarTab item1,item2,item3,item4;
    android.app.FragmentManager mfragmentManager = getFragmentManager();
    Fragment f;
    Fragment contactFragment= new ContactsFragment();
    Fragment callogFragment= new CallLogFragment();
    Fragment reminderFragment= new ReminderListFragment();
    Fragment settingsFragment= new SettingsFragment();


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


        mfragmentManager.beginTransaction()
                .add(R.id.contentContainer, contactFragment)
                .commit();

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


            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {

                    reminderCount=dbManagerReminder.count();
                    item3.setBadgeCount(reminderCount);


                    switch (tabId) {



                        case R.id.tab_item1:
                            setFragment(contactFragment);


                            break;
                        case R.id.tab_item2:


                            setFragment(callogFragment);

                            break;

                        case R.id.tab_item3:


                            setFragment(reminderFragment);

                            break;
                        case R.id.tab_item4:

                            setFragment(settingsFragment);
                            break;

                        default:
                            break;
                    }

                }
            });



            bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
                @Override
                public void onTabReSelected(@IdRes int tabId) {
                    f=mfragmentManager.findFragmentById(R.id.contentContainer);

                    if (f instanceof  BildirimFragment && f.isVisible()) {

                        getFragmentManager().popBackStack();
                    }

                    else if (f instanceof StatisticsFragment && f.isVisible()) {

                        getFragmentManager().popBackStack();

                    }

                    else {}

                }
            });

        }

    }
    void CheckUserPermsions(){

        String[] izinler={Manifest.permission.READ_CALL_LOG,
                          Manifest.permission.READ_CONTACTS,
                          Manifest.permission.CALL_PHONE};
        if ( Build.VERSION.SDK_INT >= 23){

            for(String izin:izinler) {
                if (ActivityCompat.checkSelfPermission(this, izin) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                                    android.Manifest.permission.READ_CALL_LOG,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.CALL_PHONE},
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
                    Toast.makeText( this,"Uygulamaya izin verilmedi" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.exit:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void  setFragment(Fragment fr){

        if (!fr.isAdded()) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, fr)
                    .addToBackStack(null)
                    .commit();


        }

    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }
    }



}

