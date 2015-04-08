package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Calculation;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Random;

/**
 * Created by Admin on 15.03.2015.
 */
public class CalcListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private Boolean[] checkList;

    public CalcListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int count = c.getCount();

        checkList = new Boolean[count];
        for (int i=0; i<checkList.length; i++)
            checkList[i] = false;
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

        final int position = cursor.getPosition();

        holder.checkBox.setChecked(checkList[position]);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox c = (CheckBox) view;
                boolean value = checkList[position];
                c.setChecked(!value);
                checkList[position] = !value;
            }
        });

        final String id = cursor.getString(cursor.getColumnIndex(Calculation.ID_WHO));

        OnTextClickListener textListener = new OnTextClickListener();
        textListener.setParams(holder, id, (Integer.parseInt(id) + 1)%10 + "");
        holder.firstName.setOnClickListener(textListener);
        holder.firstLayout.setOnClickListener(textListener);
        holder.secondName.setOnClickListener(textListener);
        holder.secondLayout.setOnClickListener(textListener);


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
        private String firstFileName;
        private String secondFileName;

        private void setParams(ViewHolder viewHolder, String firstFileName, String secondFileName) {
            this.viewHolder = viewHolder;
            this.firstFileName = firstFileName;
            this.secondFileName = secondFileName;
        }

        @Override
        public void onClick(View view) {
            String filePath;
            File file;
            switch (view.getId()) {
                // Кликаем на первый текст
                case R.id.item_calc_first_name:
                    view.setVisibility(View.GONE);
                    viewHolder.firstLayout.setVisibility(View.VISIBLE);
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            Constants.FILE_DIRECTORY + '/' + firstFileName +
                            ".png"; // !!!!!!!!!
                    file = new File(filePath);
                    Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.firstAvatar);
                    break;
                // Кликаем на первый layout
                case R.id.item_calc_first_layout:
                    view.setVisibility(view.GONE); // Прячем картинку
                    viewHolder.firstName.setVisibility(View.VISIBLE); // Показываем текст
                    break;
                // Кликаем на второй текст
                case R.id.item_calc_second_name:
                    view.setVisibility(View.GONE);
                    viewHolder.secondLayout.setVisibility(View.VISIBLE);
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            Constants.FILE_DIRECTORY + '/' + secondFileName +
                            ".png"; // !!!!!!!!!
                    file = new File(filePath);
                    Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.secondAvatar);
                    break;
                // Кликаем на второй layout
                case R.id.item_calc_second_layout:
                    view.setVisibility(view.GONE); // Прячем картинку
                    viewHolder.secondName.setVisibility(View.VISIBLE); // Показываем текст
                    break;
            }
        }
    }
}