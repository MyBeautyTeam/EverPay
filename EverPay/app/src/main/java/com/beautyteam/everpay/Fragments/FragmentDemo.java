package com.beautyteam.everpay.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beautyteam.everpay.R;
import com.beautyteam.everpay.REST.ServiceHelper;
import com.flurry.android.FlurryAgent;
import com.squareup.picasso.Picasso;

/**
 * Created by popka on 12.08.15.
 */
public class FragmentDemo extends Fragment {
    final static String INDEX = "INDEX";
    ImageView imageView;
    private int index;

    public static FragmentDemo getInstance(int index) {
        FragmentDemo fragmentDemo = new FragmentDemo();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragmentDemo.setArguments(bundle);

        FlurryAgent.logEvent("Демонстрация " + index);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        index = getArguments().getInt(INDEX);
        return inflater.inflate(R.layout.fragment_demo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        imageView = (ImageView) view.findViewById(R.id.demo_image);
        //mageView.setScaleType(ImageView.ScaleType.FIT_XY);


        /*switch (index) {
            case 0:
                //        imageView.setImageDrawable(resources.getDrawable(R.drawable.demo2));
                Picasso.with(getActivity())
                        .load(R.drawable.demo)
                        .error(getResources().getDrawable(R.drawable.demo))
                        .into(imageView);
                //imageView.setImageResource(R.drawable.demo2);
                break;
            case 1:
                Picasso.with(getActivity())
                        .load(R.drawable.demo2)
                        .error(getResources().getDrawable(R.drawable.demo2))
                        .into(imageView);
                break;
            case 2:
                Picasso.with(getActivity())
                        .load(R.drawable.demo3)
                        .error(getResources().getDrawable(R.drawable.demo3))
                        .into(imageView);
                break;
            case 3:
                Picasso.with(getActivity())
                        .load(R.drawable.demo4)
                        .error(getResources().getDrawable(R.drawable.demo4))
                        .into(imageView);
                break;

            case 4:
                Picasso.with(getActivity())
                        .load(R.drawable.demo5)
                        .error(getResources().getDrawable(R.drawable.demo5))
                        .into(imageView);
                break;

            case 5:
                Picasso.with(getActivity())
                        .load(R.drawable.demo6)
                        .error(getResources().getDrawable(R.drawable.demo6))
                        .into(imageView);
                break;




        }*/
    }
}

