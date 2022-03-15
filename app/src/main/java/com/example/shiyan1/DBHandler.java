package com.example.shiyan1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "shiyan";
    static final String JibenXinxiBiao_NAME = "BaseInfo";
    static final String JibenXinxiBiaoProvider_NAME = "BaseInfoProvider";
    private static final int DB_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createZhuanyeXinxiBiaoQuery = "CREATE TABLE MajorInfo ("
                + "majorid  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "major TEXT NOT NULL UNIQUE)";
        String createJibenXinxiBiaoQuery = "CREATE TABLE "+JibenXinxiBiao_NAME+" ("
                + "infoid  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "phone TEXT NOT NULL UNIQUE, "
                + "sex TEXT DEFAULT '男', "
                + "hobby TEXT, "
                + "place TEXT, "
                + "majorid INTEGER, "
                + "focus BOOLEAN DEFAULT 'FALSE', "
                + "profile TEXT)";
        String createJibenXinxiBiaoProviderQuery = "CREATE TABLE "+JibenXinxiBiaoProvider_NAME+" ("
                + "infoid  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "phone TEXT NOT NULL UNIQUE, "
                + "sex TEXT DEFAULT '男', "
                + "hobby TEXT, "
                + "place TEXT, "
                + "majorid INTEGER, "
                + "focus BOOLEAN DEFAULT 'FALSE', "
                + "profile TEXT)";
        sqLiteDatabase.execSQL(createZhuanyeXinxiBiaoQuery);
        sqLiteDatabase.execSQL(createJibenXinxiBiaoQuery);
        sqLiteDatabase.execSQL(createJibenXinxiBiaoProviderQuery);
    }

    public void addMajorInfo(String major) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("major", major);
        db.insert("MajorInfo", null, values);
        db.close();
    }

    public long getMajorsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, "MajorInfo");
        db.close();
        return count;
    }

    public List getAllMajor() {
        List majorList = new ArrayList();
        String selectQuery = "SELECT major FROM MajorInfo";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                majorList.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return majorList;
    }

    public void addBaseInfo(String tableName, int infoid) {
            SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("insert into baseinfo (name, phone, sex, hobby, place, majorid, focus, profile) select name, phone, sex, hobby, place, majorid, focus, profile from baseinfoprovider where infoid = "+infoid);
            List tmpHaoyouData = getAllHaoyouInfo(JibenXinxiBiaoProvider_NAME, infoid);
            ContentValues values = new ContentValues();
            values.put("name", tmpHaoyouData.get(0).toString());
            values.put("phone", tmpHaoyouData.get(1).toString());
            values.put("sex", tmpHaoyouData.get(2).toString());
            values.put("hobby", tmpHaoyouData.get(3).toString());
            values.put("place", tmpHaoyouData.get(4).toString());
            values.put("majorid", tmpHaoyouData.get(5).toString());
            values.put("focus", tmpHaoyouData.get(6).toString());
            values.put("profile", tmpHaoyouData.get(7).toString());
            db.insert(tableName, null, values);
            db.close();
    }

//    public void addBaseInfo(String name, String phone, String sex, String hobby, String place, int majorid, Boolean focus, String profile) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        values.put("phone", phone);
//        values.put("sex", sex);
//        values.put("hobby", hobby);
//        values.put("place", place);
//        values.put("majorid", majorid);
//        values.put("focus", focus);
//        values.put("profile", profile);
//        db.insert("BaseInfo", null, values);
//        db.close();
//    }

    public List getAllHaoyouName() {
        List haoyouList = new ArrayList();
        String selectQuery = "SELECT name FROM BaseInfo ORDER BY focus DESC, name ASC";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                haoyouList.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return haoyouList;
    }

    @SuppressLint("Range")
    public List getAllHaoyouInfo(String tableName, int infoid) {
        List haoyouInfo = new ArrayList();
        String selectQuery = "SELECT * FROM "+tableName+" WHERE infoid = "+infoid;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("name")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("phone")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("sex")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("hobby")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("place")));
                haoyouInfo.add(cursor.getInt(cursor.getColumnIndex("majorid")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("focus")));
                haoyouInfo.add(cursor.getString(cursor.getColumnIndex("profile")));
            } while(cursor.moveToNext());
        }
        return haoyouInfo;
    }

    public Dictionary getHaoyouPhoneNumber() {
        Dictionary<String, String> phoneNumberList = new Hashtable<>();
        String selectQuery = "SELECT name, phone FROM BaseInfo";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                phoneNumberList.put(cursor.getString(0), cursor.getString(1));
            } while(cursor.moveToNext());
        }
        return phoneNumberList;
    }

