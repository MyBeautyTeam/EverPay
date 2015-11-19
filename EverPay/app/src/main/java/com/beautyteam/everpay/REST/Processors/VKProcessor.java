package com.beautyteam.everpay.REST.Processors;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.REST.Service;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Admin on 27.05.2015.
 */
public class VKProcessor extends Processor {
    private final String smileApply = "&#10004;";
    private final String smileCancel = "&#10060;";
    private Context context;
    private static boolean IS_FOR_ALL = true;
    private static int groupId;

    public VKProcessor(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void request(final Intent intent, final Service service) {
        byte[] byteArray = intent.getByteArrayExtra(Constants.IntentParams.IMAGE);
        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        groupId = intent.getIntExtra(Constants.IntentParams.GROUP_ID, 0);

        IS_FOR_ALL = intent.getBooleanExtra(Constants.IntentParams.IS_FOR_ALL, true);


        VKRequest requestLoadPhoto = VKApi.uploadWallPhotoRequest(new VKUploadImage(photo, VKImageParameters.jpgImage(0.9f)), 0, 60479154);
        intent.putExtra(Constants.IntentParams.GROUP_ID, groupId);

        requestLoadPhoto.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                service.onRequestEnd(Constants.Result.ERROR, intent);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ArrayList<VKRequest> vkRequestArrayList = null;
                try {
                    JSONObject jsonResponse = response.json.getJSONArray("response").getJSONObject(0);
                    int photoId = jsonResponse.getInt("id");
                    Cursor cursor = service.getContentResolver().query(EverContentProvider.CALCULATION_CONTENT_URI, PROJECTION, Calculation.GROUPS_ID + " = " + groupId + " AND " + Calculation.IS_DELETED + "=" + 0, null, /*SORT_ORDER*/null);

                    HashSet<Integer> setUserToSend = new HashSet<Integer>(); // Список пользователей, кому будет отправлено сообщение
                    cursor.moveToFirst();

                    vkRequestArrayList = new ArrayList<VKRequest>();
                    int myVkId = getUserVkId();
                    for (int i=0; i<cursor.getCount(); i++) {
                        int userIdWhoVk = cursor.getInt(cursor.getColumnIndex(Calculation.WHO_ID_VK));
                        int userIdWhomVk = cursor.getInt(cursor.getColumnIndex(Calculation.WHOM_ID_VK));

                        // Если сообщение нужно отправлять только моим должникам
                        if (!IS_FOR_ALL)
                            if (myVkId != userIdWhoVk && myVkId != userIdWhomVk) {
                                cursor.moveToNext();
                                continue;
                            }


                        if (userIdWhomVk != 0 && userIdWhoVk != 0) {

                            if (setUserToSend.add(userIdWhoVk) && userIdWhoVk != getUserVkId()) {
                                String message = generateMessage(userIdWhoVk);
                                vkRequestArrayList.add(generateRequest(userIdWhoVk, photoId, message));
                            }

                            if (setUserToSend.add(userIdWhomVk) && userIdWhomVk != getUserVkId()) {
                                String message = generateMessage(userIdWhomVk);
                                vkRequestArrayList.add(generateRequest(userIdWhomVk, photoId, message));
                            }
                        }

                        cursor.moveToNext();
                    }

                    if (vkRequestArrayList.size() != 0) {
                        VKRequest[] requestsArr = new VKRequest[vkRequestArrayList.size()];
                        requestsArr = vkRequestArrayList.toArray(requestsArr);

                        VKBatchRequest vkBatchRequest = new VKBatchRequest(requestsArr);
                        vkBatchRequest.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
                            @Override
                            public void onComplete(VKResponse[] responses) {
                                super.onComplete(responses);
                                service.onRequestEnd(Constants.Result.OK, intent);
                            }

                            @Override
                            public void onError(VKError error) {
                                service.onRequestEnd(Constants.Result.ERROR, intent);
                                super.onError(error);
                            }
                        });
                    } else {
                        service.onRequestEnd(Constants.Result.OK, intent);
                    }


                } catch (JSONException e) {

                }

            }
        });
    }



    private VKRequest generateRequest(int userId, int photoId, String message) {
        String attachment = "photo" + getUserVkId() + "_" + photoId;

        VKParameters parameters = VKParameters.from(
                VKApiConst.USER_ID, userId,
                VKApiConst.MESSAGE, message,
                "attachment", attachment);

        VKRequest requestSendMessage = new VKRequest(
                "messages.send",
                parameters,
                VKRequest.HttpMethod.POST
        );
        return requestSendMessage;

        /*requestSendMessage.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
            }
        }); */
    }

    private static final String[] PROJECTION = new String[] {
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

    private String generateMessage(int userId) {

        String message = "**Автоматическое сообщение**\n";
        message += "\n";
        //message += "Текущее положение дел:";
        Cursor cursor = context.getContentResolver().query(EverContentProvider.CALCULATION_CONTENT_URI, PROJECTION, "(" +Calculation.WHOM_ID_VK +" = " + userId + " OR " + Calculation.WHO_ID_VK + " = " + userId + ")" + " AND " + Calculation.GROUPS_ID +" = " + groupId + " AND " + Calculation.IS_DELETED +"="+ 0, null, /*SORT_ORDER*/null);

        cursor.moveToFirst();

        String msg_whom = ""; // Кому я должен
        String msg_who = ""; // Кто мне должен
        for (int i=0; i < cursor.getCount(); cursor.moveToNext(), i++) {
            //message += "\n";

            //message += smileCancel;
            //message += " ";


            int idWho = cursor.getInt(cursor.getColumnIndex(Calculation.WHO_ID_VK));
            int idWhom = cursor.getInt(cursor.getColumnIndex(Calculation.WHOM_ID_VK));

            String nameWho = cursor.getString(cursor.getColumnIndex(Calculation.NAME_WHO));
            String nameWhom = cursor.getString(cursor.getColumnIndex(Calculation.NAME_WHOM));
            int sum = cursor.getInt(cursor.getColumnIndex(Calculation.SUMMA));

            if (idWho == userId) {
                msg_whom +=  smileCancel + " " + sum + "руб — " + nameWhom + "\n";
            } else {
                msg_who +=  smileApply + " " + sum + "руб — " + nameWho + "\n";
            }
            //message += nameWho + " -> " + sum + "руб. -> " + nameWhom + " ";
        }

        if (!"".equals(msg_who)) {
            message += "Вам должны: \n";
            message += msg_who;
        }
        message += "\n";

        if (!"".equals(msg_whom)) {
            message += "Вы должны: \n";
            message += msg_whom;
        }
        //message += "\nС Уважением, и бесконечной любовью,\n";
        message += "\n Искренне Ваш Everpay\n";
        message += "https://play.google.com/store/apps/details?id=com.beautyteam.everpay";

        return message;
    }
}

