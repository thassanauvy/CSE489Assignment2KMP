package com.cse489.assignment2.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cse489.assignment2.databinding.FragmentImageScaleBinding
import com.cse489.assignment2.shared.AssignmentNetworkContent
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageScaleFragment : Fragment() {
    private var _binding: FragmentImageScaleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImageScaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retryImageButton.setOnClickListener { loadImage() }
        loadImage()
    }

    private fun loadImage() {
        binding.imageProgress.isVisible = true
        binding.retryImageButton.isVisible = false
        binding.imageStatusText.text = "Loading image from the internet…"
        binding.zoomImageView.resetZoom()

        viewLifecycleOwner.lifecycleScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) { downloadBitmap(AssignmentNetworkContent.IMAGE_URL) }
            }

            result.onSuccess { bitmap ->
                binding.zoomImageView.setImageBitmap(bitmap)
                binding.imageStatusText.text = "Loaded from internet · pinch to zoom (1×–5×)"
                binding.imageProgress.isVisible = false
            }.onFailure { error ->
                binding.imageProgress.isVisible = false
                binding.retryImageButton.isVisible = true
                binding.imageStatusText.text = "Image load failed: ${error.message ?: "network error"}"
            }
        }
    }

    private fun downloadBitmap(urlString: String): Bitmap {
        val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
            connectTimeout = 10_000
            readTimeout = 15_000
            instanceFollowRedirects = true
            requestMethod = "GET"
        }

        return try {
            connection.connect()
            if (connection.responseCode !in 200..299) {
                error("HTTP ${connection.responseCode}")
            }
            connection.inputStream.use { stream ->
                BitmapFactory.decodeStream(stream) ?: error("Could not decode downloaded image")
            }
        } finally {
            connection.disconnect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
