package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.EditFriendsToGroupAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.ADD_MEMBER_TO_GROUP;
import static com.beautyteam.everpay.Constants.Action.REMOVE_MEMBER_FROM_GROUP;

/**
 * Created by asus on 29.04.2015.
 */
public class FragmentEditFriendsInGroup extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, RequestCallback, TitleUpdater {
    private ListView friendsList;
    private static final int LOADER_ID = 0;
    private EditFriendsToGroupAdapter mAdapter;
    private static final String FRIENDS = "FRIENDS";
    private MainActivity mainActivity;
    private static final String GROUP_ID = "GROUP_ID";

    private ServiceHelper serviceHelper;
    private ProgressDialog progressDialog;

    public static FragmentEditFriendsInGroup getInstance(int groupId) {
        FragmentEditFriendsInGroup fragmentEditFriendsInGroup = new FragmentEditFriendsInGroup();
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentEditFriendsInGroup.setArguments(bundle);
        return fragmentEditFriendsInGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_edit_friends, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList = (ListView) view.findViewById(R.id.edit_group_friends_list);
        friendsList.setFastScrollEnabled(true);
        //friendsList.setScrollingCacheEnabled(true);
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

    @Override
    public void onResume() {
        super.onResume();
        serviceHelper.onResume();
        updateTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }

    private static final String[] PROJECTION = new String[] {
            Users.USER_ID,
            Users.USER_ID_VK,
            Users.NAME,
            Users.IMG
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContentUris.withAppendedId(EverContentProvider.USERS_CONTENT_URI, getArguments().getInt(GROUP_ID));
        return new CursorLoader(getActivity(), uri, PROJECTION, null, null, Users.NAME);
    }

    public void addMemberToGroup(int userId, int groupId) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Добавление участника");
        progressDialog.show();
        serviceHelper.addMemberToGroup(userId, groupId);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new EditFriendsToGroupAdapter(getActivity(), cursor, 0, mainActivity, getArguments().getInt(GROUP_ID), this);
                friendsList.setAdapter(mAdapter);
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(ADD_MEMBER_TO_GROUP)) {
            progressDialog.dismiss();
            if (result == Constants.Result.OK) {
                mainActivity.removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка добавления пользователя", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.FRIENDS);
    }
}
