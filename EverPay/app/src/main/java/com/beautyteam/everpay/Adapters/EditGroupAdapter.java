package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;

/**
 * Created by asus on 29.04.2015.
 */
public class EditGroupAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private MainActivity mainActivity;

    public EditGroupAdapter(Context context, Cursor c, int flags, MainActivity mainActivity) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_add_group, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) view.findViewById(R.id.item_add_friends_to_group_name);
        holder.avatar = (RoundedImageView) view.findViewById(R.id.item_add_friends_to_group_avatar);
        holder.remove = (ImageView) view.findViewById(R.id.add_friend_to_group_remove);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(Users.NAME));
        holder.firstName.setText(name);
       // String avatarUrl = cursor.getString(cursor.getColumnIndex(Users.IMG));
       // Picasso.with(context).load(avatarUrl).resize(100, 100).centerInside().into(holder.avatar);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Удалить из базы
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView firstName;
        RoundedImageView avatar;
        ImageView remove;
    }
}
