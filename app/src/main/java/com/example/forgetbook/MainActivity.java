package com.example.forgetbook;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView showtime;
    String NowTime,NowTime2;

    MyDatabaseHelper mDbHelper = new MyDatabaseHelper(this, "alarm.db", null, 2);
    List list;
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long nowTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTime = DateFormat.format("hh:mm:ss", nowTime);//时间显示格式
                    showtime.setText(sysTime); //更新时间
                    NowTime = sysTime.toString();
                    for(int i=0;i<list.size();i++){
                        iteminclude item = (iteminclude) list.get(i);
                        NowTime2 = item.getTime();
                        if(NowTime.equals(NowTime2)){
                            Log.i("Sos","成功");
                            Toast.makeText(MainActivity.this, "时间到了！", Toast.LENGTH_LONG).show();
                            zhendong();
                            soundplay();
                        }
                    }
                    /*Log.i("Nowtime",NowTime);*/
                    /*Log.i("Nowtime",NowTime);
                    Log.i("Nowtime2",NowTime2);*/
                    break;
                default:
                    break;
            }
        }
    };
    //设置震动
    public void zhendong(){
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 1000, 1000};
        vibrator.vibrate(patter,0);
    }
    //关闭震动,响铃
    public void close(){
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.cancel();
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
    }
    //设置响铃
    public void soundplay(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
        rt.play();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.alarmclock:
                Intent intent = new Intent(this,alarmclock.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showtime = findViewById(R.id.showtime);
        Button close = findViewById(R.id.close);
        list = new ArrayList();
        new TimeThread().start();
        ListView listView = findViewById(R.id.activityshow);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor =db.query("huodong",null,null,
                null,null,null,null);

        if (cursor.moveToFirst()) {
            do {

                String time = cursor.getString(cursor.getColumnIndex("time"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String things = cursor.getString(cursor.getColumnIndex("things"));
                iteminclude itm = new iteminclude();
                itm.setTitle(title);
                itm.setTime(time);
                itm.setThings(things);
                list.add(itm);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        final ClockAdapter clockAdapter = new ClockAdapter(this, R.layout.item_layout, list);
        listView.setAdapter(clockAdapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }
}
