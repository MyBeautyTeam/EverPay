package com.beautyteam.everpay.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;

import java.util.Random;

/**
 * Created by Admin on 03.06.2015.
 */
public class DialogDebtorsAdapter extends CursorAdapter {

    private final LayoutInflater inflater;


    public DialogDebtorsAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_dialog_debts, viewGroup, false);

        ViewHolder holder = new ViewHolder();
        holder.bill = (TextView) itemLayout.findViewById(R.id.item_dialog_bill);
        holder.summa = (TextView) itemLayout.findViewById(R.id.item_dialog_summa);

        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final String groupTitle = cursor.getString(cursor.getColumnIndex(Debts.GROUP_TITLE));
        holder.bill.setText(groupTitle);

        int sum =  cursor.getInt(cursor.getColumnIndex(Debts.SUMMA));
        holder.summa.setText(sum + " \u20BD");

        final int groupId = cursor.getInt(cursor.getColumnIndex(Debts.GROUP_ID));
        holder.groupId = groupId;
        holder.groupTitle = groupTitle;

    }

    public static class ViewHolder {
        public TextView summa;
        public TextView bill;
        public int groupId;
        public String groupTitle;
    }

}
