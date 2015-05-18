package com.beautyteam.everpay;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by asus on 15.05.2015.
 */
public class DialogAction  extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog d;
    public Button send, delete;
    private int layout;

    public DialogAction(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_not_send);
        send = (Button) findViewById(R.id.dialog_action_btn_send);
        delete = (Button) findViewById(R.id.dialog_action_btn_delete);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }


    public void setOnSendClickListener(View.OnClickListener onClickListener) {
        send.setOnClickListener(onClickListener);
    }

    public void setOnDeleteClickListener(View.OnClickListener onClickListener) {
        delete.setOnClickListener(onClickListener);
    }
}