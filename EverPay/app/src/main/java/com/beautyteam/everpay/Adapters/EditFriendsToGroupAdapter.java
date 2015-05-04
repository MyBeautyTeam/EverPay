package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by asus on 29.04.2015.
 */
public class EditFriendsToGroupAdapter extends CursorAdapter implements SectionIndexer {
    private MainActivity mainActivity;
    private final LayoutInflater inflater;
    private String sections = "";
    private int groupId;

    public EditFriendsToGroupAdapter(Context context, Cursor c, int flags, MainActivity mainActivity, int groupId) {
        super(context, c, flags);
        this.groupId = groupId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
        int i = 0;
        c.moveToFirst();
        sections = "" + c.getString(c.getColumnIndex(Users.NAME)).charAt(0);
        while(c.moveToNext()) {
            if (c.getString(c.getColumnIndex(Users.NAME)).charAt(0) != sections.charAt(i)) {
                sections += "" + c.getString(c.getColumnIndex(Users.NAME)).charAt(0);
                i++;
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_edit_friend, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) view.findViewById(R.id.item_edit_friends_in_group_name);
        holder.avatar = (RoundedImageView) view.findViewById(R.id.item_edit_friends_in_group_avatar);
        holder.separator = (TextView) view.findViewById(R.id.separator);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder holder = (ViewHolder)view.getTag();
        final int id = cursor.getInt(cursor.getColumnIndex(Users.USER_ID));
        String name = cursor.getString(cursor.getColumnIndex(Users.NAME));
        holder.firstName.setText(name);
        holder.separator.setPadding(0,0,0,0);
        if(cursor.getPosition() != 0) {
            cursor.moveToPrevious();
            if (cursor.getString(cursor.getColumnIndex(Users.NAME)).charAt(0) != name.charAt(0)) {
                holder.separator.setText(name.subSequence(0, 1));
            } else {
                holder.separator.setText("");
                holder.separator.setPadding(32,0,0,0);
            }
            cursor.moveToNext();
        } else {
            holder.separator.setText(name.subSequence(0, 1));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemberToGroup(id,groupId);
                mainActivity.removeFragment();

            }
        });

        String avatarUrl = cursor.getString(cursor.getColumnIndex(Users.IMG));
        Picasso.with(context).load(avatarUrl).resize(100, 100).centerInside().into(holder.avatar);
        name = "";
    }

    private void addMemberToGroup(int id, int groupId) {
        mainActivity.getServiceHelper().addMemberToGroup(id, groupId);

    }

    @Override
    public Object[] getSections() {
        String[] sectionsArr = new String[sections.length()];
        for (int i=0; i < sections.length(); i++)
            sectionsArr[i] = "" + sections.charAt(i);
        return sectionsArr;
    }

    @Override
    public int getPositionForSection(int section) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(Users.NAME)).charAt(0) == sections.charAt(section))
                return cursor.getPosition();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    private static class ViewHolder {
        TextView separator;
        TextView firstName;
        RoundedImageView avatar;
    }

}
