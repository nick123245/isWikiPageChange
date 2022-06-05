package com.example.iswikipagechange;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_WIKI_PAGES = "wiki_pages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NEW_DATE = "new_date";
    public static final String COLUMN_IS_CHANGE = "is_change";

    private static final String DATABASE_NAME = "wiki.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_WIKI_PAGES + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_URL + " text not null, "
            + COLUMN_DATE + " text default (''),"
            + COLUMN_NEW_DATE + " text default (''), "
            + COLUMN_IS_CHANGE + " text default ('запись без изменений') " + ");";

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.d("wiki7777", "bd create");
        //добавим несколько адресов в базу
        ContentValues newValues = new ContentValues();
        newValues.put("url", "https://ru.wikipedia.org/w/index.php?title=Java");
        database.insert("wiki_pages", null, newValues);
        newValues.put("url", "https://ru.wikipedia.org/w/index.php?title=Python");
        database.insert("wiki_pages", null, newValues);
        newValues.put("url", "https://ru.wikipedia.org/w/index.php?title=C_Sharp");
        database.insert("wiki_pages", null, newValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIKI_PAGES);
        onCreate(db);
    }

}
