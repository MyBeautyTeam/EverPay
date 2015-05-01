package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.AddFriendsToGroupAdapter;
import com.beautyteam.everpay.Adapters.AddGroupAdapter;
import com.beautyteam.everpay.Adapters.EditGroupAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;

import java.util.ArrayList;

/**
 * Created by asus on 28.04.2015.
 */
public class FragmentEditGroup extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
private Toolbar toolbar;
private Button addBtn;
private Button saveBtn;
private Fragment self;
private MainActivity mainActivity;
private ListView friendsList;
private EditGroupAdapter mAdapter;
private EditText groupName;
private static final int LOADER_ID = 0;
private static final String GROUP_ID = "GROUP_ID";

public static FragmentEditGroup getInstance(int groupId) {
        FragmentEditGroup fragmentEditGroup = new FragmentEditGroup();
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentEditGroup.setArguments(bundle);
        return fragmentEditGroup;
        }

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_group, null);
        }

@Override
public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList = (ListView) view.findViewById(R.id.add_group_friends_list);
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View footerView = inflater.inflate(R.layout.footer_add_friend, null);
        addBtn = (Button) footerView.findViewById(R.id.add_btn_friend_foot);
        groupName = (EditText) view.findViewById(R.id.group_name);
        friendsList.addFooterView(footerView);
        self = this;
        saveBtn = (Button) view.findViewById(R.id.save_btn_group);
        saveBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
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
public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn_friend_foot:
               FragmentEditFriendsInGroup frag = FragmentEditFriendsInGroup.getInstance();
               mainActivity.addFragment(frag);
                break;
            case R.id.save_btn_group:
                // изменить базу????
                mainActivity.removeFragment();
//                Log.d("button", "push button save group");
//                String title = groupName.getText().toString();
//                if(!title.equals("")) {
//                    Log.d("groupname", title.toString());
//                    if (arrayList.size()>0) {
//                        Log.d("groupsize", String.valueOf(arrayList.size()));
//
//                        FragmentGroupDetails fragmentGroupDetails = FragmentGroupDetails.getInstance(11, title);
//                        mainActivity.replaceFragment(fragmentGroupDetails);
//                    }  else {
//                        Toast.makeText(getActivity(), "Слишком мало участников. Добавьте участников", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Введите название группы",Toast.LENGTH_SHORT).show();
//                }
            break;
        }
}

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(Constants.Titles.EDIT_GROUP);
    }


    private static final String[] PROJECTION = new String[] {
            GroupMembers.ITEM_ID,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION, GroupMembers.GROUP_ID + "=" + getArguments().getInt(GROUP_ID), null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new EditGroupAdapter(getActivity(), cursor, 0, mainActivity);
                friendsList.setAdapter(mAdapter);
                break;
        }

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}

