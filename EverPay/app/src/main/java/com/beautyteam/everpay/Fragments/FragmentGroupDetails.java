package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

import java.security.acl.Group;

/**
 * Created by Admin on 05.04.2015.
 */
public class FragmentGroupDetails extends Fragment implements View.OnClickListener{
    private static final String GROUP_ID = "GROUP_ID";

    private Button addBillBtn;
    private TextView discriptGroup;
    private MainActivity mainActivity;
    private int groupId;

    public static FragmentGroupDetails getInstance(int groupId) {
        FragmentGroupDetails fragmentGroupDetails = new FragmentGroupDetails();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        fragmentGroupDetails.setArguments(bundle);

        return fragmentGroupDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_group_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arg = getArguments();
        groupId = arg.getInt(GROUP_ID);
        super.onViewCreated(view, savedInstanceState);
        addBillBtn = (Button) view.findViewById(R.id.group_add_bill_btn);
        addBillBtn.setOnClickListener(this);

        discriptGroup =(TextView) view.findViewById(R.id.group_discript);
        discriptGroup.setText("Группа №" + groupId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_add_bill_btn:
                mainActivity.addFragment(FragmentAddBill.getInstance(groupId));
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
