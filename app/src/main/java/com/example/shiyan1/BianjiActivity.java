package com.example.shiyan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class BianjiActivity extends AppCompatActivity {

    EditText txt_xm, txt_sj;
    RadioButton rb_nan, rb_nv;
    CheckBox cb_ly, cb_yd, cb_qt;
    ListView lv_s;
    Spinner sp_jg;
    List<String> provinceList = new ArrayList<String>();
    Dictionary<String, String> dictResult = new Hashtable<String, String>();
    String tmpProvince[] = {"河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西壮族", "西藏",  "宁夏回族", "新疆维吾尔", "北京", "天津", "上海", "重庆", "香港", "澳门"};
//    String majorList[] = {"计算机", "软件工程", "物联网", "信息安全与管理", "大数据", "云计算机", "移动应用技术", "微电子"};
    List<String> majorList = new ArrayList<>();
    //    String studentData[] = new String[7];
    private DBHandler dbHandler;
    List studentData = new ArrayList();
    int infoid;
    Boolean focus = false;
    Switch sw_focus;
    String profile = "image/*";
    ImageView ivProfileBianji;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bianji);

        Toolbar toolbar;
        toolbar = findViewById(R.id.bianji_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BianjiActivity.this.finish();
            }
        });

        dbHandler = new DBHandler(BianjiActivity.this);

        // get haoyou intent data
        Intent intentFromHaoyouliebiao = getIntent();
        Bundle studentDataBundle = intentFromHaoyouliebiao.getExtras();

        // get haoyou data from db and assign it to studentData List
        infoid = (Integer) studentDataBundle.get("infoid");
        studentData.addAll(dbHandler.getAllHaoyouInfo(DBHandler.JibenXinxiBiao_NAME, infoid));

//        ivProfileBianji = findViewById(R.id.ivProfileBianji);
//        ivProfileBianji.setImageResource(R.drawable.qq_logo_clipart_3);

        txt_xm = (EditText) findViewById(R.id.txt_bianji_xm);
        txt_xm.setText(studentData.get(0).toString());
        txt_sj = (EditText) findViewById(R.id.txt_bianji_sj);
        txt_sj.setText(studentData.get(1).toString());
        rb_nan = (RadioButton) findViewById(R.id.rb_bianji_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_bianji_nv);
        if(studentData.get(2).equals("男"))
            rb_nan.setChecked(true);
        else
            rb_nv.setChecked(true);
        cb_ly = (CheckBox) findViewById(R.id.cb_bianji_ly);
        cb_yd = (CheckBox) findViewById(R.id.cb_bianji_yd);
        cb_qt = (CheckBox) findViewById(R.id.cb_bianji_qt);
        String tmpAihao = studentData.get(3).toString();
        if(tmpAihao.contains("旅游"))
            cb_ly.setChecked(true);
        if(tmpAihao.contains("运动"))
            cb_yd.setChecked(true);
        if(tmpAihao.contains("其他"))
            cb_qt.setChecked(true);

        // adding content to spinner
        sp_jg = (Spinner) findViewById(R.id.sp_bianji_jg);
        Collections.addAll(provinceList, tmpProvince);
        ArrayAdapter<String> adProvinceSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, provinceList);
        sp_jg.setAdapter(adProvinceSpinner);

        int selectionPosition = adProvinceSpinner.getPosition(studentData.get(4).toString());
        // set the default item
        sp_jg.setSelection(selectionPosition);
        // adding spinner select listener
        sp_jg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dictResult.put("籍贵", provinceList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sw_focus = findViewById(R.id.sw_bianji_focus);
        sw_focus.setChecked(studentData.get(6).equals("1"));
        sw_focus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                focus = b;
            }
        });

        // adding content to listview
        lv_s = (ListView) findViewById(R.id.lv_bianji_s);
        majorList = dbHandler.getAllMajor();
        ArrayAdapter<String> adZhuanyeListView = new ArrayAdapter<String>(this, R.layout.activity_zhuanye, R.id.tv_zhuanye, majorList);
        String selectedMajor = dbHandler.getMajorName(studentData.get(5).toString());
        int selectedMajorPosition = adZhuanyeListView.getPosition(selectedMajor);
        dictResult.put("专业", String.valueOf(studentData.get(5)));
        lv_s.setAdapter(adZhuanyeListView);
        // set selected item backgroundcolor
        lv_s.post(new Runnable() {
            @Override
            public void run() {
//                lv_s.getChildAt(selectedMajorPosition).setBackgroundColor(Color.parseColor("#5DE094"));
            }
        });
        // adding listview select listener
        lv_s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dictResult.put("专业", String.valueOf(i + 1));
            }
        });
    }

    public void btn_bianji_queding_click(View view) {

        dictResult.put("性别", rb_nan.isChecked() ? rb_nan.getText().toString() : rb_nv.getText().toString());

        List<String> tmpAihao = new ArrayList<>();
        if(cb_ly.isChecked())
            tmpAihao.add("旅游");
        if(cb_yd.isChecked())
            tmpAihao.add("运动");
        if(cb_qt.isChecked())
            tmpAihao.add("其他");
        dictResult.put("爱好", tmpAihao.toString());

        String tmpXm = txt_xm.getText().toString();
        String tmpSj = txt_sj.getText().toString();
        Boolean flagXm = true;
        Boolean flagSj = true;
        // checking 姓名 isDigit()
        char[] tmpChars = tmpXm.toCharArray();
        for(char c : tmpChars) {
            if(Character.isDigit(c)) {
                flagXm = false;
                Toast.makeText(this, "There's a number in 姓名", Toast.LENGTH_LONG).show();
            }
        }
        // checking 手机号 is contain alphabets
        char[] tmpCharsSj = tmpSj.toCharArray();
        for(char c : tmpCharsSj) {
            if(!Character.isDigit(c)) {
                flagSj = false;
                Toast.makeText(this, "Phone number contain alphabet", Toast.LENGTH_LONG).show();
            }
        }
//
        if(flagXm && flagSj) {
            dictResult.put("姓名", txt_xm.getText().toString());
            dictResult.put("手机", txt_sj.getText().toString());
////            studentData[1] = dictResult.get("姓名");
////            studentData[2] = dictResult.get("手机");
////            studentData[3] = dictResult.get("性别");
////            studentData[4] = dictResult.get("爱好").toString();
////            studentData[5] = dictResult.get("籍贵");
////            studentData[6] = dictResult.get("专业");
////            Boolean focus = studentData.get(6).equals(1) ? Boolean.TRUE : Boolean.FALSE;
            int majorId = Integer.parseInt(dictResult.get("专业"));
//////            int focus = (int) studentData.get(6);
            dbHandler.updateHaoyouBaseInfo(infoid, dictResult.get("姓名"), dictResult.get("手机"), dictResult.get("性别"), dictResult.get("爱好").toString(), dictResult.get("籍贵"), majorId, focus, profile);
////            Toast.makeText(this, infoid + " " + dictResult.get("姓名") +" "+dictResult.get("手机")+" "+dictResult.get("性别")+" "+dictResult.get("爱好").toString()+" "+ dictResult.get("籍贵")+" "+majorId, Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentBackToHaoyouList = new Intent(BianjiActivity.this, HaoyouliebiaoActivity.class);
                    startActivity(intentBackToHaoyouList);
                }
            }, 1000);
            Toast.makeText(this, "编辑成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "编辑失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_bianji_quxiao_click(View view) {
        finish();
    }
}