package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;import android.support.v4.app.LoaderManager;
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

import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.Database.MyContentProvider;

/**
 * Created by asus on 15.03.2015.
 */
public class FragmentGroups extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView groupList;
    private Button addBtn;
    private Fragment self;
    private MainActivity mainActivity;

    private static final int LOADER_ID = 0;
    private GroupsListAdapter mAdapter;

    public static FragmentGroups getInstance() {
        FragmentGroups fragmentGroups = new FragmentGroups();
        return fragmentGroups;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_groups, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        self=this;
        groupList = (ListView) view.findViewById(R.id.groups_list);
        addBtn = (Button) view.findViewById(R.id.add_group_button);
        addBtn.setOnClickListener(this);

    }

    private static final String[] PROJECTION = new String[] {
        Groups.GROUP_ID,
        Groups.TITLE,
        Groups.USER_ID
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.GROUPS_CONTENT_URI, PROJECTION, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                /*
                Проверить, не падает ли из-за возможно неинициализированной mainActivity;
                 */
                mAdapter = new GroupsListAdapter(getActivity(), cursor, 0, mainActivity);
                groupList.setAdapter(mAdapter);
                break;
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.add_group_tlb:
                FragmentAddGroup frag= FragmentAddGroup.getInstance();
                mainActivity.addFragment(frag);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group_button:
                FragmentAddGroup frag= FragmentAddGroup.getInstance();
                mainActivity.addFragment(frag);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

}
