package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Dialogs.DialogWindow;
import com.beautyteam.everpay.LoginActivity;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.flurry.android.FlurryAgent;
import com.vk.sdk.VKSdk;

import static com.beautyteam.everpay.Constants.Preference.SETTING_ADVICE;
import static com.beautyteam.everpay.Constants.Preference.SETTING_PUSH;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by asus on 04.05.2015.
 */
public class FragmentSettings  extends Fragment
        implements View.OnClickListener,
        TitleUpdater,
        CompoundButton.OnCheckedChangeListener
{

    private String screenName="Настройки";
    private MainActivity mainActivity;

    private SwitchCompat pushSwitch;
    private SwitchCompat adviceSwitch;

    private SharedPreferences sPref;
    private ProgressDialog progressDialog;
    private DialogWindow dialogWindow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sPref = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        FlurryAgent.logEvent("Фрагмент настройки");

        Button quitButton = (Button) view.findViewById(R.id.quit_button);
        quitButton.setOnClickListener(this);

        Button bugReport = (Button) view.findViewById(R.id.bug_report_btn);
        bugReport.setOnClickListener(this);

        Button evaluateBtn = (Button) view.findViewById(R.id.evaluate_btn);
        evaluateBtn.setOnClickListener(this);

        pushSwitch = (SwitchCompat) view.findViewById(R.id.setting_switch_push);
        adviceSwitch = (SwitchCompat) view.findViewById(R.id.setting_switch_advice);
        setupSwitch();

        pushSwitch.setOnCheckedChangeListener(this);
        adviceSwitch.setOnCheckedChangeListener(this);

        ((MainActivity)getActivity()).hideSoftKeyboard();


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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_button:
                showDialog();
                break;

            case R.id.bug_report_btn:
                if (sPref.getInt(FragmentBugReport.getDate(), 2) < 1 ) {
                    Toast.makeText(getActivity(), "Не больше двух запросов в день", Toast.LENGTH_SHORT).show();
                } else {
                    mainActivity.addFragment(FragmentBugReport.getInstance());
                }
                break;

            case R.id.evaluate_btn:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse
                        ("https://play.google.com/store/apps/details?id=com.beautyteam.everpay"));
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

    private void setupSwitch() {
        String regId = sPref.getString(Constants.Preference.GCM_REGID, null);

        pushSwitch.setChecked(sPref.getBoolean(Constants.Preference.SETTING_PUSH, true));
        if (regId == null)
            pushSwitch.setChecked(false);
        adviceSwitch.setChecked(sPref.getBoolean(Constants.Preference.SETTING_ADVICE, false));
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.setting_switch_push:

                sPref.edit()
                        .putBoolean(SETTING_PUSH, b)
                    .commit();
                break;
            case R.id.setting_switch_advice:
                sPref.edit()
                    .putBoolean(SETTING_ADVICE, b)
                    .commit();
                break;
        }

    }

    private void showDialog() {
        dialogWindow = new DialogWindow(getActivity(),R.layout.dialog_quit);
        dialogWindow.show();
        Window window = dialogWindow.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setOnYesClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindow.dismiss();
                FlurryAgent.logEvent("Выход");

                VKSdk.logout();

                ((MainActivity)getActivity()).clearData();

                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentLogin);
                getActivity().finish();

            }
        });
    }


}
