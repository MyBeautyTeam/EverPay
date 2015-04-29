package com.beautyteam.everpay.REST.Processors;

import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
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

import java.util.Random;

import static com.beautyteam.everpay.Constants.Action.INIT_VK_USERS;
import static com.beautyteam.everpay.Constants.Preference.ACCESS_TOKEN;
import static com.beautyteam.everpay.Constants.Preference.IMG_URL;
import static com.beautyteam.everpay.Constants.Preference.USER_ID;
import static com.beautyteam.everpay.Constants.Preference.USER_NAME;

/**
 * Created by Admin on 29.04.2015.
 */
public class VKProcessor extends Processor {

    @Override
    public void request(Intent intent, Service service) {
        String action = intent.getAction();
        if (INIT_VK_USERS.equals(action)) {
            initVKUsers(service, intent);
        }
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
                intent.putExtra(ACCESS_TOKEN, "wjekwewue");
                intent.putExtra(USER_ID, userFull.id + "");
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
