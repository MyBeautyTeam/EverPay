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
    private AlphabetIndexer indexer;
    private final LayoutInflater inflater;
    private User userCheck;
    private HashSet <String>  set = new HashSet<String>();
    ArrayList <User> arrayList;

    public AddFriendsToGroupAdapter(Context context, Cursor c, int flags,ArrayList <User> arrayList) {
        super(context, c, flags);


        int count = c.getCount();
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String billId = c.getString(c.getColumnIndex(Users.USER_ID));
                String billTitle = c.getString(c.getColumnIndex(Users.USER_ID_VK));
                String groupId = c.getString(c.getColumnIndex(Users.IMG));
                String userId = c.getString(c.getColumnIndex(Users.NAME));
                        /*String userName = c.getString(c.getColumnIndex(Bills.USER_NAME));
                        String need = c.getString(c.getColumnIndex(Bills.NEED_SUM));
                        String invest = c.getString(c.getColumnIndex(Bills.INVEST_SUM));*/

                //String oldText = textView.getText().toString();
               // S.ring newText = oldText + "\n" + billId + "  " + billTitle + "  " + groupId + "  ";// + userId + "  " + userName + "   " + invest + "   " + need;
                //textView.setText(newText);
                c.moveToNext();
            }
        }



        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexer = new AlphabetIndexer(c,
                c.getColumnIndex(Users.NAME)," "+
                "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");//ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
        indexer.setCursor(c);
//        c.moveToPosition(-1);
//        while(c.moveToNext()) {
//            Log.d("cur",c.getString(c.getColumnIndex(Users.NAME)));
//        }
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
                user = new User(Integer.valueOf(idName), name, "", img);
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
        holder.separator.setVisibility(View.GONE);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(Users.NAME));
        holder.firstName.setText(name);
        //final Integer position = cursor.getPosition();
        final String idName = cursor.getString(cursor.getColumnIndex(Users.USER_ID_VK));
        boolean isChecked = false;
            if (set.contains(idName)) {
                isChecked = true;
            }

        holder.checkBox.setChecked(isChecked);
        Log.d("pos cursor","name= " +name +" cursor=" + String.valueOf(cursor.getPosition())+" section="+ String.valueOf(getSectionForPosition(cursor.getPosition())));

        if (getSectionForPosition(cursor.getPosition())==0) {
            holder.separator.setText(name.subSequence(0, 1));
            holder.separator.setVisibility(View.VISIBLE);
        }
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

        String avatarUrl = cursor.getString(cursor.getColumnIndex(Users.IMG));
        Picasso.with(context).load(avatarUrl).resize(100, 100).centerInside().into(holder.avatar);
        name = "";
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

//    public Cursor swapCursor(Cursor c) {
////        if(c!= null) {
////            indexer = new AlphabetIndexer(c,
////                    c.getColumnIndex(Users.NAME),
////                    " ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");
////          //  indexer.setCursor(c);
////        }
//        return super.swapCursor(c);
//    }

    private static class ViewHolder {
        TextView separator;
        TextView firstName;
        RoundedImageView avatar;
        CheckBox checkBox;
    }



}