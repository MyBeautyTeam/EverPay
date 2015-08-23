package com.beautyteam.everpay.GCM;

/**
 * Created by Admin on 18.05.2015.
 */
import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.LoginActivity;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

public class GcmMessageHandler extends IntentService {

    String mes;
    NotificationManager nm;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }
    SharedPreferences sPref;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);

    }
    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();
        if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {

        } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
            if (sPref.getBoolean(Constants.Preference.SETTING_PUSH, true)) {
                String msg = intent.getStringExtra("text");
                int actionId = Integer.parseInt(intent.getStringExtra("action"));
                int groupId = Integer.parseInt(intent.getStringExtra("groups_id"));
                String billidStr = intent.getStringExtra("bills_id");
                int billId = 0;
                if (billidStr != null)
                    billId = Integer.parseInt(billidStr);
                switchOnScreen();

                sendNotif("EverPay", msg, actionId, groupId, billId);
            }
        }


        GcmBroadcastReceiver.completeWakefulIntent(intent);

        //sendNotif("Everpay", "asdasdas sadsd adas asdask jsfl ПАША ОЧЕНЬ ХОРОШИЙ МАЛЬЧИК!!! ЫВФЫДВ ООООЧЕНЬ!", 1,demo2,demo3);

    }

    void sendNotif(String title, String message, int action, int groupId, int billId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setAutoCancel(true);

        Intent intent = new Intent(this, LoginActivity.class);

        intent.setAction(Constants.Action.NOTIFICATION);
        intent.putExtra(Constants.IntentParams.ACTION_NOTIF, action);
        intent.putExtra(Constants.IntentParams.BILL_ID, billId);
        intent.putExtra(Constants.IntentParams.GROUP_ID, groupId);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }


    private void switchOnScreen() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on............", ""+isScreenOn);

        if(isScreenOn==false) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
    }


}