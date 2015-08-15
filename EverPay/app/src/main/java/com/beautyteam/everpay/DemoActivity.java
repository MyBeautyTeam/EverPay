package com.beautyteam.everpay;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.beautyteam.everpay.Adapters.DemoPageAdapter;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by popka on 12.08.15.
 */
public class DemoActivity extends android.support.v4.app.FragmentActivity {

    private DemoPageAdapter demoAdapter;
    private ViewPager viewPager;
    private SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        viewPager = (ViewPager)findViewById(R.id.demo_view_pager);

        demoAdapter = new DemoPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(demoAdapter);

        sPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

        /*TODO
        Запретить возможность прокрутки пальцем*/

        final Button btn = (Button)findViewById(R.id.demo_next_btn);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < demoAdapter.getCount() - 1)
                    btn.setText("Далее");
                else
                    btn.setText("Начать");
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                ++currentItem;
                if (currentItem < demoAdapter.getCount() - 1) {
                    btn.setText("Далее");
                    viewPager.setCurrentItem(currentItem);
                } else {
                    if (currentItem == demoAdapter.getCount() - 1) {
                        btn.setText("Начать");
                        viewPager.setCurrentItem(currentItem);
                    } else if (currentItem == demoAdapter.getCount()) {
                        sPref.edit()
                                .putBoolean(Constants.Preference.IS_DEMO_REVIEWED, true)
                                .commit();
                        finish();
                    }
                }
            }
        });

    }


}
