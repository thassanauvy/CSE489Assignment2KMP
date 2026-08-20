package com.cse489.assignment2.ui.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cse489.assignment2.databinding.ActivityCustomReceiverBinding
import com.cse489.assignment2.shared.CustomBroadcastContract

class CustomReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomReceiverBinding
    private var receiverRegistered = false

    private val message: String by lazy {
        intent.getStringExtra(CustomBroadcastContract.EXTRA_MESSAGE).orEmpty()
    }

    private val customReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val received = intent?.getStringExtra(CustomBroadcastContract.EXTRA_MESSAGE).orEmpty()
            binding.statusText.text = "Custom BroadcastReceiver received the message:"
            binding.receivedMessageText.text = received.ifBlank { "(empty message)" }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resendButton.setOnClickListener { sendCustomBroadcast() }
    }

    override fun onStart() {
        super.onStart()
        if (!receiverRegistered) {
            ContextCompat.registerReceiver(
                this,
                customReceiver,
                IntentFilter(CustomBroadcastContract.ACTION_CUSTOM_MESSAGE),
                ContextCompat.RECEIVER_NOT_EXPORTED,
            )
            receiverRegistered = true
        }

        // Post until after registration has completed, then demonstrate the broadcast immediately.
        binding.root.post { sendCustomBroadcast() }
    }

    override fun onStop() {
        if (receiverRegistered) {
            unregisterReceiver(customReceiver)
            receiverRegistered = false
        }
        super.onStop()
    }

    private fun sendCustomBroadcast() {
        if (message.isBlank()) {
            binding.statusText.text = "No message was supplied by the second activity."
            return
        }

        binding.statusText.text = "Sending app-only custom broadcast…"
        val broadcast = Intent(CustomBroadcastContract.ACTION_CUSTOM_MESSAGE).apply {
            setPackage(packageName)
            putExtra(CustomBroadcastContract.EXTRA_MESSAGE, message)
        }
        sendBroadcast(broadcast)
    }
}
