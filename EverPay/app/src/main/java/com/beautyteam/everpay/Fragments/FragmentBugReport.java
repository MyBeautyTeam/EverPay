package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;

import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.BUG_REPORT;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;


/**
 * Created by Admin on 04.08.2015.
 */
public class FragmentBugReport extends Fragment implements
        View.OnClickListener, RequestCallback {

    private RadioGroup radioGroup;
    private EditText editText;
    private Button sendBtn;
    private MainActivity mainActivity;

    private ProgressDialog progressDialog;
    private ServiceHelper serviceHelper;

    private SharedPreferences sPref;

    public static FragmentBugReport getInstance() {
        return new FragmentBugReport();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_bug_report, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlurryAgent.logEvent("Связаться с разработчиками");

        sPref = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        radioGroup = (RadioGroup) view.findViewById(R.id.bug_radio_group);
        editText = (EditText) view.findViewById(R.id.bug_message);
        sendBtn = (Button) view.findViewById(R.id.bug_send_btn);

        sendBtn.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        serviceHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }


    @Override
    public void onClick(View view) {

        String theme = "";
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        if (radioButtonID > 0) {
            View radioButton = radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);

            switch (idx) {
                case 0:
                    theme = "Everpay.Ошибка";
                    break;
                case 1:
                    theme = "Everpay.Предложение";
                    break;
                case 2:
                    theme = "Everpay.Другое";
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "Выберите тему письма",Toast.LENGTH_SHORT).show();
            return;
        }

        String message = editText.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(getActivity(), "Сообщение пусто",Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (message.length() < 20) {
                Toast.makeText(getActivity(), "Слишком короткое сообщение",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Отправление");
        progressDialog.show();

        serviceHelper.sendBugReport(theme, message);
    }


    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        progressDialog.dismiss();
        if (action.equals(BUG_REPORT)) {

            if (result == Constants.Result.OK) {
                Toast.makeText(getActivity(), "Успешно отправлено. Спасибо!", Toast.LENGTH_SHORT).show();

                int countOfReportToday = sPref.getInt(getDate(), 2);
                countOfReportToday--;
                sPref.edit()
                    .putInt(getDate(), countOfReportToday)
                    .commit();

                mainActivity.removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка отправки. Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getDate() {
        Date cDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return date;
    }



}
