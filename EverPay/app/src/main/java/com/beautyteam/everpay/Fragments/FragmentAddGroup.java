package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautyteam.everpay.R;

/**
 * Created by asus on 16.03.2015.
 */
public class FragmentAddGroup extends Fragment {
    public static FragmentAddGroup getInstance() {
        FragmentAddGroup fragmentAddGroup = new FragmentAddGroup();
        return fragmentAddGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         setHasOptionsMenu(true);
        //   getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_group, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
