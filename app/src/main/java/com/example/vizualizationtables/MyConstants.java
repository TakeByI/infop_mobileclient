package com.example.vizualizationtables;

public class MyConstants {
    public static final String TABLE_NAME = "clients";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String DB_NAME = "clients.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            NAME + " TEXT," + CODE + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static final String IP = "";

}
