package com.beautyteam.everpay.Fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.AddBillListAdapter;
import com.beautyteam.everpay.Adapters.BillListItem;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Utils.AnimUtils;
import com.flurry.android.FlurryAgent;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;
/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentAddBill extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, TitleUpdater, View.OnClickListener {

    private static final String GROUP_ID = "GROUP_ID";
    private static final String BILL_ID = "BILL_ID";
    private int groupId;
    private int billEditedId;

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
    private LinearLayout footerLayout;

    private ArrayList<BillListItem> billArrayList;

    private static final int LOADER_ADD = 2;
    private static final int LOADER_EDIT = 3;
    private String screenName = "Добавление счета";

    private ShowcaseView show;
    private int indexOfShowcase;
    private RelativeLayout.LayoutParams params;
    private SharedPreferences sPref;

    String title = "";

    /*
    Для добавления счета
     */
    public static FragmentAddBill getInstance(int groupId) {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentAddBill.setArguments(bundle);
        FlurryAgent.logEvent("Фрагмент добавления счета");
        return fragmentAddBill;
    }

    /*
    Для редактирования счета
     */
    public static FragmentAddBill getInstance(int groupId, int billId) {
        FragmentAddBill fragmentAddBill = new FragmentAddBill();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        bundle.putInt(BILL_ID, billId);
        fragmentAddBill.setArguments(bundle);
        FlurryAgent.logEvent("Фрагмент редактирования счета");
        return fragmentAddBill;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        billEditedId = getArguments().getInt(BILL_ID, -1);
        groupId = getArguments().getInt(GROUP_ID, -1);
        if (billEditedId == -1)
            getLoaderManager().initLoader(LOADER_ADD, null, this);
        else
            getLoaderManager().initLoader(LOADER_EDIT, null, this);

        return inflater.inflate(R.layout.fragment_add_bill, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sPref = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);

        titleEditText = (EditText) view.findViewById(R.id.add_bill_title);

        addBillList = (ListView) view.findViewById(R.id.add_bill_list);
        addBillList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        footerLayout = (LinearLayout)inflater.inflate(R.layout.footer_btn, null);
        footerBtn = (Button)footerLayout.findViewById(R.id.add_group_button);
        footerBtn.setVisibility(View.GONE);
        addBillList.addFooterView(footerLayout);

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

        Log.d(Constants.LOG, "GROUP_ID =" + groupId);

        footerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((MainActivity)getActivity()).addCoveredFragment(FragmentAddFriendToBill.getInstance(billArrayList));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, FragmentAddFriendToBill.getInstance(billArrayList))
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (!sPref.getBoolean(Constants.Preference.WAS_ADD_BILL_ADVICE_REVIEWED, false)||sPref.getBoolean(Constants.Preference.SETTING_ADVICE, false)) {

            view.post(new Runnable() {
                @Override
                public void run() {
                    demotour();
                }
            });
        }
    }

    private void demotour() {
        indexOfShowcase = 1;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER);
        params.setMargins(0, 0, 60, 60);
        show = new ShowcaseView.Builder(getActivity(), false)
                .setTarget(new ViewTarget(R.id.add_bill_switch, getActivity()))
                .setContentTitle("Если сумма разбита поровну между участниками - выберите ПОРОВНУ,\nиначе - НЕ ПОРОВНУ")
                .setScaleMultiplier(1f)
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();
        show.setButtonPosition(params);
        show.overrideButtonClick(this);
    }

    public void setLeftSumma(int summa) {
        leftSumma.setText(summa + "");
        updateTextColor();
    }

    public void setNeedSumma(int summa) {
        needSummaText.setText(summa + "");
        updateTextColor();
    }

    private void updateTextColor() {
        int needSummaValue = Integer.parseInt(needSummaText.getText().toString());
        int leftSummaValue = Integer.parseInt(leftSumma.getText().toString());

        if (needSummaValue == leftSummaValue)
            leftSumma.setTextColor(getResources().getColor(R.color.secondary_text));
        else
            leftSumma.setTextColor(getResources().getColor(R.color.red_text));
    }


    private void initializeAnimate() {
        alphaAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_appear);
        alphaDisappear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_disappear);
    }


    private void updateNeedListText(CharSequence charSequence) {
        String value = charSequence.toString();
        int summa;
        if (value.isEmpty()) summa = 0;
        else  summa = Integer.parseInt(value);
        if (mAdapter!=null)
            mAdapter.setNeedSumma(summa);

    }


    private static final String[] PROJECTION_ADD = new String[] {
            GroupMembers.ITEM_ID,
            GroupMembers.GROUP_ID,
            GroupMembers.USER_ID_VK,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME,
    };

    private static final String[] PROJECTION_EDIT = new String[] {
            Bills.ITEM_ID,
            Bills.TITLE,
            Bills.USER_ID,
            Bills.USER_ID_VK,
            Bills.USER_NAME,
            Bills.GROUP_ID,
            Bills.NEED_SUM,
            Bills.INVEST_SUM
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ADD)
            return new CursorLoader(getActivity(), EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION_ADD, GroupMembers.GROUP_ID + "=" + groupId, null, null);
        else
            return new CursorLoader(getActivity(), EverContentProvider.BILLS_CONTENT_URI, PROJECTION_EDIT, Bills.BILL_ID + "=" + billEditedId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        switch (loader.getId()) {
                case LOADER_ADD: {

                title = Constants.Titles.ADD_BILL;
                updateTitle();
                if (billArrayList == null) {
                    fillBillList(c);
                }
                int mode = switchCompat.isChecked() ? AddBillListAdapter.EDIT_TEXT_MODE : AddBillListAdapter.TEXT_VIEW_MODE;
                mAdapter = new AddBillListAdapter(getActivity(), billArrayList, this, needSummaEdit.getText().toString(), mode);
                addBillList.setAdapter(mAdapter);
                break;
            }

            case LOADER_EDIT: {
                title = Constants.Titles.EDIT_BILL;
                updateTitle();
                if (billArrayList == null) {
                    Cursor usersCursor = getActivity().getContentResolver().query(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION_ADD, GroupMembers.GROUP_ID + "=" +groupId, null, null);
                    fillBillList(usersCursor);

                    for (int i = 0; i < billArrayList.size(); i++) {
                        billArrayList.get(i).isRemoved = true;
                    }

                    c.moveToFirst();
                    int needPrev = c.getInt(c.getColumnIndex(Bills.NEED_SUM));
                    String billTitle = c.getString(c.getColumnIndex(Bills.TITLE));
                    titleEditText.setText(billTitle);

                    int generalSumm = 0;
                    boolean isNotEquals = false;

                    if (c.moveToFirst() && c.getCount() != 0) {
                        while (!c.isAfterLast()) {

                            int need = c.getInt(c.getColumnIndex(Bills.NEED_SUM));
                            int invest = c.getInt(c.getColumnIndex(Bills.INVEST_SUM));
                            String id = c.getString(c.getColumnIndex(Bills.USER_ID));
                            for (int i = 0; i < billArrayList.size(); i++) {
                                if (billArrayList.get(i).id.equals(id)) {
                                    billArrayList.get(i).need = need;
                                    billArrayList.get(i).invest = invest;
                                    billArrayList.get(i).isRemoved = false;
                                }

                            }
                            if (Math.abs(need - needPrev) > 1)
                                isNotEquals = true;
                            generalSumm += need;

                            c.moveToNext();

                        }
                    }

                    leftSumma.setText(generalSumm + "");
                    needSummaEdit.setText(generalSumm + "");
                    needSummaText.setText(generalSumm + "");
                    switchCompat.setChecked(isNotEquals);
                }
                int mode = switchCompat.isChecked() ? AddBillListAdapter.EDIT_TEXT_MODE : AddBillListAdapter.TEXT_VIEW_MODE;
                mAdapter = new AddBillListAdapter(getActivity(), billArrayList, this, needSummaEdit.getText().toString(), mode);
                addBillList.setAdapter(mAdapter);
                break;
            }
        }
    }


    private void fillBillList(Cursor c) {
        billArrayList = new ArrayList<BillListItem>();
        int i = 0;
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(GroupMembers.USER_ID));
                String vkid = c.getString(c.getColumnIndex(GroupMembers.USER_ID_VK));
                String name = c.getString(c.getColumnIndex(GroupMembers.USER_NAME));
                String img = c.getString(c.getColumnIndex(GroupMembers.USER_ID_VK)) + ".png";
                int need = 0;
                int invest = 0;
                boolean isRemoved = false;
                BillListItem billItem = new BillListItem(id, vkid, name, img, need, invest, isRemoved);
                c.moveToNext();
                i++;
                billArrayList.add(billItem);
            }
        }
    }

    public void addFooterBtn() {
        footerBtn.setVisibility(View.VISIBLE);
    }

    public void removeFooterBtn() {
        footerBtn.setVisibility(View.GONE);
    }


    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.ok_btn, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply:
                if (isCorrectData())
                {
                    int billID = insertToDB();
                    if (billEditedId < 0) {
                        ((MainActivity)getActivity()).getServiceHelper().addBill(billID, groupId);
                    }
                    else {
                        ((MainActivity)getActivity()).getServiceHelper().editBill(billID);
                    }
                    ((MainActivity)getActivity()).onBackPressed();


                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCorrectData() {
        if (titleEditText.getText().toString().length() < 3) {
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
            Toast.makeText(getActivity(), "Сумма счета должна быть равна полю \"Распределено\"", Toast.LENGTH_SHORT).show();
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

    /*
    Возвращает ID счета
     */
    private int insertToDB() {

        Cursor maxCursor = getActivity().getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, new String[]{"MAX(" + Bills.BILL_ID + ")"}, null, null, null);
        maxCursor.moveToFirst();
        int max = maxCursor.getInt(0);
        int billID = max+1;

        ContentValues cv = new ContentValues();
        cv.put(Bills.TITLE, titleEditText.getText().toString()); // Нужно ли заносить в базу???
        cv.put(Bills.GROUP_ID, groupId);
        cv.put(Bills.BILL_ID, billID);

        if (billEditedId > 0) // Если редактирование счета
            cv.put(Bills.BILL_EDITED_ID, billEditedId);

        for (int i=0; i<billArrayList.size(); i++) {
            BillListItem item = billArrayList.get(i);
            if (!item.isRemoved && !((item.invest == 0) && (item.need == 0))) { // Если не удалено и одновременно не равны нулю
                cv.put(Bills.USER_ID, billArrayList.get(i).id);
                cv.put(Bills.USER_ID_VK, billArrayList.get(i).vkid);
                cv.put(Bills.USER_NAME, billArrayList.get(i).name.replace("\n", " "));
                cv.put(Bills.INVEST_SUM, billArrayList.get(i).invest);
                cv.put(Bills.NEED_SUM, billArrayList.get(i).need);
                cv.put(Bills.STATE, Constants.State.READY_TO_SEND);
                getActivity().getContentResolver().insert(EverContentProvider.BILLS_CONTENT_URI, cv);
            }
        }
        return billID;
    }

    @Override
    public void onClick(View view) {
        if (!switchCompat.isChecked()) {
            switch (indexOfShowcase) {
                case 1: {
                    indexOfShowcase++;
                    show.hide();

                    show =  new ShowcaseView.Builder(getActivity(), true)
                            .setTarget(new ViewTarget(getView().findViewById(R.id.add_bill_need_summa_edit)))
                            .setContentTitle("Введите общую сумму счета")
                            .setStyle(R.style.CustomShowcaseTheme2)
                            .setScaleMultiplier(0.5f)
                            .build();
                    show.setButtonPosition(params);
                    show.overrideButtonClick(this);

                    /*show.setTarget(new ViewTarget(getView().findViewById(R.id.add_bill_need_summa_edit)));
                    show.setContentTitle("Введите общую сумму счета");*/
                    //show.show();
                    break;
                }
                case 2: {
                    if (addBillList.getChildAt(0) == null)
                        indexOfShowcase = 0;
                    else {
                        indexOfShowcase++;
                        show.hide();
                        View put = getViewByPosition(1).findViewById(R.id.add_bill_list_put_parrent);

                        show =  new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(new ViewTarget(put))
                                .setContentTitle("Введите сумму, которую внес участник")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .setScaleMultiplier(0.5f)
                                .build();
                        show.overrideButtonClick(this);
                        show.setButtonPosition(params);
                    }
                    break;
                }
                case 3: {
                    show.hide();
                    if (addBillList.getChildAt(0) == null)
                        indexOfShowcase = 0;
                    else {
                        indexOfShowcase++;
                        show.hide();
                        show = new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(new ViewTarget(addBillList.getChildAt(1).findViewById(R.id.add_bill_list_remove)))
                                .setContentTitle("Если пользователь не участвует в счете,\nнажмите на крестик")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .setScaleMultiplier(0.5f)
                                .build();
                        show.setButtonPosition(params);
                    }
                    sPref.edit()
                            .putBoolean(Constants.Preference.WAS_ADD_BILL_ADVICE_REVIEWED, true)
                            .commit();
                    break;
                }
            }
        } else {
            switch (indexOfShowcase) {
                case 1: {
                    if (addBillList.getChildAt(0) == null)
                        indexOfShowcase = 0;
                    else {
                        indexOfShowcase++;
                        show.hide();
                        show = new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(new ViewTarget(addBillList.getChildAt(1).findViewById(R.id.add_bill_list_need_text)))
                                .setContentTitle("Введите сумму, которую потратил участник")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .setScaleMultiplier(0.5f)
                                .build();
                        show.setButtonPosition(params);
                        show.overrideButtonClick(this);
                    }
                    break;
                }
                case 2: {
                    if (addBillList.getChildAt(0) == null)
                        indexOfShowcase = 0;
                    else {
                        indexOfShowcase++;
                        show.hide();
                        View put = getViewByPosition(1).findViewById(R.id.add_bill_list_put_parrent);
                        show = new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(new ViewTarget(put))
                                .setContentTitle("Введите сумму, которую внес участник")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .setScaleMultiplier(0.5f)
                                .build();
                        show.setButtonPosition(params);
                        show.overrideButtonClick(this);
                    }
                    break;
                }
                case 3: {
                    if (addBillList.getChildAt(0) == null)
                        indexOfShowcase = 0;
                    else {
                        show.hide();
                        show = new ShowcaseView.Builder(getActivity(), true)
                                .setTarget(new ViewTarget(addBillList.getChildAt(1).findViewById(R.id.add_bill_list_remove)))
                                .setContentTitle("Если пользователь не участвует в счете,\nнажмите на крестик")
                                .setStyle(R.style.CustomShowcaseTheme2)
                                .setScaleMultiplier(0.5f)
                                .build();
                        show.setButtonPosition(params);
                    }

                    sPref.edit()
                            .putBoolean(Constants.Preference.WAS_ADD_BILL_ADVICE_REVIEWED, true)
                            .commit();
                    break;
                }

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
            if (mAdapter != null) {
                mAdapter.setItemMode(AddBillListAdapter.EDIT_TEXT_MODE);
                mAdapter.refreshAvaliableList();
            }
        }

        private void hideListEditTextViews() {
            if (mAdapter != null) {
                mAdapter.setItemMode(AddBillListAdapter.TEXT_VIEW_MODE);
                mAdapter.refreshAvaliableList();
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

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void updateTitle() {
        try {
            ((MainActivity) getActivity()).setTitle(title);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {};
    }

    @Override
    public void onStop() {
        if( indexOfShowcase > 0 && indexOfShowcase < 5)
            show.hide();
        super.onStop();

    }

    private View getViewByPosition(int pos) {
        final int firstListItemPosition = addBillList.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + addBillList.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return addBillList.getAdapter().getView(pos, null, addBillList);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return addBillList.getChildAt(childIndex);
        }
    }

}
