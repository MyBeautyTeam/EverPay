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
import android.widget.TextView;

import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.GroupMembers;
import com.beautyteam.everpay.Dialogs.DialogDebtDetail;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.MainActivity;
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
 * Created by Admin on 10.03.2015.
 */
public class DebtorsListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private DialogDebtDetail dialogDebtDetail;
    private MainActivity activity;

    HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();
    Context context;

    public DebtorsListAdapter(Context context, final Cursor c, int flags) {
        super(context, c, flags);
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (!c.isClosed()) // Без этого падает переход на дроверу с экрана расчета
                    loadAvatarsFromVK(c);
            }
        });
    }


    private void loadAvatarsFromVK(Cursor c) {
        String usersId = "";
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(Debts.USER_VK_ID));
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
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        final int summ = cursor.getInt(cursor.getColumnIndex(Debts.SUM_SUMMA));
        holder.summa.setText(summ + " \u20BD" );

        final String userName = cursor.getString(cursor.getColumnIndex(Debts.USER_NAME));
        final String groupTitle = cursor.getString(cursor.getColumnIndex(Debts.GROUP_TITLE));
        final int groupId = cursor.getInt(cursor.getColumnIndex(Debts.GROUP_ID));

        if (userName == null) {
            holder.discript.setText(groupTitle);
            Picasso.with(context)
                    .load(R.drawable.ic_launcher)
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_launcher))
                    .error(context.getResources().getDrawable(R.drawable.ic_launcher))
                    .resize(200, 200)
                    .centerInside()
                    .into(holder.avatar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)context).addFragment(FragmentGroupDetails.getInstance(groupId));
                }
            });
        }
        else {
            holder.discript.setText(userName);
            final String id_vk = cursor.getString(cursor.getColumnIndex(Debts.USER_VK_ID));
            final int userId = cursor.getInt(cursor.getColumnIndex(Debts.USER_ID));
            final int isIDebt = cursor.getInt(cursor.getColumnIndex(Debts.IS_I_DEBT));
            final String img = mapIdToAvatar.get(id_vk);

            Picasso.with(context)
                    .load(img)
                    .placeholder(context.getResources().getDrawable(R.drawable.default_image))
                    .error(context.getResources().getDrawable(R.drawable.default_image))
                    .resize(100,100)
                    .centerInside()
                    .into(holder.avatar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(userName,userId, id_vk, isIDebt, img, summ);
                }
            });
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

    private void showDialog(String userName, int userId, String userVkId, int isIDebt, String img, int sum) {
        dialogDebtDetail = new DialogDebtDetail(context, userName, userId, userVkId, isIDebt, img, sum);
        dialogDebtDetail.show();
        Window window = dialogDebtDetail.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}

