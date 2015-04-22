package com.beautyteam.everpay.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.EverContentProvider;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.Database.Users;
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
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Admin on 10.03.2015.
 */
public class DebtorsListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();

    public DebtorsListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loadAvatarsFromVK(c);
    }


    private void loadAvatarsFromVK(Cursor c) {
        String usersId = "";
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(GroupMembers.USER_ID));
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
        View itemLayout = inflater.inflate(R.layout.item_debtors, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.summa = (TextView) itemLayout.findViewById(R.id.debtors_list_item_summa);
        holder.discript = (TextView) itemLayout.findViewById(R.id.debtors_list_item_discript);
        holder.avatar = (RoundedImageView) itemLayout.findViewById(R.id.debtors_list_item_avatar);
        itemLayout.setTag(holder);
        return itemLayout;
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        holder.summa.setText(cursor.getString(cursor.getColumnIndex(Debts.SUMMA)));

        String userName = cursor.getString(cursor.getColumnIndex(Debts.USER_NAME));
        String group = cursor.getString(cursor.getColumnIndex(Debts.GROUP_TITLE));

        if (userName == null) {
            holder.discript.setText(group);
            Picasso.with(context).load(R.drawable.group_icon).resize(200, 200).centerInside().into(holder.avatar);
        }
        else {
            holder.discript.setText(userName + ", " + group);
            String id = cursor.getString(cursor.getColumnIndex(Debts.USER_ID));
            String img = mapIdToAvatar.get(id);

            Picasso.with(context).load(img).resize(100,100).centerInside().into(holder.avatar);

        }
    }



    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView summa;
        TextView discript;
        RoundedImageView avatar;
    }
}

