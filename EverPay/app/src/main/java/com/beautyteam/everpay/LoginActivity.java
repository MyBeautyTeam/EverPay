package com.beautyteam.everpay;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;


public class LoginActivity extends Activity {
    private static String VK_APP_ID = "4799302";
    private static String sTokenKey = "VK_ACCESS_TOKEN";
    private static String[] sMyScope = new String[]{VKScope.FRIENDS, VKScope.PHOTOS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
         String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //205932A2E24B6EF94D38AEB2A9F7CC920E2B84D4 - проверка отпечатка Сертификата
        VKSdk.initialize(sdkListener, VK_APP_ID);

        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKSdk.authorize(sMyScope, true, false);
                Log.d("vk", " click");
            }
        });

        if (VKSdk.wakeUpSession()) {
            Log.d("vk", " wake up ");
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        } else {
            Log.d("vk", " no wake up");
            loginButton.setVisibility(View.VISIBLE);
        }
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
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            LoginActivity.this.finish();
            Log.d("vk", " onreceive");
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            LoginActivity.this.finish();
            Log.d("vk", " onaccept");
        }
    };
}