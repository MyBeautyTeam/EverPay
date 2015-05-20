package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.DialogWindow;
import com.beautyteam.everpay.Fragments.FragmentEditGroup;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.User;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.HashMap;

/**
 * Created by asus on 29.04.2015.
 */
public class EditGroupAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private MainActivity mainActivity;
    private DialogWindow dialogWindow;
    HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();
    private User creator;
    private int groupId;
    private FragmentEditGroup fragmentEditGroup;

    public EditGroupAdapter(Context context, final Cursor c, int flags, MainActivity mainActivity, User user, int groupId, FragmentEditGroup fragmentEditGroup) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadAvatarsFromVK(c);
            }
        });
        creator = user;
        this.groupId = groupId;
        this.fragmentEditGroup = fragmentEditGroup;
    }

    private void loadAvatarsFromVK(Cursor c) {
        String usersId = "";
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(GroupMembers.USER_ID_VK));
                if (id != null)
                    usersId += id + ",";
                c.moveToNext();
            }
        }

        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, usersId, VKApiConst.FIELDS, "photo_100"));
        VKBatchRequest batch = new VKBatchRequest(request);

        batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                VKList<VKApiUser> userList = (VKList<VKApiUser>) responses[0].parsedModel;
                int count = userList.size();
                for (int i=0; i<count; i++)
                    mapIdToAvatar.put(userList.get(i).id + "", userList.get(i).photo_100);

                notifyDataSetChanged();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }
        });
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
        holder.remove.setVisibility(View.VISIBLE);
        String name = cursor.getString(cursor.getColumnIndex(GroupMembers.USER_NAME));
        holder.firstName.setText(name);
        final int id = cursor.getInt(cursor.getColumnIndex(GroupMembers.USER_ID));
        String id_vk = cursor.getString(cursor.getColumnIndex(GroupMembers.USER_ID_VK));
        String img = mapIdToAvatar.get(id_vk);
        Picasso.with(context).load(img).resize(100,100).centerInside().into(holder.avatar);
        if (creator.getId() == cursor.getInt(cursor.getColumnIndex(GroupMembers.USER_ID)))
            holder.remove.setVisibility(View.GONE);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogWindow = new DialogWindow(mainActivity,R.layout.dialog_delete_friend);
                Window window = dialogWindow.getWindow();
                window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogWindow.show();
                dialogWindow.setOnYesClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogWindow.dismiss();
                        fragmentEditGroup.removeUserFromGroup(id, groupId);
                        //notifyDataSetChanged();
                    }
                });
                //Удалить из базы
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
