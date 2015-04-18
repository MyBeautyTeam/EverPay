package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.beautyteam.everpay.Adapters.AddGroupAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;

import java.util.ArrayList;

/**
 * Created by asus on 16.03.2015.
 */
public class FragmentAddGroup extends Fragment
        implements View.OnClickListener {
    private Toolbar toolbar;
    private Button addBtn;
    private Button saveBtn;
    private Fragment self;
    private MainActivity mainActivity;
    private ArrayList<User> arrayList = new ArrayList<User>();
    private ListView friendsList;
    private AddGroupAdapter mAdapter;

    public static FragmentAddGroup getInstance() {
        FragmentAddGroup fragmentAddGroup = new FragmentAddGroup();
        return fragmentAddGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_group, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendsList = (ListView) view.findViewById(R.id.add_group_friends_list);
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View footerView = inflater.inflate(R.layout.footer_add_friend, null);
        addBtn = (Button) footerView.findViewById(R.id.add_btn_friend_foot);
        friendsList.addFooterView(footerView);
        self = this;
        saveBtn = (Button) view.findViewById(R.id.save_btn_group);
        addBtn.setOnClickListener(this);
        mAdapter = new AddGroupAdapter(getActivity(), arrayList);
        friendsList.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn_friend_foot:
                FragmentAddFriends frag= FragmentAddFriends.getInstance(arrayList);
                mainActivity.addFragment(frag);
                break;
            case R.id.save_btn_group:
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
        ((MainActivity) getActivity()).setTitle(Constants.Titles.ADD_GROUP);
    }
}
