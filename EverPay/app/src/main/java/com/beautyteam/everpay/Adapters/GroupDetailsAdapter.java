package com.beautyteam.everpay.Adapters;

        import android.app.Dialog;
        import android.content.Context;
        import android.database.Cursor;
        import android.text.Html;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.CursorAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import com.beautyteam.everpay.Constants;
        import com.beautyteam.everpay.Database.History;
        import com.beautyteam.everpay.DialogWindow;
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
    OnDialogClickListener dialogListener;

    public GroupDetailsAdapter(Context context, Cursor c, int flags, MainActivity mainActivity, OnDialogClickListener dialogListener) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainActivity = mainActivity;
        this.dialogListener = dialogListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.item_group_details, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.discript = (TextView) itemLayout.findViewById(R.id.item_group_details_text);
        holder.send = (ImageView) itemLayout.findViewById(R.id.not_sended);
        holder.inprocess = (ProgressBar) itemLayout.findViewById(R.id.progressBar_send);
        itemLayout.setTag(holder);
        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        final int groupId = cursor.getInt(cursor.getColumnIndex(History.GROUP_ID));
        final int billId = cursor.getInt(cursor.getColumnIndex(History.BILL_ID));
        final int newsId = cursor.getInt(cursor.getColumnIndex(History.ITEM_ID));
        holder.discript.setBackgroundResource(0);
        holder.discript.setOnClickListener(null);
        holder.send.setVisibility(View.GONE);
        holder.inprocess.setVisibility(View.GONE);
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
                if (cursor.getInt(cursor.getColumnIndex(History.RESULT)) == Constants.Result.ERROR) {
                    holder.send.setVisibility(View.VISIBLE);
                    holder.send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog(newsId, groupId, billId);
                        }
                    });
                }
                if (cursor.getInt(cursor.getColumnIndex(History.STATE)) == Constants.State.IN_PROCESS) {
                    holder.inprocess.setVisibility(View.VISIBLE);
                }
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
        ProgressBar inprocess;
    }

    public interface OnDialogClickListener  {
        void onResendBill(int historyItemid, int groupId, int billId);
        void onDeleteBill(int historyItemid, int groupId, int billId);
    }

    private void showDialog(final int newsId, final int groupId, final int billId){
        String names[] = {"Повторить попытку", "Удалить"};
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_send);
        ListView lv = (ListView) dialog.findViewById(R.id.dialog_action_list);
        dialog.setCancelable(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity,R.layout.item_dialog_not_send, R.id.item_dialog, names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view1,int position, long id) {
                dialog.dismiss();
                if (position == 0)
                    dialogListener.onResendBill(newsId, groupId, billId);
                else
                    dialogListener.onDeleteBill(newsId, groupId, billId);
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
