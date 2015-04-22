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

/**
 * Created by Admin on 05.04.2015.
 */
public class FragmentGroupDetails extends Fragment implements View.OnClickListener{
    private static final String GROUP_ID = "GROUP_ID";
    private static final String GROUP_TITLE = "GROUP_TITLE";

    private Button addBillBtn;
    private Button calcBtn;
    private Button showBillBtn;
    private TextView discriptGroup;
    private MainActivity mainActivity;
    private int groupId;
    private String groupTitle;

    public static FragmentGroupDetails getInstance(int groupId, String groupTitle) {
        FragmentGroupDetails fragmentGroupDetails = new FragmentGroupDetails();

        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ID, groupId);
        bundle.putString(GROUP_TITLE, groupTitle);
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
        groupTitle = arg.getString(GROUP_TITLE);
        super.onViewCreated(view, savedInstanceState);

        addBillBtn = (Button) view.findViewById(R.id.group_add_bill_btn);
        addBillBtn.setOnClickListener(this);

        calcBtn = (Button) view.findViewById(R.id.group_calc_btn);
        calcBtn.setOnClickListener(this);

        showBillBtn = (Button) view.findViewById(R.id.group_show_bill_btn);
        showBillBtn.setOnClickListener(this);

        discriptGroup =(TextView) view.findViewById(R.id.group_discript);
        discriptGroup.setText(groupTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_add_bill_btn:
                mainActivity.addFragment(FragmentAddBill.getInstance(groupId));
                break;
            case R.id.group_calc_btn:
                mainActivity.addFragment(FragmentCalculation.getInstance(groupId));
                break;
            case R.id.group_show_bill_btn:
                mainActivity.addFragment(FragmentShowBill.getInstance(groupId, groupId)); // Потом заменить на номер счета!
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle(groupTitle);
    }
}
