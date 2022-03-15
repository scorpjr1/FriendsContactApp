package com.example.shiyan1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class HaoyouliebiaoActivity extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Toolbar toolbar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HaoyouliebiaoActivity() {
        // Required empty public constructor
    }

    public static HaoyouliebiaoActivity newInstance(String param1, String param2) {
        HaoyouliebiaoActivity fragment = new HaoyouliebiaoActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_haoyouliebiao, container, false);
    }

    // fragment override method (toolbar setting and clickListener)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.haoyouliebiao_toolbar);
        toolbar.inflateMenu(R.menu.mainmenu);
        Menu menu = toolbar.getMenu();

        SearchView sv = (SearchView) menu.findItem(R.id.searchbar).getActionView();
        SearchManager sm = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getActivity().getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List searchResult = dbHandler.searchHaoyouName(s);
                if(searchResult.isEmpty()) {
                    Toast.makeText(getActivity(), "没有叫"+s+"的好友", Toast.LENGTH_SHORT).show();
                    haoyouList.clear();
                    adHaoyou.notifyDataSetChanged();
                } else {
                    haoyouList.clear();
                    haoyouList.addAll(searchResult);
                    adHaoyou.notifyDataSetChanged();
                }
                return false;
            }
        });

        // toolbar menu clickListener
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tianjia_moshengren:
                        Intent intentTianjia = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(intentTianjia);
                        return true;
                    case R.id.haoyou_fenxiang:
                        Intent intentTongxueHaoyou = new Intent(getActivity().getApplicationContext(), TongxueHaoyouActivity.class);
                        startActivity(intentTongxueHaoyou);
                        break;
                    case R.id.nbcc_daoru:
                        try {
                            FileInputStream fisNbDaoru = getActivity().openFileInput(nbFileName);
//                    StringBuilder tmp = new StringBuilder();
////                    byte[] readBytes = new byte[fisNbDaoru.available()];
////                    while (fisNbDaoru.read(readBytes) != 1) {
//
//                    while((a = fisNbDaoru.read()) != -1) {
//                        tmp.append((char) a);
//                    }

                            Scanner sc = new Scanner(fisNbDaoru);
                            List tmp = new ArrayList();
                            while(sc.hasNextLine()) {
                                tmp.add(sc.nextLine());
                            }
                            Toast.makeText(getActivity(), tmp.toString(), Toast.LENGTH_SHORT).show();
                            sc.close();
//                    Toast.makeText(HaoyouliebiaoActivity.this, readBytes.toString(), Toast.LENGTH_SHORT).show();
//                    fisNbDaoru.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "导入失败", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case R.id.nbcc_daochu:
                        try {
                            FileOutputStream fosNbDaochu = getActivity().openFileOutput(nbFileName, Context.MODE_PRIVATE);
//                    fosNbDaochu.write("CCZU".getBytes());
//                    fosNbDaochu.write("CCZU1".getBytes());
//                    fosNbDaochu.flush();
//                    fosNbDaochu.close();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fosNbDaochu));
                            for(int i=0;i<haoyouList.size();i++) {
                                bw.write(haoyouList.get(i));
                                bw.newLine();
                            }
                            bw.close();
                            Toast.makeText(getActivity(), "数据成功导出到内部存储", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "导出失败", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.wbcc_daoru:
                        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    String filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + wbFileName;
                                    FileInputStream fisWbDaoru = new FileInputStream(filename);
                                    Scanner sc = new Scanner(fisWbDaoru);
                                    List tmp = new ArrayList();
                                    while(sc.hasNextLine()) {
                                        tmp.add(sc.nextLine());
                                    }
                                    Toast.makeText(getActivity(), tmp.toString(), Toast.LENGTH_SHORT).show();
                                    sc.close();
                                }
                            } catch(IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "导入失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                        }
                        break;
                    case R.id.wbcc_daochu:
                        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                    String filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + wbFileName;
                                    FileOutputStream fosWbDaochu = new FileOutputStream(filename);
//                            fosWbDaochu.write("CCZU".getBytes());
//                            fosWbDaochu.flush();
//                            fosWbDaochu.close();
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fosWbDaochu));
                                    for(int i=0;i<haoyouList.size();i++) {
                                        bw.write(haoyouList.get(i));
                                        bw.newLine();
                                    }
                                    bw.close();
                                    Toast.makeText(getActivity(), "数据成功导出到外部存储", Toast.LENGTH_SHORT).show();
                                }
                            } catch(IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "导入失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                        }
                        break;

                }
                return false;
            }
        });


        dbHandler = new DBHandler(getActivity());

        // binding the service
        Intent intentBindService = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intentBindService, myConnection, getActivity().BIND_AUTO_CREATE);

