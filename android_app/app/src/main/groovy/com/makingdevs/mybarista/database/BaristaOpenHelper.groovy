package com.makingdevs.mybarista.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build

import static com.makingdevs.mybarista.database.BaristaDbSchema.*

class BaristaOpenHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "barista.db"
    static final int VERSION = 1

    BaristaOpenHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION)
    }

    @Override
    void onCreate(SQLiteDatabase db) {
        db.execSQL("""\
      create table ${UserTable.NAME}( _id integer primary key autoincrement,
      ${UserTable.Column.USERNAME},
      ${UserTable.Column.TOKEN} )
    """)
    }

    @Override
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists ${UserTable.NAME} ")
        onCreate(db)
    }

}