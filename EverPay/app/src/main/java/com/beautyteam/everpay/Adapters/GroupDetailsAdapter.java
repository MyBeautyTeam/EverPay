package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.Fragments.FragmentShowBill;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by asus on 25.04.2015.
 */
public class GroupDetailsAdapter extends CursorAdapter {
    private final LayoutInflater inflater;
    private MainActivity mainActivity;

    public GroupDetailsAdapter(Context context, Cursor c, int flags, MainActivity mainActivity) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_group_details, viewGroup, false);

        ViewHolder holder = new ViewHolder();
        holder.discript = (TextView) itemLayout.findViewById(R.id.item_group_details_text);
        itemLayout.setTag(holder);

        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
//        final String groupTitle = cursor.getString(cursor.getColumnIndex(Groups.TITLE));
//        holder.discript.setText(groupTitle);
//
//        final String id = cursor.getString(cursor.getColumnIndex(Groups.GROUP_ID));


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // mainActivity.addFragment(FragmentShowBill.getInstance(groupId, groupId));
//            }
//        });
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView discript;
    }
}
