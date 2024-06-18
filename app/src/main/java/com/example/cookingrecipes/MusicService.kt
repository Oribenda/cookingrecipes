package com.example.cookingrecipes

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music) // Replace with your music file
        mediaPlayer.isLooping = true // Set looping if needed
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
            ACTION_PAUSE -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
            ACTION_STOP -> {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.prepare()
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_PLAY = "com.example.cookingrecipes.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.cookingrecipes.ACTION_PAUSE"
        const val ACTION_STOP = "com.example.cookingrecipes.ACTION_STOP"
    }
}
