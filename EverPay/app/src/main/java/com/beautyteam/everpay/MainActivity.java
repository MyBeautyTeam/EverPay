package com.beautyteam.everpay;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements ActivityCallback,
        LoaderManager.LoaderCallbacks<Cursor> {


    private ServiceHelper serviceHelper;
    ContactCursorAdapter cursorAdapter;

    private static final String[] PROJECTION = new String[] {
            MyContentProvider.CONTACT_ID,
            MyContentProvider.CONTACT_NAME,
            MyContentProvider.CONTACT_EMAIL,
            MyContentProvider.IMG_NAME,
            MyContentProvider.STATE,
            MyContentProvider.RESULT
    };

    ListView lvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_actity);

        /*
        Нужно ли два аргумента. Такое ощущение, что использование serviceHelper в качестве ресивера неоправдано.
         */
        serviceHelper = new ServiceHelper(this, this);

        lvContact = (ListView) findViewById(R.id.listView);
        cursorAdapter = new ContactCursorAdapter(this);
        lvContact.setAdapter(cursorAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceHelper.resumeBind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serviceHelper.pauseBind();
    }


    @Override
    public void onRequestEnd(int result) {
        switch (result) {
            case Constants.Result.OK:
                //loader.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
                break;
            case Constants.Result.ERROR:
                //loader.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Constants.CONTACT_URI, PROJECTION, null, null, /*SORT_ORDER*/null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter = new ContactCursorAdapter(this);
        lvContact.setAdapter(cursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
