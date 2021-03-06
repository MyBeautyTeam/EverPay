package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.CalcListAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.beautyteam.everpay.Utils.PrintScreener;
import com.flurry.android.FlurryAgent;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.tjeannin.apprate.AppRate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.CALCULATE;
import static com.beautyteam.everpay.Constants.Action.EDIT_CALCULATION;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by Admin on 14.03.2015.
 */
public class FragmentCalculation extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, RequestCallback,
        TitleUpdater {

    private static final int LOADER_ID = 1;
    private ListView calcList;
    private Button calcBtn;
    private Button detailsBtn;
    private int groupId;
    private ShowcaseView show;
    private int indexOfShowcase;
    private SharedPreferences sPref;


    private CalcListAdapter mAdapter;
    private static final String GROUP_ID = "GROUP_ID";

    private LinearLayout loadingLayout;
    private ServiceHelper serviceHelper;

    private TextView emptyText;
    private String screenName = "Расчет";

    public static final String NOTIFICATION = "NOTIF";
    private ProgressDialog progressDialog;

    private static MainActivity mainActivity;


    public static FragmentCalculation getInstance(int groupId) {
        FragmentCalculation fragmentCalculation = new FragmentCalculation();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentCalculation.setArguments(bundle);

        return fragmentCalculation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupId = getArguments().getInt(GROUP_ID);
        setHasOptionsMenu(true);

        sPref = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_calculation, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sPref = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        FlurryAgent.logEvent("Фрагмент расчет");
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingPanel);
        calcList = (ListView) view.findViewById(R.id.calc_list);
        calcBtn = (Button) view.findViewById(R.id.calc_ok_btn);
        calcBtn.setOnClickListener(this);

        detailsBtn = (Button) view.findViewById(R.id.calc_details_btn);
        detailsBtn.setOnClickListener(this);

        if (sPref.getBoolean(Constants.Preference.WAS_CALCULATION_ADVICE_REVIEWED, false) && new Random().nextInt() % 3 == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage("Поставьте нам оценку! Для Вас пустяк, а для нас это очень важно!")
                    .setIcon(R.drawable.group_icon)
                    .setPositiveButton("Хорошо", null)
                    .setNegativeButton("Не хочу", null)
                    .setNeutralButton("Потом", null);

            new AppRate(getActivity())
                    .setCustomDialog(builder)
                    .init();
        }


        setupEmptyList(view);

    }

    private void demotour() {
        indexOfShowcase = 1;

        if (!isCalculationScreen())
            return;

        if (calcList.getChildAt(0) == null) {
            indexOfShowcase = 0;
        }else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0, 0, 70, 120);
            show = new ShowcaseView.Builder(getActivity(), true)
                .setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.item_calc_first_name)))
                .setContentTitle("Чтобы посмотреть аватарку пользователя - нажмите на имя пользователя")

                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setScaleMultiplier(0.5f)
                    .build();
            show.setButtonPosition(params);
            show.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                @Override
                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                }

                @Override
                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                        if (!isCalculationScreen())
                            return;

                        switch (indexOfShowcase) {
                            case 1: {
                                indexOfShowcase++;
                                show.setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.calc_summa)));
                                show.setContentTitle("Число над стрелочкой означает сумму долга");
                                show.show();
                                break;
                            }
                            case 2: {
                                indexOfShowcase++;
                                show.setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.calc_checkbox)));
                                show.setContentTitle("Если долг возвращен - поставьте галочку");
                                show.show();
                                break;
                            }
                            case 3: {
                                indexOfShowcase++;
                                show.setTarget(new ViewTarget(R.id.notify_vk, getActivity()));
                                show.setContentTitle("Нажмите, чтобы оповестить пользователей");
                                show.show();
                                sPref.edit()
                                        .putBoolean(Constants.Preference.WAS_CALCULATION_ADVICE_REVIEWED, true)
                                        .commit();
                                break;
                            }
                        }
                }

                @Override
                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                }
            });
        }

    }

    private boolean isCalculationScreen() {
        if (mainActivity == null)
            return false;

        String title = mainActivity.getCurrentTitle().toString();
        return title.equals(Constants.Titles.CALCULATION);
    }

    private void setupEmptyList(View view) {
        ViewStub stub = (ViewStub) view.findViewById(R.id.empty);
        emptyText = (TextView)stub.inflate();
        emptyText.setText("");
        calcList.setEmptyView(emptyText);
    }

    private static final String[] PROJECTION = new String[] {
            Calculation.ITEM_ID,
            Calculation.CALC_ID,
            Calculation.GROUPS_ID,
            Calculation.WHO_ID,
            Calculation.WHO_ID_VK,
            Calculation.NAME_WHO,
            Calculation.WHOM_ID,
            Calculation.WHOM_ID_VK,
            Calculation.NAME_WHOM,
            Calculation.SUMMA,
            Calculation.IS_DELETED
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), EverContentProvider.CALCULATION_CONTENT_URI, PROJECTION, Calculation.GROUPS_ID +" = " + groupId, null, /*SORT_ORDER*/null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter = new CalcListAdapter(getActivity(), cursor, 0);
                calcList.setAdapter(mAdapter);
                break;
        }
    }
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.notify, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notify_vk:
                indexOfShowcase = 0;
                if (show != null)
                    show.hide();
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        loadingLayout.setVisibility(View.VISIBLE);
        serviceHelper.onResume();
        serviceHelper.calculate(groupId);
    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calc_ok_btn:
                indexOfShowcase = 0;
                if (show != null)
                    show.hide();

                HashMap<String, Integer> mapIdToIsDeleted = mAdapter.getMapIdToIsdeleted();
                /*if (mapIdToIsDeleted.size() == 0) { // Если экран пуст - просто закрываем
                    ((MainActivity)getActivity()).removeFragment();
                    break;
                }*/

                Iterator it = mapIdToIsDeleted.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    long id = Long.parseLong(pair.getKey().toString());
                    int isDeleted = Integer.parseInt(pair.getValue().toString());

                    Uri uri = ContentUris.withAppendedId(EverContentProvider.CALCULATION_CONTENT_URI, id);

                    ContentValues cv = new ContentValues();
                    cv.put(Calculation.IS_DELETED, isDeleted);

                    int length = getActivity().getContentResolver().update(uri, cv, Calculation.CALC_ID + "=" + id, null);

                }
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Применение изменений");
                progressDialog.setCancelable(false);
                progressDialog.show();
                serviceHelper.editCalculation(groupId);
                break;
            case R.id.calc_details_btn:
                indexOfShowcase = 0;
                if (show != null)
                    show.hide();
                ((MainActivity) getActivity()).addFragment(FragmentCalcDetails.getInstance(groupId));
                break;

        }
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(CALCULATE)) {

            if (!sPref.getBoolean(Constants.Preference.WAS_CALCULATION_ADVICE_REVIEWED, false)||sPref.getBoolean(Constants.Preference.SETTING_ADVICE, false))
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            demotour();
                        }
                    }, 1000);
            emptyText.setText("Расчет пуст \nЧтобы здесь что-то появилось, нужно добавить счет");
            loadingLayout.setVisibility(View.GONE);
            if (result == Constants.Result.OK) {
            } else {
                if (mAdapter != null)
                    mAdapter.setEnabled(false);
                emptyText.setText("Произошла ошибка =(\n Проверьте соединение с интернетом");
                Toast.makeText(getActivity(), "Неудалось загрузить новые данные", Toast.LENGTH_SHORT).show();
            }
        } else
        if (action.equals(EDIT_CALCULATION)) {
            progressDialog.dismiss();
            if (result == Constants.Result.OK) {
                ((MainActivity)getActivity()).removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка. Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }


    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.CALCULATION);
    }

    private void showDialog(){

        FlurryAgent.logEvent("Открыто оповещение");
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list);
        TextView title = (TextView)dialog.findViewById(R.id.dialog_list_title);
        title.setText("Оповещение");
        ListView lv = (ListView) dialog.findViewById(R.id.dialog_action_list);
        dialog.setCancelable(true);


        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> datum = new HashMap<String, String>(2);
        datum.put("First Line", "ВКонтакте всем");
        datum.put("Second Line","Личное сообщение VK всей группе");
        data.add(datum);

        datum = new HashMap<String, String>(2);
        datum.put("First Line", "ВКонтакте моим должникам");
        datum.put("Second Line","Личное сообщение VK связанным со мной");
        data.add(datum);

        datum = new HashMap<String, String>(2);
        datum.put("First Line", "Оповещение в приложении");
        datum.put("Second Line", "Push-уведомление в EverPay связанным со мной");
        data.add(datum);


        SimpleAdapter adapter1 = new SimpleAdapter(getActivity(), data,
                R.layout.item_dialog_send_message,
                new String[] {"First Line", "Second Line" },
                new int[] {R.id.item_dialog_send_main, R.id.item_dialog_send_second });
        //
        lv.setAdapter(adapter1);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view1,int position, long id) {
                dialog.dismiss();

                String prefStr = groupId + NOTIFICATION + getDate();
                int countOfNotif = sPref.getInt(prefStr, 2);
                if (countOfNotif < 1) {
                    Toast.makeText(getActivity(), "Не больше двух оповещений в день :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (position) {
                    case 0: {
                        FlurryAgent.logEvent("Оповещение vk всем");
                        Bitmap screen = new PrintScreener().printscreen2(calcList);
                        ((MainActivity) getActivity()).getServiceHelper().sendVkMessage(screen, groupId, true);
                        break;
                    }
                    case 1: {
                        FlurryAgent.logEvent("Оповещение vk моим");
                        Bitmap screen = new PrintScreener().printscreen2(calcList);
                        ((MainActivity) getActivity()).getServiceHelper().sendVkMessage(screen, groupId, false);
                        break;
                    }
                    case 2: {
                        FlurryAgent.logEvent("Пуш-уведомление в EverPay");
                        ((MainActivity) getActivity()).getServiceHelper().sendNotification(groupId);
                        break;
                    }

                }
                /*if (position == 0) {
                    FlurryAgent.logEvent("Оповещение vk всем");
                    Bitmap screen = new PrintScreener().printscreen2(calcList);
                    ((MainActivity) getActivity()).getServiceHelper().sendVkMessage(screen, groupId);
                }
                else {
                    ((MainActivity) getActivity()).getServiceHelper().sendNotification(groupId);
                }*/

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public static String getDate() {
        Date cDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return date;
    }

    @Override
    public void onStop() {
        if(indexOfShowcase > 0 && indexOfShowcase < 5) {
            indexOfShowcase = 0;
            if (show != null)
                show.hide();
        }
        super.onStop();

    }

}
