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

import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by asus on 26.03.2015.
 */
public class AddFriendsToGroupAdapter extends CursorAdapter implements SectionIndexer {
    private final LayoutInflater inflater;
    private HashSet <String>  set = new HashSet<String>();
    private ArrayList <User> arrayList;
    private String sections = "";

    public AddFriendsToGroupAdapter(Context context, Cursor c, int flags,ArrayList <User> arrayList) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        Iterator <User> itr = this.arrayList.iterator();
        while(itr.hasNext()) {
               set.add(String.valueOf(itr.next().getId()));
        }
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

    public Set<String> getSet() { return set;}

    public ArrayList<User> getArrayList() {
        Cursor cursor = getCursor();
        User user;
        String id;
        String id_vk;
        String name;
        String img;
        arrayList.clear();
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(Users.USER_ID));
            if (set.contains(id)) {
                id_vk = cursor.getString(cursor.getColumnIndex(Users.USER_ID_VK));
                name = cursor.getString(cursor.getColumnIndex(Users.NAME));
                img = cursor.getString(cursor.getColumnIndex(Users.IMG));
                user = new User( Integer.valueOf(id),  Integer.valueOf(id_vk), name, "", img);
                arrayList.add(user);
            }
        }
        return arrayList;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_add_friend, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) view.findViewById(R.id.friends_list_item_discript);
        holder.checkBox = (CheckBox) view.findViewById(R.id.friends_checkbox);
        holder.avatar = (RoundedImageView) view.findViewById(R.id.friends_list_item_avatar);
        holder.separator = (TextView) view.findViewById(R.id.separator);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(Users.NAME));
        holder.firstName.setText(name);
        final String id = cursor.getString(cursor.getColumnIndex(Users.USER_ID));
        boolean isChecked = false;
            if (set.contains(id)) {
                isChecked = true;
            }
        holder.checkBox.setChecked(isChecked);

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
       holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox c = (CheckBox) view;
                if (!c.isChecked()) {
                    set.remove(id);
                } else {
                    set.add(id);
                }
            }
        });

        String avatarUrl = cursor.getString(cursor.getColumnIndex(Users.IMG));
        Picasso.with(context).load(avatarUrl).resize(100, 100).centerInside().into(holder.avatar);
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
        CheckBox checkBox;
    }



}