package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Adapters.GroupsListAdapter;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;

import static android.support.v4.widget.SwipeRefreshLayout.*;
import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.GET_DEBTS;
import static com.beautyteam.everpay.Constants.Action.GET_GROUPS;
import static com.beautyteam.everpay.Constants.LOG;

/**
 * Created by asus on 15.03.2015.
 */
public class FragmentGroups extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, RequestCallback, OnRefreshListener, TitleUpdater {

    private ListView groupList;
    private Button addBtn;
    private Fragment self;
    private MainActivity mainActivity;

    private static final int LOADER_ID = 0;
    private GroupsListAdapter mAdapter;
    ServiceHelper serviceHelper;
    private LinearLayout loadingLayout;
    private SwipeRefreshLayout refreshLayout;

    private boolean isFirstLaunch = true;

    public static FragmentGroups getInstance() {
        FragmentGroups fragmentGroups = new FragmentGroups();
        return fragmentGroups;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        mAdapter = null;
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_groups, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        self=this;
        groupList = (ListView) view.findViewById(R.id.groups_list);
        addBtn = (Button) view.findViewById(R.id.add_group_button);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingPanel);
        addBtn.setOnClickListener(this);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.groups_refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.vk_light_color, R.color.vk_share_blue_color, R.color.vk_grey_color);
        refreshLayout.setOnRefreshListener(this);
    }

    private static final String[] PROJECTION = new String[] {
        Groups.GROUP_ID,
        Groups.TITLE,
        Groups.UPDATE_TIME,
        Groups.IS_CALCULATED
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.GROUPS_CONTENT_URI, PROJECTION, null, null, Groups.UPDATE_TIME + " desc");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        loadingLayout.setVisibility(View.GONE);
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
        menu.clear();
        inflater.inflate(R.menu.group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.add_group_tlb:
                FragmentAddGroup frag= FragmentAddGroup.getInstance();
                //mainActivity.addFragment(frag);
                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, frag)
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.alpha_disappear, R.anim.alpha_appear)
                        .commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group_button:
                FragmentAddGroup frag = FragmentAddGroup.getInstance();
                mainActivity.addFragment(frag);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
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

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(GET_GROUPS)) {
            if (result == Constants.Result.OK) {
            } else {
                Toast.makeText(getActivity(), "Неудалось загрузить новые данные", Toast.LENGTH_SHORT).show();
            }
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        serviceHelper.getGroups();
    }

    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.GROUPS);
    }
}
