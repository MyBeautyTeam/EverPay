package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.flurry.android.FlurryAgent;
import com.vk.sdk.VKSdk;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by asus on 04.05.2015.
 */
public class FragmentSettings  extends Fragment
        implements View.OnClickListener, TitleUpdater {

    private String screenName="Настройки";
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlurryAgent.logEvent("Фрагмент настройки");
        Button quitButton = (Button) view.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(this);

        Button bugReport = (Button) view.findViewById(R.id.bug_report_btn);
        bugReport.setOnClickListener(this);

        Button evaluateBtn = (Button) view.findViewById(R.id.evaluate_btn);
        evaluateBtn.setOnClickListener(this);

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

            case R.id.bug_report_btn:
                mainActivity.addFragment(FragmentBugReport.getInstance());
                break;

            case R.id.evaluate_btn:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse
                        ("https://play.google.com/store/apps/details?id=com.scimob.ninetyfour.percent"));
                startActivity(intent);
                break;
        }
    }


    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.SETTINGS);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
