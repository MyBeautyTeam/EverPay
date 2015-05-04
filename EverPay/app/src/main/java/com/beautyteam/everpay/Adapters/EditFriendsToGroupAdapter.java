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
    private AlphabetIndexer indexer;
    private final LayoutInflater inflater;
    private User userCheck;
    private HashSet<String> set = new HashSet<String>();
    ArrayList<User> arrayList;

    public EditFriendsToGroupAdapter(Context context, Cursor c, int flags, MainActivity mainActivity) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexer = new AlphabetIndexer(c,
                c.getColumnIndex(Users.NAME)," "+
                "ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");//ABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
        indexer.setCursor(c);
        this.mainActivity = mainActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.item_edit_friend, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) view.findViewById(R.id.item_edit_friends_in_group_name);
        holder.avatar = (RoundedImageView) view.findViewById(R.id.item_edit_friends_in_group_avatar);
//        holder.separator = (TextView) view.findViewById(R.id.separator);
//        holder.separator.setVisibility(View.INVISIBLE);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder holder = (ViewHolder)view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(Users.NAME));
        holder.firstName.setText(name);

        Log.d("pos cursor", "name= " + name + " cursor=" + String.valueOf(cursor.getPosition()) + " section=" + String.valueOf(getSectionForPosition(cursor.getPosition())));

//        if (getSectionForPosition(cursor.getPosition()) == 0)
//            holder.separator.setText(name.subSequence(0,1));
//        else
//            holder.separator.setVisibility(View.VISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick",holder.firstName.toString());
                                //добавить в базу
                mainActivity.getServiceHelper().addMemberToGroup(cursor.getInt(cursor.getColumnIndex(Users.USER_ID)), 8);
                //
                mainActivity.removeFragment();

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
    }





}
