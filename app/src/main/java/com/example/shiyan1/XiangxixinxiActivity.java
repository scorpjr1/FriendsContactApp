package com.example.shiyan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class XiangxixinxiActivity extends AppCompatActivity {

    Dictionary<Integer, String[]> haoyouList;
    TextView txt_xiangxi_xm, txt_xiangxi_sj, txt_xiangxi_xb, txt_xiangxi_ah, txt_xiangxi_jg, txt_xiangxi_zy;
    private DBHandler dbHandler;
    List studentData = new ArrayList();
    int infoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangxixinxi);

        Toolbar toolbar;
        toolbar = findViewById(R.id.xiangxixinxi_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XiangxixinxiActivity.this.finish();
            }
        });

        dbHandler = new DBHandler(XiangxixinxiActivity.this);

        // get haoyou data
        Intent intentFromHaoyouliebiao = getIntent();
        Bundle studentDataBundle = intentFromHaoyouliebiao.getExtras();
//        String studentData[] = new String[7];

        infoid = (Integer) studentDataBundle.get("infoid");
        studentData.addAll(dbHandler.getAllHaoyouInfo(DBHandler.JibenXinxiBiao_NAME, infoid));

        // initialize jingtai data
//        if(studentDataBundle.get("id").equals(0))
//            studentData = new String[]{"1", "张三", "13506110490", "男", "旅游, 运动", "江苏", "计算机"};
//        else if(studentDataBundle.get("id").equals(1))
//            studentData = new String[]{"2", "李四", "15961137073", "男", "运动", "逐渐", "软件工程"};
//        else
//            studentData = new String[]{"3", "王五", "18801502022", "女", "其他", "上海", "物联网"};
        //haoyouList.put(1, );
        // Toast.makeText(this,  studentDataBundle.get("id").toString(), Toast.LENGTH_LONG).show();

        txt_xiangxi_xm = findViewById(R.id.txt_xiangxi_xm);
        txt_xiangxi_sj = findViewById(R.id.txt_xiangxi_sj);
        txt_xiangxi_xb = findViewById(R.id.txt_xiangxi_xb);
        txt_xiangxi_ah = findViewById(R.id.txt_xiangxi_ah);
        txt_xiangxi_jg = findViewById(R.id.txt_xiangxi_jg);
        txt_xiangxi_zy = findViewById(R.id.txt_xiangxi_zy);

        txt_xiangxi_xm.setText(studentData.get(0).toString());
        txt_xiangxi_sj.setText(studentData.get(1).toString());
        txt_xiangxi_xb.setText(studentData.get(2).toString());
        txt_xiangxi_ah.setText(studentData.get(3).toString());
        txt_xiangxi_jg.setText(studentData.get(4).toString());
        txt_xiangxi_zy.setText(dbHandler.getMajorName(studentData.get(5).toString()));
    }

}