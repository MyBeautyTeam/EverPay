package com.beautyteam.everpay.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 23.04.2015.
 */
public class FragmentLoading extends Fragment implements TitleUpdater {

    private TextView loadingText;
    private TextSwitcher attentionTextSwitcher;
    private Animation loopAppear;
    private String firstAttemp = "Загрузка идет медленно, но верно! \nНе сдавайтесь!";
    private String secondAttemp = "Медленно =(\nНо мы очень любим тебя и не хотим, чтобы ты уходил";

    private Animation in;
    private Animation out;

    private String screenName = "Загрузка";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).sendGoogleAnalytics(screenName);

        loadingText = (TextView) view.findViewById(R.id.loading_text);
        attentionTextSwitcher = (TextSwitcher) view.findViewById(R.id.loading_attention);
        attentionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(getActivity());
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(20);
                myText.setTextColor(getResources().getColor(R.color.secondary_text));
                return myText;
            }

         });

        attentionTextSwitcher.setInAnimation(in);
        attentionTextSwitcher.setOutAnimation(out);

        loopAppear = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_animation);
        in = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_appear);
        out = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_disappear);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingText.startAnimation(loopAppear);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attentionTextSwitcher.setText(firstAttemp);
            }
        }, 15000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                attentionTextSwitcher.setText(secondAttemp);
            }
        }, 30000);
    }


    @Override
    public void updateTitle() {

    }
}