//        phoneNumbers.put("张三", "13506110490");
        if (dbHandler.getHaoyouPhoneNumber() != null)
            phoneNumbers = dbHandler.getHaoyouPhoneNumber();

        // check the getHaoyouPhoneNumber() result
//        List tmp = new ArrayList();
//        for (Enumeration dictElems = phoneNumbers.elements(); dictElems.hasMoreElements();) {
//            tmp.add(dictElems.nextElement().toString());
//        }
//            Toast.makeText(getActivity(), tmp.toString(), Toast.LENGTH_SHORT).show();

        // 好友列表的初始化
//        tv_haoyou = (TextView) getLayoutInflater().inflate(R.layout.activity_haoyoulist, null).findViewById(R.id.tv_haoyou);
        tv_haoyou = (TextView) view.findViewById(R.id.tv_haoyou);
        // selecting the listview
//        lv_haoyou = (ListView) getLayoutInflater().inflate(R.layout.activity_haoyouliebiao, null).findViewById(R.id.lv_haoyou);
        lv_haoyou = (ListView) view.findViewById(R.id.lv_haoyou);
        // declare the array data
//        Collections.addAll(haoyouList, tmphaoyouList);
        // set array to the listview
        adHaoyou = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.activity_haoyoulist, R.id.tv_haoyou, haoyouList);
        lv_haoyou.setAdapter(adHaoyou);
        initializeLvHaouyouItem();

        lv_haoyou.setFocusableInTouchMode(true);
        lv_haoyou.post(new Runnable() {
            @Override
            public void run() {
                // loop liat focusstatus, if true
                lv_haoyou.getChildAt(0).setBackgroundColor(Color.parseColor("#198991"));
                // taro zheduan daima di dlem fungsi set backcolor, tiap buka, guanzhu, unguanzhu panggil ini
            }
        });


        // listview items long click listener
        lv_haoyou.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) { // (, , position, )
                // pop up ing the menu
                PopupMenu popupmenu_haoyou = new PopupMenu(getActivity(), view, Gravity.RIGHT);
                popupmenu_haoyou.getMenuInflater().inflate(R.menu.kuaijiecaidan, popupmenu_haoyou.getMenu());

                // menu.add("groupId", "ItemId", "OrderID", "title")
                // getFocusStatus()
                if(dbHandler.getFocusStatus(phoneNumbers.get(haoyouList.get(i))).equals("1")) {
                    popupmenu_haoyou.getMenu().removeItem(2);
                    popupmenu_haoyou.getMenu().add(1, 4, 2, "取消关注");
                } else
                    popupmenu_haoyou.getMenu().add(1, 2, 2, "关注");

                popupmenu_haoyou.getMenu().add(1, 3, 3, "呼叫");

                // popup menu click listener
                popupmenu_haoyou.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.shanchu:
                                dbHandler.deleteHaoyou(phoneNumbers.get(haoyouList.get(i)));
                                Toast.makeText(getActivity(), haoyouList.get(i) + " Deleted Successfully!", Toast.LENGTH_LONG).show();
                                haoyouList.remove(i);
                                adHaoyou.notifyDataSetChanged();
                                break;
                            case R.id.bianji:
                                Intent intentBianji = new Intent(getActivity(), BianjiActivity.class);
                                intentBianji.putExtra("infoid", dbHandler.getHaoyouId(phoneNumbers.get(haoyouList.get(i))));
                                startActivity(intentBianji);
//                                Toast.makeText(HaoyouliebiaoActivity.this, dbHandler.getAllHaoyouInfo(Integer.parseInt(phoneNumbers.get(haoyouList.get(i)))).toString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(HaoyouliebiaoActivity.this, dbHandler.getHaoyouId(Integer.parseInt(phoneNumbers.get(haoyouList.get(i)))).toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case 2: // 关注

                                // 获取手机号最后两位
                                String tmpPhoneNumber = new String(phoneNumbers.get(haoyouList.get(i)));
                                phoneNumberSum = myService.sumPhoneNumber(
                                        Integer.parseInt(tmpPhoneNumber.substring(tmpPhoneNumber.length()-2, tmpPhoneNumber.length()-1)),
                                        Integer.parseInt(tmpPhoneNumber.substring(tmpPhoneNumber.length()-1))
                                );

                                // di oncreate check focus shifou 1,
                                // kalo iya, panggil loopingnya, buat fungsi
                                // atau pake order by focus desc and name asc, jadi tiap guanzhu refresh smua namanya
                                // select * from BaseInfo order by focus desc, name asc

                                // reorder the haoyouList
//                                for(int cx = i; cx>0; cx--) {
//                                    String tmpListBefore = haoyouList.get(cx-1);
//                                    haoyouList.set(cx-1, haoyouList.get(cx));
//                                    haoyouList.set(cx, tmpListBefore);
//                                }
                                dbHandler.updateHaoyouFocus(dbHandler.getHaoyouId(phoneNumbers.get(haoyouList.get(i))), 1);
                                initializeLvHaouyouItem();
//                                lv_haoyou.getChildAt(0).setBackgroundColor(Color.parseColor("#5DE094"));
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "程序关闭"+phoneNumberSum+"秒后重新启动！", Toast.LENGTH_LONG).show();
                                    }
                                }, 2000);
                                Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_LONG).show();
                                adHaoyou.notifyDataSetChanged();
                                break;
                            case 3: // 呼叫
