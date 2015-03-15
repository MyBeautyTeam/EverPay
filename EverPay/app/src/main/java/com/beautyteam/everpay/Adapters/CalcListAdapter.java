package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Random;

/**
 * Created by Admin on 15.03.2015.
 */
public class CalcListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public CalcListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_calculation, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) itemLayout.findViewById(R.id.calc_first_name);
        holder.summa = (TextView) itemLayout.findViewById(R.id.calc_summa);
        holder.secondName = (TextView) itemLayout.findViewById(R.id.calc_second_name);
        holder.checkBox = (CheckBox) itemLayout.findViewById(R.id.calc_checkbox);
        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        holder.firstName.setText("Танька\nПетрова");
        holder.summa.setText(new Random().nextInt(10000) + "");
        holder.secondName.setText("Егор\nРакитянский");
        if ((new Random().nextInt() % 2) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView firstName;
        TextView secondName;
        TextView summa;
        CheckBox checkBox;
    }
}
