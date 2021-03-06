package com.harbhajan.foodie

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseMessageReceiver:FirebaseMessagingService(){
    val channelId="notification_channel"
    val channelName="com.harbhajan.foodie"

    fun getRemoteView(title:String,message: String) :RemoteViews{
        val remoteView=RemoteViews("com.harbhajan.foodie",R.layout.pushnotification)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.icon,R.drawable.dmlogo)

        return remoteView
    }
     override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            val data: Map<String, String> = remoteMessage.data
            val title = data["title"]
            val message = data["message"]
            //generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
            generateNotification(title!!,message!!)
        }
    }
    fun generateNotification(title: String, message: String){
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.dmlogo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder=builder.setContent(getRemoteView(title,message))
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build()
        )
    }
}
