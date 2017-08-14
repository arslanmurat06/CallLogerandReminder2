package com.marstech.app.calllogerandreminder;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Switch switchNotify,switchTone,switchVibrate;
    SharedPreferences ShredRef;




    public SettingsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.settings_action_bar));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);

        switchNotify=(Switch)view.findViewById(R.id.switchNotify);
        switchTone=(Switch)view.findViewById(R.id.switchTone);
        switchVibrate=(Switch)view.findViewById(R.id.switchVibrate);



        ShredRef=getActivity().getSharedPreferences("myRef", Context.MODE_PRIVATE);
        switchNotify.setChecked(ShredRef.getBoolean("switchNotify", false)); //false default
        switchTone.setChecked(ShredRef.getBoolean("switchTone", false)); //false default
        switchVibrate.setChecked(ShredRef.getBoolean("switchVibrate", false)); //false default


        switchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor=ShredRef.edit();
                editor.putBoolean("switchNotify", isChecked);
                editor.commit();

            }
        });


        switchTone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor=ShredRef.edit();
                editor.putBoolean("switchTone", isChecked);
                editor.commit();

            }
        });

        switchVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor=ShredRef.edit();
                editor.putBoolean("switchVibrate", isChecked);
                editor.commit();

            }
        });

        return  view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);




    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false).setEnabled(false);
    }


}
