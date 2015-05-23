package com.beautyteam.everpay.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.DBHelper;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.vk.sdk.VKSdk;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by asus on 04.05.2015.
 */
public class FragmentSettings  extends Fragment
        implements View.OnClickListener, TitleUpdater {

    private String screenName="Настройки";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button quitButton = (Button) view.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(this);
        ((MainActivity)getActivity()).sendGoogleAnalytics(screenName);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_button:
                VKSdk.logout();

                ((MainActivity)getActivity()).clearData();
                getActivity().finish();

                break;
        }
    }


    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.SETTINGS);
    }
}
