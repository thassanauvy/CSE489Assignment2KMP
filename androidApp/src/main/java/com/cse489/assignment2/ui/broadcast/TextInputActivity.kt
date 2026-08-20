package com.cse489.assignment2.ui.broadcast

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cse489.assignment2.databinding.ActivityTextInputBinding
import com.cse489.assignment2.shared.CustomBroadcastContract

class TextInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {
            val message = binding.messageInput.text?.toString()?.trim().orEmpty()
            if (message.isBlank()) {
                binding.messageInput.error = "Enter a message first"
                return@setOnClickListener
            }

            val next = Intent(this, CustomReceiverActivity::class.java).apply {
                putExtra(CustomBroadcastContract.EXTRA_MESSAGE, message)
            }
            startActivity(next)
        }
    }
}
