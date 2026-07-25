package com.example.cozelike.data.model

import java.util.UUID

data class Bot(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var prompt: String = "",
    var avatar: String = "🤖",
    var model: String = "gpt-3.5-turbo",
    var temperature: Float = 0.7f,
    var maxTokens: Int = 2048,
    var createdAt: Long = System.currentTimeMillis()
)
