package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.GroupDetailsAdapter;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.beautyteam.everpay.Views.SwipeRefreshLayoutBottom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.GET_HISTORY;

/**
 * Created by Admin on 05.04.2015.
 */
public class FragmentGroupDetails extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, RequestCallback,
        SwipeRefreshLayoutBottom.OnRefreshListener, TitleUpdater {

    private static final String GROUP_ID = "GROUP_ID";
    private static final String GROUP_TITLE = "GROUP_TITLE";

    private Button addBillBtn;
    private Button calcBtn;
    private MainActivity mainActivity;
    private int groupId;
    private String groupTitle;
    private ListView historyList;
    private LinearLayout loadingLayout;

    private static final int LOADER_ID = 0;
    private GroupDetailsAdapter mAdapter;

    private boolean isFirstLaunch = true;
    ServiceHelper serviceHelper;

    private LinearLayout loadHistoryLayout;
    private Button loadHistoryBtn;

    private boolean isAllHistoryLoaded = true;
    private int countOfLoadedItem = 20;

    private SwipeRefreshLayoutBottom refreshLayout;
    public static HashSet<Integer> downloadedGroupSet;


    public static FragmentGroupDetails getInstance(int groupId, String groupTitle) {
        FragmentGroupDetails fragmentGroupDetails = new FragmentGroupDetails();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        bundle.putString(GROUP_TITLE, groupTitle);
        fragmentGroupDetails.setArguments(bundle);

        return fragmentGroupDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Bundle arg = getArguments();
        serviceHelper = new ServiceHelper(getActivity(), this);
        groupId = arg.getInt(GROUP_ID);
        groupTitle = arg.getString(GROUP_TITLE);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        loadHistoryLayout = (LinearLayout) inflater.inflate(R.layout.header_load_history, null);
        loadHistoryBtn = (Button) loadHistoryLayout.findViewById(R.id.load_history_btn);
        /*
        ПОДХОДИТ ЛИ!? ПОДУМАТЬ!
         */


        return inflater.inflate(R.layout.fragment_group_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calcBtn = (Button) view.findViewById(R.id.group_calc_btn);
        calcBtn.setOnClickListener(this);
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingPanel);

        if (!downloadedGroupSet.contains(groupId)) { // Если еще ни разу не загужалось - загружаем
            calcBtn.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
            serviceHelper.onResume();
            serviceHelper.getHistory(groupId, 0);
            serviceHelper.getGroupMembers(groupId);
        } else {
            loadingLayout.setVisibility(View.GONE);
            calcBtn.setVisibility(View.VISIBLE);
        }

        historyList = (ListView) view.findViewById(R.id.group_detail_history);
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View footerView = inflater.inflate(R.layout.footer_add_bill, null);
        historyList.addFooterView(footerView);
        historyList.addHeaderView(loadHistoryLayout);

        addBillBtn = (Button) view.findViewById(R.id.group_add_bill_btn);
        addBillBtn.setOnClickListener(this);

        loadHistoryBtn.setOnClickListener(this);

        refreshLayout = (SwipeRefreshLayoutBottom) view.findViewById(R.id.group_detail_refresh);
        refreshLayout.setColorSchemeResources(R.color.vk_light_color, R.color.vk_share_blue_color, R.color.vk_grey_color);
        refreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_add_bill_btn:
                Log.d(Constants.LOG, "GROUP_ID =" + groupId);
                mainActivity.addFragment(FragmentAddBill.getInstance(groupId));
                break;
            case R.id.group_calc_btn:
                mainActivity.addFragment(FragmentCalculation.getInstance(groupId));
                break;

            case R.id.load_history_btn:
                countOfLoadedItem += 20;
                serviceHelper.getHistory(groupId, countOfLoadedItem);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.group_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.edit_group_tlb:
                FragmentEditGroup frag= FragmentEditGroup.getInstance(groupId, groupTitle);
                mainActivity.addFragment(frag);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String[] PROJECTION = new String[] {
            History.ITEM_ID,
            History.GROUP_ID,
            History.BILL_ID,
            History.ACTION,
            History.ACTION_DATETIME,
            History.DEBTS_ID,
            History.EDITED_BILL_ID,
            History.USERS_ID_WHO,
            History.USERS_ID_WHO_SAY,
            History.USERS_ID_WHOM,
            History.TEXT_WHO,
            History.TEXT_SAY,
            History.TEXT_WHO_SAY,
            History.TEXT_WHAT_WHOM,
            History.TEXT_DESCRIPTION,
            History.STATE,
            History.RESULT

    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.HISTORY_CONTENT_URI, PROJECTION, History.GROUP_ID + "=" + groupId, null, History.ACTION_DATETIME + " asc");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch (loader.getId()) {
            case LOADER_ID:
                /*
                Проверить, не падает ли из-за возможно неинициализированной mainActivity;
                 */
                mAdapter = new GroupDetailsAdapter(getActivity(), cursor, 0, mainActivity);
                historyList.setAdapter(mAdapter);
                break;
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);

        if (action.equals(GET_HISTORY)) {

            if (data.getBoolean(Constants.IntentParams.IS_ENDS)) {
                historyList.removeHeaderView(loadHistoryLayout);
                isAllHistoryLoaded = true;
            } else {
                isAllHistoryLoaded = false;
            }

            loadingLayout.setVisibility(View.GONE);
            calcBtn.setVisibility(View.VISIBLE);
            if (result == Constants.Result.OK) {
                downloadedGroupSet.add(groupId);
            } else {
                Toast.makeText(getActivity(), "Неудалось загрузить новые данные", Toast.LENGTH_SHORT).show();
            }
            refreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onRefresh() {
        serviceHelper.getHistory(groupId, 0);
        serviceHelper.getGroupMembers(groupId);
    }

    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(groupTitle);
    }
}
