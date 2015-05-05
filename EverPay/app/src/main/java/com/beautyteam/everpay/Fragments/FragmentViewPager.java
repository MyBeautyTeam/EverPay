package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.SlidingTabLayout;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentViewPager extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private MainActivity mainActivity;
    private SlidingTabLayout slidingTabLayout;

    private static final int LOADER_ID_I_DEBT = 1;
    private static final int LOADER_ID_DEBT_FOR_ME = 0;

    public static FragmentViewPager getInstance() {
        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        return fragmentViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(LOADER_ID_I_DEBT, null, this);
        getLoaderManager().initLoader(LOADER_ID_DEBT_FOR_ME, null, this);
        return inflater.inflate(R.layout.fragment_view_pager, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager());
        viewPager.setAdapter(pageAdapter);

        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
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
    public void onResume() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.MAIN);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG, "DESTROY");
    }

    @Override
    public void onAttach(Activity activity) {
        mainActivity = (MainActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private static final String[] PROJECTION = new String[] {
            Debts.ITEM_ID,
            Debts.SUMMA,
            Debts.USER_VK_ID,
            Debts.USER_NAME,
            Debts.GROUP_TITLE,
            Debts.IS_I_DEBT
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_I_DEBT) {
            return new CursorLoader(getActivity(), EverContentProvider.DEBTS_CONTENT_URI, PROJECTION, Debts.IS_I_DEBT +"=1", null, Debts.SUMMA + " desc");
        } else {
            return new CursorLoader(getActivity(), EverContentProvider.DEBTS_CONTENT_URI, PROJECTION, Debts.IS_I_DEBT +"=0", null, Debts.SUMMA +" desc");
        }
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        int summa = 0;
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                summa += c.getInt(c.getColumnIndex(Debts.SUMMA));
                c.moveToNext();
            }
        }

        if (loader.getId() == LOADER_ID_I_DEBT) {
            slidingTabLayout.setIDebt(summa);
        } else {
            slidingTabLayout.setDebtForMe(summa);
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
