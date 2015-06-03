package com.beautyteam.everpay.Adapters;

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

import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;

import java.util.Random;

/**
 * Created by Admin on 03.06.2015.
 */
public class DialogDebtorsAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private int redColor;
    private int greenColor;

    public DialogDebtorsAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        redColor = context.getResources().getColor(R.color.light_red);
        greenColor = context.getResources().getColor(R.color.light_green);

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
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.bill.setText("Счет за что-то");

        int summa = new Random().nextInt(10000) - 5000;
        if (summa > 0) {
            view.setBackgroundColor(greenColor);
            holder.summa.setText("+" + summa);
        }
        else {
            view.setBackgroundColor(redColor);
            holder.summa.setText("" + summa);
        }



    }

    private static class ViewHolder {
        TextView summa;
        TextView bill;
    }

}
