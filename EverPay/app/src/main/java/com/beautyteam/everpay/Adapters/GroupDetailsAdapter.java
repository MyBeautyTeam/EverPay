package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Database.History;
import com.beautyteam.everpay.DialogAction;
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
    private DialogAction dialogAction;


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
        holder.send = (ImageView) itemLayout.findViewById(R.id.not_sended);
        itemLayout.setTag(holder);
        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        final int groupId = cursor.getInt(cursor.getColumnIndex(History.GROUP_ID));
        final int billId = cursor.getInt(cursor.getColumnIndex(History.BILL_ID));
        holder.discript.setBackgroundResource(0);
        holder.discript.setOnClickListener(null);
        holder.send.setVisibility(View.GONE);
        switch (cursor.getInt(cursor.getColumnIndex(History.ACTION))) {
            case REMOVE_BILLS:
            case ADD_GROUPS:
            case EDIT_GROUPS:
            case REMOVE_MEMBERS:
            case ADD_MEMBERS:
            case ADD_DEBTS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO)) + "</b>" + " " +
                        cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) + " " +
                        "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>" + " "));
                break;
            case ADD_BILLS:
                holder.send.setVisibility(View.VISIBLE);
                holder.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAction = new DialogAction(mainActivity);
                        dialogAction.show();
                        Window window = dialogAction.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialogAction.setOnSendClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogAction.dismiss();
                                //notifyDataSetChanged();
                            }
                        });
                        dialogAction.setOnDeleteClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogAction.dismiss();
                                //notifyDataSetChanged();
                            }
                        });
                    }
                });
//                if (cursor.getInt(cursor.getColumnIndex(History.RESULT)) == Constants.Result.ERROR) {
//                    holder.send.setVisibility(View.VISIBLE);
//                    holder.send.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialogAction = new DialogAction(mainActivity);
//                            dialogAction.show();
//                            dialogAction.setOnSendClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialogAction.dismiss();
//                                    //notifyDataSetChanged();
//                                }
//                            });
//                            dialogAction.setOnDeleteClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dialogAction.dismiss();
//                                    //notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    });
//                }
            case EDIT_BILLS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO)) + "</b>" + " " +
                        cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) + " " +
                        "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>" + " "));
                holder.discript.setBackgroundResource(R.drawable.history_style);
                holder.discript.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.addFragment(FragmentShowBill.getInstance(groupId, billId));
                    }
                });
                break;
            case EDIT_DEBTS:
                holder.discript.setText(Html.fromHtml("<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO_SAY)) + "</b>" +
                        " " + cursor.getString(cursor.getColumnIndex(History.TEXT_SAY)) +
                        " " + "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHO)) + "</b>" +
                        "&nbsp;&nbsp;" + cursor.getString(cursor.getColumnIndex(History.TEXT_DESCRIPTION)) +
                        " " + "<b>" + cursor.getString(cursor.getColumnIndex(History.TEXT_WHAT_WHOM)) + "</b>" + "&nbsp"));
                break;
        }
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView discript;
        ImageView send;
    }
}
