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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.beautyteam.everpay.Constants.Action.EDIT_BILL;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by Admin on 02.05.2015.
 */
public class PutProcessor extends Processor{

    @Override
    public void request(Intent intent, Service service) {
        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE
        SharedPreferences sPref = service.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
        int userId = 8;//sPref.getInt(Constants.Preference.USER_ID, 0);
        String accessToken = "wjekwewue";//sPref.getString(ACCESS_TOKEN, "0");
        String action = intent.getAction();

        if (EDIT_BILL.equals(action)) {
            result = Constants.Result.OK;
            int billId = intent.getIntExtra(Constants.IntentParams.BILL_ID, 0);
            Cursor c = service.getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_BILL, Bills.BILL_ID + "=" + billId, null, null);

            c.moveToFirst();
            String title = c.getString(c.getColumnIndex(Bills.TITLE));
            int groupId = c.getInt(c.getColumnIndex(Bills.GROUP_ID));
            int billEditedId = c.getInt(c.getColumnIndex(Bills.BILL_EDITED_ID));
            JSONObject requestJSON = new JSONObject();
            try {
                requestJSON.put("users_id", userId);
                requestJSON.put("access_token", accessToken);
                requestJSON.put("groups_id", groupId);
                requestJSON.put("title", title);
                requestJSON.put("id", billId);
                requestJSON.put("edited_bills_id", billEditedId);

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

                requestJSON.put("bills_details", billDetails);

                String response = urlConnectionPut(Constants.URL.EDIT_BILL, requestJSON.toString());
                if (response != null && response.contains("200")) {

                } else
                    result = Constants.Result.ERROR;

            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        }
        service.onRequestEnd(result, intent);
    }

/*    private String put(String url, JSONObject body) {
        HttpResponse response = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15);
            HttpConnectionParams.setSoTimeout(httpParameters, 15);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPut putConnection = new HttpPut(url);
            StringEntity se = new StringEntity(body.toString(), "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            putConnection.setEntity(se);
            try {
                response = httpClient.execute(putConnection);
                String JSONString = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null)
            return response.getEntity().toString();
        else
            return null;
    }
*/

    private static final String[] PROJECTION_BILL = new String[] {
            Bills.ITEM_ID,
            Bills.BILL_ID,
            Bills.BILL_EDITED_ID,
            Bills.TITLE,
            Bills.USER_ID_VK,
            Bills.USER_ID,
            Bills.USER_NAME,
            Bills.GROUP_ID,
            Bills.NEED_SUM,
            Bills.INVEST_SUM
    };

    public String urlConnectionPut(String strUrl, String urlParameters) {
        HttpURLConnection connection = null;
        String str = "";
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
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

}
