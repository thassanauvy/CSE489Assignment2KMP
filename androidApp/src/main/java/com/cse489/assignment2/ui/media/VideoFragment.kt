package com.cse489.assignment2.ui.media

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.cse489.assignment2.R
import com.cse489.assignment2.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controller = MediaController(requireContext()).apply {
            setAnchorView(binding.videoView)
        }
        binding.videoView.setMediaController(controller)

        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${R.raw.sample_video}")
        binding.videoView.setVideoURI(videoUri)
        binding.videoView.setOnPreparedListener { player ->
            player.isLooping = true
            binding.videoStatusText.text = "Playing bundled MP4 · tap video for controls"
            binding.videoView.start()
        }
        binding.videoView.setOnErrorListener { _, what, extra ->
            binding.videoStatusText.text = "Video playback error ($what/$extra)"
            true
        }
    }

    override fun onPause() {
        binding.videoView.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.videoView.stopPlayback()
        super.onDestroyView()
        _binding = null
    }
}
