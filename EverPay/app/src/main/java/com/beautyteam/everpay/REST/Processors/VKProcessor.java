package com.beautyteam.everpay.REST.Processors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.REST.Service;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 27.05.2015.
 */
public class VKProcessor extends Processor {

    public VKProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {
        byte[] byteArray = intent.getByteArrayExtra(Constants.IntentParams.IMAGE);
        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        VKRequest requestLoadPhoto = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), 0, 60479154);

        requestLoadPhoto.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonResponse = response.json.getJSONArray("response").getJSONObject(0);
                    //response.json.getInt("id");
                    /*int photoId = response.json.getJSONObject("response").getInt("id");*/
                    int photoId = jsonResponse.getInt("id");
                    String attachment = "photo" + getUserVkId() + "_" + photoId;

                    /*JSONObject params = new JSONObject();
                    params.put("user_id", getUserVkId());
                    params.put("message", "Долг платежем красен");
                    params.put("attachment", attachment);*/
                    VKParameters parameters = VKParameters.from(
                            VKApiConst.USER_ID, getUserVkId(),
                            VKApiConst.MESSAGE, "ТЕКУЩЕЕ ПОЛОЖЕНИЕ ДЕЛ",
                            "attachment", attachment);

                    VKRequest requestSendMessage = new VKRequest(
                            "messages.send",
                            parameters,
                            VKRequest.HttpMethod.POST
                    );

                    requestSendMessage.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onError(VKError error) {
                            super.onError(error);
                        }

                        @Override
                        public void onComplete(VKResponse response) {
                            super.onComplete(response);
                        }
                    });


                } catch (JSONException e) {

                }
            }
        });
    }
}
