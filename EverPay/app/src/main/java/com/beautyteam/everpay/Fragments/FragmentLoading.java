package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.beautyteam.everpay.R;

/**
 * Created by Admin on 23.04.2015.
 */
public class FragmentLoading extends Fragment {

    private TextView loadingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loadingText = (TextView) view.findViewById(R.id.loading_text);

        Animation loopAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_animation);

        loadingText.startAnimation(loopAppear);

    }


}
