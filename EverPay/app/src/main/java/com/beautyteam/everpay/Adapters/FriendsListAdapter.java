package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;

import java.util.Random;

/**
 * Created by asus on 26.03.2015.
 */
public class FriendsListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public FriendsListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_friend, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) itemLayout.findViewById(R.id.friends_list_item_discript);
       // holder.secondName = (TextView) itemLayout.findViewById(R.id.calc_second_name);
        holder.checkBox = (CheckBox) itemLayout.findViewById(R.id.friends_checkbox);
        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        holder.firstName.setText(cursor.getString(cursor.getColumnIndex(Users.NAME)));
       // holder.secondName.setText("Егор\nРакитянский");
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
        //TextView secondName;
        CheckBox checkBox;
    }
}