//    @SuppressLint("Range")
//    public ArrayList<TongxueHaoyou> allDataForTongxueHaoyou() {
//        ArrayList<TongxueHaoyou> tongxueHaoyous = new ArrayList<>();
//        String selectQuery = "SELECT infoid, name, phone FROM "+JibenXinxiBiaoProvider_NAME;
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if(cursor.moveToFirst()) {
//            do {
//                tongxueHaoyous.add(new TongxueHaoyou(
//                        cursor.getInt(cursor.getColumnIndex("infoid")),
//                        cursor.getString(cursor.getColumnIndex("name")),
//                        cursor.getString(cursor.getColumnIndex("phone"))
//                ));
//            } while(cursor.moveToNext());
//        }
//        return tongxueHaoyous;
//    }

    @SuppressLint("Range")
    public Integer getHaoyouId(String phone) {
//        List infoid = new ArrayList();
        String selectQuery = "SELECT infoid FROM BaseInfo WHERE phone = "+phone;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int infoid = 0;
        if(cursor.moveToFirst()) {
            do {
                infoid = cursor.getInt(cursor.getColumnIndex("infoid"));
            } while(cursor.moveToNext());
        }
        return infoid;
    }

    @SuppressLint("Range")
    public String getMajorName(String majorId) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT major FROM MajorInfo WHERE majorid="+majorId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String majorName = "";
        if(cursor.moveToFirst()) {
            do {
                majorName = cursor.getString(cursor.getColumnIndex("major"));
            } while(cursor.moveToNext());
        }
        return majorName;
    }

    public List searchHaoyouName(String search) {
        List haoyouList = new ArrayList();
        String selectQuery = "SELECT name FROM BaseInfo WHERE name like '%"+search+"%'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                haoyouList.add(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return haoyouList;
    }

    @SuppressLint("Range")
    public String getFocusStatus(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT focus FROM BaseInfo WHERE phone="+phone;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String focusStatus = "";
        if(cursor.moveToFirst()) {
            do {
                focusStatus = cursor.getString(cursor.getColumnIndex("focus"));
            } while(cursor.moveToNext());
        }
        return focusStatus;
    }

    public void updateHaoyouBaseInfo(int infoid, String name, String phone, String sex, String hobby, String place, int majorid, Boolean focus, String profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("sex", sex);
        values.put("hobby", hobby);
        values.put("place", place);
        values.put("majorid", majorid);
        values.put("focus", focus);
        values.put("profile", profile);
        db.update("BaseInfo", values, "infoid = ?", new String[] { String.valueOf(infoid) });
    }

    // guanzhu status
    public void updateHaoyouFocus(int infoid, int focusStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("focus", focusStatus);
        db.update("BaseInfo", values, "infoid = ?", new String[] { String.valueOf(infoid) });
    }

    public void deleteHaoyou(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("BaseInfo", "phone = ?", new String[] { String.valueOf(phone) });
        db.close();
    }

    public void deleteHaoyouById(String tableName, int infoid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, "infoid = ?", new String[] { String.valueOf(infoid) });
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MajorInfo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS BaseInfo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS BaseInfoProvider");
        onCreate(sqLiteDatabase);
    }
}
