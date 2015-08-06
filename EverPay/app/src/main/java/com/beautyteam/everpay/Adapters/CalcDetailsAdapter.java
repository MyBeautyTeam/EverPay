package com.beautyteam.everpay.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.beautyteam.everpay.Database.CalculationDetails;
import com.beautyteam.everpay.R;

/**
 * Created by popka on 06.08.15.
 */
public class CalcDetailsAdapter extends CursorAdapter {

    private final LayoutInflater inflater;
    private Context context;

    public CalcDetailsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_calc_details, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.billTextView = (TextView) itemLayout.findViewById(R.id.item_calc_details_bill);
        holder.needTextView = (TextView) itemLayout.findViewById(R.id.item_calc_details_need);
        holder.investTextView = (TextView) itemLayout.findViewById(R.id.item_calc_details_invest);
        holder.balanceTextView = (TextView) itemLayout.findViewById(R.id.item_calc_details_balance);
        itemLayout.setTag(holder);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String billTitle = cursor.getString(cursor.getColumnIndex(CalculationDetails.BILL_TITLE));
        int need = cursor.getInt(cursor.getColumnIndex(CalculationDetails.NEED_SUM));
        int invest = cursor.getInt(cursor.getColumnIndex(CalculationDetails.INVEST_SUM));
        int balance = cursor.getInt(cursor.getColumnIndex(CalculationDetails.BALANCE));

        holder.billTextView.setText(billTitle);
        holder.needTextView.setText(need+"");
        holder.investTextView.setText(invest + "");

        String balanceStr = ""+balance;
        if (balance > 0) {
            balanceStr = "+"+balanceStr;
            holder.balanceTextView.setTextColor(context.getResources().getColor(R.color.green_text));
        } else {
            holder.balanceTextView.setTextColor(context.getResources().getColor(R.color.red_text));
        }

        holder.balanceTextView.setText(balanceStr);



    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView billTextView;
        TextView needTextView;
        TextView investTextView;
        TextView balanceTextView;
    }
}
