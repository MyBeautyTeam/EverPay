package com.beautyteam.everpay;

import android.app.Activity;
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


public class MainActivity extends Activity implements ActivityCallback{

    private Button sendBtn;
    private TextView loader;
    private ServiceHelper serviceHelper;

    SimpleCursorAdapter adapter;
    ListView lvContact;

    EditText nameEditText;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_actity);

        /*
        Нужно ли два аргумента. Такое ощущение, что использование serviceHelper в качестве ресивера неоправдано.
         */
        serviceHelper = new ServiceHelper(this, this);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        loader = (TextView) findViewById(R.id.loader);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sendBtn:
                        loader.setVisibility(View.VISIBLE);
                        serviceHelper.send(nameEditText.getText().toString(), emailEditText.getText().toString());
                        Log.d(Constants.LOG, "onClick");
                }
            }
        });

        Cursor cursor = getContentResolver().query(Constants.CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        String from[] = { "name", "email" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to);

        lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
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
    public void onSuccess() {
        loader.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        loader.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
    }
}
