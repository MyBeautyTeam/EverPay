package com.beautyteam.everpay.GCM;

/**
 * Created by Admin on 18.05.2015.
 */
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        //showToast();
        sendNotif();
        Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

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
        notification.setLatestEventInfo(this, "EverPay", "Notification's text", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // отправляем
        nm.notify(1, notification);
    }

}
