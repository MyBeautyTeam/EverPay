package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
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

import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
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

    /*
    private static final String[] PROJECTION = new String[] {
            Bills.ITEM_ID,
            Bills.TITLE,
            Bills.GROUP_ID,
            Bills.USER_ID_VK,
            Bills.USER_NAME,
            Bills.INVEST_SUM,
            Bills.NEED_SUM
    };
    */

    private static final String[] PROJECTION = new String[] {
            GroupMembers.ITEM_ID,
            GroupMembers.GROUP_ID,
            GroupMembers.USER_ID_VK,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME,
    };
    /*
    private static final String[] PROJECTION = new String[] {
            History.TEXT_DESCRIPTION,
            History.ACTION_DATETIME
    };
    */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (c.moveToFirst() && c.getCount() != 0) {
                    while (!c.isAfterLast()) {
                        String billId = c.getString(c.getColumnIndex(GroupMembers.GROUP_ID));
                        String billTitle = c.getString(c.getColumnIndex(GroupMembers.USER_ID));
                        //String groupId = c.getString(c.getColumnIndex(GroupMembers.GROUP_ID));
                        //String userId = c.getString(c.getColumnIndex(GroupMembers.USER_ID_VK));
                        /*String userName = c.getString(c.getColumnIndex(Bills.USER_NAME));
                        String need = c.getString(c.getColumnIndex(Bills.NEED_SUM));
                        String invest = c.getString(c.getColumnIndex(Bills.INVEST_SUM));*/

                        String oldText = textView.getText().toString();
                        String newText = oldText + "\n" + billId + "  " + billTitle + "  "; //); //+ groupId + "  ";// + userId + "  " + userName + "   " + invest + "   " + need;
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
        BillDetails.NEED_SUM,
        BillDetails.INVEST_SUM
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(EverContentProvider.BILL_DETAILS_CONTENT_URI, "bill");
        uri = Uri.withAppendedPath(uri, "3");
        return new CursorLoader(getActivity(), uri, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID_I_DEBT:
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
