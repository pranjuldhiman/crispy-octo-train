package com.rs.roundupclasses.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rs.roundupclasses.R
import com.rs.roundupclasses.splash.SplashActivity
import com.rs.roundupclasses.webview.WebViewActivity

class MyFirebaseMessagingService : FirebaseMessagingService()
{

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("NOTIFICATION", "From: notification is called")

    //    Log.d("NOTIFICATION", "From: ${remoteMessage?.from}")
        Log.e("NOTIFICATION", "onMessageReceive is called" + remoteMessage.data)
   //     sendNotification(remoteMessage.data.get("message").toString())

     //   Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()

        remoteMessage?.data?.let {
            Log.e("NOTIFICATION", "onMessageReceive is called" +
                    it.get("message"))

            sendNotification(it.get("message").toString(),it.get("url").toString())

        }


        // Check if message contains a notification payload.
       /* remoteMessage?.notification?.let {
            Log.d("NOTIFICATION", "Message Notification Body: ${it.body}")

            sendNotification(it.body!!)
            //Message Services handle notification
            //Message Services handle notification
          *//*  val notification = NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.from)
                .setContentText(it.body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(*//**//*notification id*//**//*0, notification)*//*
        }*/
    }
    override fun onNewToken(token: String) {
        //handle token
        Log.d("NOTIFICATION", "Message Notification Body: ${token.toString()}")
   }

    private fun sendNotification(messageBody: String,url:String) {

       /* val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        intent.putExtra("type", "About Us")
        intent.putExtra("URL", "http://roundupclasses.com/about-us.html")
        startActivity(intent)*/

        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("type","Notification")
        intent.putExtra("URL",url.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder: NotificationCompat.Builder? = null
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                packageName,
                packageName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = packageName
            notificationManager.createNotificationChannel(channel)
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application, packageName)
            }
        } else {
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(application,packageName)
            }
        }
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


}