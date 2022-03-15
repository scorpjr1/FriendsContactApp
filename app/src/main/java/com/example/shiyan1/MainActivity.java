package com.example.shiyan1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.shiyan1.databinding.ActivityMainBinding;
import com.example.shiyan1.databinding.DialogCustomImageSelectionBinding;
import com.example.shiyan1.model.entities.Haoyou;
import com.example.shiyan1.view.activities.AddHaoyouActivity;
import com.example.shiyan1.viewmodel.HaoyouViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText txt_xm, txt_sj;
    RadioButton rb_nan, rb_nv;
    CheckBox cb_ly, cb_yd, cb_qt;
    ListView lv_s;
    Spinner sp_jg;
    List<String> provinceList = new ArrayList<String>();
    Dictionary<String, String> dictResult = new Hashtable<String, String>();
    String tmpProvince[] = {"河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西壮族", "西藏",  "宁夏回族", "新疆维吾尔", "北京", "天津", "上海", "重庆", "香港", "澳门"};
    String tmpMajor[] = {"计算机", "软件工程", "物联网", "信息安全与管理", "大数据", "云计算机", "移动应用技术", "微电子"};
    List<String> majorList = new ArrayList<>();
    private DBHandler dbHandler;
    int majorid;
    Boolean focus = false;
    Switch sw_focus;

    ImageView ivProfile;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;

    ActivityMainBinding activityMainBinding;
    private static final int CAMERA_ID = 1;
    private static final int GALLERY_ID = 2;
    private static final String IMAGE_DIRECTORY = "ProfilePictureImages";
    private static final String EXTRA_REPLY = "com.example.shiyan1.haoyoulistsql.REPLY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar setting and clickListener
        Toolbar toolbar = findViewById(R.id.tianjia_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });

        // passing context to it.
        dbHandler = new DBHandler(MainActivity.this);

        txt_xm = (EditText) findViewById(R.id.txt_xm);
        txt_sj = (EditText) findViewById(R.id.txt_sj);
        rb_nan = (RadioButton) findViewById(R.id.rb_nan);
        rb_nv = (RadioButton) findViewById(R.id.rb_nv);
        cb_ly = (CheckBox) findViewById(R.id.cb_ly);
        cb_yd = (CheckBox) findViewById(R.id.cb_yd);
        cb_qt = (CheckBox) findViewById(R.id.cb_qt);



        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource(R.drawable.qq_logo_clipart_3);
        ivProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu_profile = new PopupMenu(MainActivity.this, view, Gravity.BOTTOM);
                popupMenu_profile.getMenuInflater().inflate(R.menu.kuaijiecaidan_profile, popupMenu_profile.getMenu());
                popupMenu_profile.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.browsePhoto_add:
//                                openGallery();
//                                mGetContent.launch("image/*");
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, PICK_IMAGE);
                                Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu_profile.show();
                return false;
            }
        });

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater()); // mau pake viewBinding trus this.setContentView(.getroot()) tpi dari awal sdah pake findViewById(), jadi hrus ganti smua
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCustomImageSelectionBinding dialogCustomImageSelectionBinding;
                dialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(getLayoutInflater());
                Dialog selectImageDialog = new Dialog(MainActivity.this);
                selectImageDialog.setCancelable(true);
                selectImageDialog.setCanceledOnTouchOutside(true);
