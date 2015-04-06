package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.AddBillListAdapter;
import com.beautyteam.everpay.Adapters.AddBillListNEWAdapter;
import com.beautyteam.everpay.Adapters.BillListItem;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.DialogWindow;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Utils.AnimUtils;

import java.util.ArrayList;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentAddBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String GROUP_ID = "GROUP_ID";
    private int groupId;

    private AddBillListNEWAdapter mAdapter;
    private ListView addBillList;
    private SwitchCompat switchCompat;
    private LinearLayout leftSummaLayout;
    private EditText needSummaEdit;
    private TextView needSummaText;

    private TextView leftSumma;
    private TextView eqText;
    private TextView notEqText;

    private Animation alphaAppear;
    private Animation alphaDisappear;

    private Button footerBtn;

    private ArrayList<BillListItem> billArrayList;

    private static final int LOADER_ID = 2;

    public static FragmentAddBill getInstance(int groupId) {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentAddBill.setArguments(bundle);

        return fragmentAddBill;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return inflater.inflate(R.layout.fragment_add_bill, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);

        groupId = getArguments().getInt(GROUP_ID);

        addBillList = (ListView) view.findViewById(R.id.add_bill_list);
        addBillList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        footerBtn = (Button)inflater.inflate(R.layout.footer_btn, null);

        leftSummaLayout = (LinearLayout) view.findViewById(R.id.add_bill_left_summa_layout);

        needSummaText = (TextView) view.findViewById(R.id.add_bill_need_summa_text);
        needSummaEdit = (EditText) view.findViewById(R.id.add_bill_need_summa_edit);

        needSummaEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                updateNeedListText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        leftSumma = (TextView) view.findViewById(R.id.add_bill_left_summa);
        leftSumma.setText("0");

        switchCompat = (SwitchCompat) view.findViewById(R.id.add_bill_switch);
        switchCompat.setOnCheckedChangeListener(new SwitchChangeListener());

        eqText = (TextView) view.findViewById(R.id.add_bill_equally_text);

        eqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompat.setChecked(false);
            }
        });

        notEqText = (TextView) view.findViewById(R.id.add_bill_not_equally_text);
        notEqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompat.setChecked(true);
            }
        });

        initializeAnimate();

        footerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).addCoveredFragment(FragmentAddFriendToBill.getInstance(billArrayList));
            }
        });

    }

    public void setLeftSumma(int summa) {
        leftSumma.setText(summa + "");
    }

    public void setNeedSumma(int summa) {
        needSummaText.setText(summa + "");
    }


    private void initializeAnimate() {
        alphaAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_appear);
        //alphaAppear.setAnimationListener(new AnimationListenerImpl(leftSummaLayout, AnimationListenerImpl.ACTION_APPEAR));

        alphaDisappear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_disappear);
        //alphaDisappear.setAnimationListener(new AnimationListenerImpl(leftSummaLayout, AnimationListenerImpl.ACTION_DISAPPEAR));

    }


    private void updateNeedListText(CharSequence charSequence) {
        /*
        String value = "";
        try {
            float summa = Float.parseFloat(charSequence.toString());
            summa /= addBillList.getCount();
            value = String.format("%.0f", summa);
        } catch (NumberFormatException e) {
            value = "0";
        }

        int first = addBillList.getFirstVisiblePosition();
        int last = addBillList.getLastVisiblePosition();

        mAdapter.setNeedSumma(value);
        */
        String value = charSequence.toString();
        int summa;
        if (value.isEmpty()) summa = 0;
        else  summa = Integer.parseInt(value);
        mAdapter.setNeedSumma(summa);

    }


    private static final String[] PROJECTION = new String[] {
            Users.USER_ID_VK,
            Users.NAME,
            Users.IMG
    };


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri= Uri.withAppendedPath(EverContentProvider.GROUP_DETAILS_CONTENT_URI, "users");
        uri= Uri.withAppendedPath(uri, groupId+"");
        return new CursorLoader(getActivity(), uri, PROJECTION, null, null, null);
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (billArrayList == null) {
                    billArrayList = new ArrayList<BillListItem>();
                    int i = 0;
                    if (c.moveToFirst() && c.getCount() != 0) {
                        while (!c.isAfterLast()) {
                            String id = c.getString(c.getColumnIndex(Users.USER_ID_VK));
                            String name = c.getString(c.getColumnIndex(Users.NAME));
                            String img = c.getString(c.getColumnIndex(Users.IMG));
                            int need = 0;
                            int invest = 0;
                            boolean isRemoved = false;
                            BillListItem billItem = new BillListItem(id, name, img, need, invest, isRemoved);
                            c.moveToNext();
                            i++;
                            billArrayList.add(billItem);
                        }
                    }
                }
                mAdapter = new AddBillListNEWAdapter(getActivity(), billArrayList, this);
                addBillList.setAdapter(mAdapter);
                break;
        }
    }

    public void showDialog() {
        DialogWindow dialogWindow = new DialogWindow(getActivity());
        dialogWindow.show();
    }

    public void addFooterBtn() {
        if (addBillList.getFooterViewsCount() == 0) {
            addBillList.addFooterView(footerBtn);
        }
    }

    public void removeFooterBtn() {
        if (addBillList.getFooterViewsCount() != 0) {
            addBillList.removeFooterView(footerBtn);
        }
    }


    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ok_btn, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
        }
        return super.onOptionsItemSelected(item);
    }

    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            int colorFrom = getResources().getColor(R.color.secondary_text);
            int colorTo = getResources().getColor(R.color.dark_primary);

            if (b == true) { // Не поровну
                hideListTextViews();

                needSummaEdit.setVisibility(View.GONE);
                needSummaText.setVisibility(View.VISIBLE);

                AnimUtils.animateText(notEqText, colorFrom, colorTo, 300, 12, 18);
                AnimUtils.animateText(eqText, colorTo, colorFrom, 300, 18, 12);
                //needSummaText.startAnimation(alphaAppear);

                //leftSummaLayout.startAnimation(new ScaleAnim(1.0f, 1.0f, 0.0f, 1.0f, 300, leftSummaLayout, true));
            } else { // Поровну
                hideListEditTextViews();

                needSummaText.setVisibility(View.GONE);
                needSummaEdit.setVisibility(View.VISIBLE);

                AnimUtils.animateText(eqText, colorFrom, colorTo, 300, 12, 18);
                AnimUtils.animateText(notEqText, colorTo, colorFrom, 300, 18, 12);

                //leftSummaLayout.startAnimation(alphaDisappear);

            }
        }

        private void hideListTextViews() {
            mAdapter.setItemMode(AddBillListAdapter.EDIT_TEXT_MODE);
            mAdapter.refreshAvaliableList();
        }

        private void hideListEditTextViews() {
            mAdapter.setItemMode(AddBillListAdapter.TEXT_VIEW_MODE);
            mAdapter.refreshAvaliableList();
        }
    }


    private class AnimationListenerImpl implements Animation.AnimationListener {
        static public final int ACTION_APPEAR = 1;
        static public final int ACTION_DISAPPEAR = 2;

        private int action;
        private View view;

        public AnimationListenerImpl(View _view, int _action) {
            action = _action;
            view = _view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (action == ACTION_APPEAR) view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (action == ACTION_DISAPPEAR) view.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public void onPause() {
        super.onPause();
        Log.d("FRAG", "onPasuse");
    }

    public void onResume() {
        super.onResume();
        Log.d("FRAG", "onRESUME");
    }

}
