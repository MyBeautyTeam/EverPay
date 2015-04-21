package com.beautyteam.everpay;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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

import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.Fragments.FragmentEmptyToDBTest;
import com.beautyteam.everpay.Fragments.FragmentGroups;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.ArrayList;
import java.util.List;

import com.beautyteam.everpay.Adapters.DrawerAdapter;
import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Fragments.FragmentAddBill;
import com.beautyteam.everpay.Fragments.FragmentCalculation;
import com.beautyteam.everpay.Fragments.FragmentViewPager;

import java.util.Iterator;
import java.util.Random;


/**
 * Created by Admin on 07.03.2015.
 */
public class MainActivity extends ActionBarActivity {//} implements MaterialTabListener {


    String TITLES[] = {"Главная" ,"Группы", "Выход"};
    int ICONS[] = {R.drawable.ic_home_white_24dp, R.drawable.ic_group_white_24dp, R.drawable.ic_exit_to_app_white_24dp, R.drawable.ic_exit_to_app_white_24dp};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Alexander Gornii";
    String EMAIL = "gornii@mail.ru";
    int PROFILE = R.drawable.avatar;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startLoading();
        //setupViewPager(); // ViewPager
        setupDrawer();
        replaceFragment(FragmentViewPager.getInstance());

    }

    private void setupDrawer(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                             // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
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
                            replaceAllFragment(FragmentGroups.getInstance());
                            break;
                        case 2:
                            finish();
                            break;
                        /*
                        case 3:
                            replaceAllFragment(FragmentEmptyToDBTest.getInstance());
                            break;
                         */
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
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        //fTran.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
        fTran.replace(R.id.main_container, fragment);
        fTran.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fTran.replace(R.id.main_container, fragment);
        fTran.addToBackStack(null);
        fTran.commit();
    }

    public void startLoading() {
        VKRequest request1 = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100"));
        VKRequest request2 = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100"));
        VKBatchRequest batch = new VKBatchRequest(request1, request2);
        batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                Log.d("VkDemoApp", "onComplete " + responses);

                VKApiUserFull userFull = ((VKList<VKApiUserFull>) responses[0].parsedModel).get(0);
                User user = new User(userFull.id, userFull.first_name, userFull.last_name, userFull.photo_100);

                Log.d("vksdk", responses[1].parsedModel.toString());
                VKUsersArray usersArray = (VKUsersArray) responses[1].parsedModel;

                ContentValues cv = new ContentValues();
                for (VKApiUserFull friends : usersArray) {
                    cv.put(Users.USER_ID_VK, friends.id);
                    cv.put(Users.NAME, friends.last_name+ " " +friends.first_name);
                    cv.put(Users.IMG, friends.photo_100);
                    getContentResolver().insert(EverContentProvider.USERS_CONTENT_URI, cv);

                    if (new Random().nextFloat() > 0.95) {
                        ContentValues wq = new ContentValues();
                        wq.put(Debts.SUMMA, new Random().nextInt(500));
                        wq.put(Debts.USER_ID, friends.id);
                        wq.put(Debts.USER_NAME, friends.last_name+ " " +friends.first_name);
                        wq.put(Debts.GROUP_TITLE, "МОЯ ГРУППА");
                        wq.put(Debts.IS_I_DEBT, new Random().nextBoolean()? 1:0);
                        getContentResolver().insert(EverContentProvider.DEBTS_CONTENT_URI, wq);
                    }
                }


            }


            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }
        });
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

}
