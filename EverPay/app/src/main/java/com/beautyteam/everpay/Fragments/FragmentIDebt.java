package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.DebtorsListAdapter;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 10.03.2015.
 */
public class FragmentIDebt extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView debtorsList;

    private static final int LOADER_ID = 0;
    private DebtorsListAdapter mAdapter;

    public static FragmentIDebt getInstance() {
        FragmentIDebt fragmentIDebt = new FragmentIDebt();
        return fragmentIDebt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_i_debts, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        debtorsList = (ListView) view.findViewById(R.id.debtors_fragment_list);

    }

    private static final String[] PROJECTION = new String[] {
            Users.USER_ID_VK,
            Users.NAME,
            Users.IMG
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.USERS_CONTENT_URI, PROJECTION, null, null, /*SORT_ORDER*/null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new DebtorsListAdapter(getActivity(), cursor, 0);
                debtorsList.setAdapter(mAdapter);
                break;
        }
    }
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }



}
