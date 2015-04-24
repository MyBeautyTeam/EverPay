package com.beautyteam.everpay;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Admin on 04.04.2015.
 */
public class DialogWindow extends Dialog implements View.OnClickListener{
    public Activity activity;
    public Dialog d;
    public Button yes, no;

    public DialogWindow(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete);
        yes = (Button) findViewById(R.id.dialog_btn_yes);
        no = (Button) findViewById(R.id.dialog_btn_no);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setOnYesClickListener(View.OnClickListener onClickListener) {
        yes.setOnClickListener(onClickListener);
    }
}
