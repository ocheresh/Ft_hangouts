package com.ocheresh.ft_hangouts.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DBAbonents extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Abonents.db";
    public static final String TABLE_PRODUCTS = "abonents";

    public static final String ID_KEY = "_id";
    public static final String ID_NAME = "name";
    public static final String ID_SURNAME = "surname";
    public static final String ID_NUMBER = "number";
    public static final String ID_EMAIL = "email";
    public static final String ID_ADRES = "adres";
    public static final String ID_PHOTO_PATH = "photo_path";


    public DBAbonents(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PRODUCTS + "(" + ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_NAME + " TEXT,"
                + ID_SURNAME + " text,"  + ID_NUMBER + " text,"  + ID_EMAIL + " text," + ID_ADRES + " text," + ID_PHOTO_PATH + " text" + ")");
    }

    public boolean insertContact (Abonent abonent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_NAME, abonent.getName().replaceAll("\\t", ""));
        contentValues.put(ID_SURNAME, abonent.getSurname());
        contentValues.put(ID_NUMBER, abonent.getTelephonenumber());
        contentValues.put(ID_EMAIL, abonent.getMail());
        contentValues.put(ID_ADRES, abonent.getAdres());
        contentValues.put(ID_PHOTO_PATH, abonent.getPhoto_path());

        long result = db.insert(TABLE_PRODUCTS, null, contentValues);

        db.close();

        if (result == -1)
            return false;
        return true;
    }

    public List<Abonent> readData()
    {
        List<Abonent> result = new ArrayList<Abonent>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null,null, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do {
                result.add(new Abonent.Builder()
                        .setName(cursor.getString(cursor.getColumnIndex(ID_NAME)))
                        .setSurname(cursor.getString(cursor.getColumnIndex(ID_SURNAME)))
                        .setTelephNumber(cursor.getString(cursor.getColumnIndex(ID_NUMBER)))
                        .setMail(cursor.getString(cursor.getColumnIndex(ID_EMAIL)))
                        .setAdres(cursor.getString(cursor.getColumnIndex(ID_ADRES)))
                        .setPhotoPath(cursor.getString(cursor.getColumnIndex(ID_PHOTO_PATH)))
                        .build());

            }
            while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    public Integer deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTS, ID_NAME + " = ?", new String[] {name});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
