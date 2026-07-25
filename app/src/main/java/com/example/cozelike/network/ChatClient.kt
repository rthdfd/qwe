package com.example.cozelike.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object ChatClient {

    private val gson = Gson()

    fun chat(
        baseUrl: String,
        apiKey: String,
        model: String,
        messages: List<ChatMessage>,
        temperature: Float,
        maxTokens: Int,
        timeoutMs: Int = 120000
    ): String {
        val endpoint = baseUrl.trimEnd('/') + "/chat/completions"
        val connection = URL(endpoint).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.connectTimeout = timeoutMs
        connection.readTimeout = timeoutMs
        connection.setRequestProperty("Content-Type", "application/json")
        if (apiKey.isNotBlank()) {
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
        }

        val payload = mapOf(
            "model" to model,
            "messages" to messages.map { mapOf("role" to it.role, "content" to it.content) },
            "temperature" to temperature,
            "max_tokens" to maxTokens
        )

        val json = gson.toJson(payload)
        connection.outputStream.use { os ->
            os.write(json.toByteArray(StandardCharsets.UTF_8))
        }

        val code = connection.responseCode
        val body: String = if (code in 200..299) {
            connection.inputStream.bufferedReader(StandardCharsets.UTF_8).readText()
        } else {
            connection.errorStream?.bufferedReader(StandardCharsets.UTF_8)?.readText() ?: ""
        }

        if (code !in 200..299) {
            throw IOException("HTTP $code: $body")
        }

        val root: JsonObject = gson.fromJson(body, JsonObject::class.java)
        val choices = root.getAsJsonArray("choices")
        if (choices == null || choices.size() == 0) {
            throw IOException("响应中没有 choices：$body")
        }
        val messageObj = choices[0].asJsonObject.getAsJsonObject("message")
        val contentElement = messageObj.get("content")
        return if (contentElement == null || contentElement.isJsonNull) {
            ""
        } else {
            contentElement.asString
        }
    }
}
