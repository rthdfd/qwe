package com.example.cozelike.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cozelike.data.AppRepository
import com.example.cozelike.data.Presets
import com.example.cozelike.data.model.Bot
import com.example.cozelike.databinding.FragmentDiscoverBinding
import com.example.cozelike.ui.adapters.PresetAdapter

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PresetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PresetAdapter { preset ->
            val bot = Bot(
                name = preset.name,
                description = preset.description,
                prompt = preset.prompt,
                avatar = preset.avatar
            )
            AppRepository.saveBot(bot)
            Toast.makeText(requireContext(), "已添加到我的智能体", Toast.LENGTH_SHORT).show()
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        adapter.submitList(Presets.LIST)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
