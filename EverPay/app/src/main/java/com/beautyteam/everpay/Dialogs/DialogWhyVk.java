package com.beautyteam.everpay.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.beautyteam.everpay.R;

/**
 * Created by popka on 11.02.16.
 */
public class DialogWhyVk extends Dialog implements View.OnClickListener {


    public DialogWhyVk(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_vk_only);
        Button okBtn = (Button) findViewById(R.id.dialog_btn_ok);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}