//                selectImageDialog.setContentView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_custom_image_selection, null));
                selectImageDialog.setContentView(dialogCustomImageSelectionBinding.getRoot());
                dialogCustomImageSelectionBinding.tvDcisCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dexter.withContext(MainActivity.this)
                                .withPermissions(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA
                                )
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                        if (multiplePermissionsReport != null && multiplePermissionsReport.areAllPermissionsGranted()) {
//                                            Toast.makeText(MainActivity.this, "All Permission is Granted", Toast.LENGTH_SHORT).show();
                                            Intent intentOpenCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intentOpenCamera, CAMERA_ID);
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                        showRationalDialogPermissions();
                                    }
                                })
                                .onSameThread()
                                .check();
                        selectImageDialog.dismiss();
                    }
                });
                dialogCustomImageSelectionBinding.tvDcisGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dexter.withContext(MainActivity.this)
                                .withPermission(
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intentGallery, GALLERY_ID);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                        Toast.makeText(MainActivity.this, "Permission is Denied", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        showRationalDialogPermissions();
                                    }
                                })
                                .onSameThread()
                                .check();
                        selectImageDialog.dismiss();
                    }
                });

                selectImageDialog.show();
            }
        });


        // adding content to spinner
        sp_jg = (Spinner) findViewById(R.id.sp_jg);
        Collections.addAll(provinceList, tmpProvince);
        ArrayAdapter<String> adProvinceSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, provinceList);
        sp_jg.setAdapter(adProvinceSpinner);
        // getting 江苏 position id
        ArrayAdapter tmpAD = (ArrayAdapter) sp_jg.getAdapter();
        int jiangsuPosition = tmpAD.getPosition("江苏");
        // set the default item
        sp_jg.setSelection(jiangsuPosition);
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


        sw_focus = findViewById(R.id.sw_focus);
        sw_focus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                focus = b;
            }
        });


        Collections.addAll(majorList, tmpMajor);
        if (dbHandler.getMajorsCount() == 0) {
            // insert 专业信息表, 意思就是专业信息表还没被初始化的
            for (int zxbi = 0; zxbi < majorList.size(); zxbi++) {
                dbHandler.addMajorInfo(majorList.get(zxbi));
            }
        } else {
            majorList.clear();
            majorList = dbHandler.getAllMajor();
        }

        // adding content to listview
        lv_s = (ListView) findViewById(R.id.lv_s);
        ArrayAdapter<String> adZhuanyeListView = new ArrayAdapter<String>(this, R.layout.activity_zhuanye, R.id.tv_zhuanye, majorList);
        lv_s.setAdapter(adZhuanyeListView);
        // adding listview select listener
        lv_s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dictResult.put("专业", majorList.get(i));
