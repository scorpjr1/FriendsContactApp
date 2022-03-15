package com.example.shiyan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TongxueHaoyouActivity extends AppCompatActivity {

    RecyclerView rv_tongxueHaoyou;
    TongxueHaoyouCustomAdapter tongxueHaoyouCustomAdapter;
    public ArrayList<TongxueHaoyou> tongxueHaoyous = new ArrayList<>();
    private DBHandler dbHandler;
    public static List<Integer> haoyouIdCheckedList = new ArrayList<Integer>();

    @SuppressLint("Range")
    public ArrayList<TongxueHaoyou> allDataForTongxueHaoyou(Cursor cursor) {
        ArrayList<TongxueHaoyou> tmpTongxueHaoyous = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                tmpTongxueHaoyous.add(new TongxueHaoyou(
                        cursor.getInt(cursor.getColumnIndex("infoid")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("phone"))
                ));
            } while(cursor.moveToNext());
        }
        return tmpTongxueHaoyous;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongxue_haoyou);

        dbHandler = new DBHandler(TongxueHaoyouActivity.this);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tongxue_haoyou_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haoyouIdCheckedList.clear();
                TongxueHaoyouActivity.this.finish();
            }
        });
        toolbar.inflateMenu(R.menu.tongxuehaoyou_menu);
        Menu menu = toolbar.getMenu();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tongxuehaoyou_queding_menu:
                        if (haoyouIdCheckedList.isEmpty()) {
                            Toast.makeText(TongxueHaoyouActivity.this, "Nothing Selected!", Toast.LENGTH_SHORT).show();
                        } else if (haoyouIdCheckedList.size() == 1) {
                            Collections.sort(haoyouIdCheckedList);
//                Toast.makeText(TongxueHaoyouActivity.this, haoyouIdCheckedList.toString(), Toast.LENGTH_SHORT).show();
                            /**
                             * 11月13号的作业
                             * loop sebanyak checklist length
                             *      tiap loop insert into base values(select * from provider where id = checklist[i])
                             *      delete from provider where id = checklist[i]
                             * endloop
                             * clear checklist
                             * intentToHaoyouliebiao
                             */
//                            int i = 0;
//                            for (i = 0; i < haoyouIdCheckedList.size() - 1; i++) {
                                dbHandler.addBaseInfo(DBHandler.JibenXinxiBiao_NAME, haoyouIdCheckedList.get(0));
                                dbHandler.deleteHaoyouById(DBHandler.JibenXinxiBiaoProvider_NAME, haoyouIdCheckedList.get(0));
                                tongxueHaoyouCustomAdapter.notifyDataSetChanged();
//                            }
//                            int tmpid = haoyouIdCheckedList.get(0);
//                            List tmpHaoyouData = dbHandler.getAllHaoyouInfo(DBHandler.JibenXinxiBiaoProvider_NAME, tmpid);
//                            if (++i == haoyouIdCheckedList.size()) {
                                Toast.makeText(TongxueHaoyouActivity.this, "成功添加到好友基本信息中", Toast.LENGTH_LONG).show();
                                haoyouIdCheckedList.clear();
                                startActivity(new Intent(TongxueHaoyouActivity.this, FriendsAppMainActivity.class));
//                            }
                            return true;
                        } else {
                            Toast.makeText(TongxueHaoyouActivity.this, "目前只支持单条插入", Toast.LENGTH_LONG).show();
                            return true;
                        }
                }
                return false;
            }
        });

//        tongxueHaoyous = dbHandler.allDataForTongxueHaoyou();
        ContentResolver resolver = getContentResolver();
        String AUTHORITY = "com.example.shiyan1";
        String URL = "content://" + AUTHORITY + "/haoyou";
        Uri CONTENT_URI = Uri.parse(URL);
        Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
        tongxueHaoyous = allDataForTongxueHaoyou(cursor);

        rv_tongxueHaoyou = findViewById(R.id.rv_tongxueHaoyou);
        LinearLayoutManager laoyoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_tongxueHaoyou.setLayoutManager(laoyoutManager);
        tongxueHaoyouCustomAdapter = new TongxueHaoyouCustomAdapter(getApplicationContext(), tongxueHaoyous);
        rv_tongxueHaoyou.setAdapter(tongxueHaoyouCustomAdapter);


    }


}