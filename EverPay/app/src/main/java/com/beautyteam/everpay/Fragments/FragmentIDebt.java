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
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 10.03.2015.
 */
public class FragmentIDebt extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView debtorsList;

    public static final int LOADER_ID_I_DEBT = 1;
    public static final int LOADER_ID_DEBT_FOR_ME = 0;
    private DebtorsListAdapter mAdapter;
    int loader;

    public static final String LOADER = "LOADER";

    public static FragmentIDebt getInstance(int loader) {
        FragmentIDebt fragmentIDebt = new FragmentIDebt();

        Bundle bundle = new Bundle();
        bundle.putInt(LOADER, loader);
        fragmentIDebt.setArguments(bundle);

        return fragmentIDebt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loader = getArguments().getInt(LOADER);
        getLoaderManager().initLoader(loader, null, this);
        return inflater.inflate(R.layout.fragment_i_debts, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        debtorsList = (ListView) view.findViewById(R.id.debtors_fragment_list);

        if (loader == LOADER_ID_I_DEBT)
            ((MainActivity)getActivity()).getServiceHelper().getDebts();


    }

    private static final String[] PROJECTION = new String[] {
        Debts.ITEM_ID,
        Debts.SUMMA,
        Debts.USER_ID,
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

        c.moveToFirst();

        mAdapter = new DebtorsListAdapter(getActivity(), c, 0);
        debtorsList.setAdapter(mAdapter);

        /*
        Uri uri = Uri.withAppendedPath(EverContentProvider.DEBTS_CONTENT_URI, "summa");
        uri = Uri.withAppendedPath(uri, loader.getId() + "");
        Cursor c = getActivity().getContentResolver().query(uri, PROJECTION_SUM, null, null, null);

        String text = c.getString(0);
        Log.e("TEXT", text);
        */

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }



}
