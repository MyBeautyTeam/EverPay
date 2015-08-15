package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Admin on 15.03.2015.
 */
public class CalcListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private HashMap<String, Integer> mapIdToIsdeleted = new HashMap<String, Integer>();
    private HashMap<Integer, Boolean> mapPositionToIsOpenFirstAvatar = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> mapPositionToIsOpenSecondAvatar = new HashMap<Integer, Boolean>();
    private HashMap<String, String> mapIdToAvatar = new HashMap<String, String>();
    private boolean isEnabled = true;

    private int userIdOwner; // ID пользователя, чтобы узнать, какая должна быть стрелочка

    public CalcListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        userIdOwner = context.getSharedPreferences(Constants.Preference.SHARED_PREFERENCES, Context.MODE_WORLD_WRITEABLE).getInt(Constants.Preference.USER_ID, 0);

        String usersId = "";
        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(Calculation.CALC_ID));

                String userId = "";
                userId = c.getString(c.getColumnIndex(Calculation.WHO_ID_VK));
                if (!usersId.contains(","+userId+","))
                    usersId += userId + ",";

                userId = c.getString(c.getColumnIndex(Calculation.WHOM_ID_VK));
                if (!usersId.contains(","+userId+","))
                    usersId += userId + ",";

                Integer value = c.getInt(c.getColumnIndex(Calculation.IS_DELETED));
                mapIdToIsdeleted.put(id, value);

                mapPositionToIsOpenSecondAvatar.put(c.getPosition(), false);
                mapPositionToIsOpenFirstAvatar.put(c.getPosition(), false);

                c.moveToNext();
            }
        }
        c.moveToFirst();

        loadAvatarsFromVK(usersId);

    }

    private void loadAvatarsFromVK(String usersId) {

        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, usersId, VKApiConst.FIELDS, "photo_100"));
        VKBatchRequest batch = new VKBatchRequest(request);

        batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                VKList<VKApiUser> userList = (VKList<VKApiUser>) responses[0].parsedModel;
                int count = userList.size();
                for (int i = 0; i < count; i++)
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

    public HashMap<String, Integer> getMapIdToIsdeleted() {
        return mapIdToIsdeleted;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_calculation, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.firstName = (TextView) itemLayout.findViewById(R.id.item_calc_first_name);
        holder.summa = (TextView) itemLayout.findViewById(R.id.calc_summa);
        holder.secondName = (TextView) itemLayout.findViewById(R.id.item_calc_second_name);
        holder.checkBox = (CheckBox) itemLayout.findViewById(R.id.calc_checkbox);

        holder.firstAvatar = (RoundedImageView) itemLayout.findViewById(R.id.item_calc_first_avatar);
        holder.secondAvatar = (RoundedImageView) itemLayout.findViewById(R.id.item_calc_second_avatar);

        holder.firstLayout = (LinearLayout) itemLayout.findViewById(R.id.item_calc_first_layout);
        holder.secondLayout = (LinearLayout) itemLayout.findViewById(R.id.item_calc_second_layout);

        holder.arrow = (ImageView) itemLayout.findViewById(R.id.calc_arrow);

        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        String firstNameValue = cursor.getString(cursor.getColumnIndex(Calculation.NAME_WHO)).replace(" ", "\n");
        holder.firstName.setText(firstNameValue);

        holder.summa.setText(cursor.getString(cursor.getColumnIndex(Calculation.SUMMA)) + " \u20BD");

        String secondNameValue = cursor.getString(cursor.getColumnIndex(Calculation.NAME_WHOM)).replace(" ", "\n");
        holder.secondName.setText(secondNameValue);

        final String calcId = cursor.getString(cursor.getColumnIndex(Calculation.CALC_ID));
        int value = mapIdToIsdeleted.get(calcId);
        boolean isChecked = (value == 1);
        holder.checkBox.setChecked(isChecked);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox c = (CheckBox) view;
                int value = mapIdToIsdeleted.get(calcId);

                if (value == 1) {
                    c.setChecked(false);
                    mapIdToIsdeleted.put(calcId, 0);
                } else {
                    c.setChecked(true);
                    mapIdToIsdeleted.put(calcId, 1);
                }


            }
        });

        if (!isEnabled) {
            holder.checkBox.setEnabled(false);
        }

        String userWhoVK = cursor.getString(cursor.getColumnIndex(Calculation.WHO_ID_VK));
        String userWhomVK = cursor.getString(cursor.getColumnIndex(Calculation.WHOM_ID_VK));

        /*
        Определяем цвет стрелочки
         */
        int userWhoId = cursor.getInt(cursor.getColumnIndex(Calculation.WHO_ID));
        int userWhomId = cursor.getInt(cursor.getColumnIndex(Calculation.WHOM_ID));
        if (userWhoId != userIdOwner && userWhomId != userIdOwner) {
            /*Picasso.with(context)
                    .load(R.drawable.ic_trending_neutral_black_36dp)
                    .centerInside()
                    .into(holder.arrow);*/
            holder.arrow.setImageResource(R.drawable.ic_trending_neutral_blue_36dp);
            holder.summa.setTextColor(context.getResources().getColor(R.color.primary));
        } else {
            holder.arrow.setImageResource(R.drawable.ic_trending_neutral_black_36dp);
            holder.summa.setTextColor(context.getResources().getColor(R.color.primary_text));
        }

        OnTextClickListener textListener = new OnTextClickListener();
        textListener.setParams(holder, userWhoVK, userWhomVK, cursor.getPosition());
        holder.firstName.setOnClickListener(textListener);
        holder.firstLayout.setOnClickListener(textListener);
        holder.secondName.setOnClickListener(textListener);
        holder.secondLayout.setOnClickListener(textListener);

        int position = cursor.getPosition();
        if (mapPositionToIsOpenFirstAvatar.get(position)) {
            //holder.firstName.performClick();
            openAvatar(holder.firstLayout, holder.firstAvatar, holder.firstName, userWhoVK);
            mapPositionToIsOpenFirstAvatar.put(position, true);
        } else {
            //holder.firstLayout.performClick();
            closeAvatar(holder.firstLayout, holder.firstName);
            mapPositionToIsOpenFirstAvatar.put(position, false);
            //openAvatar(position, holder.firstAvatar, holder.firstLayout, userWhoVK);
        }

        if (mapPositionToIsOpenSecondAvatar.get(position)) {

            openAvatar(holder.secondLayout, holder.secondAvatar, holder.secondName, userWhomVK);
            mapPositionToIsOpenSecondAvatar.put(position, true);
        } else {
            closeAvatar(holder.secondLayout, holder.secondName);
            mapPositionToIsOpenSecondAvatar.put(position, false);
        }
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView firstName;
        TextView secondName;
        TextView summa;
        CheckBox checkBox;
        RoundedImageView firstAvatar;
        RoundedImageView secondAvatar;
        LinearLayout firstLayout;
        LinearLayout secondLayout;
        ImageView arrow;
    }

    private class OnTextClickListener implements View.OnClickListener {
        private ViewHolder viewHolder;
        private String whoUserId;
        private String whomUserId;
        private int position;

        private void setParams(ViewHolder viewHolder, String whoUserId, String whomUserId, int position) {
            this.viewHolder = viewHolder;
            this.whoUserId = whoUserId;
            this.whomUserId = whomUserId;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            String filePath;
            File file;
            switch (view.getId()) {
                // Кликаем на первый текст
                case R.id.item_calc_first_name:
                    openAvatar(viewHolder.firstLayout, viewHolder.firstAvatar, viewHolder.firstName, whoUserId);
                    mapPositionToIsOpenFirstAvatar.put(position, true);

                    break;

                // Кликаем на первый layout
                case R.id.item_calc_first_layout:
                    closeAvatar(viewHolder.firstLayout, viewHolder.firstName);
                    mapPositionToIsOpenFirstAvatar.put(position, false);
                    break;

                // Кликаем на второй текст
                case R.id.item_calc_second_name:
                    openAvatar(viewHolder.secondLayout, viewHolder.secondAvatar, viewHolder.secondName, whomUserId);
                    mapPositionToIsOpenSecondAvatar.put(position, true);
                    break;

                // Кликаем на второй layout
                case R.id.item_calc_second_layout:
                    closeAvatar(viewHolder.secondLayout, viewHolder.secondName);
                    mapPositionToIsOpenSecondAvatar.put(position, false);
                    break;
            }
        }

    }

    private void openAvatar(LinearLayout imageLayout, RoundedImageView image, TextView text, String userId) {

        text.setVisibility(View.GONE);
        imageLayout.setVisibility(View.VISIBLE);

        String firstAvatarUrl = mapIdToAvatar.get(userId);
        Picasso.with(context)
                .load(firstAvatarUrl)
                .placeholder(context.getResources().getDrawable(R.drawable.default_image))
                .error(context.getResources().getDrawable(R.drawable.default_image))
                .resize(100, 100)
                .centerInside()
                .into(image);
    }

    private void closeAvatar(LinearLayout imageLayout, TextView text) {

        imageLayout.setVisibility(View.GONE); // Прячем картинку
        text.setVisibility(View.VISIBLE); // Показываем текст
    }

    public void setEnabled(boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;
            notifyDataSetChanged();
        }
    }

}