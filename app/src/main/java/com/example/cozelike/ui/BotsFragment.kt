package com.example.cozelike.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cozelike.data.AppRepository
import com.example.cozelike.databinding.FragmentBotsBinding
import com.example.cozelike.ui.adapters.BotAdapter

class BotsFragment : Fragment() {

    private var _binding: FragmentBotsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBotsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BotAdapter(
            onChat = { bot ->
                startActivity(
                    Intent(requireContext(), ChatActivity::class.java)
                        .putExtra("bot_id", bot.id)
                )
            },
            onEdit = { bot ->
                startActivity(
                    Intent(requireContext(), BotEditorActivity::class.java)
                        .putExtra("bot_id", bot.id)
                )
            },
            onDelete = { bot ->
                AppRepository.deleteBot(bot.id)
                loadData()
                Toast.makeText(requireContext(), "已删除智能体", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(), BotEditorActivity::class.java))
        }

        loadData()
    }

    private fun loadData() {
        val bots = AppRepository.data.bots.sortedByDescending { it.createdAt }
        adapter.submitList(bots)
        binding.empty.visibility = if (bots.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
