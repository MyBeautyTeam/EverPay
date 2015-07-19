package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.REST.Service;
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

import static com.beautyteam.everpay.Constants.Action.INIT_VK_USERS;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;
import static com.beautyteam.everpay.Constants.Preference.IMG_URL;
import static com.beautyteam.everpay.Constants.Preference.USER_ID;
import static com.beautyteam.everpay.Constants.Preference.USER_ID_VK;
import static com.beautyteam.everpay.Constants.Preference.USER_NAME;

/**
 * Created by Admin on 29.04.2015.
 */
public class VKAuthProcessor extends Processor {

    private Intent mIntent;
    private Service mService;

    public VKAuthProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {
        mIntent = intent;
        mService = service;
        String action = intent.getAction();
        if (INIT_VK_USERS.equals(action)) {
            new getFriendFromVK().execute();
        }
    }



    private class sendFriendToServer extends AsyncTask<String, Void, String> {

        private Exception exception;

        private String handleInputStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result = "", line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            Log.e("", result);
            return result;
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

        @Override
        protected String doInBackground(String... params) {
            String response = urlConnectionPost(Constants.URL.SIGNUP, params[0]);
            if ((response != null)&&(response.contains("200"))) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("response");
                    int userId = jsonObject.getInt("users_id");
                    String accessToken = jsonObject.getString("access_token");
                    JSONObject friendsId = jsonObject.getJSONObject("friends_ids");
                    Iterator<String> iterator = friendsId.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        String value = friendsId.getString(key);
                        ContentValues cv = new ContentValues();
                        cv.put(Users.USER_ID, value);
                        mService.getContentResolver().update(EverContentProvider.USERS_CONTENT_URI, cv, Users.USER_ID +"=" + key, null);
                    }
                    mIntent.putExtra(USER_ID, userId);

                    // /* ОТЛАДОЧНО!!!
                    mIntent.putExtra(USER_ID, userId);
                    mIntent.putExtra(ACCESS_TOKEN, accessToken);
                     //*/
                    mService.onRequestEnd(Constants.Result.OK, mIntent);
                } catch (JSONException e) {}
            } else {
                mService.onRequestEnd(Constants.Result.ERROR, mIntent);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String feed) {

        }
    }

    private class getFriendFromVK extends AsyncTask<Void, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(Void... params) {
            initVKUsers(mService, mIntent);
            return "";
        }

        @Override
        protected void onPostExecute(String feed) {

        }

        private void initVKUsers(final Service service, final Intent intent) {
            final VKRequest request1 = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100, sex"));
            VKRequest request2 = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100, sex"));

            VKBatchRequest batch = new VKBatchRequest(request1, request2);
            batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
                @Override
                public void onComplete(final VKResponse[] responses) {
                    super.onComplete(responses);
                    Log.d("VkDemoApp", "onComplete " + responses);
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            VKApiUserFull userFull = ((VKList<VKApiUserFull>) responses[0].parsedModel).get(0);
                            //user = new User(userFull.id, userFull.first_name, userFull.last_name, userFull.photo_100);
                            intent.putExtra(ACCESS_TOKEN, "wjekwewue12345");
                            intent.putExtra(USER_ID_VK, userFull.id );
                            intent.putExtra(USER_NAME, userFull.first_name + " " + userFull.last_name);
                            intent.putExtra(IMG_URL, userFull.photo_100);
                            intent.putExtra(Constants.IntentParams.MALE, Math.abs(userFull.sex - 2));

                            JSONObject jsonObject = new JSONObject();
                            JSONObject user = new JSONObject();
                            JSONObject friends = new JSONObject();
                            try {
                                user.put("vk_id", userFull.id );
                                user.put("last_name", userFull.last_name);
                                user.put("name", userFull.first_name);
                                user.put("sex", Math.abs(userFull.sex-2));
                                user.put("access_token", "wjekwewue12345");
                                jsonObject.put("user", user);

                                Log.d("vksdk", responses[1].parsedModel.toString());
                                VKUsersArray usersArray = (VKUsersArray) responses[1].parsedModel;

                                // TODO УДАЛИТЬ!!!
                                ContentValues cav = new ContentValues();
                                cav.put(Users.USER_ID, 1500);
                                cav.put(Users.USER_ID_VK, 2500);
                                cav.put(Users.NAME, "ЖИРНЫЙ НЕПРИЯТЕЛЬ");
                                cav.put(Users.IMG, "http://cs14113.vk.me/c540104/v540104654/293e4/oqmgTryKZgM.jpg");
                                service.getContentResolver().insert(EverContentProvider.USERS_CONTENT_URI, cav);
                                //=========

                                ContentValues cv = new ContentValues();
                                for (VKApiUserFull vkFriend : usersArray) {
                                    cv.put(Users.USER_ID_VK, vkFriend.id);
                                    cv.put(Users.NAME, vkFriend.last_name + " " + vkFriend.first_name);
                                    cv.put(Users.IMG, vkFriend.photo_100);
                                    Uri uri = service.getContentResolver().insert(EverContentProvider.USERS_CONTENT_URI, cv);
                                    String id = uri.getLastPathSegment();

                                    JSONObject friend = new JSONObject();
                                    friend.put("vk_id", vkFriend.id);
                                    friend.put("last_name", vkFriend.last_name);
                                    friend.put("name", vkFriend.first_name);
                                    friend.put("sex", Math.abs(vkFriend.sex-2));
                                    friends.put(id, friend);


                        /*if (new Random().nextFloat() > 0.98) {
                            ContentValues wq = new ContentValues();
                            wq.put(Debts.SUMMA, new Random().nextInt(500));
                            wq.put(Debts.USER_ID_VK, vkFriend.id);
                            wq.put(Debts.USER_NAME, vkFriend.last_name + " " + vkFriend.first_name);
                            wq.put(Debts.GROUP_TITLE, "МОЯ ГРУППА");
                            wq.put(Debts.IS_I_DEBT, new Random().nextBoolean() ? 1 : 0);
                            service.getContentResolver().insert(EverContentProvider.DEBTS_CONTENT_URI, wq);
                        }*/
                                }

                                jsonObject.put("friends", friends);




                            } catch (JSONException e) {

                            }

                            new sendFriendToServer().execute(jsonObject.toString());

                            return null;
                        }
                    }.execute();

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

}
