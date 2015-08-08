package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.Groups;
import com.beautyteam.everpay.Fragments.FragmentGroupDetails;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by asus on 15.03.2015.
 */
public class GroupsListAdapter  extends CursorAdapter {

    private final LayoutInflater inflater;
    private MainActivity mainActivity;

    public GroupsListAdapter(Context context, Cursor c, int flags, MainActivity mainActivity) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_groups, viewGroup, false);

        ViewHolder holder = new ViewHolder();
        holder.discript = (TextView) itemLayout.findViewById(R.id.groups_list_item);
        itemLayout.setTag(holder);

        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        final String groupTitle = cursor.getString(cursor.getColumnIndex(Groups.TITLE));
        holder.discript.setText(groupTitle);

        final int id = cursor.getInt(cursor.getColumnIndex(Groups.GROUP_ID));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.LOG, "GROUP_ID =" + id);
                mainActivity.addFragment(FragmentGroupDetails.getInstance(id));
            }
        });
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView discript;
    }
}