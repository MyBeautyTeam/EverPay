package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Fragments.FragmentAddBill;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 06.04.2015.
 */
public class AddFriendsToBillAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BillListItem> billFullArrayList;
    public ArrayList<BillListItem> billDeletedArrayList;
    private HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();

    public AddFriendsToBillAdapter(Context _context, ArrayList<BillListItem> billFullArrayList) {
        this.context = _context;
        this.billFullArrayList = billFullArrayList;
        billDeletedArrayList = new ArrayList<BillListItem>();
        for (int i=0; i<billFullArrayList.size(); i++) {
            if (billFullArrayList.get(i).isRemoved)
                billDeletedArrayList.add(billFullArrayList.get(i));
        }

        loadAvatarsFromVK();
    }

    private void loadAvatarsFromVK() {
        String usersId = "";
        int count = billDeletedArrayList.size();
        for (int i=0; i<count; i++) {
            String vkid = billDeletedArrayList.get(i).vkid;
            usersId += vkid + ",";
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
    public int getCount() {
        return billDeletedArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return billDeletedArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_add_friends_to_bill, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_add_friends_to_bill_name);
            viewHolder.avatar = (RoundedImageView) convertView.findViewById(R.id.item_add_friends_to_bill_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(billDeletedArrayList.get(position).name);

        String avatarUrl = mapIdToAvatar.get(billDeletedArrayList.get(position).vkid);
        Picasso.with(context)
                .load(avatarUrl)
                .placeholder(context.getResources().getDrawable(R.drawable.default_image))
                .error(context.getResources().getDrawable(R.drawable.default_image))
                .resize(100, 100)
                .centerInside()
                .into(viewHolder.avatar);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        RoundedImageView avatar;
    }
}
