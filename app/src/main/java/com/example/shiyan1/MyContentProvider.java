package com.example.shiyan1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    static final String HAOYOU_TABLE_NAME = "BaseInfoProvider";
    static final String _ID = "infoid";
    static final String NAME = "name";
    static final String PHONE = "phone";
    private DBHandler dbHandler;
    static final String AUTHORITY = "com.example.shiyan1";


    public MyContentProvider() {
    }


    private static class DBHandler extends SQLiteOpenHelper {

        DBHandler(Context context) {
            super(context, "shiyan", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String createJibenXinxiBiaoProviderQuery = "CREATE TABLE BaseInfoProvider ("
                    + "infoid  INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "phone TEXT NOT NULL UNIQUE, "
                    + "sex TEXT DEFAULT '男', "
                    + "hobby TEXT, "
                    + "place TEXT, "
                    + "majorid INTEGER, "
                    + "focus BOOLEAN DEFAULT 'FALSE', "
                    + "profile TEXT)";
            sqLiteDatabase.execSQL(createJibenXinxiBiaoProviderQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HAOYOU_TABLE_NAME);
//            onCreate(sqLiteDatabase);
        }
    }

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    {
        uriMatcher.addURI(AUTHORITY, "haoyou", 1); // 单条记录
        uriMatcher.addURI(AUTHORITY, "haoyou/#", 2); // 多条记录
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = db.delete(HAOYOU_TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                selection = _ID + "=" + uri.getPathSegments().get(1);
                count = db.delete(HAOYOU_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
//        throw new UnsupportedOperationException("Not yet implemented");
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case 1:
                long id = db.insert(HAOYOU_TABLE_NAME, null, values);
                uriReturn = ContentUris.withAppendedId(uri, id);
                break;
            case 2:
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHandler = new DBHandler(getContext());
        try {
            db = dbHandler.getWritableDatabase();
        } catch (Exception e) {
            db = dbHandler.getReadableDatabase();
        }
        if (db == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case 1:
                cursor = db.query(HAOYOU_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case 2:
                selection = _ID + "=" + uri.getPathSegments().get(1);
                cursor = db.query(HAOYOU_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = db.update(HAOYOU_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 2:
                selection = _ID + "=" + uri.getPathSegments().get(1);
                count = db.update(HAOYOU_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                break;
        }
        return count;
    }
}