package com.beautyteam.everpay.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beautyteam.everpay.Adapters.DialogDebtorsAdapter;
import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Fragments.FragmentCalculation;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.Fragments.FragmentGroups;
import com.beautyteam.everpay.Fragments.FragmentViewPager;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.beautyteam.everpay.Adapters.DialogDebtorsAdapter.*;

/**
 * Created by Admin on 03.06.2015.
 */
public class DialogDebtDetail extends Dialog implements View.OnClickListener {

    private int isIDebt;
    private int sum;
    private int userId;
    private String userIdVk;
    private String img;
    private String userName;


    private TextView dialogDebtName;
    private Button showProfileBtn;
    private ListView listView;
    private TextView summaTextView;

    private MainActivity mainActivity;

    public DialogDebtDetail(Context context, String userName, int userId, String userIdVk, int isIDebt, String img, int sum) {
        super(context);
        this.isIDebt = isIDebt;
        this.userId = userId;
        this.img = img;
        this.userName = userName;
        this.sum = sum;
        this.userIdVk = userIdVk;
        mainActivity = (MainActivity) context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_debt_detail);
        showProfileBtn = (Button) findViewById(R.id.dialog_btn_show_profile);
        showProfileBtn.setOnClickListener(this);
        if ("0".equals(userIdVk)) {
            showProfileBtn.setEnabled(false);
            showProfileBtn.setText("Нет профиля vk");
        }

        dialogDebtName = (TextView) findViewById(R.id.dialog_debt_name);
        dialogDebtName.setText(userName);

        summaTextView = (TextView) findViewById(R.id.dialog_debts_sum);
        summaTextView.setText("Долг: "+sum+"");

        listView = (ListView) findViewById(R.id.dialog_debt_list);
        Cursor c = getContext().getContentResolver().query(EverContentProvider.DEBTS_CONTENT_URI_DIALOG, PROJECTION_BILL, Debts.USER_ID +"=" + userId + " AND " + Debts.IS_I_DEBT + "=" + isIDebt, null, null);
        listView.setAdapter(new DialogDebtorsAdapter(getContext(), c) );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder holder = (ViewHolder) view.getTag();
                dismiss();
                FragmentViewPager.isLoaded = true;
                mainActivity.addFragmentWithoutAnim(FragmentCalculation.getInstance(holder.groupId));
            }
        });

        CircleImageView avatar = (CircleImageView)findViewById(R.id.dialog_debt_avatar);

        Picasso.with(getContext())
                .load(img)
                .error(getContext().getResources().getDrawable(R.drawable.default_image))
                .placeholder(getContext().getResources().getDrawable(R.drawable.default_image))
                .resize(100, 100)
                .centerInside()
                .into(avatar);
    }

    @Override
    public void onClick(View view) {
        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/id"+userIdVk)));
    }

    private static final String[] PROJECTION_BILL = new String[] {
            Debts.ITEM_ID,
            Debts.SUMMA,
            Debts.IS_I_DEBT,
            Debts.GROUP_TITLE,
            Debts.GROUP_ID
    };
}
