package com.example.cozelike.ui.adapters

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cozelike.data.model.Message
import com.example.cozelike.databinding.ItemMessageBinding

class MessageAdapter : ListAdapter<Message, MessageAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            val isUser = message.role == "user"

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = if (isUser) Gravity.END else Gravity.START
            params.setMargins(if (isUser) 48 else 0, 4, if (isUser) 0 else 48, 4)
            binding.bubble.layoutParams = params

            if (isUser) {
                binding.bubble.setBackgroundResource(R.drawable.bubble_user)
                binding.bubble.setTextColor(Color.WHITE)
            } else {
                binding.bubble.setBackgroundResource(R.drawable.bubble_bot)
                binding.bubble.setTextColor(Color.BLACK)
            }

            binding.bubble.text = message.content
        }
    }
}
