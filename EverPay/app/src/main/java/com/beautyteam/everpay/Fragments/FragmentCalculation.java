package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.CalcListAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 14.03.2015.
 */
public class FragmentCalculation extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;
    private ListView calcList;
    private Button calcBtn;

    private CalcListAdapter mAdapter;
    private static final String GROUP_ID = "GROUP_ID";

    public static FragmentCalculation getInstance(int groupId) {
        FragmentCalculation fragmentCalculation = new FragmentCalculation();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentCalculation.setArguments(bundle);

        return fragmentCalculation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_calculation, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calcList = (ListView) view.findViewById(R.id.calc_list);
        calcBtn = (Button) view.findViewById(R.id.calc_ok_btn);
    }

    private static final String[] PROJECTION = new String[] {
            Calculation.CALC_ID,
            Calculation.GROUPS_ID,
            Calculation.WHO_ID,
            Calculation.NAME_WHO,
            Calculation.WHOM_ID,
            Calculation.NAME_WHOM,
            Calculation.SUMMA,
            Calculation.IS_DELETED
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int groupId = getArguments().getInt(GROUP_ID);
        return new CursorLoader(getActivity(), EverContentProvider.CALCULATION_CONTENT_URI, PROJECTION, Calculation.GROUPS_ID +" = " + groupId, null, /*SORT_ORDER*/null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new CalcListAdapter(getActivity(), cursor, 0);
                calcList.setAdapter(mAdapter);
                break;
        }
    }
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(Constants.Titles.CALCULATION);
    }

}
