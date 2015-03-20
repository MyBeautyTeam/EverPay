package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.AddBillListAdapter;
import com.beautyteam.everpay.Adapters.CalcListAdapter;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentAddBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private AddBillListAdapter mAdapter;
    private ListView addBillList;
    private SwitchCompat switchCompat;
    private LinearLayout leftSummaLayout;

    private static final int LOADER_ID = 2;

    public static FragmentAddBill getInstance() {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();
        return fragmentAddBill;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_bill, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBillList = (ListView) view.findViewById(R.id.add_bill_list);
        switchCompat = (SwitchCompat) view.findViewById(R.id.add_bill_switch);
        leftSummaLayout = (LinearLayout) view.findViewById(R.id.add_bill_left_summa);

        switchCompat.setOnCheckedChangeListener(new SwitchChangeListener());

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
        return new CursorLoader(getActivity(), MyContentProvider.CONTACT_CONTENT_URI, PROJECTION, null, null, /*SORT_ORDER*/null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new AddBillListAdapter(getActivity(), cursor, 0);
                addBillList.setAdapter(mAdapter);
                break;
        }
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b == true) {
                hideTextViews();
                leftSummaLayout.setVisibility(View.VISIBLE);
            } else {
                hideEditTextViews();
                leftSummaLayout.setVisibility(View.GONE);
            }
        }

        private void hideTextViews() {
            int first = addBillList.getFirstVisiblePosition();
            int last = addBillList.getLastVisiblePosition();
            for (int i = 0; i <= last - first; i++) {

                mAdapter.setItemMode(AddBillListAdapter.EDIT_TEXT_MODE);

                View view = addBillList.getChildAt(i);
                EditText editText = (EditText) view.findViewById(R.id.add_bill_list_need_edit);
                editText.setVisibility(View.VISIBLE);

                TextView textView = (TextView) view.findViewById(R.id.add_bill_list_need_text);
                textView.setVisibility(View.GONE);
            }
        }

        private void hideEditTextViews() {
            int first = addBillList.getFirstVisiblePosition();
            int last = addBillList.getLastVisiblePosition();
            for (int i = 0; i <= last - first; i++) {

                mAdapter.setItemMode(AddBillListAdapter.TEXT_VIEW_MODE);

                View view = addBillList.getChildAt(i);
                EditText editText = (EditText) view.findViewById(R.id.add_bill_list_need_edit);
                editText.setVisibility(View.GONE);

                TextView textView = (TextView) view.findViewById(R.id.add_bill_list_need_text);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
}
