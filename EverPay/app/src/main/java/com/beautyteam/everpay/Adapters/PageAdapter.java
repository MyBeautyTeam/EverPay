package com.beautyteam.everpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Fragments.MainPageFragment;
import com.beautyteam.everpay.MainActivity;

/**
 * Created by Admin on 10.03.2015.
 */
public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainPageFragment.getInstance();
            case 1:
                return MainPageFragment.getInstance();
        }
        return MainPageFragment.getInstance();
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
