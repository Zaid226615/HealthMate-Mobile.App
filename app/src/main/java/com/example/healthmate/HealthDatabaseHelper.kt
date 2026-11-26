package com.example.healthmate

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HealthDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "HealthMate.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "health_records"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                steps INTEGER NOT NULL,
                calories INTEGER NOT NULL,
                water INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Database upgrade logic
    }

    // CREATE - Insert a health record
    fun insertRecord(record: HealthRecord): Long {
        val values = ContentValues().apply {
            put("date", record.date)
            put("steps", record.steps)
            put("calories", record.calories)
            put("water", record.water)
        }

        return writableDatabase.insert(TABLE_NAME, null, values)
    }

    // READ - Get all records
    fun getAllRecords(): List<HealthRecord> {
        return readRecords()
    }

    // SEARCH - Search records by date
    fun getRecordsByDate(date: String): List<HealthRecord> {
        return readRecords("date = ?", arrayOf(date))
    }

    private fun readRecords(
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): List<HealthRecord> {

        val records = mutableListOf<HealthRecord>()

        val cursor = readableDatabase.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "date DESC, id DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                records.add(
                    HealthRecord(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        date = it.getString(it.getColumnIndexOrThrow("date")),
                        steps = it.getInt(it.getColumnIndexOrThrow("steps")),
                        calories = it.getInt(it.getColumnIndexOrThrow("calories")),
                        water = it.getInt(it.getColumnIndexOrThrow("water"))
                    )
                )
            }
        }

        return records
    }

    // UPDATE - Update an existing record
    fun updateRecord(record: HealthRecord): Int {
        val values = ContentValues().apply {
            put("date", record.date)
            put("steps", record.steps)
            put("calories", record.calories)
            put("water", record.water)
        }

        return writableDatabase.update(
            TABLE_NAME,
            values,
            "id = ?",
            arrayOf(record.id.toString())
        )
    }

    // DELETE - Delete a record
    fun deleteRecord(id: Int): Int {
        return writableDatabase.delete(
            TABLE_NAME,
            "id = ?",
            arrayOf(id.toString())
        )
    }
}