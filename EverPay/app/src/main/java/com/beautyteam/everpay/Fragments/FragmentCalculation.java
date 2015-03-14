package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautyteam.everpay.R;

/**
 * Created by Admin on 14.03.2015.
 */
public class FragmentCalculation extends Fragment {

    private static final int LOADER_ID = 1;

    public static FragmentCalculation getInstance() {
        FragmentCalculation fragmentCalculation = new FragmentCalculation();
        return fragmentCalculation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_calculation, null);
    }

}
