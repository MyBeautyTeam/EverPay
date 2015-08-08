package com.beautyteam.everpay.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.beautyteam.everpay.User;

import java.net.URI;

/**
 * Created by Admin on 27.02.2015.
 */
public class EverContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";
    static final String AUTHORITY = "com.beautyteam.everpay.EverpayDB";

    public static final Uri USERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Users.USERS_TABLE);
    public static final Uri USERS_FRIENDS_GROUP_BY_VK = Uri.parse("content://" + AUTHORITY + "/" + Users.USERS_TABLE + "/" + "order_vk");
    public static final Uri GROUPS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Groups.GROUPS_TABLE);
    public static final Uri DEBTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Debts.DEBTS_TABLE);
    public static final Uri DEBTS_CONTENT_URI_DIALOG = Uri.parse("content://" + AUTHORITY + "/" + Debts.DEBTS_TABLE + "/" + "dialog");
    public static final Uri CALCULATION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Calculation.CALCULATION_TABLE);
    public static final Uri GROUP_MEMBERS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + GroupMembers.GROUP_MEMBERS_TABLE);
    public static final Uri BILLS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + Bills.BILLS_TABLE);
    public static final Uri HISTORY_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + History.HISTORY_TABLE);
    public static final Uri CALC_DETAILS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CalculationDetails.CALCULATION_DETAILS_TABLE);

    static final String USERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Users.USERS_TABLE;
    static final String GROUPS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Groups.GROUPS_TABLE;
    static final String GROUP_MEMBERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + GroupMembers.GROUP_MEMBERS_TABLE;
    static final String BILLS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Bills.BILLS_TABLE;
    static final String DEBTS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Debts.DEBTS_TABLE;
    static final String CALCULATION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + Calculation.CALCULATION_TABLE;
    static final String HISTORY_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + History.HISTORY_TABLE;
    static final String CALC_DETAILS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CalculationDetails.CALCULATION_DETAILS_TABLE;



    static final int URI_USERS = 1;
    static final int URI_GROUPS = 2;
    static final int URI_GROUP_MEMBERS = 3;
    static final int URI_BILLS = 4;
    static final int URI_DEBTS = 5;
    static final int URI_CALCULATION = 6;
    static final int URI_HISTORY = 7;
    static final int URI_USERS_LEFT_IN_GROUP = 8;
    static final int URI_DEBTS_DIALOG = 9;
    static final int URI_CALC_DETAILS = 10;

    static final int URI_FRIENDS_GROUP_BY_VK = 11; //


    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, Users.USERS_TABLE, URI_USERS);
        uriMatcher.addURI(AUTHORITY, Users.USERS_TABLE + "/#", URI_USERS_LEFT_IN_GROUP);
        uriMatcher.addURI(AUTHORITY, Groups.GROUPS_TABLE, URI_GROUPS);
        uriMatcher.addURI(AUTHORITY, GroupMembers.GROUP_MEMBERS_TABLE, URI_GROUP_MEMBERS);
        uriMatcher.addURI(AUTHORITY, Bills.BILLS_TABLE, URI_BILLS);
        uriMatcher.addURI(AUTHORITY, Debts.DEBTS_TABLE, URI_DEBTS);
        uriMatcher.addURI(AUTHORITY, Calculation.CALCULATION_TABLE, URI_CALCULATION);
        uriMatcher.addURI(AUTHORITY, Calculation.CALCULATION_TABLE+ "/#", URI_CALCULATION);
        uriMatcher.addURI(AUTHORITY, History.HISTORY_TABLE, URI_HISTORY);
        uriMatcher.addURI(AUTHORITY, Debts.DEBTS_TABLE + "/dialog", URI_DEBTS_DIALOG);
        uriMatcher.addURI(AUTHORITY, CalculationDetails.CALCULATION_DETAILS_TABLE, URI_CALC_DETAILS);
        uriMatcher.addURI(AUTHORITY, Users.USERS_TABLE + "/order_vk", URI_FRIENDS_GROUP_BY_VK);
    }


    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_USERS_LEFT_IN_GROUP:
            case URI_USERS:
            case URI_FRIENDS_GROUP_BY_VK:
                return USERS_CONTENT_TYPE;

            case URI_GROUPS:
                return GROUPS_CONTENT_TYPE;

            case URI_GROUP_MEMBERS:
                return GROUP_MEMBERS_CONTENT_TYPE;

            case URI_BILLS:
                return BILLS_CONTENT_TYPE;

            case URI_DEBTS:
                return DEBTS_CONTENT_TYPE;

            case URI_CALCULATION:
                return CALCULATION_CONTENT_TYPE;

            case URI_HISTORY:
                return HISTORY_CONTENT_TYPE;

            case URI_CALC_DETAILS:
                return CALC_DETAILS_CONTENT_TYPE;



        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Uri notifyUri;
        db = dbHelper.getWritableDatabase();
        String id;
        String table;
        Log.d("SQL", uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_USERS:
                table = Users.USERS_TABLE;
                notifyUri = USERS_CONTENT_URI;
                break;

            case URI_GROUPS:
                table = Groups.GROUPS_TABLE;
                notifyUri = GROUPS_CONTENT_URI;
                break;

            case URI_GROUP_MEMBERS:
                table = GroupMembers.GROUP_MEMBERS_TABLE;
                notifyUri = GROUP_MEMBERS_CONTENT_URI;
                break;

            case URI_BILLS:
                table = Bills.BILLS_TABLE;
                notifyUri = BILLS_CONTENT_URI;
                break;

            case URI_DEBTS:
                table = Debts.DEBTS_TABLE;
                notifyUri = DEBTS_CONTENT_URI;

                Cursor cursor = db.query(table, projection, selection,
                        selectionArgs, "(case when " + Debts.USER_ID + " is null then "+Debts.ITEM_ID+" else "+ Debts.USER_ID + " end)", null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), notifyUri);
                return cursor;
                //break;

            case URI_DEBTS_DIALOG:
                table = Debts.DEBTS_TABLE;
                notifyUri = DEBTS_CONTENT_URI;
                break;

            case URI_CALCULATION:
                table = Calculation.CALCULATION_TABLE;
                notifyUri = CALCULATION_CONTENT_URI;
                break;

            case URI_HISTORY:
                table = History.HISTORY_TABLE;
                notifyUri = HISTORY_CONTENT_URI;
                break;

            case URI_USERS_LEFT_IN_GROUP: {
                id = uri.getLastPathSegment();
                notifyUri = USERS_CONTENT_URI;
                String request = "select * from " +
                        " (select " +
                        Users.USERS_TABLE + "." + Users.ITEM_ID + " as " + Users.ITEM_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID + " as " + Users.USER_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID_VK + " as " + Users.USER_ID_VK + ", " +
                        Users.USERS_TABLE + "." + Users.NAME + " as " + Users.NAME + ", " +
                        Users.USERS_TABLE + "." + Users.IMG + " as " + Users.IMG + ", " +
                        Users.USERS_TABLE + "." + Users.STATE + " as " + Users.STATE + ", " +
                        Users.USERS_TABLE + "." + Users.RESULT + " as " + Users.RESULT +
                        " from "
                        //=========
                        + Users.USERS_TABLE + " left join " + GroupMembers.GROUP_MEMBERS_TABLE +
                        " on " + Users.USERS_TABLE + "." + Users.USER_ID + " = " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID +
                        " and " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.GROUP_ID + " = " + id +
                        " where " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID + " is null" +
                        " and " + Users.USER_ID_VK + "=0" +
                        //=========
                        " Order By " + Users.NAME + ")"
                        + " UNION ALL " +
                        "select * from (" +
                        "select " + Users.USERS_TABLE + "." + Users.ITEM_ID + " as " + Users.ITEM_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID + " as " + Users.USER_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID_VK + " as " + Users.USER_ID_VK + ", " +
                        Users.USERS_TABLE + "." + Users.NAME + " as " + Users.NAME + ", " +
                        Users.USERS_TABLE + "." + Users.IMG + " as " + Users.IMG + ", " +
                        Users.USERS_TABLE + "." + Users.STATE + " as " + Users.STATE + ", " +
                        Users.USERS_TABLE + "." + Users.RESULT + " as " + Users.RESULT +
                        " from "
                        + Users.USERS_TABLE + " left join " + GroupMembers.GROUP_MEMBERS_TABLE +
                        " on " + Users.USERS_TABLE + "." + Users.USER_ID + " = " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID +
                        " and " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.GROUP_ID + " = " + id +
                        " where " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID + " is null" +
                        " and " + Users.USER_ID_VK + "!=0" +
                        " Order By " + Users.NAME + ")";

                String reque1st = "select " +
                        Users.USERS_TABLE + "." + Users.ITEM_ID + " as " + Users.ITEM_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID + " as " + Users.USER_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID_VK + " as " + Users.USER_ID_VK + ", " +
                        Users.USERS_TABLE + "." + Users.NAME + " as " + Users.NAME + ", " +
                        Users.USERS_TABLE + "." + Users.IMG + " as " + Users.IMG + ", " +
                        Users.USERS_TABLE + "." + Users.STATE + " as " + Users.STATE + ", " +
                        Users.USERS_TABLE + "." + Users.RESULT + " as " + Users.RESULT +
                        " from "
                        + Users.USERS_TABLE + " left join " + GroupMembers.GROUP_MEMBERS_TABLE +
                        " on " + Users.USERS_TABLE + "." + Users.USER_ID + " = " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID +
                        " and " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.GROUP_ID + " = " + id +
                        " where " + GroupMembers.GROUP_MEMBERS_TABLE + "." + GroupMembers.USER_ID + " is null" +
                        " Order By " + Users.NAME;
                Cursor c = db.rawQuery(request, null);
                int count = c.getCount();
                c.setNotificationUri(getContext().getContentResolver(), notifyUri);
                return c;
            }
            case URI_CALC_DETAILS:
                table = CalculationDetails.CALCULATION_DETAILS_TABLE;
                notifyUri = CALC_DETAILS_CONTENT_URI;
                break;

            case URI_FRIENDS_GROUP_BY_VK: {
                notifyUri = USERS_CONTENT_URI;
                String request = "select * from " +
                        " (select " +
                        Users.USERS_TABLE + "." + Users.ITEM_ID + " as " + Users.ITEM_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID + " as " + Users.USER_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID_VK + " as " + Users.USER_ID_VK + ", " +
                        Users.USERS_TABLE + "." + Users.NAME + " as " + Users.NAME + ", " +
                        Users.USERS_TABLE + "." + Users.IMG + " as " + Users.IMG + ", " +
                        Users.USERS_TABLE + "." + Users.STATE + " as " + Users.STATE + ", " +
                        Users.USERS_TABLE + "." + Users.RESULT + " as " + Users.RESULT +
                        " from " + Users.USERS_TABLE +
                        " where " + Users.USER_ID_VK + "=0" +
                        " Order By " + Users.NAME + ")"
                        + " UNION ALL " +
                        "select * from (" +
                        "select " + Users.USERS_TABLE + "." + Users.ITEM_ID + " as " + Users.ITEM_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID + " as " + Users.USER_ID + ", " +
                        Users.USERS_TABLE + "." + Users.USER_ID_VK + " as " + Users.USER_ID_VK + ", " +
                        Users.USERS_TABLE + "." + Users.NAME + " as " + Users.NAME + ", " +
                        Users.USERS_TABLE + "." + Users.IMG + " as " + Users.IMG + ", " +
                        Users.USERS_TABLE + "." + Users.STATE + " as " + Users.STATE + ", " +
                        Users.USERS_TABLE + "." + Users.RESULT + " as " + Users.RESULT +
                        " from " + Users.USERS_TABLE +
                        " where " + Users.USER_ID_VK + "!=0" +
                        " Order By " + Users.NAME + ")";
                Cursor c = db.rawQuery(request, null);
                c.setNotificationUri(getContext().getContentResolver(), notifyUri);
                return c;
            }

            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);

        }

        //getContext().getContentResolver().notifyChange(notifyUri, null);
        Cursor cursor = db.query(table, projection, selection,
                selectionArgs, null, null, sortOrder);
        int length = cursor.getCount();
        cursor.setNotificationUri(getContext().getContentResolver(), notifyUri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String table;
        switch (uriMatcher.match(uri)) {
            case URI_BILLS:
                table = Bills.BILLS_TABLE;
                break;
            case URI_USERS:
                table = Users.USERS_TABLE;
                break;
            case URI_DEBTS:
                table = Debts.DEBTS_TABLE;
                break;
            case URI_GROUPS:
                table = Groups.GROUPS_TABLE;
                break;
            case URI_GROUP_MEMBERS:
                table = GroupMembers.GROUP_MEMBERS_TABLE;
                break;
            case URI_CALCULATION:
                table = Calculation.CALCULATION_TABLE;
                break;
            case URI_HISTORY:
                table = History.HISTORY_TABLE;
                break;
            case URI_CALC_DETAILS:
                table = CalculationDetails.CALCULATION_DETAILS_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(table, null, contentValues);
        Uri resultUri = ContentUris.withAppendedId(uri, rowID);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        String table;
        switch (uriMatcher.match(uri)) {
            case URI_BILLS:
                table = Bills.BILLS_TABLE;
                break;
            case URI_USERS:
                table = Users.USERS_TABLE;
                break;
            case URI_DEBTS:
                table = Debts.DEBTS_TABLE;
                break;
            case URI_GROUPS:
                table = Groups.GROUPS_TABLE;
                break;
            case URI_GROUP_MEMBERS:
                table = GroupMembers.GROUP_MEMBERS_TABLE;
                break;
            case URI_CALCULATION:
                table = Calculation.CALCULATION_TABLE;
                break;
            case URI_HISTORY:
                table = History.HISTORY_TABLE;
                break;
            case URI_CALC_DETAILS:
                table = CalculationDetails.CALCULATION_DETAILS_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String table;
        switch (uriMatcher.match(uri)) {
            case URI_BILLS:
                table = Bills.BILLS_TABLE;
                break;
            case URI_USERS:
                table = Users.USERS_TABLE;
                break;
            case URI_DEBTS:
                table = Debts.DEBTS_TABLE;
                break;
            case URI_GROUPS:
                table = Groups.GROUPS_TABLE;
                break;
            case URI_GROUP_MEMBERS:
                table = GroupMembers.GROUP_MEMBERS_TABLE;
                break;
            case URI_CALCULATION:
                table = Calculation.CALCULATION_TABLE;
                break;
            case URI_HISTORY:
                table = History.HISTORY_TABLE;
                break;
            case URI_CALC_DETAILS:
                table = CalculationDetails.CALCULATION_DETAILS_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        int cnt = db.update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
