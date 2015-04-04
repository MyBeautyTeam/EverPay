package com.beautyteam.everpay.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Admin on 27.02.2015.
 */
public class EverContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";
    static final String AUTHORITY = "com.beautyteam.everpay.EverpayDB";
    static final String USERS_PATH = "users";

    public static final Uri USERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + USERS_PATH);


    static final String USERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + USERS_PATH;
    // одна строка
    static final String USERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + USERS_PATH;

    static final int URI_USERS = 1;
    static final int URI_USERS_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USERS_PATH, URI_USERS);
        uriMatcher.addURI(AUTHORITY, USERS_PATH + "/#", URI_USERS_ID);
    }


    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(Users.USERS_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
