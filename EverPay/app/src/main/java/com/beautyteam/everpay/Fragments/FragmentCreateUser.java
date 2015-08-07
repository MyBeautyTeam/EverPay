package com.beautyteam.everpay.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.CREATE_AND_ADD_USER;
import static com.beautyteam.everpay.Constants.Action.CREATE_USER;

/**
 * Created by asus on 21.07.2015.
 */
public class FragmentCreateUser extends Fragment
    implements View.OnClickListener, TitleUpdater,
        RequestCallback{

    private Button saveBtn;
    private EditText name;
    private EditText lastname;
    private int sex;
    private ServiceHelper serviceHelper;
    ProgressDialog progressDialog;
    private static final String GROUP_ID = "GROUP_ID";
    private int groupId;

    // У тебя скорее всего будет другой getInstance!

    public static FragmentCreateUser getInstance(int groupId) {
        FragmentCreateUser fragmentCreateUser = new FragmentCreateUser();
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentCreateUser.setArguments(bundle);
        FlurryAgent.logEvent("Фрагмент создания пользователя");
        return fragmentCreateUser;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_create_user, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupId = getArguments().getInt(GROUP_ID, -1);

        name = (EditText) view.findViewById(R.id.name_create_user);
        lastname = (EditText) view.findViewById(R.id.last_name_create_user);
        saveBtn = (Button) view.findViewById(R.id.save_btn_user);
        saveBtn.setOnClickListener(this);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioSex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rdbtn = (RadioButton) radioGroup.findViewById(i);
                Log.d("пол", rdbtn.getText().toString());
                if (rdbtn.getText().toString().equals("Мужской"))
                    sex = 0;
                else
                    sex = 1;
            }
        });

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
        serviceHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn_user:
                if((!name.getText().toString().equals(""))&&(!lastname.getText().toString().equals(""))) {
                    createAndAddUser(sex, name.getText().toString(), lastname.getText().toString());// добавить созданного польщователя в группу
                }
                else
                    Toast.makeText(getActivity(), "Введите имя и фамилию", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void createAndAddUser(int sex, String nameUser, String lastNameUser){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Создаем пользователя");
        progressDialog.setCancelable(false);
        progressDialog.show();
        serviceHelper.createAndAddUser(nameUser, lastNameUser, sex, groupId);

    }

    public void createUser(int sex, String nameUser, String lastNameUser){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Создаем пользователя");
        progressDialog.setCancelable(false);
        progressDialog.show();
        serviceHelper.createUser(nameUser, lastNameUser, sex);
    }

    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.CREATE_USER);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(CREATE_AND_ADD_USER)) {

            if (result == Constants.Result.OK) {
                Toast.makeText(getActivity(), "Пользователь добавлен", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).removeFragment();
                ((MainActivity) getActivity()).removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
            }
        }
        if (action.equals(CREATE_USER)) {


            progressDialog.dismiss();
            if (result == Constants.Result.OK) {
                Toast.makeText(getActivity(), "Пользователь создан", Toast.LENGTH_SHORT).show();
                // TODO ДЛЯ ТАТЬЯНЫ
                int newUserId = data.getInt(Constants.IntentParams.USER_ID); // ID НОВОГО ПОЛЬЗОВАТЕЛЯ, ОН УЖЕ ЛЕЖИТ В БАЗЕ

                ((MainActivity) getActivity()).removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
