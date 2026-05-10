package com.example.chatbot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbot.adapter.ChatAdapter
import com.example.chatbot.data.ChatRepository
import com.example.chatbot.data.local.ChatDatabase
import com.example.chatbot.data.local.ChatMessage
import com.example.chatbot.data.remote.RetrofitClient
import com.example.chatbot.databinding.ActivityChatBinding
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var repository: ChatRepository
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("USERNAME") ?: "User"

        val database = ChatDatabase.getDatabase(this)
        repository = ChatRepository(database, RetrofitClient.instance)

        setupRecyclerView()
        loadChatHistory()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }
    }

    private fun loadChatHistory() {
        lifecycleScope.launch {
            val history = repository.getMessages(username)
            chatAdapter.setMessages(history)

            if (history.isNotEmpty()) {
                binding.rvChat.scrollToPosition(history.size - 1)
            }
        }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()

        if (messageText.isEmpty()) {
            return
        }

        val userMessage = ChatMessage(
            username = username,
            message = messageText,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )

        binding.etMessage.setText("")

        lifecycleScope.launch {
            repository.saveMessage(userMessage)
            chatAdapter.addMessage(userMessage)
            binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)

            val result = repository.sendMessageToBot(username, messageText)

            val botMessageText = result.getOrElse {
                "Sorry, I could not get a response. Please try again."
            }

            val botMessage = ChatMessage(
                username = username,
                message = botMessageText,
                isFromUser = false,
                timestamp = System.currentTimeMillis()
            )

            repository.saveMessage(botMessage)
            chatAdapter.addMessage(botMessage)
            binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }
}