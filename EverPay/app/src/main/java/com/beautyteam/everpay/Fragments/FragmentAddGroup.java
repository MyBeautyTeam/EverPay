package com.beautyteam.everpay.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.AddGroupAdapter;
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.beautyteam.everpay.User;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;

/**
 * Created by asus on 16.03.2015.
 */
public class FragmentAddGroup extends Fragment
        implements
        View.OnClickListener,
            TitleUpdater,
        RequestCallback {
    private Toolbar toolbar;
    private Button addBtn;
    private Button saveBtn;
    private Fragment self;
    private MainActivity mainActivity;
    private ArrayList<User> arrayList = new ArrayList<User>();
    private ListView friendsList;
    private AddGroupAdapter mAdapter;
    private EditText groupName;

    private ProgressDialog progressDialog;
    private ServiceHelper serviceHelper;
    private String screenName = "Добавление группы";

    public static FragmentAddGroup getInstance() {
        FragmentAddGroup fragmentAddGroup = new FragmentAddGroup();
        return fragmentAddGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        serviceHelper = new ServiceHelper(getActivity(), this);
        return inflater.inflate(R.layout.fragment_add_group, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlurryAgent.logEvent("Фрагмент добавления группы");
        friendsList = (ListView) view.findViewById(R.id.add_group_friends_list);
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        View footerView = inflater.inflate(R.layout.footer_add_friend, null);
        addBtn = (Button) footerView.findViewById(R.id.add_btn_friend_foot);
        groupName = (EditText) view.findViewById(R.id.group_name);
        friendsList.addFooterView(footerView);
        self = this;
        saveBtn = (Button) view.findViewById(R.id.save_btn_group);
        saveBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        SharedPreferences sPref = getActivity().getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);
        User user  = new User(sPref.getInt(Constants.Preference.USER_ID, 0),
                sPref.getInt(Constants.Preference.USER_ID_VK, 0),
                sPref.getString(Constants.Preference.USER_NAME,""), "",
                sPref.getString(Constants.Preference.IMG_URL,"") );
        mAdapter = new AddGroupAdapter(getActivity(), arrayList, user);
        friendsList.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
                FragmentAddFriendsToGroup frag= FragmentAddFriendsToGroup.getInstance(arrayList);
                mainActivity.addFragment(frag);
                break;
            case R.id.save_btn_group:
                Log.d("button", "push button save group");
                if (isCorrectData()) {
                    int groupId = insertToDB();
                    serviceHelper.addGroup(groupId);
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Создаем группу");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                break;
        }
    }

    private boolean isCorrectData() {
        String title = groupName.getText().toString();
        if(!title.equals("")) {
            Log.d("groupname", title.toString());
            if (arrayList.size()> 1) {
                Log.d("groupsize", String.valueOf(arrayList.size()));
                return true;
            }  else {
                Toast.makeText(getActivity(), "Слишком мало участников. Добавьте участников", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getActivity(), "Введите название группы",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private int insertToDB() {
        String title = groupName.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Groups.TITLE, title);
        cv.put(Groups.STATE, Constants.State.IN_PROCESS);
        cv.put(Groups.IS_CALCULATED, 0);
        Uri uri = getActivity().getContentResolver().insert(EverContentProvider.GROUPS_CONTENT_URI, cv);

        String groupId = uri.getLastPathSegment();
        for (int i=0; i<arrayList.size(); i++) {
            User friend = arrayList.get(i);
            cv = new ContentValues();
            cv.put(GroupMembers.GROUP_ID, groupId);
            cv.put(GroupMembers.USER_ID, friend.getId());
            cv.put(GroupMembers.USER_ID_VK, friend.getId_vk());
            cv.put(GroupMembers.USER_NAME, friend.getLast_name() + " " + friend.getName());
            cv.put(GroupMembers.STATE, Constants.State.IN_PROCESS);
            getActivity().getContentResolver().insert(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, cv);
        }

        return Integer.parseInt(groupId);
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
        updateTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }

    @Override
    public void updateTitle() {
        ((MainActivity) getActivity()).setTitle(Constants.Titles.ADD_GROUP);
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        progressDialog.dismiss();
        if (result == Constants.Result.OK) {
            int groupId = data.getInt(Constants.IntentParams.GROUP_ID);
            String groupTitle = data.getString(Constants.IntentParams.GROUP_TITLE);
            FragmentGroupDetails fragmentGroupDetails = FragmentGroupDetails.getInstance(groupId);
            mainActivity.replaceFragment(fragmentGroupDetails);
        } else {
            Toast.makeText(getActivity(), "Ошибка соединения с интернетом", Toast.LENGTH_SHORT).show();
        }
    }
}
