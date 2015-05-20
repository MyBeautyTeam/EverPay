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
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.Database.HistoryGenerator;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.Utils.DateFormetter;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.beautyteam.everpay.Constants.Action.*;

/**
 * Created by Admin on 29.04.2015.
 */
public class PostProcessor extends Processor {

    public PostProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {

        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE

        int userId = getUserId();
        String accessToken = getAccessToken();
        String action = intent.getAction();

        if (ADD_BILL.equals(action)) {
            int billId = intent.getIntExtra(Constants.IntentParams.BILL_ID, 0);
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);

            // Добавим новость
            HistoryGenerator historyGenerator = new HistoryGenerator(service);
            String historyId = historyGenerator.addBill(billId);

            // Обновим дату в группе
            updateDateInGroup(groupId, service);

            Cursor c = service.getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_BILL, Bills.BILL_ID + "=" + billId, null, null);
            c.moveToFirst();

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
            String response = urlConnectionPost(Constants.URL.ADD_BILL, jsonObject.toString());
            if (response !=null && response.contains("200")) {
                JSONObject responseJSON;
                try {
                    responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");

                    int newBillId = responseJSON.getInt("bills_id");
                    int oldBillId = responseJSON.getInt("id");
                    ContentValues cv = new ContentValues();
                    cv.put(Bills.BILL_ID, newBillId);
                    cv.put(Bills.STATE, Constants.State.ENDS);
                    cv.put(Bills.RESULT, Constants.Result.OK);
                    service.getContentResolver().update(EverContentProvider.BILLS_CONTENT_URI, cv, Bills.BILL_ID + "=" + oldBillId, null);

                    /*
                    JSONObject history = responseJSON.getJSONObject("history");

                    service.getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, History.GROUP_ID + "=" + groupId, null);
                    Iterator<String> historyKeys = history.keys();
                    while (historyKeys.hasNext()) {
                        JSONObject historyItem = history.getJSONObject(historyKeys.next());
                        cv = readHistory(historyItem);
                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
                    }
                    */

                    // Обновляем статус новости
                    cv = new ContentValues();
                    cv.put(History.BILL_ID, newBillId);
                    cv.put(History.STATE, Constants.State.ENDS);
                    cv.put(History.RESULT, Constants.Result.OK);
                    service.getContentResolver().update(EverContentProvider.HISTORY_CONTENT_URI, cv, Bills.ITEM_ID + "=" + historyId, null);

                    result = Constants.Result.OK;
                } catch (JSONException asdw) {
                    result = Constants.Result.ERROR;
                }
            } else {
                result = Constants.Result.ERROR;
                ContentValues cv = new ContentValues();
                cv.put(Bills.STATE, Constants.State.ENDS);
                cv.put(Bills.RESULT, Constants.Result.ERROR);
                service.getContentResolver().update(EverContentProvider.BILLS_CONTENT_URI, cv, Bills.BILL_ID + "=" + billId, null);
                service.getContentResolver().update(EverContentProvider.HISTORY_CONTENT_URI, cv, Bills.ITEM_ID + "=" + historyId, null);
                // ДОБАВИТЬ ИСТОРИЮ С ОШИБКОЙ!
            }

        } else
        if (ADD_MEMBER_TO_GROUP.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            int userIdWhom = intent.getIntExtra(Constants.IntentParams.USER_ID, 0);
            try {
                JSONObject paramsJSON = new JSONObject();
                paramsJSON.put("users_id", userId);
                paramsJSON.put("access_token", accessToken);
                paramsJSON.put("groups_id", groupId);
                paramsJSON.put("users_id_whom", userIdWhom);
                String response = urlConnectionPost(Constants.URL.ADD_GROUP_MEMBER, paramsJSON.toString());
                if ((response != null) && response.contains("200")) {
                    result = Constants.Result.OK;

                    service.getContentResolver().delete(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, GroupMembers.GROUP_ID + "=" +groupId, null );
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("response");
                    JSONObject group = jsonObject.getJSONObject("group");
                    JSONObject members = jsonObject.getJSONObject("members");
                    JSONObject history = jsonObject.getJSONObject("history");
                    JSONObject member;
                    for (int i = 0; i<members.length(); i++) {
                        member = members.getJSONObject(i + "");
                        ContentValues cv = new ContentValues();
                        cv.put(GroupMembers.GROUP_ID, group.getString("groups_id"));
                        cv.put(GroupMembers.USER_ID_VK, member.getString("vk_id"));
                        cv.put(GroupMembers.USER_ID, member.getString("users_id"));
                        cv.put(GroupMembers.USER_NAME, member.getString("last_name") + " " + member.getString("name"));

                        service.getContentResolver().insert(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, cv);
                    }

                    ContentValues cv = readHistory(history);
                    if (cv != null)
                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);
                } else {
                    result = Constants.Result.ERROR;
                }
            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        } else
        if (ADD_GROUP.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            try {
                Cursor groupCursor = service.getContentResolver().query(EverContentProvider.GROUPS_CONTENT_URI, PROJECTION_GROUPS, Groups.GROUP_ID +"="+groupId, null, null);
                groupCursor.moveToFirst();
                String groupTitle = groupCursor.getString(groupCursor.getColumnIndex(Groups.TITLE));

                JSONObject paramsJSON = new JSONObject();
                paramsJSON.put("users_id", userId);
                paramsJSON.put("access_token", accessToken);
                paramsJSON.put("id", groupId);
                paramsJSON.put("title", groupTitle);

                Cursor groupMembersCursor = service.getContentResolver().query(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, PROJECTION_GROUP_MEMBERS, GroupMembers.GROUP_ID +"="+groupId, null, null);
                JSONObject groupMembers = new JSONObject();
                int i = 1;
                if (groupMembersCursor.moveToFirst() && groupMembersCursor.getCount() != 0) {
                    while (!groupMembersCursor.isAfterLast()) {
                        int memberId = groupMembersCursor.getInt(groupMembersCursor.getColumnIndex(GroupMembers.USER_ID));
                        groupMembers.put(i+"", memberId);
                        i++;
                        groupMembersCursor.moveToNext();
                    }
                    paramsJSON.put("users_id_members", groupMembers);

                    String response = urlConnectionPost(Constants.URL.ADD_GROUP, paramsJSON.toString());
                    if (response != null && response.contains("200")) {
                        result = Constants.Result.OK;

                        JSONObject responseJSON = new JSONObject(response);
                        responseJSON = responseJSON.getJSONObject("response");
                        int newGroupId = responseJSON.getInt("groups_id");
                        int oldGroupId = responseJSON.getInt("id");

                        ContentValues cv = new ContentValues();

                        cv.put(Groups.GROUP_ID, newGroupId);
                        cv.put(Groups.RESULT, Constants.Result.OK);
                        cv.put(Groups.STATE, Constants.State.ENDS);
                        service.getContentResolver().update(EverContentProvider.GROUPS_CONTENT_URI, cv, Groups.GROUP_ID + "=" + oldGroupId, null);

                        // Обновим дату в группе
                        updateDateInGroup(newGroupId, service);

                        cv = new ContentValues();
                        cv.put(GroupMembers.GROUP_ID, newGroupId);
                        cv.put(Groups.RESULT, Constants.Result.OK);
                        cv.put(Groups.STATE, Constants.State.ENDS);
                        service.getContentResolver().update(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, cv, GroupMembers.GROUP_ID + "=" + oldGroupId, null);

                        JSONObject history = responseJSON.getJSONObject("history");

                        intent.putExtra(Constants.IntentParams.GROUP_ID, newGroupId);
                        intent.putExtra(Constants.IntentParams.GROUP_TITLE, groupTitle);
                        cv = readHistory(history);
                        if (cv != null) {
                            service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);
                            intent.putExtra(Constants.IntentParams.IS_ENDS, true);
                        }
                        else
                            result = Constants.Result.ERROR;
                        // ====================
                    } else {
                        result = Constants.Result.ERROR;
                    }

                }

            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        } else
        if (CALCULATE.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            try {
                JSONObject paramsJSON = new JSONObject();
                paramsJSON.put("users_id", userId);
                paramsJSON.put("access_token", accessToken);
                paramsJSON.put("groups_id", groupId);
                String response = urlConnectionPost(Constants.URL.CALCULATE, paramsJSON.toString());
                if (response != null && response.contains("200")) {
                    result = Constants.Result.OK;

                    service.getContentResolver().delete(EverContentProvider.CALCULATION_CONTENT_URI, Calculation.GROUPS_ID + "=" + groupId, null);
                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");

                    JSONObject groupJSON = responseJSON.getJSONObject("group");
                    int groupIdFromResponse = groupJSON.getInt("groups_id");

                    JSONObject debtsJSOB = responseJSON.getJSONObject("debts");
                    JSONObject history = responseJSON.getJSONObject("history");
                    ContentValues cv;
                    Iterator<String> iterator = debtsJSOB.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONObject debt = debtsJSOB.getJSONObject(key);

                        cv = new ContentValues();
                        int debtId = debt.getInt("debts_id");
                        int sum = debt.getInt("sum");
                        boolean isDeleted = debt.getBoolean("is_deleted");

                        JSONObject userWhoJSON = debt.getJSONObject("user_who");

                        int whoId = userWhoJSON.getInt("users_id");
                        int whoIdVK = userWhoJSON.getInt("vk_id");
                        String whoName = userWhoJSON.getString("last_name") + " " + userWhoJSON.getString("name") ;

                        JSONObject userWhomJSON = debt.getJSONObject("user_whom");
                        int whomId = userWhomJSON.getInt("users_id");
                        int whomIdVK = userWhomJSON.getInt("vk_id");
                        String whomName = userWhomJSON.getString("last_name") + " " + userWhomJSON.getString("name") ;

                        cv.put(Calculation.CALC_ID, debtId);
                        cv.put(Calculation.GROUPS_ID, groupIdFromResponse);
                        cv.put(Calculation.WHO_ID, whoId);
                        cv.put(Calculation.WHO_ID_VK, whoIdVK);
                        cv.put(Calculation.NAME_WHO, whoName);

                        cv.put(Calculation.WHOM_ID, whomId);
                        cv.put(Calculation.WHOM_ID_VK, whomIdVK);
                        cv.put(Calculation.NAME_WHOM, whomName);

                        cv.put(Calculation.SUMMA, sum);
                        cv.put(Calculation.IS_DELETED, isDeleted? 1:0);

                        cv.put(Calculation.STATE, Constants.State.ENDS);
                        cv.put(Calculation.RESULT, Constants.Result.OK);

                        service.getContentResolver().insert(EverContentProvider.CALCULATION_CONTENT_URI, cv);
                    }

                    cv = readHistory(history);
                    if (cv != null)
                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);

                    // Обновим дату в группе
                    updateDateInGroup(groupId, service);


                } else
                    result = Constants.Result.ERROR;
            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }
        }
        service.onRequestEnd(result, intent);
    }

    public String urlConnectionPost(String strUrl, String urlParameters) {
        HttpURLConnection connection = null;
        String str = null;
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

    private static final String[] PROJECTION_BILL = new String[] {
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

    private static final String[] PROJECTION_GROUPS = new String[] {
            Groups.GROUP_ID,
            Groups.TITLE,
            Groups.UPDATE_TIME,
            Groups.IS_CALCULATED
    };


    private static final String[] PROJECTION_GROUP_MEMBERS = new String[] {
            GroupMembers.ITEM_ID,
            GroupMembers.GROUP_ID,
            GroupMembers.USER_ID_VK,
            GroupMembers.USER_ID,
            GroupMembers.USER_NAME,
    };


}
