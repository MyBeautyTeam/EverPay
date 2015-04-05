package com.beautyteam.everpay.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.security.acl.Group;

/**
 * Created by Admin on 27.02.2015.
 */
public class EverContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";
    static final String AUTHORITY = "com.beautyteam.everpay.EverpayDB";



    public static final Uri USERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Users.USERS_TABLE);
    public static final Uri GROUPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Groups.GROUPS_TABLE);



    static final String USERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Users.USERS_TABLE;
    static final String USERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + Users.USERS_TABLE;
    static final String GROUPS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Groups.GROUPS_TABLE;
    static final String GROUPS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + Groups.GROUPS_TABLE;




    static final int URI_USERS = 1;
    static final int URI_USERS_ID = 2;
    static final int URI_GROUPS = 3;
    static final int URI_GROUPS_ID = 4;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Users.USERS_TABLE, URI_USERS);
        uriMatcher.addURI(AUTHORITY, Users.USERS_TABLE + "/#", URI_USERS_ID);

        uriMatcher.addURI(AUTHORITY, Groups.GROUPS_TABLE, URI_GROUPS);
        uriMatcher.addURI(AUTHORITY, Groups.CREATE_TABLE+ "/#", URI_GROUPS_ID);

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
        String id;
        String table;
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                table = Users.USERS_TABLE;
                break;
            case URI_USERS_ID:
                id = uri.getLastPathSegment();
                table = Users.USERS_TABLE;
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = Users.USER_ID_VK + " = " + id;
                } else selection = selection + " AND " + Users.USER_ID_VK + " = " + id;
                break;
            case URI_GROUPS:
                table = Groups.GROUPS_TABLE;
                break;
            case URI_GROUPS_ID:
                id = uri.getLastPathSegment();
                table = Groups.GROUPS_TABLE;
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = Groups.GROUP_ID + " = " + id;
                } else selection = selection + " AND " + Groups.GROUP_ID + " = " + id;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        Cursor cursor = db.query(table, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                return USERS_CONTENT_TYPE;
            case URI_USERS_ID:
                return USERS_CONTENT_ITEM_TYPE;
            case URI_GROUPS:
                return GROUPS_CONTENT_TYPE;
            case URI_GROUPS_ID:
                return GROUPS_CONTENT_ITEM_TYPE;
        }
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
