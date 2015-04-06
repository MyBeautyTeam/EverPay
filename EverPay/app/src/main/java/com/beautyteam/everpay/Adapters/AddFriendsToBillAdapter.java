package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Constants;
import com.beautyteam.everpay.Fragments.FragmentAddBill;
import com.beautyteam.everpay.R;
import com.beautyteam.everpay.Views.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Admin on 06.04.2015.
 */
public class AddFriendsToBillAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BillListItem> billFullArrayList;
    public ArrayList<BillListItem> billDeletedArrayList;

    public AddFriendsToBillAdapter(Context _context, ArrayList<BillListItem> billFullArrayList) {
        this.context = _context;
        this.billFullArrayList = billFullArrayList;
        billDeletedArrayList = new ArrayList<BillListItem>();
        for (int i=0; i<billFullArrayList.size(); i++) {
            if (billFullArrayList.get(i).isRemoved)
                billDeletedArrayList.add(billFullArrayList.get(i));
        }
    }


    @Override
    public int getCount() {
        return billDeletedArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return billDeletedArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.item_add_friends_to_bill, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_add_friends_to_bill_name);
            viewHolder.avatar = (RoundedImageView) convertView.findViewById(R.id.item_add_friends_to_bill_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(billDeletedArrayList.get(position).name);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constants.FILE_DIRECTORY + '/' + billDeletedArrayList.get(position).img;

        File file = new File(filePath);
        Picasso.with(context).load(file).resize(200, 200).centerInside().into(viewHolder.avatar);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        RoundedImageView avatar;
    }
}
