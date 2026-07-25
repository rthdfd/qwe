package com.example.cozelike.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cozelike.data.AppRepository
import com.example.cozelike.data.model.Bot
import com.example.cozelike.data.model.Message
import com.example.cozelike.databinding.ActivityChatBinding
import com.example.cozelike.network.ChatClient
import com.example.cozelike.network.ChatMessage
import com.example.cozelike.ui.adapters.MessageAdapter
import com.example.cozelike.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var bot: Bot
    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private var busy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val botId = intent.getStringExtra("bot_id")
        val found = botId?.let { AppRepository.getBot(it) }
        if (found == null) {
            Toast.makeText(this, "智能体不存在", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        bot = found

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topBar.title = bot.name
        binding.topBar.setNavigationOnClickListener { finish() }
        binding.topBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_clear -> {
                    AppRepository.clearMessages(bot.id)
                    messages.clear()
                    adapter.submitList(emptyList())
                    Toast.makeText(this, "对话已清空", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        adapter = MessageAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        loadMessages()

        binding.sendBtn.setOnClickListener { sendMessage() }
    }

    private fun loadMessages() {
        messages.clear()
        messages.addAll(AppRepository.getMessages(bot.id))
        adapter.submitList(messages.toList())
        scrollToBottom()
    }

    private fun scrollToBottom() {
        if (adapter.itemCount > 0) {
            binding.recycler.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun sendMessage() {
        val text = binding.inputEdit.text.toString().trim()
        if (text.isEmpty() || busy) return
        binding.inputEdit.text?.clear()

        val userMessage = Message(botId = bot.id, role = "user", content = text)
        messages.add(userMessage)
        adapter.submitList(messages.toList())
        scrollToBottom()
        AppRepository.addMessage(userMessage)

        busy = true
        setBusy(true)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val system = ChatMessage(
                    "system",
                    bot.prompt.ifBlank { "你是一个乐于助人的AI助手。" }
                )
                val history = messages.map { ChatMessage(it.role, it.content) }
                val all = listOf(system) + history

                val reply = ChatClient.chat(
                    baseUrl = Prefs.apiBaseUrl(this@ChatActivity),
                    apiKey = Prefs.apiKey(this@ChatActivity),
                    model = bot.model.ifBlank { Prefs.defaultModel(this@ChatActivity) },
                    messages = all,
                    temperature = bot.temperature,
                    maxTokens = bot.maxTokens
                )

                val botMessage = Message(botId = bot.id, role = "assistant", content = reply)
                AppRepository.addMessage(botMessage)

                withContext(Dispatchers.Main) {
                    messages.add(botMessage)
                    adapter.submitList(messages.toList())
                    scrollToBottom()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChatActivity,
                        "请求失败：${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    busy = false
                    setBusy(false)
                }
            }
        }
    }

    private fun setBusy(value: Boolean) {
        binding.sendBtn.isEnabled = !value
        binding.progress.visibility = if (value) View.VISIBLE else View.GONE
    }
}