//                                Toast.makeText(HaoyouliebiaoActivity.this, phoneNumbers.get(haoyouList.get(i)), Toast.LENGTH_SHORT).show();

                                Intent intentPhoneCall = new Intent(Intent.ACTION_DIAL);
                                intentPhoneCall.setData(Uri.parse("tel:" + phoneNumbers.get(haoyouList.get(i))));
                                startActivity(intentPhoneCall);
                                break;
                            case 4: // 取消关注
                                dbHandler.updateHaoyouFocus(dbHandler.getHaoyouId(phoneNumbers.get(haoyouList.get(i))), 0);
                                initializeLvHaouyouItem();
                                Toast.makeText(getActivity(), "取消关注成功", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupmenu_haoyou.show();
                return true;
            }
        });

        // listview items click listener
        lv_haoyou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentXiangxi = new Intent(getActivity(), XiangxixinxiActivity.class);
                intentXiangxi.putExtra("infoid", dbHandler.getHaoyouId(phoneNumbers.get(haoyouList.get(i))));
                startActivity(intentXiangxi);
            }
        });
    }

    List<String> haoyouList = new ArrayList<>();
    ArrayAdapter<String> adHaoyou;
    Dictionary<String, String> phoneNumbers = new Hashtable<String, String>();
    ListView lv_haoyou;
    TextView tv_haoyou;
    Integer phoneNumberSum = 5;

    // shiyan 3
    // 1. onDestroy() { delay 2秒后给提示 程序已经关闭！ }
//        https://www.tutorialspoint.com/how-do-i-programmatically-restart-an-android-app
    // 2. IBinder, 关注的好友 手机号substring(length-2, length), sum, sum秒后自动启动
    // onRebind()
//        https://www.tutlane.com/tutorial/android/android-services-with-examples

    private MyService myService = null;
    private Boolean isBind = false;

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myService = ((MyService.LocalBinder)iBinder).getService();
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    };

    String nbFileName = "neibuFileTest.txt";
    String wbFileName = "waibuFileTest.txt";
    private DBHandler dbHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_haoyouliebiao);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    public void initializeLvHaouyouItem() {
        haoyouList.clear();
        haoyouList.addAll(dbHandler.getAllHaoyouName());
//        lv_haoyou.getChildAt(0).setBackgroundColor(Color.parseColor("#5DE094"));
        adHaoyou.notifyDataSetChanged();
    }

    /** initialize the actionbar mainmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu); // (menu.xml, Menu folder)



        return true;
    }
    */

    // actionbar menu click listener
    /**@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }*/

//    @Override
//    public void onDestroy() {
//        Intent intentService = new Intent(getActivity(), MyService.class);
//        getActivity().startService(intentService);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getActivity(), "程序已经关闭！", Toast.LENGTH_SHORT).show();
//                getActivity().stopService(intentService);
//            }
//        }, phoneNumberSum <= 1 ? 0 : 1000);
//
//
//        super.onDestroy();
//    }


    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    public void btn_restart(View view) {
        Intent intentService = new Intent(getActivity(), MyService.class);
        getActivity().startService(intentService);
        myService.restart();

        //https://stackoverflow.com/questions/6609414/how-do-i-programmatically-restart-an-android-app
//        https://www.geeksforgeeks.org/different-ways-to-programmatically-restart-an-android-app-on-button-click/
    }

}


/*
* shiyan 4
* 1. sharedpreferences
*
* 2. wenjian
* https://www.jianshu.com/p/a39bc4b3a1a6
* 内部存储：持久的数据，目录在 /data/data
* openFileOutput() = create and save to file
* openFileInput() = read from file
* 外部存储：比如放在sd卡的数据，当卸载时数据不会被删除
* https://www.geeksforgeeks.org/external-storage-in-android-with-example/
*
* 3. 数据库
* https://www.geeksforgeeks.org/how-to-create-and-add-data-to-sqlite-database-in-android/
* https://www.geeksforgeeks.org/how-to-view-and-locate-sqlite-database-in-android-studio/
*
* 4. https://www.tutorialspoint.com/android/android_content_providers.htm
* */