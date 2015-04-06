package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.FriendsListAdapter;
import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by asus on 16.03.2015.
 */


public class FragmentAddFriends extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private StickyListHeadersListView friendsList;
    private Button addBtn;
    private Fragment self;

    private static final int LOADER_ID = 0;
    private FriendsListAdapter mAdapter;

    public static FragmentAddFriends getInstance() {
        FragmentAddFriends fragmentAddFriends = new FragmentAddFriends();
        return fragmentAddFriends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_friends, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList = (StickyListHeadersListView) view.findViewById(R.id.friends_list);

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
            Users.USER_ID_VK,
            Users.NAME,
            Users.IMG
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.USERS_CONTENT_URI, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:

                mAdapter = new FriendsListAdapter(getActivity(), cursor, 0);
                friendsList.setAdapter(mAdapter);
                break;
        }

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {

    }
}
