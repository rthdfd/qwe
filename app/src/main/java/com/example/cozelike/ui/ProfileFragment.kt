package com.example.cozelike.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cozelike.databinding.FragmentProfileBinding
import com.example.cozelike.util.Prefs

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.baseUrlEdit.setText(Prefs.apiBaseUrl(requireContext()))
        binding.apiKeyEdit.setText(Prefs.apiKey(requireContext()))
        binding.modelEdit.setText(Prefs.defaultModel(requireContext()))

        binding.saveBtn.setOnClickListener {
            Prefs.setApiBaseUrl(requireContext(), binding.baseUrlEdit.text.toString().trim())
            Prefs.setApiKey(requireContext(), binding.apiKeyEdit.text.toString().trim())
            Prefs.setDefaultModel(requireContext(), binding.modelEdit.text.toString().trim())
            Toast.makeText(requireContext(), "设置已保存", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
