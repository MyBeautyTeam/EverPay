package com.beautyteam.everpay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.beautyteam.everpay.Adapters.DrawerAdapter;
import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Fragments.FragmentAddBill;
import com.beautyteam.everpay.Fragments.FragmentCalculation;
import com.beautyteam.everpay.Fragments.FragmentViewPager;


/**
 * Created by Admin on 07.03.2015.
 */
public class MainActivity extends ActionBarActivity {//} implements MaterialTabListener {

    ViewPager viewPager;
    PageAdapter pageAdapter;

    String TITLES[] = {"Главная" ,"Группы", "Выход", "Добавить счет"};
    int ICONS[] = {R.drawable.ic_home_white_24dp, R.drawable.ic_group_white_24dp, R.drawable.ic_exit_to_app_white_24dp, R.drawable.ic_exit_to_app_white_24dp};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Egor Rakitsky";
    String EMAIL = "Rakitsky@brazzers.com";
    int PROFILE = R.drawable.avatar;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            replaceFragment(FragmentViewPager.getInstance());
                            break;
                        case 1:

                            break;
                        case 2:
                            replaceFragment(FragmentCalculation.getInstance());
                            break;
                        case 3:
                            replaceFragment(FragmentAddBill.getInstance());
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
        FragmentTransaction fTran = getSupportFragmentManager().beginTransaction();
        fTran.replace(R.id.main_container, fragment);
        fTran.commit();
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fTran = getSupportFragmentManager().beginTransaction();
        fTran.add(R.id.main_container, fragment);
        fTran.addToBackStack(null);
        fTran.commit();
    }

}
