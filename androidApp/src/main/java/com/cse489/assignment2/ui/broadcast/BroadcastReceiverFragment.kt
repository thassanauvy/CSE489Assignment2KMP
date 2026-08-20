package com.cse489.assignment2.ui.broadcast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.cse489.assignment2.databinding.FragmentBroadcastReceiverBinding
import com.cse489.assignment2.shared.BroadcastChoice

class BroadcastReceiverFragment : Fragment() {
    private var _binding: FragmentBroadcastReceiverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBroadcastReceiverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val choices = BroadcastChoice.entries
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            choices.map { it.label },
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.broadcastSpinner.adapter = adapter
        binding.proceedButton.setOnClickListener {
            when (choices[binding.broadcastSpinner.selectedItemPosition]) {
                BroadcastChoice.Custom -> {
                    startActivity(Intent(requireContext(), TextInputActivity::class.java))
                }

                BroadcastChoice.Battery -> {
                    startActivity(Intent(requireContext(), BatteryReceiverActivity::class.java))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
