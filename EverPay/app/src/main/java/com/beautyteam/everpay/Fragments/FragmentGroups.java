package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.Database.MyContentProvider;

/**
 * Created by asus on 15.03.2015.
 */
public class FragmentGroups extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView groupList;
    private Button addBtn;

    private static final int LOADER_ID = 0;
    private GroupsListAdapter mAdapter;

    public static FragmentGroups getInstance() {
            FragmentGroups fragmentGroups = new FragmentGroups();
            return fragmentGroups;
            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
            return inflater.inflate(R.layout.fragment_groups, null);
            }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            groupList = (ListView) view.findViewById(R.id.groups_list);
            addBtn = (Button) view.findViewById(R.id.add_group_button);

            }

    private static final String[] PROJECTION = new String[] {
            MyContentProvider.CONTACT_ID,
            MyContentProvider.CONTACT_NAME,
            MyContentProvider.CONTACT_EMAIL,
            MyContentProvider.IMG_NAME,
            MyContentProvider.STATE,
            MyContentProvider.RESULT,
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), MyContentProvider.CONTACT_CONTENT_URI, PROJECTION, null, null, null);
            }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case LOADER_ID:
                mAdapter = new GroupsListAdapter(getActivity(), cursor, 0);
                groupList.setAdapter(mAdapter);
                break;
            }

            }
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
