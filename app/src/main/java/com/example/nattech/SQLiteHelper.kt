package com.example.nattech

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper private constructor(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        private const val DB_NAME = "machinedb"
        private const val DB_VERSION = 1
        private var sqliteHelper: SQLiteHelper? = null

        @Synchronized
        fun getInstance(c: Context?): SQLiteHelper{
            return if (sqliteHelper == null){
                SQLiteHelper(c!!.applicationContext)
            } else {
                sqliteHelper!!
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = """CREATE TABLE machine (
        _id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        clientID TEXT,
        token TEXT,
        timestamp REAL)"""
        db?.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}