package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Bills;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.R;
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
 * Created by Admin on 22.04.2015.
 */
public class ShowBillListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();

    public ShowBillListAdapter(Context context, final Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadAvatarsFromVK(c);
            }
        });
    }

    private void loadAvatarsFromVK(Cursor c) {
        String usersId = "";
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(Bills.USER_ID_VK));
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
        View itemLayout = inflater.inflate(R.layout.item_show_bill, viewGroup, false);
        ViewHolder holder = new ViewHolder();

        holder.name = (TextView) itemLayout.findViewById(R.id.show_bill_list_name);
        holder.need = (TextView) itemLayout.findViewById(R.id.show_bill_list_need);
        holder.invest = (TextView) itemLayout.findViewById(R.id.show_bill_list_invest);
        holder.avatar = (RoundedImageView) itemLayout.findViewById(R.id.show_bill_item_avatar);

        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String userName = cursor.getString(cursor.getColumnIndex(Bills.USER_NAME));
        userName = userName.replace(" ", "\n");
        holder.name.setText(userName);
        holder.need.setText(cursor.getString(cursor.getColumnIndex(Bills.NEED_SUM)));
        holder.invest.setText(cursor.getString(cursor.getColumnIndex(Bills.INVEST_SUM)));

        String userId = cursor.getString(cursor.getColumnIndex(Bills.USER_ID_VK));
        String img = mapIdToAvatar.get(userId);
        Picasso.with(context).load(img).resize(100,100).centerInside().into(holder.avatar);


    }

    private static class ViewHolder {
        TextView name;
        TextView need;
        TextView invest;
        RoundedImageView avatar;
    }

}
