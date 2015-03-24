package com.beautyteam.everpay.Utils;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Admin on 20.03.2015.
 */
public class AnimUtils {

    public static void animateText(TextView animatingView, int fromColor, int toColor, int duration, final int sizeFrom, final int sizeTo) {

        final TextView view = animatingView;
        final float[] from = new float[3],
                to =   new float[3];

        Color.colorToHSV(fromColor, from);   // from white
        Color.colorToHSV(toColor, to);     // to red

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
        anim.setDuration(duration);                              // for 300 ms

        final float[] hsv  = new float[3];                  // transition color
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                // Transition along each axis of HSV (hue, saturation, value)
                hsv[0] = from[0] + (to[0] - from[0])*animation.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1])*animation.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2])*animation.getAnimatedFraction();

                view.setTextColor(Color.HSVToColor(hsv));
                view.setTextSize(sizeFrom + (sizeTo-sizeFrom) * animation.getAnimatedFraction());
            }
        });

        anim.start();
    }
}
