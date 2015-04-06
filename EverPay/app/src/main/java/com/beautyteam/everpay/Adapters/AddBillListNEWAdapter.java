package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautyteam.everpay.Fragments.FragmentAddBill;
import com.beautyteam.everpay.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Admin on 06.04.2015.
 */
public class AddBillListNEWAdapter extends BaseAdapter {

    private ArrayList<BillListItem> billFullArrayList;
    private ArrayList<BillListItem> billAvailableArrayList;
    private Context context;
    private LayoutInflater inflater;
    private String needSumma = "0";
    private int mode=TEXT_VIEW_MODE;

    public static int TEXT_VIEW_MODE = 1;
    public static int EDIT_TEXT_MODE = 2;
    private FragmentAddBill mFragmentAddBill;

    ArrayList<String> myItems = new ArrayList<String>();

    public AddBillListNEWAdapter(Context _context, ArrayList<BillListItem> billFullArrayList, FragmentAddBill fragmentAddBill) {
        context = _context;
        this.billFullArrayList = billFullArrayList;

        refreshAvaliableList();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentAddBill = fragmentAddBill;

        for (int i = 0; i < billAvailableArrayList.size(); i++) {
            myItems.add(Integer.toString(0));
        }

    }

    public void refreshAvaliableList() {
        billAvailableArrayList = new ArrayList<BillListItem>();
        for (int i = 0; i < billFullArrayList.size(); i++) {
            if (!billFullArrayList.get(i).isRemoved)
                billAvailableArrayList.add(billFullArrayList.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return billAvailableArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return billAvailableArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_add_bill, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.add_bill_list_name);
            viewHolder.editNeed = (EditText) convertView.findViewById(R.id.add_bill_list_need_edit);
            viewHolder.textNeed = (TextView) convertView.findViewById(R.id.add_bill_list_need_text);
            viewHolder.put = (EditText) convertView.findViewById(R.id.add_bill_list_put);
            viewHolder.put.setId(position);
            viewHolder.remove = (ImageView) convertView.findViewById(R.id.add_bill_list_remove);
            viewHolder.position = position;
            convertView.setTag(viewHolder);

            viewHolder.put.addTextChangedListener(new GenericTextWatcher(viewHolder.put));

/*            viewHolder.put.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int newValue = 0;
                    if (editable.toString().isEmpty()) {
                        newValue = 0;
                    } else {
                        int firstVisible = ((ListView) parent).getFirstVisiblePosition();
                        int lastVisiblePosition = ((ListView) parent).getLastVisiblePosition();
                        if (( viewHolder.position >= firstVisible) && (viewHolder.position <= lastVisiblePosition)) {
                            newValue = Integer.parseInt(editable.toString());
                            BillListItem billListItem = (BillListItem) getItem(position);
                            billListItem.invest = newValue;
                        }
                    }
                    mFragmentAddBill.setLeftSumma(getLeftSumma());
                }
            });
            */
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.put.setId(position);
        }

        BillListItem billListItem = (BillListItem)getItem(position);
        viewHolder.name.setText(billListItem.name + "");
        viewHolder.put.setText(myItems.get(position));
        viewHolder.textNeed.setText(needSumma);

        if (mode == TEXT_VIEW_MODE) {
            viewHolder.textNeed.setVisibility(View.VISIBLE);
            viewHolder.editNeed.setVisibility(View.GONE);
        } else {
            viewHolder.textNeed.setVisibility(View.GONE);
            viewHolder.editNeed.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private int getLeftSumma() {
        int summa = 0;
        for (int i=0; i< billAvailableArrayList.size(); i++) {
            summa += billAvailableArrayList.get(i).invest;
        }
        return summa;

    }


    private static class ViewHolder {
        TextView name;
        EditText editNeed;
        TextView textNeed;
        EditText put;
        ImageView remove;
        int position;
    }

    public void setNeedSumma(String summa) {
        needSumma = summa;
    }

    public void setItemMode(int _mode) {
        mode = _mode;
    }

    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            final int position = view.getId();
            final EditText editText = (EditText) view;
            myItems.set(position, editText.getText().toString());
            BillListItem billListItem = billAvailableArrayList.get(position);
            billListItem.invest = Integer.parseInt(editText.getText().toString());
            mFragmentAddBill.setLeftSumma(getLeftSumma());
        }
    }
}
