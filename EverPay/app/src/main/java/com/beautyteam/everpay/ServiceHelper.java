package com.beautyteam.everpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Admin on 27.02.2015.
 */
public class ServiceHelper implements AppResultsReceiver.Receiver {

    private Activity activity;
    private AppResultsReceiver mReceiver;
    private ActivityCallback activityCallback;

    public ServiceHelper(Activity _activity, ActivityCallback _activityCallback) {
        this.activity = _activity; // Подумать, может быть утечка памяти, если делать serviceHelper Singleton
        this.activityCallback = _activityCallback;
    }

    public void resumeBind() {
        mReceiver = new AppResultsReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    public void pauseBind() {
        mReceiver.setReceiver(null);
    }

    public void send(String name, String email) {
        Intent intentService = new Intent(activity, Service.class);
        intentService.setAction(Constants.Action.ADD_CONTACT);
        intentService.putExtra("name", name);
        intentService.putExtra("email", email);
        intentService.putExtra(Constants.RECEIVER, mReceiver);
        activity.startService(intentService);
        Log.d(Constants.LOG, "ServiceHelper, send()");
    }

    // Обработка результата из сервиса
    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        Log.d(Constants.LOG, "ServiceHelper, onReceiveResult()");
        switch (resultCode) {
            case Constants.Result.OK:
                activityCallback.onSuccess();
                break;
            case Constants.Result.ERROR:
                activityCallback.onError();
        }

    }
}
