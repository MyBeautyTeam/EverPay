package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by asus on 12.04.2015.
 */
public class AddGroupAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<User> friendsArrayList;

    public AddGroupAdapter(Context _context,ArrayList <User> arrayList) {
        this.context = _context;
        friendsArrayList =arrayList;
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
            convertView = inflater.from(context).inflate(R.layout.item_add_friends_to_group, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_add_friends_to_group_name);
            viewHolder.avatar = (RoundedImageView) convertView.findViewById(R.id.item_add_friends_to_group_avatar);
            viewHolder.remove = (ImageView) convertView.findViewById(R.id.add_friend_to_group_remove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final User user = friendsArrayList.get(position);
        viewHolder.name.setText(user.getName());
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constants.FILE_DIRECTORY + '/' + user.getPhoto();

        File file = new File(filePath);
        Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.avatar);

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
