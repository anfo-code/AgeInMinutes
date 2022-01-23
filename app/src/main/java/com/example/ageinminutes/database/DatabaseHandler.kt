package com.example.ageinminutes.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

@SuppressLint("Range")
public class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BirthDayRecords.db"
        private const val TABLE_RECORDER = "recorder"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BIRTHDAY = "birth_day"
        private const val COLUMN_RECORDDATE = "recorder_day"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_RECORDS_TABLE = ("CREATE TABLE " + TABLE_RECORDER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_BIRTHDAY + " TEXT," + COLUMN_RECORDDATE + " TEXT" + ")")
        db?.execSQL(CREATE_RECORDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDER)
        onCreate(db)
    }

    fun addRecord(name: String, birthDate: String, recordDate: String): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_BIRTHDAY, birthDate)
        values.put(COLUMN_RECORDDATE, recordDate)

        val success = db.insert(TABLE_RECORDER, null, values)

        db.close()

        return success
    }


    fun viewRecord(): ArrayList<RecordModel> {

        val recList: ArrayList<RecordModel> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_RECORDER"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var birthDate: String
        var recordDate: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                birthDate = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY))
                recordDate = cursor.getString(cursor.getColumnIndex(COLUMN_RECORDDATE))

                val rec = RecordModel(
                    id = id,
                    name = name,
                    birthDate = birthDate,
                    recordDate = recordDate
                )
                recList.add(rec)
            } while (cursor.moveToNext())
        }

        return recList
    }

    fun updateRecord(rec: RecordModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, rec.name)
        contentValues.put(COLUMN_BIRTHDAY, rec.birthDate)
        contentValues.put(COLUMN_RECORDDATE, rec.recordDate)

        val success = db.update(TABLE_RECORDER, contentValues, COLUMN_ID + "=" + rec.id, null)

        db.close()

        return success
    }

    fun deleteRecord(rec: RecordModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, rec.id)

        val success = db.delete(TABLE_RECORDER, COLUMN_ID + "=" + rec.id, null)

        db.close()

        return success
    }

}