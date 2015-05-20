package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.Utils.DateFormetter;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static com.beautyteam.everpay.Constants.Action.EDIT_BILL;
import static com.beautyteam.everpay.Constants.Action.EDIT_CALCULATION;
import static com.beautyteam.everpay.Constants.Action.EDIT_GROUP;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by Admin on 02.05.2015.
 */
public class PutProcessor extends Processor{

    public PutProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {
        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE
        SharedPreferences sPref = service.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
        int userId = getUserId();
        String accessToken = getAccessToken();
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

                    ContentValues cv = new ContentValues();
                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    cv.put(Bills.BILL_EDITED_ID, responseJSON.getInt("bills_id"));
                    int newBillId = responseJSON.getInt("bills_id");
                    cv.put(Bills.BILL_ID, newBillId);
                    cv.put(Bills.RESULT, Constants.Result.OK);
                    cv.put(Bills.STATE, Constants.State.ENDS);

                    service.getContentResolver().update(EverContentProvider.BILLS_CONTENT_URI, cv, Bills.BILL_ID + "=" + billId, null);

                    JSONObject history = responseJSON.getJSONObject("history");
                    cv = readHistory(history);
                    service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);
                } else {
                    result = Constants.Result.ERROR;
                    ContentValues cv = new ContentValues();
                    cv.put(Bills.RESULT, Constants.Result.ERROR);
                    cv.put(Bills.STATE, Constants.State.ENDS);
                    service.getContentResolver().update(EverContentProvider.BILLS_CONTENT_URI, cv, Bills.BILL_ID + "=" + billId, null);

                    /*
                    TODO ДОБАВИТЬ НОВОСТЬ
                     */
                }

            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        } else
        if (EDIT_GROUP.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);

            Cursor c = service.getContentResolver().query(EverContentProvider.GROUPS_CONTENT_URI, PROJECTION_GROUPS, Groups.GROUP_ID + "=" + groupId, null, null);
            c.moveToFirst();
            String title = c.getString(c.getColumnIndex(Groups.TITLE));
            JSONObject requestJSON = new JSONObject();
            try {
                requestJSON.put("users_id", userId);
                requestJSON.put("access_token", accessToken);
                requestJSON.put("groups_id", groupId);
                requestJSON.put("title", title);

                String response = urlConnectionPut(Constants.URL.EDIT_GROUP, requestJSON.toString());
                if (response != null && response.contains("200")) {
                    result = Constants.Result.OK;
                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    JSONObject history = responseJSON.getJSONObject("history");


                    //service.getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, History.GROUP_ID + "=" + groupId, null);
                    Iterator<String> historyKeys = history.keys();
                    while (historyKeys.hasNext()) {
                        JSONObject historyItem = history.getJSONObject(historyKeys.next());
                        ContentValues cv = readHistory(historyItem);
                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
                    }

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);
                } else {
                    /*
                    TODO ДОПИСАТЬ ИСТОРИЮ
                     */
                    result = Constants.Result.ERROR;
                }

            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        } else
        if (EDIT_CALCULATION.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            JSONObject requestJSON = new JSONObject();
            try {
                requestJSON.put("users_id", userId);
                requestJSON.put("access_token", accessToken);
                requestJSON.put("groups_id", groupId);
                JSONObject debts = new JSONObject();

                Cursor c = service.getContentResolver().query(EverContentProvider.CALCULATION_CONTENT_URI, PROJECTION_CALCULATION, Calculation.GROUPS_ID + "="+ groupId, null, null);
                int i=0;
                if (c.moveToFirst() && c.getCount() != 0) {
                    while (!c.isAfterLast()) {
                        int debtId = c.getInt(c.getColumnIndex(Calculation.CALC_ID));
                        int isDeleted = c.getInt(c.getColumnIndex(Calculation.IS_DELETED));
                        JSONObject debtsItem = new JSONObject();
                        debtsItem.put("debts_id", debtId);
                        debtsItem.put("is_deleted", isDeleted);

                        debts.put(i + "", debtsItem);
                        i++;
                        c.moveToNext();
                    }
                }
                requestJSON.put("debts", debts);

                String response = urlConnectionPut(Constants.URL.EDIT_CALCULATION, requestJSON.toString());
                if (response != null && response.contains("200")) {

                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    JSONObject history = responseJSON.getJSONObject("history");

                    //service.getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, History.GROUP_ID + "=" + groupId, null);
                    Iterator<String> historyKeys = history.keys();
                    while (historyKeys.hasNext()) {
                        JSONObject historyItem = history.getJSONObject(historyKeys.next());
                        ContentValues cv = readHistory(historyItem);
                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
                    }

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);

                    result = Constants.Result.OK;
                } else {
                    /*
                    TODO ДОПИСАТЬ ИСТОРИЮ
                     */
                    result = Constants.Result.ERROR;
                }

            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        }
        service.onRequestEnd(result, intent);
    }

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


    private static final String[] PROJECTION_GROUPS = new String[] {
            Groups.GROUP_ID,
            Groups.TITLE,
            Groups.UPDATE_TIME,
            Groups.IS_CALCULATED
    };

    private static final String[] PROJECTION_CALCULATION = new String[] {
            Calculation.ITEM_ID,
            Calculation.CALC_ID,
            Calculation.GROUPS_ID,
            Calculation.WHO_ID,
            Calculation.WHO_ID_VK,
            Calculation.NAME_WHO,
            Calculation.WHOM_ID,
            Calculation.WHOM_ID_VK,
            Calculation.NAME_WHOM,
            Calculation.SUMMA,
            Calculation.IS_DELETED
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
