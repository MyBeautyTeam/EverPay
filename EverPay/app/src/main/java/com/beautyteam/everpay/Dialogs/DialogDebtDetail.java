package com.beautyteam.everpay.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.beautyteam.everpay.Adapters.DialogDebtorsAdapter;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 03.06.2015.
 */
public class DialogDebtDetail extends Dialog implements View.OnClickListener {

    private Button showProfileBtn;
    private ListView listView;


    public DialogDebtDetail(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_debt_detail);
        showProfileBtn = (Button) findViewById(R.id.dialog_btn_show_profile);
        showProfileBtn.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.dialog_debt_list);

        Cursor c = getContext().getContentResolver().query(EverContentProvider.BILLS_CONTENT_URI, PROJECTION_BILL, null, null, null);
        listView.setAdapter(new DialogDebtorsAdapter(getContext(), c) );
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    private static final String[] PROJECTION_BILL = new String[] {
            Bills.ITEM_ID,
            Bills.BILL_ID,
            Bills.TITLE,
            Bills.USER_ID_VK,
            Bills.USER_ID,
            Bills.USER_NAME,
            Bills.GROUP_ID,
            Bills.NEED_SUM,
            Bills.INVEST_SUM
    };
}
