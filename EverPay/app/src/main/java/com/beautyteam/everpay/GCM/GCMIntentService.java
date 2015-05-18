package com.beautyteam.everpay.GCM;

/**
 * Created by Admin on 18.05.2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.google.android.gcm.GCMBaseIntentService;
/*
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    NotificationManager nm;


    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public GCMIntentService() {
        super(Constants.SENDER_ID);
    }


    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered #" + registrationId);

        // Здесь мы должны отправить registrationId на наш сервер, чтобы он смог на него отправлять уведомления
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");

    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received new message");
        sendNotif();
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    void sendNotif() {
        // 1-я часть
        Notification notification = new Notification(R.drawable.ic_launcher, "Text in status bar",
                System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("INFA", "somefile");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть
        notification.setLatestEventInfo(this, "SmartKettle", "Notification's text", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // отправляем
        nm.notify(1, notification);
    }

}
*/
