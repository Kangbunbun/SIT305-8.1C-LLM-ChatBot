package com.example.chatbot.data

import com.example.chatbot.data.local.ChatDatabase
import com.example.chatbot.data.local.ChatMessage
import com.example.chatbot.data.remote.ChatApiService
import com.example.chatbot.data.remote.ChatRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(
    private val database: ChatDatabase,
    private val apiService: ChatApiService
) {
    suspend fun getMessages(username: String): List<ChatMessage> {
        return withContext(Dispatchers.IO) {
            database.getMessagesByUsername(username)
        }
    }

    suspend fun saveMessage(message: ChatMessage) {
        withContext(Dispatchers.IO) {
            database.insertMessage(message)
        }
    }

    suspend fun sendMessageToBot(username: String, messageText: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ChatRequest(username, messageText)
                val response = apiService.sendMessage(request)
                Result.success(response.reply)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}