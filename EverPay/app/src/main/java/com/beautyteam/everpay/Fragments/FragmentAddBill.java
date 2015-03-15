package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.R;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentAddBill extends Fragment {
    public static FragmentAddBill getInstance() {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();
        return fragmentAddBill;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // setHasOptionsMenu(true);
     //   getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_bill, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
