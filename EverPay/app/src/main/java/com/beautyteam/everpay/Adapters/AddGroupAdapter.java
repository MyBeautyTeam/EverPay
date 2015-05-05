package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asus on 12.04.2015.
 */
public class AddGroupAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<User> friendsArrayList;
    private HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();
    private User creator;

    public AddGroupAdapter(Context _context,ArrayList <User> arrayList, User user) {
        this.context = _context;
        friendsArrayList = arrayList;
        this.creator = user;
        if (friendsArrayList.isEmpty())
            friendsArrayList.add(user);
        else if (friendsArrayList.get(friendsArrayList.size()-1).getId() != user.getId())
            friendsArrayList.add(user);
    }


    @Override
    public int getCount() {
        return friendsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_add_group, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_add_friends_to_group_name);
            viewHolder.avatar = (RoundedImageView) convertView.findViewById(R.id.item_add_friends_to_group_avatar);
            viewHolder.remove = (ImageView) convertView.findViewById(R.id.add_friend_to_group_remove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.remove.setVisibility(View.VISIBLE);
        final User user = friendsArrayList.get(position);
        viewHolder.name.setText(user.getName());
        if(creator.getId() == user.getId())
            viewHolder.remove.setVisibility(View.GONE);
        String avatarUrl = user.getPhoto();
        Picasso.with(context).load(avatarUrl).resize(100, 100).centerInside().into(viewHolder.avatar);

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsArrayList.remove(user);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        RoundedImageView avatar;
        ImageView remove;
    }

}
