package com.beautyteam.everpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Fragments.FragmentEmptyToDBTest;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.Fragments.FragmentGroups;
import com.beautyteam.everpay.Fragments.FragmentLoading;
import com.beautyteam.everpay.Fragments.FragmentSettings;
import com.beautyteam.everpay.Fragments.FragmentShowBill;
import com.beautyteam.everpay.Fragments.TitleUpdater;
import com.beautyteam.everpay.REST.RequestCallback;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.vk.sdk.VKSdk;

import com.beautyteam.everpay.Adapters.DrawerAdapter;
import com.beautyteam.everpay.Fragments.FragmentViewPager;
import com.vk.sdk.VKUIHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.content.SharedPreferences.*;
import static com.beautyteam.everpay.Constants.*;
import static com.beautyteam.everpay.Constants.Action.*;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;
import static com.beautyteam.everpay.Constants.Preference.MALE;
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

    private SharedPreferences sPref;

    private ServiceHelper serviceHelper;
    private LinkedList<String> titlesQueue = new LinkedList<String>();

    private GoogleCloudMessaging gcm;
    private String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_main);
        serviceHelper = new ServiceHelper(this, this);
        serviceHelper.onResume();
        setupTracker();

        FragmentGroupDetails.downloadedGroupSet = new HashSet<Integer>();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        sPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);//PreferenceManager.getDefaultSharedPreferences(this);//getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, MODE_PRIVATE);
        boolean isFirstLaunch = sPref.getBoolean(Constants.Preference.IS_FIRST_LAUNCH, true);


        if (isFirstLaunch) {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, new FragmentLoading())
                    .commit();
            //replaceAllFragment(new FragmentLoading());
            sPref.edit()
                    .putInt(Constants.Preference.REGISTATION_STATUS, Constants.State.IN_PROCESS)
                    .commit();
            serviceHelper.initVKUsers();

        } else {
            setupDrawer();
            if (Constants.Action.NOTIFICATION.equals(getIntent().getAction())) { //Если интент пришел из нотификации
                handleNotificationIntent();
            } else { // Если запустили сами
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, FragmentViewPager.getInstance());
                fragmentTransaction.commit();
            }
        }

    }

    private void setupTracker() {
        Tracker t = ((AnalyticsApp)this.getApplication()).getTracker(AnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("Home");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        serviceHelper.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        FragmentGroups.isFirstLaunch = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(MainActivity.this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
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
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(R.anim.left_to_right, 0, 0, R.anim.right_to_left)
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit();
    }

    public void setTitle(String title) {
        this.toolbar.setTitle(title);
    }

    public void replaceAllFragment(Fragment fragment) {
        for(int i = 0; i <= fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fTran.replace(R.id.main_container, fragment);
        fTran.commit();
        //correctTitle();
    }

    public void removeFragment() {
        FragmentTransaction fTran = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate();
        fTran.commit();
        correctTitle();
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
                editor.putInt(MALE, data.getInt(Constants.IntentParams.MALE, 0));
                editor.putInt(Constants.Preference.REGISTATION_STATUS, Constants.State.ENDS);
                editor.putBoolean(Constants.Preference.IS_FIRST_LAUNCH, false);
                editor.commit();
                registerGCM();
                setupDrawer();
                //replaceFragment(FragmentViewPager.getInstance());
                fragmentManager.beginTransaction()
                        .replace(R.id.main_container, FragmentViewPager.getInstance())
                        .setCustomAnimations(R.anim.alpha_appear, R.anim.alpha_disappear)
                        .commit();

            } else {
                Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
                VKSdk.logout();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                clearData();
                sPref.edit().putInt(Constants.Preference.REGISTATION_STATUS, Constants.State.ENDS);
                this.finish();
            }
        } else if (action.equals(ADD_BILL)){
            if (result == Constants.Result.OK) {
                Toast.makeText(this, "Счет добавлен", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка соединения с интернетом. Попробуйте позже", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*    private void registerGCM() {

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        // Достаем идентификатор регистрации
        final String regId = GCMRegistrar.getRegistrationId(this);
        //GCMRegistrar.unregister(getBaseContext());
        if (regId.isEmpty()) { // Если отсутствует, то регистрируемся
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            Log.d("GCM", "Already registered: " + regId);
        }
    }
    */

    public void registerGCM(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

            }
        }.execute(null, null, null);
    }

    @Override
    public void onBackPressed() {
        try {
            correctTitle();
        } catch (Exception e){};
        super.onBackPressed();
    }

    private void correctTitle() throws ClassCastException{
        List<Fragment> list = fragmentManager.getFragments();
        int count = fragmentManager.getBackStackEntryCount();
        Fragment prevFragment;
        if (count - 1 >= 0) {
            prevFragment = list.get(count - 1);
            if (prevFragment != null)
                ((TitleUpdater) prevFragment).updateTitle();
        }
    }


    /*
    Обраобтка сообщения, полученного из Нотификации
     */
    private void handleNotificationIntent() {
        Log.e("handleNotificationIntent", "was called");
        int billId = getIntent().getExtras().getInt(Constants.IntentParams.BILL_ID, 0);
        int groupId = getIntent().getExtras().getInt(Constants.IntentParams.GROUP_ID, 0);

        replaceAllFragment(FragmentShowBill.getInstance(groupId, billId));

    }

    public void clearData() {
        sPref.edit()
            .clear()
            .commit();

        getContentResolver().delete(EverContentProvider.USERS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.GROUPS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.DEBTS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.CALCULATION_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.BILLS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, null, null);

        FragmentGroupDetails.downloadedGroupSet.clear();

    }

    public void sendGoogleAnalytics(String screenName) {
        Tracker tracker = ((AnalyticsApp)(getApplication())).getTracker(AnalyticsApp.TrackerName.APP_TRACKER);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.AppViewBuilder().build());
    }

}

