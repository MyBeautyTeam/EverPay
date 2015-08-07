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
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

public class GcmMessageHandler extends IntentService {

    String mes;
    NotificationManager nm;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }
    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();
        if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {

        } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
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


        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }


    void sendNotif(String title, String message, int action, int groupId, int billId) {
        // 1-я часть
        Notification notification = new Notification(R.drawable.ic_launcher, message,
                System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, LoginActivity.class);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
        intent.setAction(Constants.Action.NOTIFICATION);
        intent.putExtra(Constants.IntentParams.ACTION_NOTIF, action);
        intent.putExtra(Constants.IntentParams.BILL_ID, billId);
        intent.putExtra(Constants.IntentParams.GROUP_ID, groupId);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 2-я часть
        notification.setLatestEventInfo(this, title, message, pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        // отправляем
        nm.notify(1, notification);

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

