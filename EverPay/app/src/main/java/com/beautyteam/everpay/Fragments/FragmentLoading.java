package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.beautyteam.everpay.R;

/**
 * Created by Admin on 23.04.2015.
 */
public class FragmentLoading extends Fragment implements TitleUpdater {

    private TextView loadingText;
    private TextView attentionText;
    private Animation loopAppear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loadingText = (TextView) view.findViewById(R.id.loading_text);
        attentionText = (TextView) view.findViewById(R.id.loading_attention);
        loopAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_animation);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingText.startAnimation(loopAppear);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attentionText.setVisibility(View.VISIBLE);
            }
        }, 8000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attentionText.setText("Медленно =(\nНо мы очень любим тебя и не хотим, чтобы ты уходил");
            }
        }, 16000);
    }


    @Override
    public void updateTitle() {

    }
}
