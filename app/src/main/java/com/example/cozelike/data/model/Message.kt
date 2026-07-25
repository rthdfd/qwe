package com.example.cozelike.data.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val botId: String = "",
    val role: String = "user",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
