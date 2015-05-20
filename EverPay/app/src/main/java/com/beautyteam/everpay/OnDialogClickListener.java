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
public interface OnDialogClickListener  {
    void onResendBill(int id);
    void onDeleteBill(int id);
}
