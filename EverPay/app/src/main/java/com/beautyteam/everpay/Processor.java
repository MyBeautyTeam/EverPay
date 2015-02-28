package com.beautyteam.everpay;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Admin on 27.02.2015.
 */
public class Processor {

    public void request(Intent intent, ServiceCallback service) {
        HttpClient client = new DefaultHttpClient();
        Log.d(Constants.LOG, "Processor, request()");
        HttpGet request = new HttpGet("http://www.vogella.com");


        ContentValues cv = new ContentValues();
        cv.put(MyContentProvider.CONTACT_NAME, intent.getStringExtra("name"));
        cv.put(MyContentProvider.CONTACT_EMAIL, intent.getStringExtra("email"));
        cv.put(MyContentProvider.STATE, Constants.State.IN_PROCESS);
        //cv.put(MyContentProvider.RESULT, Constants.Result.OK);
        /*
        Если необходимо сразу добавлять запись (до отправки), а потом, если случилась ошибка,
         удалять, то в базе данных нужно сделать замену результата на дефолтное, поскольку 5 != null дает false.
        */

        //!!! КОСТЫЛЬ, ПЕРЕПРОДУМАТЬ!!!
        Uri newUri = ((IntentService)service).getContentResolver().insert(MyContentProvider.CONTACT_CONTENT_URI, cv);
        try {
            Log.d(Constants.LOG, "insert, result Uri : " + newUri.toString());

            HttpResponse response = client.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String result = "";
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            Log.d("MyProgram", "result = " + result);
            service.onRequestEnd(Constants.Result.OK, intent);

            cv = new ContentValues();
            cv.put(MyContentProvider.STATE, -1);
            cv.put(MyContentProvider.RESULT, Constants.Result.OK);
            // ТОТ ЖЕ САМЫЙ КОСТЫЛЬ
            ((IntentService)service).getContentResolver().update(newUri, cv, null, null);
        } catch (IOException e) {
            service.onRequestEnd(Constants.Result.ERROR, intent);
            cv = new ContentValues();
            cv.put(MyContentProvider.STATE, -1);
            cv.put(MyContentProvider.RESULT, Constants.Result.ERROR);
            // ТОТ ЖЕ САМЫЙ КОСТЫЛЬ
            ((IntentService)service).getContentResolver().update(newUri, cv, null, null);
        }
    }
}
