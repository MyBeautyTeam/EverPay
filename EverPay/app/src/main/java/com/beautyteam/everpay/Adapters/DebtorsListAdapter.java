package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Path;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Debts;
import com.beautyteam.everpay.Database.MyContentProvider;
import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Admin on 10.03.2015.
 */
public class DebtorsListAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public DebtorsListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        //holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        holder.summa.setText(cursor.getString(cursor.getColumnIndex(Debts.SUMMA)));

        String userName = cursor.getString(cursor.getColumnIndex(Debts.USER_NAME));
        String group = cursor.getString(cursor.getColumnIndex(Debts.GROUP_TITLE));
        String img = cursor.getString(cursor.getColumnIndex(Debts.USER_IMG));

        if (userName == null) {
            holder.discript.setText(group);
            Picasso.with(context).load(R.drawable.group_icon).resize(200, 200).centerInside().into(holder.avatar);
        }
        else {
            holder.discript.setText(userName + ", " + group);
            /*String fileName = cursor.getString(cursor.getColumnIndex(Debts.USER_ID)); // Возможно, в дальнейшем будет id
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    Constants.FILE_DIRECTORY + '/' + fileName +
                    ".png"; // !!!!!!!!!*/

            Picasso.with(context).load(img).resize(200, 200).centerInside().into(holder.avatar);
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

