package com.beautyteam.everpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.beautyteam.everpay.Database.EverContentProvider;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

import static com.beautyteam.everpay.Constants.Preference.SHARED_PREFERENCES;


public class LoginActivity extends Activity {
    private static String VK_APP_ID = "4799302";
    private static String sTokenKey = "VK_ACCESS_TOKEN";
    private static String[] sMyScope = new String[]{VKScope.FRIENDS, VKScope.PHOTOS, VKScope.MESSAGES};
    private SharedPreferences sPref;
    private String screenName = "Авторизация";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VKUIHelper.onCreate(this);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //205932A2E24B6EF94D38AEB2A9F7CC920E2B84D4 - проверка отпечатка Сертификата
        sPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_MULTI_PROCESS);
        VKSdk.initialize(sdkListener, VK_APP_ID,VKAccessToken.tokenFromSharedPreferences(this,sTokenKey));

        if (!isPreviousCorrectEnds()) { // Проверка, что предыдущая оперция входа выполнилась
            return;
        }

        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);

        if (VKSdk.wakeUpSession()) {
            Log.d("vk", " wake up ");

            Intent intentStartMain = new Intent(LoginActivity.this, MainActivity.class);
            if (Constants.Action.NOTIFICATION.equals(getIntent().getAction())) { //Если интент пришел из нотификации

                Bundle arg = getIntent().getExtras();

                int billId = arg.getInt(Constants.IntentParams.BILL_ID, 0);
                int groupId = arg.getInt(Constants.IntentParams.GROUP_ID, 0);

                intentStartMain.setAction(Constants.Action.NOTIFICATION);
                intentStartMain.putExtra(Constants.IntentParams.BILL_ID, billId);
                intentStartMain.putExtra(Constants.IntentParams.GROUP_ID, groupId);
            }
            startActivity(intentStartMain);
            this.finish();

        } else {
            Log.d("vk", " no wake up");
            loginButton.setVisibility(View.VISIBLE);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKSdk.authorize(sMyScope, true, false);
                Log.d("vk", " click");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        Log.d("vk", " resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        Log.d("vk", " destry");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
        Log.d("vk", " onactivity result");
    }

    private VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
            Log.d("vk", " oncaptcha");
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
            Log.d("vk", " ontoken expired");
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            //new AlertDialog.Builder(LoginActivity.this)
            //        .setMessage("Необходимо авторизоваться")
            //        .show();
            Log.d("vk", " ondenied");

        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            String accessToken = newToken.accessToken;
            newToken.saveTokenToSharedPreferences(LoginActivity.this,sTokenKey);
            Log.d("TOken receive", accessToken);
            Log.d("TOken receivw", String.valueOf(newToken.expiresIn));
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            LoginActivity.this.finish();
            Log.d("vk", " onreceive");
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            String accessToken = token.accessToken;
            Log.d("TOken accept", accessToken);
            Log.d("TOken accept", String.valueOf(token.expiresIn));
            //Intent i = new Intent(LoginActivity.this, MainActivity.class);
            //startActivity(i);
            //LoginActivity.this.finish();
            Log.d("vk", " onaccept");
        }
    };

    /*
    Проверка, завершился ли предыдущий запрос.
    Если некоректно, то выполняем действия, для инвалидации некоректной информации.

     */
    private boolean isPreviousCorrectEnds(){
        int registrationStatus = sPref.getInt(Constants.Preference.REGISTATION_STATUS, Constants.State.ENDS);
        if (registrationStatus == Constants.State.IN_PROCESS) {
            VKSdk.logout();
            clearData();
            sPref.edit()
                    .putInt(Constants.Preference.REGISTATION_STATUS, Constants.State.ENDS)
                    .commit();
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();
            return false;
        }
        return true;
    }

    public void clearData() {
        sPref.edit().clear().commit();

        getContentResolver().delete(EverContentProvider.USERS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.GROUPS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.DEBTS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.CALCULATION_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.GROUP_MEMBERS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.BILLS_CONTENT_URI, null, null);
        getContentResolver().delete(EverContentProvider.HISTORY_CONTENT_URI, null, null);

    }


}