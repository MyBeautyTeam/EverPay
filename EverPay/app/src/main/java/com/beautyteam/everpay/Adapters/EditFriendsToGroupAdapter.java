package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.Fragments.FragmentEditFriendsInGroup;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by asus on 29.04.2015.
 */
public class EditFriendsToGroupAdapter extends CursorAdapter implements SectionIndexer {
    private MainActivity mainActivity;
    private final LayoutInflater inflater;
    private String sections = "";
    private int groupId;
    private FragmentEditFriendsInGroup friendsInGroup;

    public EditFriendsToGroupAdapter(Context context, Cursor c, int flags, MainActivity mainActivity, int groupId, FragmentEditFriendsInGroup fragmentEditFriends) {
        super(context, c, flags);
        this.groupId = groupId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
        this.friendsInGroup = fragmentEditFriends;
        int i = 0;
        c.moveToFirst();
        if(c.getInt(c.getColumnIndex(Users.USER_ID_VK)) == 0)
            sections +="\u2606";
        else
            sections += "" + c.getString(c.getColumnIndex(Users.NAME)).charAt(0);
        while(c.moveToNext()) {
            if(c.getInt(c.getColumnIndex(Users.USER_ID_VK)) != 0)
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
        int userVkId = cursor.getInt(cursor.getColumnIndex(Users.USER_ID_VK));
        holder.firstName.setText(name);
        if(cursor.getPosition() != 0) {
            cursor.moveToPrevious();
            if (userVkId == 0)
                holder.separator.setText("");
            else if ((cursor.getInt(cursor.getColumnIndex(Users.USER_ID_VK)) == 0)||(cursor.getString(cursor.getColumnIndex(Users.NAME)).charAt(0) != name.charAt(0)))
                holder.separator.setText(name.subSequence(0, 1));
            else
                holder.separator.setText("");
            cursor.moveToNext();
        } else {
            if(cursor.getInt(cursor.getColumnIndex(Users.USER_ID_VK)) == 0)
                holder.separator.setText("\u2605");
            else
                holder.separator.setText(name.subSequence(0, 1));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsInGroup.addMemberToGroup(id, groupId);
            }
        });

        String avatarUrl = cursor.getString(cursor.getColumnIndex(Users.IMG));
        Picasso.with(context)
                .load(avatarUrl)
                .placeholder(context.getResources().getDrawable(R.drawable.default_image))
                .error(context.getResources().getDrawable(R.drawable.default_image))
                .resize(100, 100)
                .centerInside()
                .into(holder.avatar);
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
        if (section == 0 ) {
            return 0;
        } else
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
