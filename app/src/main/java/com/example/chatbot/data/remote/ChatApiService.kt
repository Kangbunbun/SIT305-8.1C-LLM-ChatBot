package com.example.chatbot.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApiService {
    @POST("chat")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}
