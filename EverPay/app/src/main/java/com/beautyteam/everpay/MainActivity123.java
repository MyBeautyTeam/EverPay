package com.beautyteam.everpay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;

import com.beautyteam.everpay.Adapters.DebtorsListAdapter;
import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Fragments.MainPageFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Admin on 07.03.2015.
 */
public class MainActivity123 extends ActionBarActivity implements MaterialTabListener {

    ViewPager viewPager;
    PageAdapter pageAdapter;
    MaterialTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acrivity123);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        for (int i = 0; i < pageAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pageAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        //PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);

        //FragmentTransaction fTran = getSupportFragmentManager().beginTransaction();
        //fTran.replace(R.id.mainLayout, MainPageFragment.getInstance());
        //fTran.commit();
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabReselected(MaterialTab tab) {
    }
    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

}
