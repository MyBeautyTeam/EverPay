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

/**
 * Created by Admin on 06.04.2015.
 */
public class AddBillListNEWAdapter extends BaseAdapter {

    private ArrayList<BillListItem> billFullArrayList;
    private ArrayList<BillListItem> billAvailableArrayList;
    private Context context;
    private LayoutInflater inflater;
    private int needSumma = 0;
    private int mode=TEXT_VIEW_MODE;

    public static int TEXT_VIEW_MODE = 1;
    public static int EDIT_TEXT_MODE = 2;
    private FragmentAddBill mFragmentAddBill;

    ArrayList<String> investSummaArray = new ArrayList<String>();
    ArrayList<String> needSummaArray = new ArrayList<String>();

    public AddBillListNEWAdapter(Context _context, ArrayList<BillListItem> billFullArrayList, FragmentAddBill fragmentAddBill) {
        context = _context;
        this.billFullArrayList = billFullArrayList;
        mFragmentAddBill = fragmentAddBill;

        refreshAvaliableList();
        for (int i=0; i<billAvailableArrayList.size(); i++) {
            investSummaArray.add("");
            needSummaArray.add("");
        }

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshAvaliableList() {
        billAvailableArrayList = new ArrayList<BillListItem>(); // Собираем новый список доступных пользователей
        for (int i = 0; i < billFullArrayList.size(); i++) {
            if (!billFullArrayList.get(i).isRemoved)
                billAvailableArrayList.add(billFullArrayList.get(i));
        }
        if (billAvailableArrayList.size() == billFullArrayList.size()) { // Если хотя бы один удален - показываем кнопку "добавить участника"
            mFragmentAddBill.removeFooterBtn();
        } else {
            mFragmentAddBill.addFooterBtn();
        }

        /*
        Если ввод поля Должен доступен, то определяем дог каждого по общему
         */
        if (mode == TEXT_VIEW_MODE) {
            int needSummaPerUser;
            int count = billAvailableArrayList.size();
            if (count != 0) // чтобы избежать деления на ноль, когда всех пользователей удалили
                needSummaPerUser = needSumma / billAvailableArrayList.size();
            else
                needSummaPerUser = 0;
            for (int i=0; i<billAvailableArrayList.size(); i++)
                billAvailableArrayList.get(i).need = needSummaPerUser;
        }

        notifyDataSetChanged();
        mFragmentAddBill.setNeedSumma(getNeedSumma());
        mFragmentAddBill.setLeftSumma(getInvestSumma());
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_add_bill, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.add_bill_list_name);
            viewHolder.textNeed = (TextView) convertView.findViewById(R.id.add_bill_list_need_text);

            viewHolder.editNeed = (EditText) convertView.findViewById(R.id.add_bill_list_need_edit);
            viewHolder.editNeed.setId(position);
            viewHolder.editNeed.addTextChangedListener(new GenericTextWatcherNeed(viewHolder.editNeed));

            viewHolder.put = (EditText) convertView.findViewById(R.id.add_bill_list_put);
            viewHolder.put.setId(position);
            viewHolder.put.addTextChangedListener(new GenericTextWatcher(viewHolder.put));

            viewHolder.remove = (ImageView) convertView.findViewById(R.id.add_bill_list_remove);
            viewHolder.position = position;
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.put.setId(position);
            viewHolder.editNeed.setId(position);
        }

        BillListItem billListItem = (BillListItem)getItem(position);
        viewHolder.name.setText(billListItem.name + "");

        viewHolder.textNeed.setText(billListItem.need + "");

        if (billListItem.invest == 0)
            viewHolder.put.setText("");
        else
            viewHolder.put.setText(billListItem.invest + "");

        if (billListItem.need == 0)
            viewHolder.editNeed.setText("");
        else
            viewHolder.editNeed.setText(billListItem.need + "");

        /*
        Удаляем элемент
         */
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billAvailableArrayList.get(position).isRemoved = true;
                billAvailableArrayList.get(position).invest = 0;
                billAvailableArrayList.get(position).need = 0;
                investSummaArray.remove(position);
                refreshAvaliableList();
            }
        });

        /*
        В зависимости от мода прячем нужные вьюшки
         */
        if (mode == TEXT_VIEW_MODE) {
            viewHolder.textNeed.setVisibility(View.VISIBLE);
            viewHolder.editNeed.setVisibility(View.GONE);
        } else {
            viewHolder.textNeed.setVisibility(View.GONE);
            viewHolder.editNeed.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /*
    Считает сумму колонки Внес
     */
    private int getInvestSumma() {
        int summa = 0;
        for (int i=0; i< billAvailableArrayList.size(); i++) {
            summa += billAvailableArrayList.get(i).invest;
        }
        return summa;
    }

    /*
    Считает сумму колонки Должен
     */
    private int getNeedSumma() {
        int summa = 0;
        for (int i=0; i< billAvailableArrayList.size(); i++) {
            summa += billAvailableArrayList.get(i).need;
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

    /*
    Передаем адаптеру информацию о сумме "ДОЛЖНЫ" из фрагмента
     */
    public void setNeedSumma(int summa) {
        needSumma = summa;
        refreshAvaliableList();
    }

    /*
    Устанавливает режим EditText или TextView
     */
    public void setItemMode(int _mode) {
        mode = _mode;
    }

    /*
    Удивительный черный ящик-спаситель!
     */
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
            String value = editText.getText().toString();
            investSummaArray.set(position, value);
            BillListItem billListItem = billAvailableArrayList.get(position);
            if (value.isEmpty()) billListItem.invest = 0;
            else billListItem.invest = Integer.parseInt(value);
            mFragmentAddBill.setLeftSumma(getInvestSumma());
        }
    }

    /*
    Удивительный черный ящик-спаситель!
     */
    private class GenericTextWatcherNeed implements TextWatcher{

        private View view;
        private GenericTextWatcherNeed(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            final int position = view.getId();
            final EditText editText = (EditText) view;
            String value = editText.getText().toString();
            needSummaArray.set(position, value);
            BillListItem billListItem = billAvailableArrayList.get(position);
            if (value.isEmpty()) billListItem.need = 0;
            else billListItem.need = Integer.parseInt(value);
            mFragmentAddBill.setNeedSumma(getNeedSumma());
        }
    }
}
