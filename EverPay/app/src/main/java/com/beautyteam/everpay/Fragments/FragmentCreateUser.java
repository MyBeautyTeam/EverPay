package com.beautyteam.everpay.Fragments;

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

/**
 * Created by asus on 21.07.2015.
 */
public class FragmentCreateUser extends Fragment
    implements View.OnClickListener, TitleUpdater {

    private Button saveBtn;
    private EditText name;
    private EditText lastname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
            }
        });
//        RadioButton rdbtnFemale = (RadioButton) view.findViewById(R.id.radioFemale);
//        RadioButton rdbtnMale = (RadioButton) view.findViewById(R.id.radioMale);

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
            case R.id.save_btn_user:
                if((!name.getText().toString().equals(""))&&(!lastname.getText().toString().equals(""))) {
                    // добавить созданного польщователя в группу
                    ((MainActivity) getActivity()).removeFragment();
                    ((MainActivity) getActivity()).removeFragment();
                }
                else
                    Toast.makeText(getActivity(), "Введите имя и фамилию", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void updateTitle() {
        ((MainActivity)getActivity()).setTitle(Constants.Titles.CREATE_USER);
    }
}
