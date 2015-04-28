package com.beautyteam.everpay.REST;

import android.content.ContentValues;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.beautyteam.everpay.Constants.Action.*;
import static com.beautyteam.everpay.Constants.Preference.*;

/**
 * Created by Admin on 27.02.2015.
 */
public class Processor {

    public void request(Intent intent, Service service) {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        Log.d(Constants.LOG, "Processor, request()");
        String action = intent.getAction();
        int result = -999; // Должно быть изменено. Написал, чтобы не ругалась IDE

        if (INIT_VK_USERS.equals(action)) {
            initVKUsers(service, intent);
        }
        else if (GET_GROUPS.equals(action)) {
            params.add(new BasicNameValuePair("users_id", "8"));
            params.add(new BasicNameValuePair("access_token", "wjekwewue"));
            String response = get(Constants.URL.GET_GROUPS, params);
            if (response != null) {
                if (response.contains("200")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("response");
                        jsonObject = jsonObject.getJSONObject("groups");

                        ContentValues cv;
                        JSONObject group;
                        for (int i=0; (group=jsonObject.getJSONObject(i + "")) != null; i++) {
                            cv = new ContentValues();
                            cv.put(Groups.GROUP_ID, group.getString("groups_id"));
                            cv.put(Groups.TITLE, group.getString("title"));
                            //cv.put(Groups.UPDATE_TIME, group.getString("update_datetime"));
                            if (group.getBoolean("is_calculated"))
                                cv.put(Groups.IS_CALCULATED, 1);
                            else
                                cv.put(Groups.IS_CALCULATED, 0);
                            service.getContentResolver().insert(EverContentProvider.GROUPS_CONTENT_URI, cv);
                        }
                        service.onRequestEnd(Constants.Result.OK, intent);
                    } catch (JSONException e) {
                        service.onRequestEnd(Constants.Result.ERROR, intent);
                    }
                }

            } else {

            }
        }
        else if (CALCULATE.equals(action)) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e){};
            service.onRequestEnd(Constants.Result.OK, intent);
        }
        else if (ADD_CONTACT.equals(action)) {
            /*ContentValues cv = new ContentValues();
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
            service.getContentResolver().update(uriOfInsertedRow, cv, null, null);*/
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
        //service.onRequestEnd(result, intent);
    }

    private String get(String stringUrl, LinkedList<NameValuePair> params) {

        Log.d(Constants.LOG, "Processor, request()");

        String paramString = URLEncodedUtils.format(params, "utf-8");
        if(!stringUrl.endsWith("?"))
            stringUrl += "?";
        stringUrl += paramString;

        HttpClient httpClient = new DefaultHttpClient(); // должен быть один в классе или несколько???
        HttpGet request = new HttpGet(stringUrl);

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


    private void initVKUsers(final Service service, final Intent intent) {

        final VKRequest request1 = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100, sex"));
        VKRequest request2 = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100, sex"));

        VKBatchRequest batch = new VKBatchRequest(request1, request2);
        batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                Log.d("VkDemoApp", "onComplete " + responses);


                VKApiUserFull userFull = ((VKList<VKApiUserFull>) responses[0].parsedModel).get(0);
                //user = new User(userFull.id, userFull.first_name, userFull.last_name, userFull.photo_100);

                intent.putExtra(USER_NAME, userFull.last_name + " " + userFull.first_name);
                intent.putExtra(IMG_URL, userFull.photo_100);

                Log.d("vksdk", responses[1].parsedModel.toString());
                VKUsersArray usersArray = (VKUsersArray) responses[1].parsedModel;


                ContentValues cv = new ContentValues();
                for (VKApiUserFull friends : usersArray) {
                    cv.put(Users.USER_ID_VK, friends.id);
                    cv.put(Users.NAME, friends.last_name+ " " +friends.first_name);
                    cv.put(Users.IMG, friends.photo_100);
                    service.getContentResolver().insert(EverContentProvider.USERS_CONTENT_URI, cv);

                    if (new Random().nextFloat() > 0.98) {
                        ContentValues wq = new ContentValues();
                        wq.put(Debts.SUMMA, new Random().nextInt(500));
                        wq.put(Debts.USER_ID, friends.id);
                        wq.put(Debts.USER_NAME, friends.last_name+ " " +friends.first_name);
                        wq.put(Debts.GROUP_TITLE, "МОЯ ГРУППА");
                        wq.put(Debts.IS_I_DEBT, new Random().nextBoolean()? 1:0);
                        service.getContentResolver().insert(EverContentProvider.DEBTS_CONTENT_URI, wq);
                    }
                }

                service.onRequestEnd(Constants.Result.OK, intent);

            }


            @Override
            public void onError(VKError error) {
                service.onRequestEnd(Constants.Result.ERROR, intent);
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }
        });
    }

}
