package com.example.forgetbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK = "create table huodong("
            +"id  integer primary key autoincrement,"
            +"time text ,"
            +"title text  ,"
            +"things text)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }
}
