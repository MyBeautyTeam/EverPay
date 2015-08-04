package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautyteam.everpay.R;

/**
 * Created by Admin on 04.08.2015.
 */
public class FragmentBugReport extends Fragment {

    public static FragmentBugReport getInstance() {
        return new FragmentBugReport();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bug_report, null);
    }
}
