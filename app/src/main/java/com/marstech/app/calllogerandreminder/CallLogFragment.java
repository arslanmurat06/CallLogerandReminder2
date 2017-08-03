package com.marstech.app.calllogerandreminder;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marstech.app.calllogerandreminder.Adapter.MyAdapter;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterComWithFragment;
import com.marstech.app.calllogerandreminder.Database.CallDetails;
import com.marstech.app.calllogerandreminder.Database.DBManager;
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.Model.Contacts;

import java.util.ArrayList;

public class CallLogFragment extends Fragment implements SearchView.OnQueryTextListener {



    public ArrayList<CalLog> mDataList= new ArrayList<CalLog>();
    RecyclerView recyclerView;
    MyAdapterComWithFragment myAdapterComWithFragment;
    DBManager dbManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_calllog, container, false);


        dbManager = new DBManager(getActivity());

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        CallDetails callDetails= new CallDetails(getActivity(),dbManager);
        callDetails.getCallDetails();
        mDataList = dbManager.loadData(null,null);//DBMANAger Load Data hem statisticsFragmentten hem de Callogfragmenttan çağrıldığı için null gönderdik
        myAdapterComWithFragment = new MyAdapterComWithFragment(getActivity(), mDataList,"");
        recyclerView.setAdapter(myAdapterComWithFragment);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getActivity());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagert);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle("Call Logs");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
//for searchbutton http://tutorialsbuzz.com/2015/11/android-filter-recyclerview-using-searchview-in-toolbar.html
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        myAdapterComWithFragment.setFilter(mDataList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<CalLog> filteredModelList = filter(mDataList, newText);

        myAdapterComWithFragment.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<CalLog> filter(ArrayList<CalLog> models, String query) {
        query = query.toLowerCase();final ArrayList<CalLog> filteredModelList = new ArrayList<>();
        for (CalLog model : models) {
            final String text = model.getCagriIsim().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


}
