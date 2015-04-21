package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 22.04.2015.
 */
/*
public class FragmentShowBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 2;

    private static final String BILL_ID = "BILL_ID";

    public static FragmentAddBill getInstance(int billId) {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();

        Bundle bundle = new Bundle();
        bundle.putInt(BILL_ID, billId);
        fragmentAddBill.setArguments(bundle);

        return fragmentAddBill;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_bill, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
*/
/*
    private static final String[] PROJECTION = new String[] {
            Bills.ITEM_ID,
            GroupMembers.GROUP_ID,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME,
    };



}*/