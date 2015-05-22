package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.AddFriendsToBillAdapter;
import com.beautyteam.everpay.Adapters.BillListItem;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

import java.util.ArrayList;

/**
 * Created by Admin on 06.04.2015.
 */
public class FragmentAddFriendToBill extends Fragment implements TitleUpdater{

    private ArrayList<BillListItem> billArrayList;
    private ListView friendsList;
    AddFriendsToBillAdapter mAdapter;

    private final static String ARRAY_LIST = "ARRAY_LIST";
    public static FragmentAddFriendToBill getInstance(ArrayList<BillListItem> billListItems) {
        FragmentAddFriendToBill fragmentAddFriendToBill = new FragmentAddFriendToBill();

        Bundle arg = new Bundle();
        arg.putParcelableArrayList(ARRAY_LIST, billListItems);
        fragmentAddFriendToBill.setArguments(arg);

        return fragmentAddFriendToBill;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_friends_to_bill, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        billArrayList = getArguments().getParcelableArrayList(ARRAY_LIST);
        friendsList = (ListView) view.findViewById(R.id.add_friends_to_bill_list);

        mAdapter = new AddFriendsToBillAdapter(getActivity(), billArrayList);
        friendsList.setAdapter(mAdapter);

        final Fragment self = this;
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.billDeletedArrayList.get(i).isRemoved = false;
                ((MainActivity)getActivity()).removeFragment();
                /*getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .hide(self)
                        .commit();*/
            }
        });
    }

    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.ADD_BILL);
    }
}
