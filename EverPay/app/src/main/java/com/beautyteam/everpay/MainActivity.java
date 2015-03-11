package com.beautyteam.everpay;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKBatchRequest;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ActivityCallback {
        //LoaderManager.LoaderCallbacks<Cursor> {

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

        startLoading();

        /*
        Нужно ли два аргумента. Такое ощущение, что использование serviceHelper в качестве ресивера неоправдано.
         */
        serviceHelper = new ServiceHelper(this, this);

        lvContact = (ListView) findViewById(R.id.listView);
        Cursor cursor = getContentResolver().query(Constants.CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);
        cursorAdapter = new ContactCursorAdapter(this, cursor, 0);
        lvContact.setAdapter(cursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serviceHelper.onPause();
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

    public void startLoading() {
        VKRequest request1 = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100"));
        VKRequest request2 = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name, photo_100"));
        VKBatchRequest batch = new VKBatchRequest(request1, request2);
        batch.executeWithListener(new VKBatchRequest.VKBatchRequestListener() {
            @Override
            public void onComplete(VKResponse[] responses) {
                super.onComplete(responses);
                Log.d("VkDemoApp", "onComplete " + responses);

                VKApiUserFull userFull = ((VKList<VKApiUserFull>) responses[0].parsedModel).get(0);
                User user = new User(userFull.id,userFull.first_name,userFull.last_name,userFull.photo_100);


                final List<User> users = new ArrayList<User>();
                Log.d("vksdk",responses[1].parsedModel.toString());
                VKUsersArray usersArray = (VKUsersArray) responses[1].parsedModel;
                for (VKApiUserFull friends : usersArray) {
                    users.add(new User(friends.id,friends.first_name,friends.last_name, friends.photo_100));
                }
            }
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("VkDemoApp", "onError: " + error);
            }
        });
    }

/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Constants.CONTACT_URI, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter = new ContactCursorAdapter(this, data, 0);
        lvContact.setAdapter(cursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
*/
}
