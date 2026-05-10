package com.example.chatbot.data.local

data class ChatMessage(
    var id: Int = 0,
    var username: String = "",
    var message: String = "",
    var isFromUser: Boolean = false,
    var timestamp: Long = 0
)