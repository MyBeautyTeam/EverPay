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

import static com.beautyteam.everpay.Constants.ACTION;
import static com.beautyteam.everpay.Constants.Action.ADD_USER;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_create_user, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                if (rdbtn.getText().toString() == "Мужской")
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
                    addUser(sex, name.getText().toString(), lastname.getText().toString());// добавить созданного польщователя в группу
                }
                else
                    Toast.makeText(getActivity(), "Введите имя и фамилию", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void addUser(int sex, String nameUser, String lastNameUser){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Создаем пользователя");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("dannie", sex+"");
        Log.d("dannie", nameUser);
        Log.d("dannie", lastNameUser);
        serviceHelper.createUser(nameUser, lastNameUser, sex);

    }

    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.CREATE_USER);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(ADD_USER)) {
            progressDialog.dismiss();

            if (result == Constants.Result.OK) {
                Toast.makeText(getActivity(), "Пользователь добавлен", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).removeFragment();
            } else {
                Toast.makeText(getActivity(), "Ошибка. Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
