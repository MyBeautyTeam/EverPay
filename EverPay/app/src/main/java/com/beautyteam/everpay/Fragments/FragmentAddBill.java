package com.beautyteam.everpay.Fragments;

import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.AddBillListAdapter;
import com.beautyteam.everpay.Adapters.BillListItem;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
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

    private AddBillListAdapter mAdapter;
    private ListView addBillList;
    private SwitchCompat switchCompat;
    private LinearLayout leftSummaLayout;
    private EditText needSummaEdit;
    private TextView needSummaText;

    private TextView leftSumma;
    private TextView eqText;
    private TextView notEqText;
    private EditText titleEditText;

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

        titleEditText = (EditText) view.findViewById(R.id.add_bill_title);

        groupId = getArguments().getInt(GROUP_ID);

        addBillList = (ListView) view.findViewById(R.id.add_bill_list);
        addBillList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        footerBtn = (Button)inflater.inflate(R.layout.footer_btn, null);
        footerBtn.setVisibility(View.GONE);
        addBillList.addFooterView(footerBtn);

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
        String value = charSequence.toString();
        int summa;
        if (value.isEmpty()) summa = 0;
        else  summa = Integer.parseInt(value);
        mAdapter.setNeedSumma(summa);

    }


    private static final String[] PROJECTION = new String[] {
            GroupMembers.ITEM_ID,
            GroupMembers.GROUP_ID,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME,
    };


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION, GroupMembers.GROUP_ID + "=" + groupId, null, null);
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (billArrayList == null) {
                    billArrayList = new ArrayList<BillListItem>();
                    int i = 0;
                    if (c.moveToFirst() && c.getCount() != 0) {
                        while (!c.isAfterLast()) {
                            String id = c.getString(c.getColumnIndex(GroupMembers.USER_ID));
                            String name = c.getString(c.getColumnIndex(GroupMembers.USER_NAME));
                            String img = c.getString(c.getColumnIndex(GroupMembers.USER_ID))  + ".png";
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
                int mode = switchCompat.isChecked() ? AddBillListAdapter.EDIT_TEXT_MODE : AddBillListAdapter.TEXT_VIEW_MODE;
                mAdapter = new AddBillListAdapter(getActivity(), billArrayList, this, needSummaEdit.getText().toString(), mode);
                addBillList.setAdapter(mAdapter);
                break;
        }
    }

    public void showDialog() {
        DialogWindow dialogWindow = new DialogWindow(getActivity());
        dialogWindow.show();
    }

    public void addFooterBtn() {
        footerBtn.setVisibility(View.VISIBLE);
    }

    public void removeFooterBtn() {
        footerBtn.setVisibility(View.GONE);
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
                if (isCorrectData()) {
                    insertToDB();
                    Toast.makeText(getActivity(), "Счет был добавлен", Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).removeFragment();

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCorrectData() {
        if (titleEditText.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), "Слишком короткое название", Toast.LENGTH_SHORT).show();
            titleEditText.requestFocus();
            return false;
        }

        int needSumma = mAdapter.getNeedSumma();
        int investSumma = mAdapter.getInvestSumma();


        if (needSumma == 0) {
            Toast.makeText(getActivity(), "С нулевым долгом не принимаем!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (needSumma != investSumma) {
            Toast.makeText(getActivity(), "Сколько должны, столько и вносите!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mAdapter.getCountAvailable() < 2) {
            Toast.makeText(getActivity(), "Ха! Не маловато ли народу для счета?", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*
        Может быть ситуация, когда все поля пустые, кроме одного. Отсекаем ее.
        Проверяем количество ненулевых (и при этом неудаленных) элементов
         */
        int count = 0;
        for (int i=0; i<billArrayList.size(); i++) {
            if (! ((billArrayList.get(i).need == 0) && (billArrayList.get(i).invest == 0)))
                count++;
        }

        if (count < 2) {
            Toast.makeText(getActivity(), "Ха! Не маловато ли народу для счета?", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void insertToDB() {
        ContentValues cv = new ContentValues();
        cv.put(Bills.TITLE, titleEditText.getText().toString()); // Нужно ли заносить в базу???
        cv.put(Bills.GROUP_ID, groupId);

        for (int i=0; i<billArrayList.size(); i++) {
            BillListItem item = billArrayList.get(i);
            if (!item.isRemoved && !((item.invest == 0) && (item.need == 0))) { // Если не удалено и одновременно не равны нулю
                cv.put(Bills.USER_ID, billArrayList.get(i).id);
                cv.put(Bills.USER_NAME, billArrayList.get(i).name.replace("\n", " "));
                cv.put(Bills.INVEST_SUM, billArrayList.get(i).invest);
                cv.put(Bills.NEED_SUM, billArrayList.get(i).need);
                getActivity().getContentResolver().insert(EverContentProvider.BILLS_CONTENT_URI, cv);
            }
        }
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
    }

    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(Constants.Titles.ADD_BILL);
    }

}
