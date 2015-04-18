package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by asus on 26.03.2015.
 */
public class AddFriendsToGroupAdapter extends CursorAdapter implements SectionIndexer {
    private AlphabetIndexer indexer;
    private final LayoutInflater inflater;
    private User userCheck;
    private HashSet <String>  set = new HashSet<String>();
    ArrayList <User> arrayList;

    public AddFriendsToGroupAdapter(Context context, Cursor c, int flags,ArrayList <User> arrayList) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexer = new AlphabetIndexer(c,
                c.getColumnIndex(Users.NAME),
                " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        indexer.setCursor(c);
        this.arrayList = arrayList;
        Iterator <User> itr = this.arrayList.iterator();
        while(itr.hasNext()) {
               set.add(String.valueOf(itr.next().getId()));
        }
    }

    public Set<String> getSet() { return set;}

    public ArrayList<User> getArrayList() {
        Cursor cursor = getCursor();
        User user;
        String idName;
        String name;
        String img;
        arrayList.clear();
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            idName = cursor.getString(cursor.getColumnIndex(Users.USER_ID_VK));
            if (set.contains(idName)) {
                name = cursor.getString(cursor.getColumnIndex(Users.NAME));
                img = cursor.getString(cursor.getColumnIndex(Users.IMG));
                user = new User(Integer.valueOf(idName), name, img, "");
                arrayList.add(user);
            }
        }
        return arrayList;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_friend, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) view.findViewById(R.id.friends_list_item_discript);
        holder.checkBox = (CheckBox) view.findViewById(R.id.friends_checkbox);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        holder.firstName.setText(cursor.getString(cursor.getColumnIndex(Users.NAME)));
        //final Integer position = cursor.getPosition();
        final String idName = cursor.getString(cursor.getColumnIndex(Users.USER_ID_VK));
        boolean isChecked = false;
            if (set.contains(idName)) {
                isChecked = true;
            }

        holder.checkBox.setChecked(isChecked);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox c = (CheckBox) view;
                if (!c.isChecked()) {
                    set.remove(idName);
                } else {
                    set.add(idName);
                }
            }
        });
    }

    @Override
    public Object[] getSections() {
        return indexer.getSections();
    }

    @Override
    public int getPositionForSection(int i) {
        return indexer.getPositionForSection(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return indexer.getSectionForPosition(i);
    }

    public Cursor swapCursor(Cursor c) {
        if(c!= null) {
            indexer = new AlphabetIndexer(c,
                    c.getColumnIndex(Users.NAME),
                    " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        return super.swapCursor(c);
    }

    private static class ViewHolder {
        TextView firstName;
        RoundedImageView avatar;
        CheckBox checkBox;
    }


}