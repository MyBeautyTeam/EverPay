package com.beautyteam.everpay.REST;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.MyContentProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
        else if (DOWNLOAD_IMG.equals(action)) {
            String url = intent.getStringExtra(Constants.IntentParams.URL);
            String name = intent.getStringExtra(Constants.IntentParams.NAME);
            result = downloadAndSaveBitmap(url, name);
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

    private Bitmap downloadImg(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            return bitmap;
        } else {
            throw new IOException("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
    }

    private void saveBitmapAtDisk(Bitmap bmp, String name) throws IOException{
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constants.FILE_DIRECTORY;
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, name + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
    }

    private int downloadAndSaveBitmap (String url, String name) {
        try {
            Bitmap bitmap = downloadImg(url);
            saveBitmapAtDisk(bitmap, name);
        } catch (IOException e) {
            return Constants.Result.ERROR;
        }
        return Constants.Result.OK;
    }

}
