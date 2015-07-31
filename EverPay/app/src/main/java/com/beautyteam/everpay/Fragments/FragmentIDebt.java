package com.beautyteam.everpay.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.DebtorsListAdapter;
import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Dialogs.DialogDebtDetail;
import com.beautyteam.everpay.Dialogs.DialogWindow;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.GET_DEBTS;
import static com.beautyteam.everpay.Constants.Action.GET_HISTORY;

/**
 * Created by Admin on 10.03.2015.
 */
public class FragmentIDebt extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PageAdapter.OnDebtsLoadedListener,
        TitleUpdater {


    private ListView debtorsList;

    public static final int LOADER_ID_I_DEBT = 1;
    public static final int LOADER_ID_DEBT_FOR_ME = 0;
    private DebtorsListAdapter mAdapter;
    private int loader;
    private LinearLayout loadingLayout;
    private TextView emptyText;
    public static final String LOADER = "LOADER";
    private DialogDebtDetail dialogDebtDetail;

    public static FragmentIDebt getInstance(int loader) {
        FragmentIDebt fragmentIDebt = new FragmentIDebt();

        Bundle bundle = new Bundle();
        bundle.putInt(LOADER, loader);
        fragmentIDebt.setArguments(bundle);

        return fragmentIDebt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        loader = getArguments().getInt(LOADER);
        return inflater.inflate(R.layout.fragment_i_debts, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        debtorsList = (ListView) view.findViewById(R.id.debtors_fragment_list);

        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingPanel);

        /*if (loader == LOADER_ID_I_DEBT)
            ((MainActivity)getActivity()).getServiceHelper().getDebts();*/
        /*debtorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //showDialog();
            }
        });*/
        setupEmptyList(view);
    }

    private void setupEmptyList(View view) {
        ViewStub stub = (ViewStub) view.findViewById(R.id.empty);
        emptyText = (TextView)stub.inflate();
        emptyText.setText("");
        debtorsList.setEmptyView(emptyText);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

        private static final String[] PROJECTION = new String[] {
        Debts.ITEM_ID,
        Debts.SUM_SUMMA,
        Debts.USER_ID,
        Debts.USER_VK_ID,
        Debts.USER_NAME,
        Debts.GROUP_ID,
        Debts.GROUP_TITLE,
        Debts.IS_I_DEBT
    };

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_I_DEBT:
                return new CursorLoader(getActivity(), EverContentProvider.DEBTS_CONTENT_URI, PROJECTION, Debts.IS_I_DEBT +"=1", null, Debts.SUMMA + " desc");
            case LOADER_ID_DEBT_FOR_ME:
                return new CursorLoader(getActivity(), EverContentProvider.DEBTS_CONTENT_URI, PROJECTION, Debts.IS_I_DEBT +"=0", null, Debts.SUMMA +" desc");
            default:
                return null;
        }
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        loadingLayout.setVisibility(View.GONE);
        emptyText.setText("Поздравляю! \nУ тебя здесь ничего нет =)");
        int summa = 0;
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                summa += c.getInt(c.getColumnIndex(Debts.SUM_SUMMA));
                c.moveToNext();
            }
        }

        c.moveToFirst();

        mAdapter = new DebtorsListAdapter(getActivity(), c, 0);
        debtorsList.setAdapter(mAdapter);

        if (loader.getId() == LOADER_ID_I_DEBT) {
            FragmentViewPager.slidingTabLayout.setIDebt(summa);
        } else {
            FragmentViewPager.slidingTabLayout.setDebtForMe(summa);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadingLayout.setVisibility(View.VISIBLE);
        FragmentViewPager.slidingTabLayout.clearSlidingTab();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.MAIN);
    }

    @Override
    public void onDebtsLoaded() {
        getLoaderManager().initLoader(loader, null, this);
    }


}
