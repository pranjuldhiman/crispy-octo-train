package com.rs.roundupclasses.youtube

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.roundupclasses.R
import com.rs.roundupclasses.models.Comment
import com.rs.roundupclasses.models.CommentListResponse
import com.rs.roundupclasses.utils.Config
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.youtube_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class YouTubePlayerActivity : YouTubeBaseActivity(),
    YouTubePlayer.OnInitializedListener {

    private var youTubeView: YouTubePlayerView? = null
    private var playerStateChangeListener: MyPlayerStateChangeListener? = null
    private var playbackEventListener: MyPlaybackEventListener? = null
    private var player: YouTubePlayer? = null
    private var videoUrl: String? = null
    private var ID: String? = null

    lateinit  var progress: ProgressDialog;
    private lateinit var viewModel: YoutubeViewModel

    lateinit var sharedPreferences: SharedPreferences
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    var fislike: Int=0
    var fdislike: Int=0

    var commentlistlist: MutableList<Comment> = ArrayList()
 //   var category_recycler_adapter: CommentAdapter? = null

    var comment_list_adapter: CommentListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.youtube_activity)
        youTubeView = findViewById<View>(R.id.youtube_view) as YouTubePlayerView
        youTubeView!!.initialize(Config.YOUTUBE_API_KEY, this)
        

        playerStateChangeListener = MyPlayerStateChangeListener()
        playbackEventListener = MyPlaybackEventListener()

        comment_list!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        comment_list_adapter =  CommentListAdapter(this@YouTubePlayerActivity);
        comment_list!!.adapter = comment_list_adapter
        commentlistlist = comment_list_adapter!!.currentList
     //   commentlistlist = category_recycler_adapter!!.getCurrentList() as MutableList<Comment>
        ID = intent.getStringExtra("ID")
        Log.e("VIDEOID","likedislike is called videoId videoId...."+ID)
        val viewModelFactory = YoutubeViewModelFactory(this)
       // viewModel = ViewModelProviders.of(applicationContext, viewModelFactory).get(YoutubeViewModel::class.java)
        sharedPreferences =
            this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        progress = ProgressDialog(this)
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()
        getCommentList(userid,ID.toString())
        like.setOnClickListener {
            if(fislike == 0)
            {
                likedislike(userid,ID.toString(),1,"A")
            }
            else
            {
                likedislike(userid,ID.toString(),0,"A")
            }
        }

       dislike.setOnClickListener {
            if(fdislike == 0)
            {
                likedislike(userid,ID.toString(),2,"B")
            }
            else
            {
                likedislike(userid,ID.toString(),0,"B")
            }
        }

        share.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + "com.rs.roundupclasses" + " APP"
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
        sendmsg.setOnClickListener {
            if(edittext_chatbox.text.toString().equals(""))
            {
                Toast.makeText(this@YouTubePlayerActivity,"text is required",Toast.LENGTH_LONG).show()

            }
            else
            {
                sendCommentMsg(userid,ID.toString(),edittext_chatbox.text.toString())

            }
        }
    }

    fun likedislike(userid: String,videoId: String,islike:Int,type:String) {
        progress.setLoading(true)
        Log.e("VIDEOID","likedislike is called userid...."+userid)
        Log.e("VIDEOID","likedislike is called videoId...."+videoId)
        Log.e("VIDEOID","likedislike is called islike...."+islike)
        // hashMap.put("user_password",password_edittext.getText().toString());
        val requestInterface: ApiInterface = Util.retrofitRegister()

        val call: Call<CommentListResponse> =
            requestInterface.likedislike(userid,videoId,islike)
        call.enqueue(object :
            Callback<CommentListResponse> {
            override fun onResponse(
                call: Call<CommentListResponse>,
                response: Response<CommentListResponse>
            ) {
                progress.setLoading(false)
                Log.e("LOGINAPI","login response is called." + response.body().toString())
                Log.e( "LOGINAPI",  "login response is called." + response.body().toString())

                if(response.body()!!.status == 200)
                {
                    if(type.equals("A"))
                    {
                        if(islike == 1)
                        {
                            fislike =1
                            like.setBackgroundResource(R.drawable.thumbupfill_icon)
                            var totalcount = (totallike.text.toString()).toInt()
                            Log.e("TOTALCOUNT","total count is..."+totalcount)
                            totalcount =totalcount.plus(1)
                            Log.e("TOTALCOUNT","total after count is..."+totalcount)
                            totallike.setText(totalcount.toString())



                            if(fdislike == 1)
                            {
                                dislike.setBackgroundResource(R.drawable.thumbdown_icon)
                                fdislike = 0
                                var totaldislikecount = (totaldislike.text.toString()).toInt()
                                Log.e("TOTALCOUNT","total count is..."+totaldislikecount)
                                totaldislikecount =totaldislikecount.minus(1)
                                Log.e("TOTALCOUNT","total after count is..."+totaldislikecount)
                                totaldislike.setText(totaldislikecount.toString())
                            }

                        }
                        else
                        {
                            like.setBackgroundResource(R.drawable.thumbup_icon)
                            fislike = 0
                            var totalcount = (totallike.text.toString()).toInt()
                            Log.e("TOTALCOUNT","total count is..."+totalcount)
                            totalcount =totalcount.minus(1)
                            Log.e("TOTALCOUNT","total after count is..."+totalcount)
                            totallike.setText(totalcount.toString())
                        }
                    }
                    else
                    {
                        if(islike == 2)
                        {
                            fdislike =1
                            dislike.setBackgroundResource(R.drawable.thumbdownfill_icon)
                            var totalcount = (totaldislike.text.toString()).toInt()
                            Log.e("TOTALCOUNT","total count is..."+totalcount)
                            totalcount =totalcount.plus(1)
                            Log.e("TOTALCOUNT","total after count is..."+totalcount)
                            totaldislike.setText(totalcount.toString())

                            if(fislike == 1)
                            {
                                like.setBackgroundResource(R.drawable.thumbup_icon)
                                fislike = 0
                                var totallikecount = (totallike.text.toString()).toInt()
                                Log.e("TOTALCOUNT","total count is..."+totallikecount)
                                totallikecount =totallikecount.minus(1)
                                Log.e("TOTALCOUNT","total after count is..."+totallikecount)
                                totallike.setText(totallikecount.toString())
                            }
                        }
                        else
                        {

                            dislike.setBackgroundResource(R.drawable.thumbdown_icon)
                            fdislike = 0
                            var totalcount = (totaldislike.text.toString()).toInt()
                            Log.e("TOTALCOUNT","total count is..."+totalcount)
                            totalcount =totalcount.minus(1)
                            Log.e("TOTALCOUNT","total after count is..."+totalcount)
                            totaldislike.setText(totalcount.toString())

                        }
                    }
                }
                else
                {
                    Toast.makeText(this@YouTubePlayerActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(
                call: Call<CommentListResponse>,
                t: Throwable
            ) {
                progress.setLoading(false)
            }
        })
    }

    fun sendCommentMsg(userid: String,videoId: String,msg:String) {
        progress.setLoading(true)
        // hashMap.put("user_password",password_edittext.getText().toString());
        val requestInterface: ApiInterface = Util.retrofitRegister()
        val call: Call<CommentListResponse> =
            requestInterface.sendComment(userid,videoId,msg)
        call.enqueue(object :
            Callback<CommentListResponse> {
            override fun onResponse(
                call: Call<CommentListResponse>,
                response: Response<CommentListResponse>
            ) {
                progress.setLoading(false)
                Log.e("LOGINAPI","login response is called." + response.body().toString())
                Log.e("LOGINAPI","login response is called." + response.body().toString())
                if(response.body()!!.status == 200)
                {
                    val sdf = SimpleDateFormat("yyyy-M-dd hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    System.out.println(" C DATE is  "+currentDate)

                    Log.e("COMMENTLIST","login response before is called." + commentlistlist.size)

                    commentlistlist.add(Comment("", edittext_chatbox.text.toString(),currentDate.toString()))
                    comment_list_adapter!!.notifyDataSetChanged()
                    edittext_chatbox.setText("")
                    totalcomment.setText(commentlistlist.size.toString())
                    Log.e("COMMENTLIST","login response after is called." + commentlistlist.size)
                }
                else
                {
                    Toast.makeText(this@YouTubePlayerActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<CommentListResponse>,t: Throwable) {
                progress.setLoading(false)
            }
        })
    }

    fun getCommentList(userid: String,videoId: String) {
        Log.e("CHECKDATA","userid is ......."+userid.toString())
        Log.e("CHECKDATA","videoId is ......."+videoId.toString())

        progress.setLoading(true)
        // hashMap.put("user_password",password_edittext.getText().toString());
        val requestInterface: ApiInterface = Util.retrofitRegister()
        val call: Call<CommentListResponse> =
            requestInterface.getComment(userid,videoId)
        call.enqueue(object :
            Callback<CommentListResponse> {
            override fun onResponse(
                call: Call<CommentListResponse>,
                response: Response<CommentListResponse>
            ) {
                progress.setLoading(false)
                Log.e(
                    "LOGINAPI",
                    "login response is called." + response.body().toString()
                )
                Log.e(
                    "LOGINAPI",
                    "login response is called." + response.body().toString()
                )
                if(response.body()!!.status == 200)
                {
                    totallike.text = response.body()!!.likecount
                    totaldislike.text = response.body()!!.unlikecount
                    totalcomment.text = response.body()!!.commentcount
                    var myList: MutableList<Comment> =
                        ArrayList<Comment>()
                    myList = response.body()!!.comments!!.toMutableList()
                    Log.e("YOUTUBEAPI","list api is called......"+myList.size)
                    data(myList)
                    likeUnlikeStatus(response.body()!!.like_status!!)

                }
                else
                {
                    totallike.text = response.body()!!.likecount
                    totaldislike.text = response.body()!!.unlikecount
                    totalcomment.text = response.body()!!.commentcount

                    Toast.makeText(this@YouTubePlayerActivity,response.body()!!.message,Toast.LENGTH_LONG).show()
                }


            }
            override fun onFailure(
                call: Call<CommentListResponse>,
                t: Throwable
            ) {
                progress.setLoading(false)
            }
        })
    }

    fun data(  myList: MutableList<Comment>)
    {
        commentlistlist.addAll(myList!!)
        comment_list_adapter!!.notifyDataSetChanged()
       /* comment_list.visibility = View.VISIBLE
        comment_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

           adapter = CommentAdapter({ title: String, message: String, time: String->
                val intent = Intent(this@YouTubePlayerActivity, NotificationDetail::class.java)
                intent.putExtra("TITLE", title)
                intent.putExtra("MESSAGE", message)
                intent.putExtra("TIME", time)
                startActivity(intent)

            }, context, myList as List<Comment>)
            addItemDecoration(VideoItemDecoration(10))
        }*/
    }

    fun likeUnlikeStatus(likestatus:Int)
    {
        if(likestatus==0)
        {
            like.setBackgroundResource(R.drawable.thumbup_icon)
        }
        else if(likestatus == 1)
        {
            like.setBackgroundResource(R.drawable.thumbupfill_icon)

             fislike = 1
        }
        else if(likestatus == 2)
        {
            dislike.setBackgroundResource(R.drawable.thumbdownfill_icon)
            fdislike = 1

        }
    }

    override fun onResume() {
        super.onResume()
        videoUrl = intent.getStringExtra("videoUrl")
        Log.e("CHECKDATA","id is ......."+ID.toString())
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
            errorReason.getErrorDialog(this,
                RECOVERY_REQUEST
            )
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


