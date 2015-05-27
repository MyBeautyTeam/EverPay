package com.beautyteam.everpay.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by Admin on 27.05.2015.
 */
public class PrintScreener {

    public Bitmap printscreen(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public Bitmap printscreen2(View view) {
        Bitmap bitmap;
        View v1 = view.getRootView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
