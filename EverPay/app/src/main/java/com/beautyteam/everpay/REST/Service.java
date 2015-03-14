package com.beautyteam.everpay.REST;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.beautyteam.everpay.Constants;

/**
 * Created by Admin on 27.02.2015.
 */
public class Service extends IntentService implements ServiceCallback {

    public Service() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.LOG, "Service, onHandleIntent()");
        Processor processor = new Processor();
        processor.request(intent, this);
    }


    @Override
    public void onRequestEnd(int resultCode, Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        receiver.send(resultCode, Bundle.EMPTY);
    }
}
