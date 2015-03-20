package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.R;

import java.net.CookieHandler;
import java.util.Random;

/**
 * Created by Admin on 15.03.2015.
 */
public class AddBillListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public static int TEXT_VIEW_MODE = 1;
    public static int EDIT_TEXT_MODE = 2;

    private int mode=TEXT_VIEW_MODE;

    public AddBillListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemMode(int _mode) {
        mode = _mode;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_add_bill, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) itemLayout.findViewById(R.id.add_bill_list_name);
        holder.editNeed = (EditText) itemLayout.findViewById(R.id.add_bill_list_need_edit);
        holder.textNeed = (TextView) itemLayout.findViewById(R.id.add_bill_list_need_text);
        holder.put = (EditText) itemLayout.findViewById(R.id.add_bill_list_put);
        holder.remove = (ImageView) itemLayout.findViewById(R.id.add_bill_list_remove);
        itemLayout.setTag(holder);
        Log.d(Constants.LOG, "newView");
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        holder.name.setText("Танька Петрова");
        if (mode == TEXT_VIEW_MODE) {
            holder.textNeed.setVisibility(View.VISIBLE);
            holder.editNeed.setVisibility(View.GONE);
        } else {
            holder.textNeed.setVisibility(View.GONE);
            holder.editNeed.setVisibility(View.VISIBLE);
        }
        Log.d(Constants.LOG, "bindView");
        //holder.summa.setText(new Random().nextInt(10000) + "");
        //holder.secondName.setText("Егор\nРакитянский");
        //if ((new Random().nextInt() % 2) == 1) {
//            holder.checkBox.setChecked(true);
//        } else {
//            holder.checkBox.setChecked(false);
//        }
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView name;
        EditText editNeed;
        TextView textNeed;
        EditText put;
        ImageView remove;
    }


}
