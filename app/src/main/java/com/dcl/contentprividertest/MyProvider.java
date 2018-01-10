package com.dcl.contentprividertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dcl.contentprividertest.DB.MyDataBaseHelper;

/**
 * Created by dcl on 2018/1/10.
 */

public class MyProvider extends ContentProvider {
    public static final int TABLE1_DIR = 0;
    public static final int TABLE1_ITEM = 1;
    public static final int TABLE2_DIR = 2;
    public static final int TABLE2_ITEM = 3;
    public static final String AUTHORITY = "com.dcl.contentprividertest.provider";
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", TABLE1_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", TABLE1_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", TABLE2_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", TABLE1_ITEM);
    }

    private MyDataBaseHelper myDataBaseHelper;

    @Override
    public boolean onCreate() {
        myDataBaseHelper = new MyDataBaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase readableDatabase = myDataBaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                cursor = readableDatabase.query("book", strings, s, strings1, null, null, s1);
                break;
            case TABLE1_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = readableDatabase.query("book", strings, "id = ?", new String[]{bookId}, null, null, s1);
                break;
            case TABLE2_DIR:
                cursor = readableDatabase.query("category", strings, s, strings1, null, null, s1);
                break;
            case TABLE2_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = readableDatabase.query("category", strings, "id = ?", new String[]{categoryId}, null, null, s1);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.dcl.contentprividertest.provider.book";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.dcl.contentprividertest.provider.book";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.dcl.contentprividertest.provider.category";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.dcl.contentprividertest.provider.category";
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase readableDatabase = myDataBaseHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
            case TABLE1_ITEM:
                long newBookId = readableDatabase.insert("book", null, contentValues);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case TABLE2_DIR:
            case TABLE2_ITEM:
                long newCategoryId = readableDatabase.insert("category", null, contentValues);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase readableDatabase = myDataBaseHelper.getReadableDatabase();
        int delRows = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                delRows = readableDatabase.delete("book",s,strings);
                break;
            case TABLE1_ITEM:
                String bookId = uri.getPathSegments().get(1);
                delRows = readableDatabase.delete("book","id = ?",new String[]{ bookId });
                break;
            case TABLE2_DIR:
                delRows = readableDatabase.delete("category",s,strings);
                break;
            case TABLE2_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                delRows = readableDatabase.delete("category","id = ?",new String[]{ categoryId });
                break;
            default:
                break;
        }
        return delRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase readableDatabase = myDataBaseHelper.getReadableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                updatedRows = readableDatabase.update("book",contentValues,s,strings);
                break;
            case TABLE1_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = readableDatabase.update("book",contentValues,"id = ?",new String[]{ bookId });
                break;
            case TABLE2_DIR:
                updatedRows = readableDatabase.update("category",contentValues,s,strings);
                break;
            case TABLE2_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = readableDatabase.update("category",contentValues,"id = ?",new String[]{ categoryId });
                break;
            default:
                break;
        }
        return updatedRows;
    }
}
