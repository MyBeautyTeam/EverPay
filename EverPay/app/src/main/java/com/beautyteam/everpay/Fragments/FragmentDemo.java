package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;

/**
 * Created by popka on 12.08.15.
 */
public class FragmentDemo extends Fragment {
    final static String INDEX = "INDEX";
    private int index;
    public static FragmentDemo getInstance(int index) {
        FragmentDemo fragmentDemo = new FragmentDemo();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragmentDemo.setArguments(bundle);

        FlurryAgent.logEvent("Демонстрация " + index);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        index = getArguments().getInt(INDEX);

        return inflater.inflate(R.layout.fragment_demo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
