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

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by asus on 26.03.2015.
 */
public class FriendsListAdapter extends CursorAdapter implements StickyListHeadersAdapter {

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_friend, parent, false);
            holder.firstName = (TextView) convertView.findViewById(R.id.friends_list_item_discript);
            // holder.secondName = (TextView) itemLayout.findViewById(R.id.calc_second_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.friends_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.firstName.setText("Танька Петрова");
        if ((new Random().nextInt() % 2) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + holder.subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return countries[position].subSequence(0, 1).charAt(0);
    }

    private static class ViewHolder {
        TextView firstName;
        //TextView secondName;
        CheckBox checkBox;
    }

    private static class HeaderViewHolder {
        TextView text;
    }
}
