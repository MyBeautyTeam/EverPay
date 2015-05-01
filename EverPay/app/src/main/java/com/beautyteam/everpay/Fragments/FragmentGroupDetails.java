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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.GroupDetailsAdapter;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 05.04.2015.
 */
public class FragmentGroupDetails extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String GROUP_ID = "GROUP_ID";
    private static final String GROUP_TITLE = "GROUP_TITLE";

    private Button addBillBtn;
    private Button calcBtn;
    private Button showBillBtn;
    private TextView discriptGroup;
    private MainActivity mainActivity;
    private int groupId;
    private String groupTitle;
    private ListView historyList;

    private static final int LOADER_ID = 0;
    private GroupDetailsAdapter mAdapter;

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
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_group_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arg = getArguments();
        groupId = arg.getInt(GROUP_ID);
        groupTitle = arg.getString(GROUP_TITLE);
        super.onViewCreated(view, savedInstanceState);

        historyList = (ListView) view.findViewById(R.id.group_detail_history);


        addBillBtn = (Button) view.findViewById(R.id.group_add_bill_btn);
        addBillBtn.setOnClickListener(this);

        calcBtn = (Button) view.findViewById(R.id.group_calc_btn);
        calcBtn.setOnClickListener(this);

//        discriptGroup =(TextView) view.findViewById(R.id.group_discript);
//        discriptGroup.setText(groupTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_add_bill_btn:
                mainActivity.addFragment(FragmentAddBill.getInstance(groupId));
                break;
            case R.id.group_calc_btn:
                mainActivity.addFragment(FragmentCalculation.getInstance(groupId));
                break;
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
        ((MainActivity) getActivity()).setTitle(groupTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.edit_group_tlb:
                FragmentEditGroup frag= FragmentEditGroup.getInstance(groupId);
                mainActivity.addFragment(frag);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final String[] PROJECTION = new String[] {
            Groups.GROUP_ID,
            Groups.TITLE,
            Groups.UPDATE_TIME,
            Groups.IS_CALCULATED
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
                mAdapter = new GroupDetailsAdapter(getActivity(), cursor, 0, mainActivity);
                historyList.setAdapter(mAdapter);
                break;
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
