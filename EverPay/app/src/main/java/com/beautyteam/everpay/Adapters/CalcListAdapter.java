package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Admin on 15.03.2015.
 */
public class CalcListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private HashMap<String, Integer> mapIdToIsdeleted = new HashMap<String, Integer>();
    private HashMap<Integer, Boolean> mapPositionToIsOpenFirstAvatar = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Boolean> mapPositionToIsOpenSecondAvatar = new HashMap<Integer, Boolean>();

    public CalcListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (c.moveToFirst() && c.getCount() != 0) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex(Calculation.CALC_ID));
                Integer value = c.getInt(c.getColumnIndex(Calculation.IS_DELETED));
                mapIdToIsdeleted.put(id, value);

                mapPositionToIsOpenSecondAvatar.put(c.getPosition(), false);
                mapPositionToIsOpenFirstAvatar.put(c.getPosition(), false);

                c.moveToNext();
            }
        }
        c.moveToFirst();
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

        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        String firstNameValue = cursor.getString(cursor.getColumnIndex(Calculation.NAME_WHO)).replace(" ", "\n");
        holder.firstName.setText(firstNameValue);

        holder.summa.setText(new Random().nextInt(10000) + "");

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

        String userWho = cursor.getString(cursor.getColumnIndex(Calculation.WHO_ID));
        String userWhom = cursor.getString(cursor.getColumnIndex(Calculation.WHOM_ID));

        OnTextClickListener textListener = new OnTextClickListener();
        textListener.setParams(holder, userWho, userWhom, cursor.getPosition());
        holder.firstName.setOnClickListener(textListener);
        holder.firstLayout.setOnClickListener(textListener);
        holder.secondName.setOnClickListener(textListener);
        holder.secondLayout.setOnClickListener(textListener);

        int position = cursor.getPosition();
        if (mapPositionToIsOpenFirstAvatar.get(position)) {
            holder.firstName.performClick();
        } else {
            holder.firstLayout.performClick();
        }

        if (mapPositionToIsOpenSecondAvatar.get(position)) {
            holder.secondName.performClick();
        } else {
            holder.secondLayout.performClick();
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
                    mapPositionToIsOpenFirstAvatar.put(position, true);

                    view.setVisibility(View.GONE);
                    viewHolder.firstLayout.setVisibility(View.VISIBLE);
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            Constants.FILE_DIRECTORY + '/' + whoUserId +
                            ".png"; // !!!!!!!!!
                    file = new File(filePath);
                    Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.firstAvatar);
                    break;
                // Кликаем на первый layout
                case R.id.item_calc_first_layout:
                    mapPositionToIsOpenFirstAvatar.put(position, false);

                    view.setVisibility(view.GONE); // Прячем картинку
                    viewHolder.firstName.setVisibility(View.VISIBLE); // Показываем текст
                    break;
                // Кликаем на второй текст
                case R.id.item_calc_second_name:
                    mapPositionToIsOpenSecondAvatar.put(position, true);

                    view.setVisibility(View.GONE);
                    viewHolder.secondLayout.setVisibility(View.VISIBLE);
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            Constants.FILE_DIRECTORY + '/' + whomUserId +
                            ".png"; // !!!!!!!!!
                    file = new File(filePath);
                    Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.secondAvatar);
                    break;
                // Кликаем на второй layout
                case R.id.item_calc_second_layout:
                    mapPositionToIsOpenSecondAvatar.put(position, false);

                    view.setVisibility(view.GONE); // Прячем картинку
                    viewHolder.secondName.setVisibility(View.VISIBLE); // Показываем текст
                    break;
            }
        }
    }

}