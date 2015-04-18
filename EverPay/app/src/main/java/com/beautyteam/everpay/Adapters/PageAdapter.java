package com.beautyteam.everpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Fragments.FragmentIDebt;

/**
 * Created by Admin on 10.03.2015.
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentIDebt.getInstance(FragmentIDebt.LOADER_ID_I_DEBT);
            case 1:
                return FragmentIDebt.getInstance(FragmentIDebt.LOADER_ID_DEBT_FOR_ME);
        }
        // Не достижимо!
        return FragmentIDebt.getInstance(FragmentIDebt.LOADER_ID_I_DEBT);
    }

    @Override
    public int getCount() {
        return Constants.SCREEN_NAMES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.SCREEN_NAMES[position];
    }

}
