package com.beautyteam.everpay;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.beautyteam.everpay.Constants.Action.*;

/**
 * Created by Admin on 27.02.2015.
 */
public class Processor {

    public void request(Intent intent, Service service) {
        Log.d(Constants.LOG, "Processor, request()");
        String action = intent.getAction();
        int result = -999; // Должно быть изменено. Написал, чтобы не ругалась IDE


        if (ADD_CONTACT.equals(action)) {
            ContentValues cv = new ContentValues();
            cv.put(MyContentProvider.CONTACT_NAME, intent.getStringExtra("name"));
            cv.put(MyContentProvider.CONTACT_EMAIL, intent.getStringExtra("email"));
            cv.put(MyContentProvider.STATE, Constants.State.IN_PROCESS);
            Uri uriOfInsertedRow = service.getContentResolver().insert(MyContentProvider.CONTACT_CONTENT_URI, cv);

            cv = new ContentValues();
            if (get("http://ya.ru") != null) {
                result = Constants.Result.OK;
                cv.put(MyContentProvider.STATE, Constants.State.ENDS);
                cv.put(MyContentProvider.RESULT, Constants.Result.OK);
            } else {
                result = Constants.Result.ERROR;
                cv.put(MyContentProvider.STATE, Constants.State.ENDS);
                cv.put(MyContentProvider.RESULT, Constants.Result.ERROR);
            }
            service.getContentResolver().update(uriOfInsertedRow, cv, null, null);
        }
        else if (ANY_ACTION_WITH_POST.equals(action)) {
            ContentValues cv = new ContentValues();
            cv.put(MyContentProvider.STATE, Constants.State.IN_PROCESS);
            Uri uriOfInsertedRow = service.getContentResolver().insert(MyContentProvider.CONTACT_CONTENT_URI, cv);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("login", "p02p"));
            nameValuePairs.add(new BasicNameValuePair("passwd", "123456789"));

            if (post("http://url.ru", nameValuePairs) != null) {
                result = Constants.Result.OK;
                cv.put(MyContentProvider.STATE, Constants.State.ENDS);
                cv.put(MyContentProvider.RESULT, Constants.Result.OK);
            } else {
                result = Constants.Result.ERROR;
                cv.put(MyContentProvider.STATE, Constants.State.ENDS);
                cv.put(MyContentProvider.RESULT, Constants.Result.ERROR);
            }
            service.getContentResolver().update(uriOfInsertedRow, cv, null, null);
        }

        service.onRequestEnd(result, intent);
    }

    private String get(String url) {
        Log.d(Constants.LOG, "Processor, request()");

        HttpClient httpClient = new DefaultHttpClient(); // должен быть один в классе или несколько???
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String result = "";
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            Log.d("MyProgram", "result = " + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    private String post(String url, List<NameValuePair> nameValuePairs) {
        // Возможно нужен всего один client - член класса
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String result = "";
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            return null;
        }
    }

}