//                lv_s.setSelection(i);
                // 数据库的自增长从1开始，而onClickListener的i是从0开始
                majorid = i + 1;
            }
        });


        /** adding back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // showing the back button*/
    }

    private void showRationalDialogPermissions() {
        AlertDialog.Builder alertDialogPermission = new AlertDialog.Builder(MainActivity.this);
        alertDialogPermission
                .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
                .setPositiveButton(
                        "GO TO SETTINGS",
                        (dialog, which) -> {
                            try {
                                Intent intentToSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intentToSettings.setData(uri);
                                startActivity(intentToSettings);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        (dialog, which) -> {
                            dialog.dismiss();
                        }
                )
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_ID && data != null) {
//                Bitmap from camera
                Bitmap newImageBitmap = (Bitmap) data.getExtras().get("data");
//                Set Capture Image bitmap to the imageView
//                ivProfile.setImageBitmap(newImageBitmap);
//                using Glide
                Glide.with(MainActivity.this)
                        .load(newImageBitmap)
                        .centerCrop()
                        .into(ivProfile);
//                Save the captured image via Camera to the app directory and get back the image path.
                String mImagePath = saveImageToInternalStorage(newImageBitmap);
            } else if(requestCode == GALLERY_ID && data != null) {
                Uri selectedPhotoUri = data.getData();
//                Set Selected Image URI to the imageView
//                ivProfile.setImageURI(selectedPhotoUri);
//                using Glide
                Glide.with(MainActivity.this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("TAG", "Error Loading Image", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                Get the Bitmap and save it to the local storage and get the Image Path.
                                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                String mImagePath = saveImageToInternalStorage(bitmap);
                                Log.i("ImagePath", mImagePath);
                                return false;
                            }
                        })
                        .into(ivProfile);
            }
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled");
        }
    }

    public String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File file = contextWrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID()+".jpg");
        try {
            OutputStream fosImage = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
            fosImage.flush();
            fosImage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    @Override
    protected void onResume() {
        super.onResume();

        @SuppressLint("WrongConstant") SharedPreferences spHuoquTianjia = getSharedPreferences("TianjiaSP", MODE_APPEND);
        if (spHuoquTianjia.contains("xm")) {
            String xm = spHuoquTianjia.getString("xm", "");
            txt_xm.setText(xm);
        }
        if (spHuoquTianjia.contains("sj")) {
            String sj = spHuoquTianjia.getString("sj", "");
            txt_sj.setText(sj);
        }

        if (spHuoquTianjia.contains("xb")) {
            String xb = spHuoquTianjia.getString("xb", "");
            if (xb.equals("男"))
                rb_nan.setChecked(true);
            else
                rb_nv.setChecked(true);
        }

        if (spHuoquTianjia.contains("ah")) {
            String ah = spHuoquTianjia.getString("ah", "");
            if (ah.contains("运动"))
                cb_yd.setChecked(true);
            if (ah.contains("旅游"))
                cb_ly.setChecked(true);
            if (ah.contains("其他"))
                cb_qt.setChecked(true);
        }

        if (spHuoquTianjia.contains("jg")) {
            String jg = spHuoquTianjia.getString("jg", "");
            sp_jg.setSelection(provinceList.indexOf(jg));
        }

        if (spHuoquTianjia.contains("zy")) {
            int zy = spHuoquTianjia.getInt("zy", majorid);
            lv_s.setItemChecked(zy, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences spBaocunTianjia = getSharedPreferences("TianjiaSP", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = spBaocunTianjia.edit();
        // save the text that user leave behind
        myEditor.putString("xm", txt_xm.getText().toString());
        myEditor.putString("sj", txt_sj.getText().toString());
        if (rb_nan.isChecked() == true && rb_nv.isChecked() == false)
            myEditor.putString("xb", rb_nan.getText().toString());
        if (rb_nan.isChecked() == false && rb_nv.isChecked() == true)
            myEditor.putString("xb", rb_nv.getText().toString());
        List<String> tmpAihao = new ArrayList<>();
        if(cb_ly.isChecked())
            tmpAihao.add("旅游");
        if(cb_yd.isChecked())
            tmpAihao.add("运动");
        if(cb_qt.isChecked())
            tmpAihao.add("其他");
        myEditor.putString("ah", tmpAihao.toString());
        myEditor.putString("jg", sp_jg.getSelectedItem().toString());
        myEditor.putInt("zy", majorid-1);
        myEditor.commit();
    }


    /** action bar menu click listener
    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public void btn_queding_click(View view) {
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

        if(flagXm == Boolean.TRUE && flagSj == Boolean.TRUE) {
            dictResult.put("姓名", tmpXm);
            dictResult.put("手机", tmpSj);

            ContentResolver resolver = getContentResolver();
            String AUTHORITY = "com.example.shiyan1";
            String URL = "content://" + AUTHORITY + "/haoyou";
            Uri CONTENT_URI = Uri.parse(URL);
            ContentValues tmpData = new ContentValues();
            tmpData.put("name", dictResult.get("姓名"));
            tmpData.put("phone", dictResult.get("手机"));
            tmpData.put("sex", dictResult.get("性别"));
            tmpData.put("hobby", dictResult.get("爱好"));
            tmpData.put("place", dictResult.get("籍贵"));
            tmpData.put("majorid", majorid);
            tmpData.put("focus", focus);
            tmpData.put("profile", "image/");
            /** insert data to database using SQLite directly **/
//            dbHandler.addBaseInfo(dbHandler.JibenXinxiBiao_NAME, tmpData);
            /** insert data to database through ContentProvider **/
//            resolver.insert(CONTENT_URI, tmpData);

            /** insert data to database using Room Database**/
            Haoyou haoyou = new Haoyou(tmpData.get("name").toString(), tmpData.get("phone").toString(), tmpData.get("sex").toString(), tmpData.get("hobby").toString(), tmpData.get("place").toString(), Integer.parseInt(tmpData.get("majorid").toString()), (Boolean) tmpData.get("focus"), tmpData.get("profile").toString());
            // Get a new or existing ViewModel from the ViewModelProvider.
            HaoyouViewModel haoyouViewModel = new ViewModelProvider(this).get(HaoyouViewModel.class);
            haoyouViewModel.insert(haoyou);

            // reset the form value
            txt_xm.setText("");
            txt_sj.setText("");
            if(dictResult.get("性别").equals("男"))
                rb_nan.setChecked(false);
            else
                rb_nv.setChecked(false);
            if(dictResult.get("爱好").contains("运动"))
                cb_yd.setChecked(false);
            if(dictResult.get("爱好").contains("旅游"))
                cb_ly.setChecked(false);
            if(dictResult.get("爱好").contains("其他"))
                cb_qt.setChecked(false);
            sp_jg.setSelection(0);

//            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intentBackToHaoyouliebiao = new Intent(MainActivity.this, FriendsAppMainActivity.class);
//            startActivity(intentBackToHaoyouliebiao);
            startActivity(new Intent(MainActivity.this, AddHaoyouActivity.class));
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_quxiao_click(View view) {
        // finish();
        this.finishAffinity();
    }

}