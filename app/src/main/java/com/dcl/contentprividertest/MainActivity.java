package com.dcl.contentprividertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dcl.contentprividertest.DB.MyDataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDataBaseHelper(this, "BookStore.db", null, 2);
    }
    public void creatTable(View view) {
        dbHelper.getWritableDatabase();
    }

    public void AddData(View view) {
        SQLiteDatabase sqDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name","To Be A Bad Man");
        values.put("author","LiuDao");
        values.put("pages",454);
        values.put("price",26.96);
        sqDb.insert("book",null,values);
        values.clear();
        values.put("name","Normal World");
        values.put("author","LuYao");
        values.put("pages",1000);
        values.put("price",200);
        sqDb.insert("book",null,values);
    }

    public void deleteData(View view) {
        SQLiteDatabase sqDb = dbHelper.getWritableDatabase();
        sqDb.delete("book","id <= ?",new String[]{"8"});
    }

    public void updateData(View view) {
        SQLiteDatabase sqDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price",150);
        sqDb.update("book",values,"name = ?",new String[]{"Normal World"});
    }

    public void queryData(View view) {
        SQLiteDatabase sqDb = dbHelper.getWritableDatabase();
        Cursor cursor = sqDb.query("book",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String author = cursor.getString(cursor.getColumnIndex("author"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            Toast.makeText(this,"书名："+name+"\n作者："+author+"\n价格："+price,Toast.LENGTH_SHORT).show();
            Log.i("Detail","书名："+name+"\n作者："+author+"\n价格："+price);
        }
        cursor.close();
    }
}
