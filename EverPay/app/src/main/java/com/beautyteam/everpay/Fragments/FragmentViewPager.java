package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.beautyteam.everpay.Views.SlidingTabLayout;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.GET_DEBTS;

/**
 * Created by Admin on 15.03.2015.
 */
public class FragmentViewPager extends Fragment implements
        RequestCallback,
        TitleUpdater {

    private String screenName = "Главная";
    private MainActivity mainActivity;
    public static SlidingTabLayout slidingTabLayout;
    private ServiceHelper serviceHelper;
    private PageAdapter pageAdapter;

    public static FragmentViewPager getInstance() {
        FragmentViewPager fragmentViewPager = new FragmentViewPager();
        return fragmentViewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_view_pager, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).sendGoogleAnalytics(screenName);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        pageAdapter = new PageAdapter(getChildFragmentManager());
        viewPager.setAdapter(pageAdapter);

        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.dark_primary);
            }
        });
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tab_header);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
        serviceHelper.onResume();
        serviceHelper.getDebts();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateTitle();
        serviceHelper.onPause();

    }



        @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG, "DESTROY");
    }

    @Override
    public void onAttach(Activity activity) {
        mainActivity = (MainActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.MAIN);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        pageAdapter.notifyLoaded();
        if (action.equals(GET_DEBTS)) {
            if (result == Constants.Result.OK) {
            } else {
                Toast.makeText(getActivity(), "Неудалось загрузить новые данные", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
