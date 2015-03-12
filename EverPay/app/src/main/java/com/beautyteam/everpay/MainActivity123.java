package com.beautyteam.everpay;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.PageAdapter;
import com.beautyteam.everpay.Views.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import it.neokree.materialtabs.MaterialTab;
//import it.neokree.materialtabs.MaterialTabHost;
//import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Admin on 07.03.2015.
 */
public class MainActivity123 extends ActionBarActivity {//} implements MaterialTabListener {

    ViewPager viewPager;
    PageAdapter pageAdapter;
//    MaterialTabHost tabHost;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acrivity123);

        setupViewPager(); // ViewPager
        setupDrawer(); // DRAWER
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Pass the event to ActionBarDrawerToggle, if it returns
// true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
// Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

// Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);

        }
    }

   private void setupDrawer() {

       final String ATTRIBUTE_NAME_TEXT = "text";
       final String ATTRIBUTE_NAME_IMAGE = "image";

       mTitle = "EverPay";

       mPlanetTitles = new String[]{"Главная", "Группы", "Выход"};
       int img[] = {R.drawable.ic_home_white_18dp, R.drawable.ic_group_white_18dp, R.drawable.ic_exit_to_app_white_18dp};

       ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
               mPlanetTitles.length);
       Map<String, Object> m;
       for (int i = 0; i < mPlanetTitles.length; i++) {
           m = new HashMap<String, Object>();
           m.put(ATTRIBUTE_NAME_TEXT, mPlanetTitles[i]);
           m.put(ATTRIBUTE_NAME_IMAGE, img[i]);
           data.add(m);
       }

       // массив имен атрибутов, из которых будут читаться данные
       String[] from = { ATTRIBUTE_NAME_TEXT,ATTRIBUTE_NAME_IMAGE };
       // массив ID View-компонентов, в которые будут вставлять данные
       int[] to = { R.id.drawerText, R.id.drawerImageView};

       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       mDrawerList = (ListView) findViewById(R.id.left_drawer);

       mDrawerList.setBackgroundColor(getResources().getColor(R.color.drawer_background));

       //mDrawerList.setAdapter(new ArrayAdapter<String>(this,
         //      R.layout.drawer_item, mPlanetTitles));
       SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.drawer_item,
               from, to);

       mDrawerList.setAdapter(sAdapter);


       mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

       mDrawerToggle = new ActionBarDrawerToggle(
               this, /* host Activity */
               mDrawerLayout, /* DrawerLayout object */
               R.drawable.ic_menu_white_18dp, /* nav drawer icon to replace 'Up' caret */
               R.string.drawer_open, /* "open drawer" description */
               R.string.drawer_close /* "close drawer" description */
       ) {

           /** Called when a drawer has settled in a completely closed state. */
           public void onDrawerClosed(View view) {
               getSupportActionBar().setTitle(mTitle);
           }

           /** Called when a drawer has settled in a completely open state. */
           public void onDrawerOpened(View drawerView) {
               getSupportActionBar().setTitle("ФАК");
               invalidateOptionsMenu();
           }
       };

       // Set the drawer toggle as the DrawerListener
       mDrawerLayout.setDrawerListener(mDrawerToggle);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);
   }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.light_blue_800);
            }
        });
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tabHeader);
        slidingTabLayout.setViewPager(viewPager);

    }
}
