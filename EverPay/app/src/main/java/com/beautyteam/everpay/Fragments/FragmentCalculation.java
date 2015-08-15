package com.beautyteam.everpay.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.CalcListAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.beautyteam.everpay.Utils.PrintScreener;
import com.flurry.android.FlurryAgent;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.tjeannin.apprate.AppRate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.CALCULATE;
import static com.beautyteam.everpay.Constants.Action.GET_HISTORY;

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
    private int indexOfShowcase = 1;


    private CalcListAdapter mAdapter;
    private static final String GROUP_ID = "GROUP_ID";

    private LinearLayout loadingLayout;
    private ServiceHelper serviceHelper;

    private TextView emptyText;
    private String screenName = "Расчет";


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

        getLoaderManager().initLoader(LOADER_ID, null, this);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_calculation, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlurryAgent.logEvent("Фрагмент расчет");
        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingPanel);
        calcList = (ListView) view.findViewById(R.id.calc_list);
        calcBtn = (Button) view.findViewById(R.id.calc_ok_btn);
        calcBtn.setOnClickListener(this);

        detailsBtn = (Button) view.findViewById(R.id.calc_details_btn);
        detailsBtn.setOnClickListener(this);

        if (new Random().nextInt() % 2 == 0) {
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

        view.post(new Runnable() {
            @Override
            public void run() {
                demotour();
            }
        });
    }

    private void demotour() {
        indexOfShowcase = 1;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 0, 70, 70);
        show = new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(R.id.notify_vk, getActivity()))
                .setContentTitle("Чтобы оповестить пользователей - нажмите на следующую кнопку")
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
                switch (indexOfShowcase) {
                    case 1: {
                        if (calcList.getChildAt(0) == null) {
                            indexOfShowcase = 0;
                        } else {
                            indexOfShowcase++;
                            show.setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.calc_summa)));
                            show.setContentTitle("Число над стрелочкой обозначает сумму долга");
                            show.show();
                        }
                        break;
                    }
                    case 2: {
                        indexOfShowcase++;
                        show.setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.item_calc_first_name)));
                        show.setContentTitle("Чтобы посмотреть аватарку пользователя - нажмите на имя пользователя");
                        show.show();
                        break;
                    }
                    case 3: {
                        indexOfShowcase++;
                        show.setTarget(new ViewTarget(calcList.getChildAt(0).findViewById(R.id.calc_checkbox)));
                        show.setContentTitle("Если долг возвращен - поставьте галочку");
                        show.show();
                        break;
                    }
                }
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }
        });

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
                show.hide();
                HashMap<String, Integer> mapIdToIsDeleted = mAdapter.getMapIdToIsdeleted();
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
                ((MainActivity) getActivity()).getServiceHelper().editCalculation(groupId);
                ((MainActivity) getActivity()).removeFragment();
                break;
            case R.id.calc_details_btn:
                indexOfShowcase = 0;
                show.hide();
                ((MainActivity) getActivity()).addFragment(FragmentCalcDetails.getInstance(groupId));
                break;

        }
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(CALCULATE)) {
            emptyText.setText("Расчет пуст \nЧтобы здесь что-то появилось, нужно добавить счет");
            loadingLayout.setVisibility(View.GONE);
            if (result == Constants.Result.OK) {
            } else {
                emptyText.setText("Произошла ошибка =(\n Проверьте соединение с интернетом");
                Toast.makeText(getActivity(), "Неудалось загрузить новые данные", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.CALCULATION);
    }

    private void showDialog(){
        String names[] = {"Личное сообщение vk.com", "Оповещение в приложении"};
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list);
        TextView title = (TextView)dialog.findViewById(R.id.dialog_list_title);
        title.setText("Оповещение");
        ListView lv = (ListView) dialog.findViewById(R.id.dialog_action_list);
        dialog.setCancelable(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_dialog_not_send, R.id.item_dialog, names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view1,int position, long id) {
                dialog.dismiss();
                if (position == 0) {
                    Bitmap screen = new PrintScreener().printscreen2(calcList);
                    ((MainActivity) getActivity()).getServiceHelper().sendPrintScreen(screen, groupId);
                    dialog.dismiss();
                }
                else {
                    ((MainActivity) getActivity()).getServiceHelper().sendNotification();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onStop() {
        indexOfShowcase = 0;
        show.hide();
        super.onStop();

    }
}
