package com.example.vizualizationtables;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

//КЛАСС ВЫПОЛНЯЕТ ОСНОВНЫЕ ФУНКЦИИ УПРАВЛЕНИЯ БАЗОЙ ДАННЫХ SQLITE В ПРИЛОЖЕНИИ
public class MyDbManager{
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    //ОТКРЫВАЕТ БАЗУ ДАННЫХ ДЛЯ ЗАПИСИ И ЧТЕНИЯ
    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }

    //ИСПОЛЬЗУЕТСЯ ДЛЯ ВСТАВКИ ДАННЫХ В БАЗУ ДАННЫХ
    public void insertToDb(String name, String code) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.NAME, name);
        cv.put(MyConstants.CODE, code);
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    //ВОЗВРАЩАЕТ ИНФОРМАЦИЮ ИЗ БАЗЫ ДАННЫХ В ВИДЕ ДИНАМИЧЕСКОГО СПИСКА ОБЪЕКТОВ
    public ArrayList<ClientsCodeNameModel> getFromDb() {
        ArrayList<ClientsCodeNameModel> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null,
                null, null, null);
        while(cursor.moveToNext()) {
            @SuppressLint("Range") ClientsCodeNameModel text = new ClientsCodeNameModel(
                    cursor.getString(cursor.getColumnIndex(MyConstants.NAME)),
                            cursor.getString(cursor.getColumnIndex(MyConstants.CODE)
            )  ) ;
            tempList.add(text);
        }
        cursor.close();
        return tempList;
    }

    //ЗАКРЫВАЕТ БАЗУ ДАННЫХ
    public void closeDb() {
        myDbHelper.close();
    }

    //УДАЛЯЕТ ВСЮ ТАБЛИЦУ ИЗ БАЗЫ ДАННЫХ
    public void deleteTable() {
        db.execSQL(MyConstants.DROP_TABLE);
        //onCreate(db); // После удаления таблицы, вы можете снова создать ее здесь
    }

    //УДАЛЯЕТ ВСЕ ЗАПИСИ ИЗ ТАБЛИЦЫ, НО НЕ УДАЛЯЕТ ТАБЛИЦУ
    public void clearTable() {
        db.delete(MyConstants.TABLE_NAME, null, null);
    }
}
