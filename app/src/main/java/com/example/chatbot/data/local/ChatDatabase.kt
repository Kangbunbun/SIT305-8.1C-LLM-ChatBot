package com.example.chatbot.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChatDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chat_database.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CHAT_MESSAGES = "chat_messages"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_MESSAGE = "message"
        private const val COLUMN_IS_FROM_USER = "is_from_user"
        private const val COLUMN_TIMESTAMP = "timestamp"

        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getDatabase(context: Context): ChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = ChatDatabase(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CHAT_MESSAGES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_MESSAGE TEXT NOT NULL,
                $COLUMN_IS_FROM_USER INTEGER NOT NULL,
                $COLUMN_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHAT_MESSAGES")
        onCreate(db)
    }

    fun insertMessage(chatMessage: ChatMessage) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_USERNAME, chatMessage.username)
            put(COLUMN_MESSAGE, chatMessage.message)
            put(COLUMN_IS_FROM_USER, if (chatMessage.isFromUser) 1 else 0)
            put(COLUMN_TIMESTAMP, chatMessage.timestamp)
        }

        db.insert(TABLE_CHAT_MESSAGES, null, values)
    }

    fun getMessagesByUsername(username: String): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_CHAT_MESSAGES,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            "$COLUMN_TIMESTAMP ASC"
        )

        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val usernameIndex = it.getColumnIndexOrThrow(COLUMN_USERNAME)
            val messageIndex = it.getColumnIndexOrThrow(COLUMN_MESSAGE)
            val isFromUserIndex = it.getColumnIndexOrThrow(COLUMN_IS_FROM_USER)
            val timestampIndex = it.getColumnIndexOrThrow(COLUMN_TIMESTAMP)

            while (it.moveToNext()) {
                val chatMessage = ChatMessage(
                    id = it.getInt(idIndex),
                    username = it.getString(usernameIndex),
                    message = it.getString(messageIndex),
                    isFromUser = it.getInt(isFromUserIndex) == 1,
                    timestamp = it.getLong(timestampIndex)
                )

                messages.add(chatMessage)
            }
        }

        return messages
    }
}