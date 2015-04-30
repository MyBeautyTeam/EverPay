package com.beautyteam.everpay.REST.Processors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.REST.Service;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static com.beautyteam.everpay.Constants.Action.*;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;

/**
 * Created by Admin on 29.04.2015.
 */
public class PostProcessor extends Processor {

    @Override
    public void request(Intent intent, Service service) {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE
        SharedPreferences sPref = service.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
        int userId = 8;//sPref.getInt(Constants.Preference.USER_ID, 0);
        String accessToken = "wjekwewue";//sPref.getString(ACCESS_TOKEN, "0");
        String action = intent.getAction();

        if (ADD_BILL.equals(action)) {
            int billId = intent.getIntExtra(Constants.IntentParams.BILL_ID, 0);
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            Cursor c = service.getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_ADD_BILL, Bills.BILL_ID +"=" + billId, null, null );
            c.moveToFirst();
            int count = c.getCount();
            String title = c.getString(c.getColumnIndex(Bills.TITLE));
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("users_id", userId);
                jsonObject.put("access_token", accessToken);
                jsonObject.put("groups_id", groupId);
                jsonObject.put("title", title);
                jsonObject.put("id", billId);

                JSONObject billDetails = new JSONObject();
                c.moveToFirst();
                for (int i=0; i<c.getCount(); i++) {
                    JSONObject userDetail = new JSONObject();
                    userDetail.put("users_id", c.getInt(c.getColumnIndex(Bills.USER_ID)));
                    userDetail.put("debt_sum", c.getInt(c.getColumnIndex(Bills.NEED_SUM)));
                    userDetail.put("investment_sum", c.getInt(c.getColumnIndex(Bills.INVEST_SUM)));

                    billDetails.put(i+"", userDetail);
                    c.moveToNext();
                }
                jsonObject.put("bills_details", billDetails);

            } catch (JSONException e) {}
            String UrlParams = jsonObject.toString();
            String response = urlConnectionPost(Constants.URL.ADD_BILL, jsonObject.toString());
            JSONObject jsonObject1;
            try {
                jsonObject1 = new JSONObject(response);
            } catch (JSONException asdw) {

            }
            int a = 10;
            a++;
            int d = 10;



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

    public String urlConnectionPost(String strUrl, String urlParameters) {
        HttpURLConnection connection = null;
        String str = "";
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(urlParameters);
            writer.flush();
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream in = connection.getInputStream();
                str = handleInputStream(in);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return str;
    }

    private String handleInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = "", line = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        Log.e("", result);
        return result;
    }

    private static final String[] PROJECTION_ADD_BILL = new String[] {
            Bills.ITEM_ID,
            Bills.BILL_ID,
            Bills.TITLE,
            Bills.USER_ID_VK,
            Bills.USER_ID,
            Bills.USER_NAME,
            Bills.GROUP_ID,
            Bills.NEED_SUM,
            Bills.INVEST_SUM
    };


}
