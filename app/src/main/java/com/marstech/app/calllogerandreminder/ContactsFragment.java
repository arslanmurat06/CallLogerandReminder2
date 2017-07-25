package com.marstech.app.calllogerandreminder;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marstech.app.calllogerandreminder.Adapter.ContactsAdapter;
import com.marstech.app.calllogerandreminder.Adapter.MyAdapterComWithFragment;
import com.marstech.app.calllogerandreminder.Database.ContactDetails;
import com.marstech.app.calllogerandreminder.Model.CalLog;
import com.marstech.app.calllogerandreminder.Model.Contacts;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Contacts");




        return view;
    }

}
