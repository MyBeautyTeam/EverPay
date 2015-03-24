package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.AddBillListAdapter;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Utils.AnimUtils;
import com.beautyteam.everpay.Utils.ScaleAnim;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentAddBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private AddBillListAdapter mAdapter;
    private ListView addBillList;
    private SwitchCompat switchCompat;
    private LinearLayout leftSummaLayout;
    private EditText needSumma;

    private TextView eqText;
    private TextView notEqText;

    private Animation alphaAppear;
    private Animation alphaDisappear;

    private static final int LOADER_ID = 2;

    public static FragmentAddBill getInstance() {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();
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
        addBillList = (ListView) view.findViewById(R.id.add_bill_list);
        addBillList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        addBillList.setStackFromBottom(true);

        leftSummaLayout = (LinearLayout) view.findViewById(R.id.add_bill_left_summa);
        needSumma = (EditText) view.findViewById(R.id.add_bill_need_summa);
        needSumma.addTextChangedListener(new TextWatcher() {
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


    }


    private void initializeAnimate() {
        alphaAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_appear);
        alphaAppear.setAnimationListener(new AnimationListenerImpl(leftSummaLayout, AnimationListenerImpl.ACTION_APPEAR));

        alphaDisappear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_disappear);
        alphaDisappear.setAnimationListener(new AnimationListenerImpl(leftSummaLayout, AnimationListenerImpl.ACTION_DISAPPEAR));

    }

    private void updateNeedListText(CharSequence charSequence) {
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

        for (int i = 0; i <= last - first; i++) {

            View view = addBillList.getChildAt(i);

            TextView textView = (TextView) view.findViewById(R.id.add_bill_list_need_text);
            textView.setText(value);
        }
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
                leftSummaLayout.setVisibility(View.VISIBLE);
                AnimUtils.animateText(notEqText, colorFrom, colorTo, 300, 14, 20);
                AnimUtils.animateText(eqText, colorTo, colorFrom, 300, 20, 14);
                leftSummaLayout.startAnimation(alphaAppear);

                //leftSummaLayout.startAnimation(new ScaleAnim(1.0f, 1.0f, 0.0f, 1.0f, 300, leftSummaLayout, true));
            } else { // Поровну
                hideListEditTextViews();
                AnimUtils.animateText(eqText, colorFrom, colorTo, 300, 14, 20);
                AnimUtils.animateText(notEqText, colorTo, colorFrom, 300, 20, 14);

                leftSummaLayout.startAnimation(alphaDisappear);

            }
        }

        private void hideListTextViews() {
            mAdapter.setItemMode(AddBillListAdapter.EDIT_TEXT_MODE);

            int first = addBillList.getFirstVisiblePosition();
            int last = addBillList.getLastVisiblePosition();

            for (int i = 0; i <= last - first; i++) {

                View view = addBillList.getChildAt(i);

                TextView textView = (TextView) view.findViewById(R.id.add_bill_list_need_text);
                textView.setVisibility(View.GONE);

                EditText editText = (EditText) view.findViewById(R.id.add_bill_list_need_edit);
                editText.setVisibility(View.VISIBLE);

            }
        }

        private void hideListEditTextViews() {
            mAdapter.setItemMode(AddBillListAdapter.TEXT_VIEW_MODE);

            int first = addBillList.getFirstVisiblePosition();
            int last = addBillList.getLastVisiblePosition();

            for (int i = 0; i <= last - first; i++) {

                View view = addBillList.getChildAt(i);
                EditText editText = (EditText) view.findViewById(R.id.add_bill_list_need_edit);
                editText.setVisibility(View.GONE);

                TextView textView = (TextView) view.findViewById(R.id.add_bill_list_need_text);
                textView.setVisibility(View.VISIBLE);
            }
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
}
