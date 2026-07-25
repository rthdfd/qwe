package com.example.cozelike.data

import android.content.Context
import com.example.cozelike.data.model.Bot
import com.example.cozelike.data.model.Message
import com.google.gson.GsonBuilder
import java.io.File

object AppRepository {

    private lateinit var file: File
    private val gson = GsonBuilder().setPrettyPrinting().create()

    lateinit var data: AppData
        private set

    fun initialize(context: Context) {
        file = File(context.filesDir, "app_data.json")
        data = if (file.exists()) {
            try {
                gson.fromJson(file.readText(), AppData::class.java) ?: AppData()
            } catch (e: Exception) {
                e.printStackTrace()
                AppData()
            }
        } else {
            AppData()
        }
        if (data.bots == null) data.bots = mutableListOf()
        if (data.messages == null) data.messages = mutableListOf()
        if (data.bots.isEmpty()) seed()
        save()
    }

    @Synchronized
    private fun save() {
        try {
            file.writeText(gson.toJson(data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBot(id: String): Bot? = data.bots.find { it.id == id }

    fun saveBot(bot: Bot) {
        val index = data.bots.indexOfFirst { it.id == bot.id }
        if (index >= 0) data.bots[index] = bot else data.bots.add(bot)
        save()
    }

    fun deleteBot(id: String) {
        data.bots.removeAll { it.id == id }
        data.messages.removeAll { it.botId == id }
        save()
    }

    fun getMessages(botId: String): List<Message> =
        data.messages.filter { it.botId == botId }.sortedBy { it.createdAt }

    fun addMessage(message: Message) {
        data.messages.add(message)
        save()
    }

    fun clearMessages(botId: String) {
        data.messages.removeAll { it.botId == botId }
        save()
    }

    private fun seed() {
        data.bots.add(
            Bot(
                name = "全能助手",
                description = "通用 AI 助手，回答你的各类问题",
                prompt = "你是一个乐于助人、知识渊博的 AI 助手，请用简洁清晰的中文回答问题。",
                avatar = "🤖"
            )
        )
        data.bots.add(
            Bot(
                name = "翻译官",
                description = "中英文互译，准确流畅",
                prompt = "你是一个专业翻译，将用户输入的语言准确翻译为目标语言，保持原意与语气。",
                avatar = "🌐"
            )
        )
    }
}

data class AppData(
    var bots: MutableList<Bot> = mutableListOf(),
    var messages: MutableList<Message> = mutableListOf()
)
