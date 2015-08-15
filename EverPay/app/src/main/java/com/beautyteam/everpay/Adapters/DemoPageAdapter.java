package com.beautyteam.everpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beautyteam.everpay.Fragments.FragmentDemo;

/**
 * Created by popka on 12.08.15.
 */
public class DemoPageAdapter extends FragmentStatePagerAdapter {
    public DemoPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentDemo.getInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
