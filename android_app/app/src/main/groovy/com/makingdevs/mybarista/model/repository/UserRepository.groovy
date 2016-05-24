package com.makingdevs.mybarista.model.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.makingdevs.mybarista.database.BaristaDbSchema
import com.makingdevs.mybarista.database.BaristaOpenHelper
import com.makingdevs.mybarista.model.User

class UserRepository {

    private Context mContext
    private BaristaOpenHelper mopenhelper
    private SQLiteDatabase db

    UserRepository(Context context) {
        mContext = context.getApplicationContext()
        mopenhelper = new BaristaOpenHelper(mContext)
        db = mopenhelper.getWritableDatabase()
    }

    void addUser(User user){
        ContentValues item = new ContentValues()
        item.put(BaristaDbSchema.UserTable.Column.USERNAME,user.username)
        item.put(BaristaDbSchema.UserTable.Column.TOKEN,user.token)
        def algo = db.insert(BaristaDbSchema.UserTable.NAME,null,item)
        Log.d("Repository", algo.toString())
    }

    void findById(Long id){
        Cursor c = db.rawQuery("SELECT * FROM users WHERE _id = '$id'", null);
        c.moveToNext();
        Log.d("Repository",c.getString(c.getColumnIndex("username")))
    }

}