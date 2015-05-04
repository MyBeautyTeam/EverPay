package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.REST.Service;
import com.beautyteam.everpay.Utils.DateFormetter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static com.beautyteam.everpay.Constants.Action.GET_BILL;
import static com.beautyteam.everpay.Constants.Action.GET_DEBTS;
import static com.beautyteam.everpay.Constants.Action.GET_GROUPS;
import static com.beautyteam.everpay.Constants.Action.GET_GROUP_MEMBERS;
import static com.beautyteam.everpay.Constants.Action.GET_HISTORY;
import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;

/**
 * Created by Admin on 29.04.2015.
 */
public class GetProcessors extends Processor {

    @Override
    public void request(Intent intent, Service service) {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        Log.d(Constants.LOG, "Processor, request()");
        int result = Constants.Result.OK; // Должно быть изменено. Написал, чтобы не ругалась IDE
        String action = intent.getAction();

        SharedPreferences sPref = service.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE);
        params.add(new BasicNameValuePair("users_id", 8 + ""/*sPref.getString(USER_ID, "0")*/));
        params.add(new BasicNameValuePair("access_token", "wjekwewue"/*sPref.getString(ACCESS_TOKEN, "0"))*/));

        if (GET_GROUPS.equals(action)) {
            String response = get(Constants.URL.GET_GROUPS, params);
            if (response != null) {
                if (response.contains("200")) {
                    try {
                        service.getContentResolver().delete(EverContentProvider.GROUPS_CONTENT_URI, null, null );
                        JSONObject jsonObject = new JSONObject(response);
                        jsonObject = jsonObject.getJSONObject("response");
                        jsonObject = jsonObject.getJSONObject("groups");

                        ContentValues cv;
                        JSONObject group;
                        for (int i = 0; i < jsonObject.length(); i++) {
                            group = jsonObject.getJSONObject(i + "");
                            cv = new ContentValues();
                            cv.put(Groups.GROUP_ID, group.getString("groups_id"));
                            cv.put(Groups.TITLE, group.getString("title"));
                            cv.put(Groups.UPDATE_TIME, DateFormetter.formatDateTime(group.getString("update_datetime")));
                            if (group.getBoolean("is_calculated"))
                                cv.put(Groups.IS_CALCULATED, 1);
                            else
                                cv.put(Groups.IS_CALCULATED, 0);
                            service.getContentResolver().insert(EverContentProvider.GROUPS_CONTENT_URI, cv);
                        }
                        result = Constants.Result.OK;
                    } catch (JSONException e) {
                        result = Constants.Result.ERROR;
                    }
                } else {
                    result = Constants.Result.ERROR;
                }
            }

        }
        else if (GET_GROUP_MEMBERS.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            params.add(new BasicNameValuePair("groups_id", groupId+""));
            String response = get(Constants.URL.GET_GROUP_MEMBERS, params);
            if ((response != null) && (response.contains("200"))) {
                result = Constants.Result.OK;
                try {
                    service.getContentResolver().delete(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, GroupMembers.GROUP_ID + "=" +groupId, null );
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("response");
                    JSONObject group = jsonObject.getJSONObject("group");
                    JSONObject members = jsonObject.getJSONObject("members");
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
                } catch (JSONException e) {
                    result = Constants.Result.ERROR;
                }
            }
            else {
                result = Constants.Result.ERROR;
            }
        }
        else if (GET_DEBTS.equals(action)) {
            String response = get(Constants.URL.GET_DEBTS, params);
            if ((response != null) && (response.contains("200"))) {
                service.getContentResolver().delete(EverContentProvider.DEBTS_CONTENT_URI, null, null);
                JSONObject debtsWhom = null;
                JSONObject debtsWho = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("response");
                    debtsWhom = jsonObject.getJSONObject("debts_whom");
                    debtsWho = jsonObject.getJSONObject("debts_who");

                } catch (JSONException e) {
                    result = Constants.Result.ERROR;
                    service.onRequestEnd(result, intent);
                    return;
                }
                ContentValues cv;
                JSONObject debt;

                try {
                    for (int i = 0; i < debtsWho.length(); i++) {
                        debt = debtsWho.getJSONObject(i + "");
                        cv = new ContentValues();
                        try {
                        JSONObject user = debt.getJSONObject("user");
                            cv.put(Debts.USER_ID, user.getString("users_id"));
                            cv.put(Debts.USER_VK_ID, user.getString("vk_id"));
                            cv.put(Debts.USER_NAME, user.getString("last_name") + " " + user.getString("name"));
                        } catch (JSONException e) {}

                        JSONObject group = debt.getJSONObject("group");
                        cv.put(Debts.GROUP_TITLE, group.getString("title"));
                        cv.put(Debts.GROUP_ID, group.getString("groups_id"));
                        cv.put(Debts.SUMMA, debt.getString("sum"));
                        cv.put(Debts.IS_I_DEBT, "1");

                        service.getContentResolver().insert(EverContentProvider.DEBTS_CONTENT_URI, cv);
                    }
                } catch (JSONException e) {};

                try {
                    for (int i = 0; i < debtsWhom.length(); i++) {
                        debt = debtsWhom.getJSONObject(i+"");
                        cv = new ContentValues();
                        try {
                            JSONObject user = debt.getJSONObject("user");
                            cv.put(Debts.USER_ID, user.getString("users_id"));
                            cv.put(Debts.USER_VK_ID, user.getString("vk_id"));
                            cv.put(Debts.USER_NAME, user.getString("last_name") + " " + user.getString("name"));
                        }catch (JSONException e) {};

                        JSONObject group = debt.getJSONObject("group");
                        cv.put(Debts.GROUP_TITLE, group.getString("title"));
                        cv.put(Debts.GROUP_ID, group.getString("groups_id"));
                        cv.put(Debts.SUMMA, debt.getString("sum"));
                        cv.put(Debts.IS_I_DEBT, "0");

                        service.getContentResolver().insert(EverContentProvider.DEBTS_CONTENT_URI, cv);
                    }
                } catch (JSONException e) {};
                result = Constants.Result.OK;
            }
            else {
                result = Constants.Result.ERROR;
            }
        }
        else if (GET_BILL.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            int billId = intent.getIntExtra(Constants.IntentParams.BILL_ID, 0);
            params.add(new BasicNameValuePair("groups_id", groupId + ""));
            params.add(new BasicNameValuePair("bills_id", billId + ""));

            String response = get(Constants.URL.GET_BILL, params);
            if ((response != null) && (response.contains("200"))) {
                service.getContentResolver().delete(EverContentProvider.BILLS_CONTENT_URI, Bills.BILL_ID + "=" + billId , null);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("response");
                    JSONObject bill = jsonObject.getJSONObject("bill");
                    JSONObject billDetails = jsonObject.getJSONObject("bills_details");

                    JSONObject billItem;
                    for (int i=0; i<billDetails.length(); i++) {
                        billItem = billDetails.getJSONObject(i + "");
                        JSONObject user = billItem.getJSONObject("user");
                        ContentValues cv = new ContentValues();

                        cv.put(Bills.BILL_ID, bill.getString("bills_id"));
                        cv.put(Bills.TITLE, bill.getString("title"));
                        cv.put(Bills.USER_ID, user.getString("users_id"));
                        cv.put(Bills.USER_ID_VK, user.getString("vk_id"));
                        cv.put(Bills.USER_NAME, user.getString("last_name") + " " + user.getString("name"));
                        cv.put(Bills.GROUP_ID, groupId);
                        cv.put(Bills.NEED_SUM, billItem.getString("debt_sum"));
                        cv.put(Bills.INVEST_SUM, billItem.getString("investment_sum"));

                        service.getContentResolver().insert(EverContentProvider.BILLS_CONTENT_URI, cv);

                    }

                    result = Constants.Result.OK;
                } catch (JSONException e) {
                    result = Constants.Result.ERROR;
                }
            } else {
                result = Constants.Result.ERROR;
            }

        } else
        if (GET_HISTORY.equals(action)) {
            int groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);
            params.add(new BasicNameValuePair("groups_id", groupId + ""));

            String response = get(Constants.URL.GET_HISTORY, params);
            if ((response != null) && (response.contains("200"))) {
                service.getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, History.GROUP_ID + "=" + groupId, null);

                try {
                    JSONObject responseJSON = new JSONObject(response);
                    responseJSON = responseJSON.getJSONObject("response");
                    JSONObject history = responseJSON.getJSONObject("history");

                    JSONObject historyItem;
                    for (int i=0; i<history.length(); i++) {
                        historyItem = history.getJSONObject(i + "");
                        ContentValues cv = readHistory(historyItem);

                        service.getContentResolver().insert(EverContentProvider.HISTORY_CONTENT_URI, cv);

                    }
                    result = Constants.Result.OK;

                } catch (JSONException e) {}

            } else {
                result = Constants.Result.ERROR;
            }

        }

        service.onRequestEnd(result, intent);

    }

    private String get(String stringUrl, LinkedList<NameValuePair> params) {

        Log.d(Constants.LOG, "Processor, request()");

        String paramString = URLEncodedUtils.format(params, "utf-8");
        if (!stringUrl.endsWith("?"))
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


}
