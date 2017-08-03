package com.marstech.app.calllogerandreminder;


import android.os.Bundle;
import android.app.Fragment;
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

import com.marstech.app.calllogerandreminder.Adapter.ContactsAdapter;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterComWithFragment;
import com.marstech.app.calllogerandreminder.Database.ContactDetails;
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.Model.Contacts;

import java.util.ArrayList;


public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener {
    public ArrayList<Contacts> mDataList= new ArrayList<Contacts>();
    RecyclerView recyclerViewContacts;
    ContactsAdapter myContactsAdapter;



    public ContactsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerViewContacts=(RecyclerView) view.findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.setNestedScrollingEnabled(false);

        ContactDetails contactDetails=new ContactDetails(getActivity());
        mDataList=contactDetails.getContactDetails();

        myContactsAdapter = new ContactsAdapter(getActivity(), mDataList,"");
        recyclerViewContacts.setAdapter(myContactsAdapter);
        LinearLayoutManager mLinearLayoutManagert = new LinearLayoutManager(getActivity());
        mLinearLayoutManagert.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewContacts.setLayoutManager(mLinearLayoutManagert);

     //   ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Contacts");




        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle("Contatcs");
    }

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
                        myContactsAdapter.setFilter(mDataList);
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
        final ArrayList<Contacts> filteredModelList = filter(mDataList, newText);

        myContactsAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private ArrayList<Contacts> filter(ArrayList<Contacts> models, String query) {
        query = query.toLowerCase();final ArrayList<Contacts> filteredModelList = new ArrayList<>();
        for (Contacts model : models) {
            final String text = model.getContactName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
