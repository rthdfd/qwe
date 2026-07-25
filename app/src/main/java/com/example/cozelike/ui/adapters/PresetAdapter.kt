package com.example.cozelike.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cozelike.data.PresetTemplate
import com.example.cozelike.databinding.ItemPresetBinding

class PresetAdapter(private val onImport: (PresetTemplate) -> Unit) :
    ListAdapter<PresetTemplate, PresetAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PresetTemplate>() {
            override fun areItemsTheSame(oldItem: PresetTemplate, newItem: PresetTemplate): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: PresetTemplate, newItem: PresetTemplate): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPresetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val binding: ItemPresetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnAdd.setOnClickListener { onImport(getItem(bindingAdapterPosition)) }
        }

        fun bind(preset: PresetTemplate) {
            binding.avatar.text = preset.avatar
            binding.name.text = preset.name
            binding.desc.text = preset.description
        }
    }
}
