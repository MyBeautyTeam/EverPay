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
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.CalcDetailsAdapter;
import com.beautyteam.everpay.Database.CalculationDetails;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;

import it.carlom.stikkyheader.core.StikkyHeaderBuilder;

/**
 * Created by popka on 06.08.15.
 */
public class FragmentCalcDetails extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, RequestCallback {
    private static final String GROUP_ID = "GROUP_ID";
    private int groupId;
    private static final int LOADER_ID = 1;
    private CalcDetailsAdapter mAdapter;

    private ListView list;
    private ServiceHelper serviceHelper;

    private TextView debtTitle;
    private TextView summaText;

    public static FragmentCalcDetails getInstance(int groupId) {
        FragmentCalcDetails fragmentCalcDetails = new FragmentCalcDetails();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentCalcDetails.setArguments(bundle);

        return fragmentCalcDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupId = getArguments().getInt(GROUP_ID);
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_calc_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlurryAgent.logEvent("Детализация группы");

        list = (ListView)view.findViewById(R.id.calc_details_list);
        /*debtTitle = (TextView)view.findViewById(R.id.calc_details_debt_title);
        summaText = (TextView)view.findViewById(R.id.calc_details_summa);*/
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.CALC_DETAILS_CONTENT_URI, PROJECTION, null/*Calculation.GROUPS_ID +" = " + groupId*/, null, /*SORT_ORDER*/null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new CalcDetailsAdapter(getActivity(), cursor, 0);
                list.setAdapter(mAdapter);
                break;
        }
    }
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private static final String[] PROJECTION = new String[] {
        CalculationDetails.ITEM_ID,
        CalculationDetails.GROUP_ID,
        CalculationDetails.BILL_TITLE,
        CalculationDetails.NEED_SUM,
        CalculationDetails.INVEST_SUM,
        CalculationDetails.BALANCE
    };

    @Override
    public void onResume() {
        super.onResume();
        //loadingLayout.setVisibility(View.VISIBLE);
        serviceHelper.onResume();
        //serviceHelper.calculate(groupId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StikkyHeaderBuilder.stickTo(list)
                .setHeader(R.id.calc_header, (ViewGroup) getView())
                .minHeightHeader(150)
                .build();

    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }


    @Override
    public void onRequestEnd(int result, Bundle data) {

    }
}
