package com.example.shiyan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MajorSettingActivity extends AppCompatActivity {

    List<String> majorList = new ArrayList<>();
    DBHandler dbHandler;
    ListView lv_majorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_setting);

        dbHandler = new DBHandler(MajorSettingActivity.this);

        majorList = dbHandler.getAllMajor();

        lv_majorList = findViewById(R.id.lv_majorList);
        ArrayAdapter<String> adZhuanyeListView = new ArrayAdapter<String>(this, R.layout.activity_zhuanye, R.id.tv_zhuanye, majorList);
        lv_majorList.setAdapter(adZhuanyeListView);
        AlertDialog.Builder aldBuilder = new AlertDialog.Builder(MajorSettingActivity.this);
        lv_majorList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu popupmenu_major = new PopupMenu(MajorSettingActivity.this, view, Gravity.RIGHT);
                popupmenu_major.getMenuInflater().inflate(R.menu.majorsetting_kuaijiecaidan, popupmenu_major.getMenu());
                popupmenu_major.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.ms_bianji:
                                Toast.makeText(MajorSettingActivity.this, majorList.get(i), Toast.LENGTH_SHORT).show();
                                // Create an alert builder
                                aldBuilder.setTitle("更新专业");
                                // set the custom layout
                                final View customLayout = getLayoutInflater().inflate(R.layout.bianji_diaolog_layout, null);
                                aldBuilder
                                        .setView(customLayout)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                EditText txt_diaolog_bianji = findViewById(R.id.txt_diaolog_bianji);
                                            }
                                        })
                                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                AlertDialog dialog = aldBuilder.create();
                                dialog.show();
                                break;
                            case R.id.ms_shanchu:
                                Toast.makeText(MajorSettingActivity.this, majorList.get(i), Toast.LENGTH_SHORT).show();
                                aldBuilder
                                        .setTitle("提示")
                                        .setMessage("确定删除吗？")
                                        .setCancelable(false)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                AlertDialog dialog1 = aldBuilder.create();
                                dialog1.show();
                                break;
                        }
                        return false;
                    }
                });
                popupmenu_major.show();
                return false;
            }
        });


        ActionBar xiangxiActionBar = getSupportActionBar();
        xiangxiActionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}