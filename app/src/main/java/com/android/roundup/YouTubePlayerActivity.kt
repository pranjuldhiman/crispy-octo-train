package com.android.roundup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.roundup.utils.Config
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayerView
import java.util.regex.Pattern

class YouTubePlayerActivity : YouTubeBaseActivity(),
    YouTubePlayer.OnInitializedListener {
    private var youTubeView: YouTubePlayerView? = null
    private var playerStateChangeListener: MyPlayerStateChangeListener? = null
    private var playbackEventListener: MyPlaybackEventListener? = null
    private var player: YouTubePlayer? = null
    private var videoUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.youtube_activity)
        youTubeView = findViewById<View>(R.id.youtube_view) as YouTubePlayerView
        youTubeView!!.initialize(Config.YOUTUBE_API_KEY, this)
        playerStateChangeListener = MyPlayerStateChangeListener()
        playbackEventListener = MyPlaybackEventListener()
    }

    override fun onResume() {
        super.onResume()
        videoUrl = intent.getStringExtra("videoUrl")
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider,
        player: YouTubePlayer,
        wasRestored: Boolean
    ) {
        this.player = player
        player.setPlayerStateChangeListener(playerStateChangeListener)
        player.setPlaybackEventListener(playbackEventListener)
        if (!wasRestored && videoUrl != null) {
            player.cueVideo(getVideoIdFromYoutubeUrl(videoUrl)) // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        errorReason: YouTubeInitializationResult
    ) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST)
                .show()
        } else {
            val error =
                String.format(getString(R.string.player_error), errorReason.toString())
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubePlayerProvider!!.initialize(
                Config.YOUTUBE_API_KEY,
                this
            )
        }
    }

    protected val youTubePlayerProvider: YouTubePlayer.Provider?
        protected get() = youTubeView

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private inner class MyPlaybackEventListener : PlaybackEventListener {
        override fun onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            //showMessage("Playing");
        }

        override fun onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            //showMessage("Paused");
        }

        override fun onStopped() {
            // Called when playback stops for a reason other than being paused.
            //showMessage("Stopped");
        }

        override fun onBuffering(b: Boolean) {
            // Called when buffering starts or ends.
        }

        override fun onSeekTo(i: Int) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private inner class MyPlayerStateChangeListener : PlayerStateChangeListener {
        override fun onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        override fun onLoaded(s: String) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        override fun onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        override fun onVideoStarted() {
            // Called when playback of the video starts.
        }

        override fun onVideoEnded() {
            // Called when the video reaches its end.
        }

        override fun onError(errorReason: YouTubePlayer.ErrorReason) {
            // Called when an error occurs.
        }
    }

    companion object {
        private const val RECOVERY_REQUEST = 1
    }

    /**
     * Get id from youtube url
     *
     * @param url
     * @return
     */
    fun getVideoIdFromYoutubeUrl(url: String?): String? {
        var videoId: String? = null
        val regex =
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
        val pattern =
            Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(url)
        if (matcher.find()) {
            videoId = matcher.group(1)
        }
        return videoId
    }
}