package com.example.shiyan1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {

    int phoneNumberSum = 5;

    private LocalBinder myBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public MyService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show();
        return myBinder;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
//        return super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        super.onRebind(intent);
        // true 时会调用onRebind
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    public Integer sumPhoneNumber(int first, int second) {
        int sum = first + second;
        setPhoneNumberSum(sum);
        return sum;
    }

    public void setPhoneNumberSum(int tmpPhoneNumberSum) {
        phoneNumberSum = tmpPhoneNumberSum;
    }

    public void restart() {
        Intent dialogIntent = new Intent(this, HaoyouliebiaoActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(this, "程序关闭"+phoneNumberSum+"秒后重新启动！", Toast.LENGTH_LONG).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(dialogIntent);
            }
        }, phoneNumberSum * 1000);
    }
}
