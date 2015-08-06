package com.beautyteam.everpay.REST.Processors;

import android.content.Context;
import android.content.Intent;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.REST.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.beautyteam.everpay.Constants.Action.BUG_REPORT;

/**
 * Created by popka on 06.08.15.
 */
public class EmailerProcessor extends Processor {

    public EmailerProcessor(Context context) {
        super(context);
    }

    @Override
    public void request(Intent intent, Service service) {

        String action = intent.getAction();
        int result = Constants.Result.OK;

        if (BUG_REPORT.equals(action)) {
            String theme = intent.getStringExtra(Constants.IntentParams.THEME);
            String msg = intent.getStringExtra(Constants.IntentParams.EMAIL_MSG);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("key", "BWEX1YSrlFILNCyOzs4k4g");

                JSONObject jsonMsg = new JSONObject();
                jsonMsg.put("text", msg);
                jsonMsg.put("subject", theme);
                jsonMsg.put("from_email", "everpay.user@gmail.com");
                jsonMsg.put("from_email", "everpay.user@gmail.com");
                jsonMsg.put("from_name", "EverpayUser");

                JSONArray toArray = new JSONArray();
                JSONObject to = new JSONObject();
                to.put("email", "everpay@yandex.ru");
                to.put("name", "Recipient Name");
                to.put("type", "to");
                toArray.put(0, to);

                jsonMsg.put("to", toArray);
                jsonObject.put("message", jsonMsg);

                String response = urlConnectionPost("https://mandrillapp.com/api/1.0/messages/send.json", jsonObject.toString());
                if (response == null || response.contains("error")) {
                    result = Constants.Result.ERROR;
                }



                //jsonObject.put("message", new )
            } catch (JSONException e) {
                result = Constants.Result.ERROR;
            }

            service.onRequestEnd(result, intent);

        }
    }
}
