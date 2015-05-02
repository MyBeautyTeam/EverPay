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
import com.beautyteam.everpay.Adapters.AddFriendsToGroupAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;

import java.util.ArrayList;

/**
 * Created by asus on 16.03.2015.
 */


public class FragmentAddFriendsToGroup extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private ListView friendsList;
    private static final int LOADER_ID = 0;
    private AddFriendsToGroupAdapter mAdapter;
    private static final String FRIENDS = "FRIENDS";
    private Button saveBtn;

    public static FragmentAddFriendsToGroup getInstance(ArrayList<User> arrayList) {
        FragmentAddFriendsToGroup fragmentAddFriendsToGroup = new FragmentAddFriendsToGroup();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FRIENDS,arrayList);
        fragmentAddFriendsToGroup.setArguments(bundle);
        return fragmentAddFriendsToGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_friends, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList = (ListView) view.findViewById(R.id.friends_list);
        //friendsList.setFastScrollEnabled(true);
        //friendsList.setScrollingCacheEnabled(true);
        saveBtn = (Button) view.findViewById(R.id.save_btn_friends_in_group);
        saveBtn.setOnClickListener(this);
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(Constants.Titles.FRIENDS);
    }


    private static final String[] PROJECTION = new String[] {
            Users.USER_ID,
            Users.USER_ID_VK,
            Users.NAME,
            Users.IMG
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.USERS_CONTENT_URI, PROJECTION, null, null, Users.NAME);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                ArrayList<User> arrayList = getArguments().getParcelableArrayList(FRIENDS);
                mAdapter = new AddFriendsToGroupAdapter(getActivity(), cursor, 0, arrayList);
                friendsList.setAdapter(mAdapter);
                break;
        }

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn_friends_in_group:
                ArrayList<User> arrayList = getArguments().getParcelableArrayList(FRIENDS);
                arrayList = mAdapter.getArrayList();
                ((MainActivity)getActivity()).removeFragment();
                break;
        }
    }
}
