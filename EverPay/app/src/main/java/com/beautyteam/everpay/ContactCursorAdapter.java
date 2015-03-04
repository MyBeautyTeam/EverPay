package com.beautyteam.everpay;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Admin on 05.03.2015.
 */
public class ContactCursorAdapter extends CursorAdapter {

    private final LayoutInflater inflater;

    public ContactCursorAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemLayout = inflater.inflate(R.layout.contacts_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.text = (TextView) itemLayout.findViewById(R.id.text);
        holder.icon = (ImageView) itemLayout.findViewById(R.id.icon);
        itemLayout.setTag(holder);
        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(cursor.getString(cursor.getColumnIndex(MyContentProvider.CONTACT_NAME)));
        String fileName =  cursor.getString(cursor.getColumnIndex(MyContentProvider.IMG_NAME)); // Возможно, в дальнейшем будет id
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                Constants.FILE_DIRECTORY + fileName;
        Picasso.with(context).load(filePath).resize(50,50).centerInside().into(holder.icon);
    }

    @Override
    public int getCount() {
        return 12;//getCursor() == null ? 0 : super.getCount();
    }

    private static class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
