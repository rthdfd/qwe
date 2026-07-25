package com.example.cozelike.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.cozelike.data.AppRepository
import com.example.cozelike.data.model.Bot
import com.example.cozelike.databinding.ActivityBotEditorBinding

class BotEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBotEditorBinding
    private var editingBot: Bot? = null

    private val avatars = listOf(
        "🤖", "💡", "✍️", "💻", "🌐", "📚", "🎨", "🩺", "⚖️", "🍳", "🧳", "🎯"
    )
    private val models = listOf(
        "gpt-3.5-turbo", "gpt-4", "gpt-4o",
        "claude-3-opus", "gemini-pro", "qwen-max", "deepseek-chat"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBotEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topBar.setNavigationOnClickListener { finish() }

        val botId = intent.getStringExtra("bot_id")
        if (botId != null) {
            editingBot = AppRepository.getBot(botId)
            prefill()
        }

        binding.avatarSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, avatars
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.modelSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, models
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.tempSlider.valueFrom = 0f
        binding.tempSlider.valueTo = 2f
        binding.tempSlider.stepSize = 0.1f
        binding.tempSlider.value = 0.7f
        binding.tempSlider.addOnChangeListener { _, value, _ ->
            binding.tempValue.text = String.format("%.2f", value)
        }

        binding.saveBtn.setOnClickListener { save() }
    }

    private fun prefill() {
        val bot = editingBot ?: return
        binding.nameEdit.setText(bot.name)
        binding.descEdit.setText(bot.description)
        binding.promptEdit.setText(bot.prompt)
        binding.avatarSpinner.setSelection(avatars.indexOf(bot.avatar).coerceAtLeast(0))
        binding.modelSpinner.setSelection(models.indexOf(bot.model).coerceAtLeast(0))
        binding.tempSlider.value = bot.temperature
        binding.tempValue.text = String.format("%.2f", bot.temperature)
        binding.maxTokensEdit.setText(bot.maxTokens.toString())
    }

    private fun save() {
        val name = binding.nameEdit.text.toString().trim()
        if (name.isEmpty()) {
            binding.nameLayout.error = "请输入智能体名称"
            return
        }

        val bot = editingBot ?: Bot()
        bot.name = name
        bot.description = binding.descEdit.text.toString().trim()
        bot.prompt = binding.promptEdit.text.toString()
        bot.avatar = avatars[binding.avatarSpinner.selectedItemPosition]
        bot.model = models[binding.modelSpinner.selectedItemPosition]
        bot.temperature = binding.tempSlider.value
        bot.maxTokens = binding.maxTokensEdit.text.toString().toIntOrNull() ?: 2048

        AppRepository.saveBot(bot)
        finish()
    }
}
