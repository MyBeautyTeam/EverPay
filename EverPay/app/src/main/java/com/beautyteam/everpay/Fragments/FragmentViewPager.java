package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.SlidingTabLayout;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentViewPager extends Fragment {

    private FragmentActivity fragmentActivity;

    public static FragmentViewPager getInstance() {
        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        return fragmentViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_view_pager, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        PageAdapter pageAdapter = new PageAdapter(fragmentActivity.getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.dark_primary);
            }
        });
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tab_header);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG, "DESTROY");
    }

    @Override
    public void onAttach(Activity activity) {
        fragmentActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ok_btn, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }




}
