package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Database.Users;
import com.beautyteam.everpay.Fragments.FragmentAddBill;
import com.beautyteam.everpay.R;

/**
 * Created by Admin on 15.03.2015.
 */
public class AddBillListAdapterTmp extends CursorAdapter {

    private final LayoutInflater inflater;

    public static int TEXT_VIEW_MODE = 1;
    public static int EDIT_TEXT_MODE = 2;

    private String needSumma = "0";

    private int mode=TEXT_VIEW_MODE;

    private FragmentAddBill mFragmentAddBill; // ВОЗМОЖНА ТЕЧКА!!! ^^

    public AddBillListAdapterTmp(Context context, Cursor c, int flags, FragmentAddBill fragmentAddBill) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentAddBill = fragmentAddBill;
        BillItem[] billItem = new BillItem[c.getCount()];
        for (int i=0; i<billItem.length; i++)
            billItem[i] = new BillItem();
        int i=0;
        if(c.moveToFirst() && c.getCount() != 0) {
            while(!c.isAfterLast()) {
                billItem[i].id = c.getString(c.getColumnIndex(Users.USER_ID_VK));
                billItem[i].name = c.getString(c.getColumnIndex(Users.NAME));
                billItem[i].need = "0";
                billItem[i].invest = "0";
                billItem[i].isRemoved = false;
                c.moveToNext();
                i++;
            }
        }

    }

    public void setNeedSumma(String _needSumma) {
        needSumma = _needSumma;
    }

    public void setItemMode(int _mode) {
        mode = _mode;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_add_bill, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) itemLayout.findViewById(R.id.add_bill_list_name);
        holder.editNeed = (EditText) itemLayout.findViewById(R.id.add_bill_list_need_edit);
        holder.textNeed = (TextView) itemLayout.findViewById(R.id.add_bill_list_need_text);
        holder.put = (EditText) itemLayout.findViewById(R.id.add_bill_list_put);
        holder.remove = (ImageView) itemLayout.findViewById(R.id.add_bill_list_remove);
        itemLayout.setTag(holder);

        holder.put.addTextChangedListener(new TextWatcher() {
            int oldValue;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().isEmpty()) {
                    oldValue = 0;
                } else {
                    oldValue = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int newValue;
                if (editable.toString().isEmpty()) {
                    newValue = 0;
                } else {
                    newValue = Integer.parseInt(editable.toString());
                }
                mFragmentAddBill.setLeftSumma(newValue - oldValue);
            }
        });
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.name.setText(cursor.getString(cursor.getColumnIndex(Users.NAME)));
        holder.textNeed.setText(needSumma);
        if (mode == TEXT_VIEW_MODE) {
            holder.textNeed.setVisibility(View.VISIBLE);
            holder.editNeed.setVisibility(View.GONE);
        } else {
            holder.textNeed.setVisibility(View.GONE);
            holder.editNeed.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView name;
        EditText editNeed;
        TextView textNeed;
        EditText put;
        ImageView remove;
    }


    private class BillItem {
        private String id;
        private String name;
        private String need;
        private String invest;
        private boolean isRemoved;
    }

}
