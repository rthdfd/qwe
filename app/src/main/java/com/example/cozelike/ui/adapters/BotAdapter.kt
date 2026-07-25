package com.example.cozelike.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cozelike.data.model.Bot
import com.example.cozelike.databinding.ItemBotBinding

class BotAdapter(
    private val onChat: (Bot) -> Unit,
    private val onEdit: (Bot) -> Unit,
    private val onDelete: (Bot) -> Unit
) : ListAdapter<Bot, BotAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Bot>() {
            override fun areItemsTheSame(oldItem: Bot, newItem: Bot): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Bot, newItem: Bot): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemBotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: ItemBotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onChat(getItem(bindingAdapterPosition)) }
            binding.btnEdit.setOnClickListener { onEdit(getItem(bindingAdapterPosition)) }
            binding.btnDelete.setOnClickListener { onDelete(getItem(bindingAdapterPosition)) }
        }

        fun bind(bot: Bot) {
            binding.avatar.text = bot.avatar
            binding.name.text = bot.name
            binding.desc.text = bot.description.ifBlank { "暂无描述" }
        }
    }
}
