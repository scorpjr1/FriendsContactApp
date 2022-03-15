package com.example.shiyan1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class HaoyouProvider extends ContentProvider {

    static final String AUTHORITY = "com.example.shiyan1";
    static final String URL = "content://" + AUTHORITY + "/haoyou";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String PHONE = "phone";
    DBHandler dbHandler;

    private static HashMap<String, String> HAOYOU_PROJECTION_MAP;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "haoyou", 1); // 单条记录
        uriMatcher.addURI(AUTHORITY, "haoyou/#", 2); // 多条记录
    }

    private SQLiteDatabase db;
//    static final String DATABASE_NAME = "shiyan";
    static final String HAOYOU_TABLE_NAME = "BaseInfoProvider";
//    static final int DATABASE_VERSION = 1;
//    static final String CREATE_DB_TABLE =
//            "CREATE TABLE " + HAOYOU_TABLE_NAME + " " +
//                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "name TEXT NOT NULL, " +
//                    "phone TEXT NOT NULL);";
//
//    private static class DBHandler extends SQLiteOpenHelper {
//
//        DBHandler(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase sqLiteDatabase) {
//            sqLiteDatabase.execSQL(CREATE_DB_TABLE);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HAOYOU_TABLE_NAME);
//            onCreate(sqLiteDatabase);
//        }
//    }

    @Override
    public boolean onCreate() {
        if(getContext() != null) {
            dbHandler = new DBHandler(getContext());
            return true;
        }
        return false;
//        return db != null;
//        return (db == null) ? false : true;
    }

    @Nullable
    @Override
    // (uri, projection, selection, multiselection, sortOrder)
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if(dbHandler != null) {
            db = dbHandler.getWritableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(HAOYOU_TABLE_NAME);
            switch (uriMatcher.match(uri)) {
                case 1:
                    qb.setProjectionMap(HAOYOU_PROJECTION_MAP);
                    break;
                case 2:
                    qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                    break;
                default:

            }
            if (s1 == null || s1 == "")
                s1 = NAME;
            Cursor c = qb.query(db, strings, s, strings1, null, null, s1);
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd."+ AUTHORITY +".haoyou";
            case 2:
                return "vnd.android.cursor.item/vnd."+ AUTHORITY +".haoyou";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
//        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        DBHandler dbHandler = new DBHandler(getContext());
        db = dbHandler.getWritableDatabase();
        if(db == null)
            return null;
        else {
            long rowID = db.insert(HAOYOU_TABLE_NAME, "", contentValues);
            if (rowID > 0) {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            }
            throw new SQLException("Failed to add a record into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if(dbHandler != null) {
            db = dbHandler.getWritableDatabase();
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case 1:
                    count = db.delete(HAOYOU_TABLE_NAME, s, strings);
                    break;
                case 2:
                    String id = uri.getPathSegments().get(1); // ambil # nya tok dari path /#
                    count = db.delete(
                            HAOYOU_TABLE_NAME,
                            _ID + " = " + id + (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""),
                            strings);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = db.update(HAOYOU_TABLE_NAME, contentValues, s, strings);
                break;
            case 2:
                String id = uri.getPathSegments().get(1); // ambil # nya tok dari path /#
                count = db.update(
                        HAOYOU_TABLE_NAME,
                        contentValues,
                        _ID + " = " + id + (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""),
                        strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
