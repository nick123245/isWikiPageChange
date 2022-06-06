package com.example.iswikipagechange;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DB {

    //выбрать все записи из БД в виде списка
    public static ArrayList<WikiPage> getAllRecords(Context context){
        MySQLiteOpenHelper dataBaseHelper;
        SQLiteDatabase bd;
        dataBaseHelper = new MySQLiteOpenHelper(context);
        bd = dataBaseHelper.getReadableDatabase();
        Cursor cursor;
        String sql = "SELECT * FROM wiki_pages";
        cursor = bd.rawQuery(sql,null);
        ArrayList<WikiPage> wikiPages = new ArrayList<>();
        cursor.moveToFirst();

        int i = 0;
        while (!cursor.isAfterLast()){

            WikiPage wikiPage = new WikiPage(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            wikiPages.add(wikiPage);
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return wikiPages;
    }

    //обновление данных в БД, добавление новых дат изменения
    public static void updateRecords(ArrayList<WikiPage> wikiPages, Context context){
        MySQLiteOpenHelper dataBaseHelper;
        SQLiteDatabase bd;
        dataBaseHelper = new MySQLiteOpenHelper(context);
        bd = dataBaseHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        for (int i = 0; i < wikiPages.size(); i++) {
            cv.put("date",wikiPages.get(i).getNewDate());
            cv.put("new_date",wikiPages.get(i).getNewDate());
            cv.put("is_change",wikiPages.get(i).getIsChange());
            bd.update("wiki_pages", cv, "url = ?", new String[]{wikiPages.get(i).getUrl()});
        }
    }

    // добавление новой странички в базу данных
    public static void addWikiPage(WikiPage wikiPage, Context context){
        MySQLiteOpenHelper dataBaseHelper;
        SQLiteDatabase bd;
        dataBaseHelper = new MySQLiteOpenHelper(context);
        bd = dataBaseHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("url", wikiPage.getUrl());
        bd.insert("wiki_pages",null,cv);
    }


}
