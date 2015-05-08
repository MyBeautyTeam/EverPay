package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.Fragments.FragmentShowBill;
import com.beautyteam.everpay.MainActivity;
import com.beautyteam.everpay.R;

/**
 * Created by asus on 25.04.2015.
 */
public class GroupDetailsAdapter extends CursorAdapter {
    private final LayoutInflater inflater;
    private MainActivity mainActivity;
    private static final  int ADD_GROUPS = 0;
    private static final  int EDIT_GROUPS = 1;
    private static final  int ADD_MEMBERS = 2;
    private static final  int REMOVE_MEMBERS = 3;
    private static final  int ADD_BILLS = 4;
    private static final  int EDIT_BILLS = 5;
    private static final  int REMOVE_BILLS = 6;
    private static final  int ADD_DEBTS = 7;
    private static final  int EDIT_DEBTS = 8;

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
        final int groupId = cursor.getInt(cursor.getColumnIndex(History.GROUP_ID));
        final int billId = cursor.getInt(cursor.getColumnIndex(History.BILL_ID));
        view.setBackgroundResource(0);
        view.setOnClickListener(null);
        switch (cursor.getInt(cursor.getColumnIndex(History.ACTION))) {
            case REMOVE_BILLS:
            case ADD_GROUPS:
            case EDIT_GROUPS:
            case REMOVE_MEMBERS:
            case ADD_MEMBERS:
            case ADD_DEBTS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO)) + "</b>" + " " +
                        cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) + " " +
                        "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>"+ " "));
                break;
            case ADD_BILLS:
            case EDIT_BILLS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO)) + "</b>" + " " +
                        cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) + " " +
                        "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>"+ " "));
                view.setBackgroundResource(R.drawable.history_style);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.addFragment(FragmentShowBill.getInstance(groupId, billId));
                    }
                });
                break;
            case EDIT_DEBTS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO_SAY)) + "</b>" +
                        " " + cursor.getString(cursor.getColumnIndex(History.TEXT_SAY)) +
                        " " + "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO))+ "</b>" +
                        "&nbsp;&nbsp;" + cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) +
                        " " + "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>"+"&nbsp"));
                break;
        }
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView discript;
    }
}
