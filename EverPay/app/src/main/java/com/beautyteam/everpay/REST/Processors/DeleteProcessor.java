package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.REST.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import static com.beautyteam.everpay.Constants.Action.REMOVE_BILL;
import static com.beautyteam.everpay.Constants.Action.REMOVE_MEMBER_FROM_GROUP;

/**
 * Created by Admin on 02.05.2015.
 */
public class DeleteProcessor extends Processor {

    public DeleteProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {

        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE

        int userId = getUserId();//sPref.getInt(Constants.Preference.USER_ID, 0);
        String accessToken = getAccessToken();//sPref.getString(ACCESS_TOKEN, "0");
        String action = intent.getAction();

        if (Constants.Action.REMOVE_MEMBER_FROM_GROUP.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            int userIdWhom = intent.getIntExtra(Constants.IntentParams.USER_ID, 0);
            try {
                JSONObject paramsJSON = new JSONObject();
                paramsJSON.put("users_id", userId);
                paramsJSON.put("access_token", accessToken);
                paramsJSON.put("groups_id", groupId);
                paramsJSON.put("users_id_whom", userIdWhom);
                String response = urlConnectionDelete(Constants.URL.REMOVE_GROUP_MEMBER, paramsJSON.toString());

                if ((response != null) && response.contains("200")) {
                    service.getContentResolver().delete(EverContentProvider.GROUP_MEMBERS_CONTENT_URI,
                            GroupMembers.GROUP_ID + "=" + groupId , null);

                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    JSONObject members = responseJSON.getJSONObject("members");

                    Iterator<String> iterator = members.keys();
                    while (iterator.hasNext()) {
                        JSONObject member = members.getJSONObject(iterator.next());
                        ContentValues cv = new ContentValues();
                        cv.put(GroupMembers.GROUP_ID, groupId);
                        cv.put(GroupMembers.USER_ID_VK, member.getString("vk_id"));
                        cv.put(GroupMembers.USER_ID, member.getString("users_id"));
                        cv.put(GroupMembers.USER_NAME, member.getString("last_name") + " " + member.getString("name"));

                        service.getContentResolver().insert(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, cv);
                    }

                    //ContentValues cv = readHistory(history);
                    //service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);
                    result = Constants.Result.OK;
                } else {
                    result = Constants.Result.ERROR;
                }
            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        } else
        if (REMOVE_BILL.equals(action)) {
            int billId = intent.getIntExtra(Constants.IntentParams.BILL_ID, 0);
            Cursor c = service.getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_BILL, Bills.BILL_ID + "=" + billId, null, null);
            int count = c.getCount();
            c.moveToFirst();
            int groupId = c.getInt(c.getColumnIndex(Bills.GROUP_ID));
            JSONObject requestJSON = new JSONObject();
            try {
                requestJSON.put("users_id", userId);
                requestJSON.put("access_token", accessToken);
                requestJSON.put("groups_id", groupId);
                requestJSON.put("bills_id", billId);
                String response = urlConnectionDelete(Constants.URL.REMOVE_BILL, requestJSON.toString());
                if ((response != null) && response.contains("200")) {
                    result = Constants.Result.OK;
                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    JSONObject history = responseJSON.getJSONObject("history");

                    ContentValues cv = readHistory(history);
                    service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);
                } else {
                    result = Constants.Result.ERROR;
                }
            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        }
        service.onRequestEnd(result, intent);
    }

    public String urlConnectionDelete(String strUrl, String urlParameters) {
        HttpURLConnection connection = null;
        String str = "";
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
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
}
