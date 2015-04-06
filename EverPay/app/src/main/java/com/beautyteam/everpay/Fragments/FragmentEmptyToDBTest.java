package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beautyteam.everpay.Database.BillDetails;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by asus on 15.03.2015.
 */
public class FragmentEmptyToDBTest extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MainActivity mainActivity;
    TextView textView;

    private static final int LOADER_ID = 0;

    public static FragmentEmptyToDBTest getInstance() {
        FragmentEmptyToDBTest fragmentGroups = new FragmentEmptyToDBTest();
        return fragmentGroups;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_empty_to_db_test, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView)view.findViewById(R.id.test_text_view);
    }

    private static final String[] PROJECTION = new String[] {
            Bills.BILL_ID,
            Bills.TITLE,
            Bills.USER_ID,
            Bills.GROUP_ID
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.BILLS_CONTENT_URI, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (c.moveToFirst() && c.getCount() != 0) {
                    while (!c.isAfterLast()) {
                        String newText = textView.getText().toString()+"\n"+c.getString(c.getColumnIndex(Bills.TITLE));
                        textView.setText(newText);
                        c.moveToNext();
                    }
                    break;
                }
        }
    }

    /*
    private static final String[] PROJECTION = new String[] {
        Users.USER_ID_VK,
        Users.NAME,
        Users.IMG,
        BillDetails.DEBT_SUM,
        BillDetails.INVEST_SUM
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(EverContentProvider.BILL_DETAILS_CONTENT_URI, "bill");
        uri = Uri.withAppendedPath(uri, "3");
        return new CursorLoader(getActivity(), uri, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (c.moveToFirst() && c.getCount() != 0) {
                    while (!c.isAfterLast()) {
                        String newText = textView.getText().toString()+"\n"+c.getString(c.getColumnIndex(Users.NAME));
                        textView.setText(newText);
                        c.moveToNext();
                    }
                    break;
                }
        }
    }
    */
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

}
