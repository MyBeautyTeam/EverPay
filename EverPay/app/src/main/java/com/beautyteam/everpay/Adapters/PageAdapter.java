package com.beautyteam.everpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Fragments.FragmentEmptyToDBTest;
import com.beautyteam.everpay.Fragments.FragmentIDebt;

import java.util.ArrayList;

/**
 * Created by Admin on 10.03.2015.
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    ArrayList<OnDebtsLoadedListener> listeners;

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        listeners = new ArrayList<OnDebtsLoadedListener>();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentIDebt fragmentIDebt = FragmentIDebt.getInstance(FragmentIDebt.LOADER_ID_I_DEBT);
                listeners.add((OnDebtsLoadedListener)fragmentIDebt);
                return fragmentIDebt;
            case 1:
                FragmentIDebt fragmentDebtForMe = FragmentIDebt.getInstance(FragmentIDebt.LOADER_ID_DEBT_FOR_ME);
                listeners.add((OnDebtsLoadedListener)fragmentDebtForMe);
                return fragmentDebtForMe;
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

    public interface OnDebtsLoadedListener {
        public void onDebtsLoaded();
    }

    public void notifyLoaded() {
        for(int i=0; i<listeners.size(); i++)
            listeners.get(i).onDebtsLoaded();
    }


}
