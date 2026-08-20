package com.cse489.assignment2.ui.media

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.cse489.assignment2.R
import com.cse489.assignment2.databinding.FragmentAudioBinding

class AudioFragment : Fragment() {
    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private val progressUpdater = object : Runnable {
        override fun run() {
            val player = mediaPlayer ?: return
            binding.audioSeekBar.progress = player.currentPosition
            binding.audioTimeText.text = "${formatTime(player.currentPosition)} / ${formatTime(player.duration)}"
            handler.postDelayed(this, 250)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sample_audio)
        val player = mediaPlayer ?: return

        binding.audioSeekBar.max = player.duration
        binding.audioTimeText.text = "00:00 / ${formatTime(player.duration)}"

        binding.playAudioButton.setOnClickListener {
            player.start()
            binding.audioStatusText.text = "Playing"
            startProgressUpdates()
        }

        binding.pauseAudioButton.setOnClickListener {
            if (player.isPlaying) player.pause()
            binding.audioStatusText.text = "Paused"
        }

        binding.stopAudioButton.setOnClickListener {
            if (player.isPlaying) player.pause()
            player.seekTo(0)
            binding.audioSeekBar.progress = 0
            binding.audioStatusText.text = "Reset"
        }

        binding.audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) player.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        player.setOnCompletionListener {
            it.seekTo(0)
            binding.audioSeekBar.progress = 0
            binding.audioStatusText.text = "Finished"
        }
    }

    private fun startProgressUpdates() {
        handler.removeCallbacks(progressUpdater)
        handler.post(progressUpdater)
    }

    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        return "%02d:%02d".format(totalSeconds / 60, totalSeconds % 60)
    }

    override fun onPause() {
        mediaPlayer?.let { if (it.isPlaying) it.pause() }
        handler.removeCallbacks(progressUpdater)
        super.onPause()
    }

    override fun onDestroyView() {
        handler.removeCallbacks(progressUpdater)
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroyView()
        _binding = null
    }
}
