package com.example.forgetbook;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class alarmclock extends AppCompatActivity {
    EditText text1, text2, text3,text11,text22,text33;
    List list;
    ListView listView ;
    MyDatabaseHelper mDbHelper = new MyDatabaseHelper(this, "alarm.db", null, 2);

    //内容增加
    public void insert(String strTitle, String strTime, String strThings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", strTitle);
        values.put("time", strTime);
        values.put("things", strThings);
        db.insert("huodong", null, values);
        values.clear();
        Toast.makeText(alarmclock.this, "保存成功！", Toast.LENGTH_SHORT).show();
    }

    //内容删除
    public void delete(String strTitle) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete("huodong", "title = ?", new String[]{strTitle});
        Toast.makeText(alarmclock.this, "清除成功！", Toast.LENGTH_SHORT).show();
    }
    //更新操作
    public void update(String strTitle, String strTime, String strThings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        delete(strTitle);
        ContentValues cv = new ContentValues();
        cv.put("title", strTitle);
        cv.put("time", strTime);
        cv.put("things", strThings);
        db.insert("huodong", null, cv);
    }
    //刷新界面
    public void refresh() {
        finish();
        Intent intent = new Intent(this, alarmclock.class);
        startActivity(intent);
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmclock);
        Button addButton = findViewById(R.id.add);
        listView= findViewById(R.id.savethings);
        //从数据库查询内容并保存到listview中
        list = new ArrayList();
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
        //listView中的item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(alarmclock.this);
                final LayoutInflater inflater = getLayoutInflater();
                final View v = inflater.inflate(R.layout.login_dialog, null);
                iteminclude item = (iteminclude) list.get(position);
                Log.i("titlee",item.getTitle());
                Log.i("Timee",item.getTime());
                text11 = v.findViewById(R.id.UserTitle);
                text22= v.findViewById(R.id.UserTime);
                text33 = v.findViewById(R.id.UserThings);
                text11.setText(item.getTitle());
                text22.setText(item.getTime());
                text33.setText(item.getThings());
                builder.setView(v).setTitle("事项");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String edittitle, edittime, editthings;
                        edittitle = text11.getText().toString();
                        edittime = text22.getText().toString();
                        editthings = text33.getText().toString();
                        update(edittitle, edittime, editthings);
                        refresh();
                    }
                });
                builder.setNegativeButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String edittitle;
                        edittitle = text11.getText().toString();
                        delete(edittitle);
                        refresh();
                    }
                });
                builder.show();
            }
        });
        //添加按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(alarmclock.this);
                final LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.login_dialog, null);
                text1 = view.findViewById(R.id.UserTitle);
                text2 = view.findViewById(R.id.UserTime);
                text3 = view.findViewById(R.id.UserThings);
                builder.setView(view).setTitle("事项");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String edittitle, edittime, editthings;
                        edittitle = text1.getText().toString();
                        Log.i("edittitle",edittitle);
                        edittime = text2.getText().toString();
                        Log.i("timeee",edittime);
                        editthings = text3.getText().toString();
                        insert(edittitle, edittime, editthings);
                        refresh();
                    }
                });
                builder.setNegativeButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String edittitle;
                        edittitle = text1.getText().toString();
                        delete(edittitle);
                        refresh();
                    }
                });
                builder.show();
            }
        });


    }
}
