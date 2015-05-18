package com.beautyteam.everpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.beautyteam.everpay.Fragments.FragmentEmptyToDBTest;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.Fragments.FragmentGroups;
import com.beautyteam.everpay.Fragments.FragmentLoading;
import com.beautyteam.everpay.Fragments.FragmentSettings;
import com.beautyteam.everpay.Fragments.TitleUpdater;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.vk.sdk.VKSdk;

import com.beautyteam.everpay.Adapters.DrawerAdapter;
import com.beautyteam.everpay.Fragments.FragmentViewPager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.content.SharedPreferences.*;
import static com.beautyteam.everpay.Constants.*;
import static com.beautyteam.everpay.Constants.Action.*;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;
import static com.beautyteam.everpay.Constants.Preference.USER_ID;
import static com.beautyteam.everpay.Constants.Preference.USER_ID_VK;


/**
 * Created by Admin on 07.03.2015.
 */
public class MainActivity extends ActionBarActivity
    implements RequestCallback {

    String TITLES[] = {"Главная" ,"Группы", "Настройки"};
    int ICONS[] = {R.drawable.ic_home_white_24dp, R.drawable.ic_group_white_24dp, R.drawable.ic_exit_to_app_white_24dp, R.drawable.ic_exit_to_app_white_24dp};


    final String USER_NAME = "USER_NAME";
    final String IMG_URL = "IMG_URL";
    final String EMAIL = "Да прибудет с тобой сила";

    User user;
    int PROFILE = R.drawable.avatar;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private final String IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";
    private SharedPreferences sPref;

    private ServiceHelper serviceHelper;
    private LinkedList<String> titlesQueue = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceHelper = new ServiceHelper(this, this);
        FragmentGroupDetails.downloadedGroupSet = new HashSet<Integer>();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        serviceHelper.onResume();
        sPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);//PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, MODE_PRIVATE);
        boolean isFirstLaunch = sPref.getBoolean(IS_FIRST_LAUNCH, true);
        if (isFirstLaunch) {

            replaceFragment(new FragmentLoading());
            serviceHelper.initVKUsers();

        } else {
            setupDrawer();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container,FragmentViewPager.getInstance());
            fragmentTransaction.commit();
            //replaceFragment(FragmentViewPager.getInstance());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serviceHelper.onPause();
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    private void setupDrawer(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        mRecyclerView.setHasFixedSize(true);                             // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(this,TITLES,ICONS, sPref.getString(USER_NAME, "Ваше имя"),EMAIL, sPref.getString(IMG_URL, "http://cs9591.vk.me/v9591001/74/bGqB3eciXRc.jpg"));       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    int position = recyclerView.getChildPosition(child) - 1; //Поскольку клик на картинку тоже считается
                    if (position < 0) position = 0;
                    toolbar.setTitle(TITLES[position]);
                    switch (position) {
                        case 0:
                            replaceAllFragment(FragmentViewPager.getInstance());
                            break;
                        case 1:
                            //serviceHelper.getGroups();
                            replaceAllFragment(FragmentGroups.getInstance());
                            break;
                        case 2:
                            FragmentSettings fragmentSettings = new FragmentSettings();
                            replaceAllFragment(fragmentSettings);
                            break;

                    }
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });


        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };
        // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }

    public void replaceFragment(Fragment fragment) {
        removeFragment();
        addFragment(fragment);
    }

    public void addFragment(Fragment fragment) {

        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fTran.setCustomAnimations(R.anim.left_to_right, 0, 0, R.anim.right_to_left);
        fTran.add(R.id.main_container, fragment);
        fTran.addToBackStack(null);
        fTran.commit();
    }

    public void setTitle(String title) {
        this.toolbar.setTitle(title);
    }

    public void replaceAllFragment(Fragment fragment) {

        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fTran.replace(R.id.main_container, fragment);
        fTran.commit();
    }

    public void removeFragment() {
        correctTitle();
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        //fTran.remove(fragment);
        fragmentManager.popBackStackImmediate();
        fTran.commit();
    }

    public void addCoveredFragment(Fragment fragment) {
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fTran.replace(R.id.main_container, fragment);
        fTran.addToBackStack(null);
        fTran.commit();
    }

    @Override
    public void onRequestEnd(int result, Bundle data) {
        String action = data.getString(ACTION);
        if (action.equals(INIT_VK_USERS)) {
            if (result == Constants.Result.OK) {
                Editor editor = sPref.edit();
                editor.putInt(Constants.Preference.USER_ID, data.getInt(USER_ID));
                editor.putInt(Constants.Preference.USER_ID_VK, data.getInt(USER_ID_VK));
                editor.putString(Constants.Preference.ACCESS_TOKEN, data.getString(ACCESS_TOKEN, "5"));
                editor.putString(USER_NAME, data.getString(USER_NAME, "Самый Красивый"));
                editor.putString(IMG_URL, data.getString(IMG_URL, "IMG"));
                editor.putBoolean(IS_FIRST_LAUNCH, false);
                editor.commit();
                setupDrawer();
                replaceFragment(FragmentViewPager.getInstance());
            } else {
                Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
                VKSdk.logout();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                // TODO МОЖЕТ НУЖНО ПОЧИСИТЬ БАЗУ?!
                this.finish();
            }
        } else if (action.equals(CALCULATE)){

        }
    }

    public void onBackPressed() {
        try {
            correctTitle();
        } catch (Exception e){};
        super.onBackPressed();
    }

    private void correctTitle() throws ClassCastException{
        List<Fragment> list = fragmentManager.getFragments();
        int count = fragmentManager.getBackStackEntryCount();
        Fragment prevFragment = null;
        if (count - 1 >= 0) {
            prevFragment = list.get(count - 1);
            ((TitleUpdater) prevFragment).updateTitle();
        }
    }


}

