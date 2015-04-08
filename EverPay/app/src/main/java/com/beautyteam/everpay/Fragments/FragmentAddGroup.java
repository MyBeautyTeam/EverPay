package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by asus on 16.03.2015.
 */
public class FragmentAddGroup extends Fragment
        implements View.OnClickListener {
    private Toolbar toolbar;
    private Button addBtn;
    private Fragment self;
    private MainActivity mainActivity;

    public static FragmentAddGroup getInstance() {
        FragmentAddGroup fragmentAddGroup = new FragmentAddGroup();
        return fragmentAddGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         setHasOptionsMenu(true);
        //   getLoaderManager().initLoader(LOADER_ID_I_DEBT, null, this);
        return inflater.inflate(R.layout.fragment_add_group, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        self = this;
        addBtn = (Button) view.findViewById(R.id.add_btn_friend);
        addBtn.setOnClickListener(this);

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
            case R.id.add_btn_friend:
                FragmentAddFriends frag= FragmentAddFriends.getInstance();
                mainActivity.addFragment(frag);
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
