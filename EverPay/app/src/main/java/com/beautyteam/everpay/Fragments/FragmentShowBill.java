package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.ShowBillListAdapter;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.DialogWindow;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Utils.AnimUtils;

/**
 * Created by Admin on 22.04.2015.
 */

public class FragmentShowBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 2;
    private static final String BILL_ID = "BILL_ID";
    private static final String GROUP_ID = "GROUP_ID";

    private int billId;
    private int groupId;

    private TextView billTitleView;
    private SwitchCompat switchCompat;
    private TextView eqText;
    private TextView notEqText;
    private TextView needSumma;
    private ShowBillListAdapter mAdapter;
    private ListView billList;
    private DialogWindow dialogWindow;

    public static FragmentShowBill getInstance(int groupId, int billId) {
        FragmentShowBill fragmentShowBill = new FragmentShowBill();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        bundle.putInt(BILL_ID, billId);
        fragmentShowBill.setArguments(bundle);

        return fragmentShowBill;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        billId = getArguments().getInt(BILL_ID);
        groupId = getArguments().getInt(GROUP_ID);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_show_bill, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getServiceHelper().getBill(billId, groupId);


        billTitleView = (TextView)view.findViewById(R.id.show_bill_title);
        eqText = (TextView) view.findViewById(R.id.show_bill_equally_text);
        notEqText = (TextView) view.findViewById(R.id.show_bill_not_equally_text);

        switchCompat = (SwitchCompat) view.findViewById(R.id.show_bill_switch);
        switchCompat.setOnCheckedChangeListener(new SwitchChangeListener());

        needSumma = (TextView) view.findViewById(R.id.show_bill_need_summa_text);

        billList = (ListView) view.findViewById(R.id.show_bill_list);

    }

    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            int colorFrom = getResources().getColor(R.color.secondary_text);
            int colorTo = getResources().getColor(R.color.dark_primary);

            if (b == true) { // Не поровну

                AnimUtils.animateText(notEqText, colorFrom, colorTo, 300, 12, 18);
                AnimUtils.animateText(eqText, colorTo, colorFrom, 300, 18, 12);
            } else { // Поровну

                AnimUtils.animateText(eqText, colorFrom, colorTo, 300, 12, 18);
                AnimUtils.animateText(notEqText, colorTo, colorFrom, 300, 18, 12);

            }
        }

    }

    private static final String[] PROJECTION = new String[] {
        Bills.ITEM_ID,
        Bills.BILL_ID,
        Bills.TITLE,
        Bills.USER_ID_VK,
        Bills.USER_ID,
        Bills.USER_NAME,
        Bills.GROUP_ID,
        Bills.NEED_SUM,
        Bills.INVEST_SUM
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int billId = getArguments().getInt(BILL_ID);
        return new CursorLoader(getActivity(), EverContentProvider.BILLS_CONTENT_URI, PROJECTION, Bills.BILL_ID + "=" + billId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                c.moveToFirst();
                int needGeneral = 0;
                boolean isNotEquals = false;
                if (c.getCount() > 0) {
                    String billTitle = c.getString(c.getColumnIndex(Bills.TITLE));
                    billTitleView.setText(billTitle);

                    int needOne = c.getInt(c.getColumnIndex(Bills.NEED_SUM));
                    if (c.moveToFirst() && c.getCount() != 0) {
                        while (!c.isAfterLast()) {
                            int curNeed = c.getInt(c.getColumnIndex(Bills.NEED_SUM));
                            if (Math.abs(curNeed - needOne) > 1)
                                isNotEquals = true;
                            needOne = curNeed;
                            needGeneral += curNeed;
                            c.moveToNext();
                        }
                    }
                }

                switchCompat.setChecked(isNotEquals);
                needSumma.setText(needGeneral + "");

                c.moveToFirst();

                mAdapter = new ShowBillListAdapter(getActivity(), c, 0);
                billList.setAdapter(mAdapter);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.show_bill, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_bill:

                ((MainActivity)getActivity()).replaceFragment(FragmentAddBill.getInstance(groupId, billId));
                break;

            case R.id.remove_bill:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        dialogWindow = new DialogWindow(getActivity());
        dialogWindow.show();
        dialogWindow.setOnYesClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindow.dismiss();
                removeBill(getArguments().getInt(BILL_ID));
                ((MainActivity)getActivity()).removeFragment();
            }
        });
    }

    private void removeBill(int billId) {
        //TODO удалить из БД billID
    }

}