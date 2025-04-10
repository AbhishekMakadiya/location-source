package com.location.location.fcmNotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.location.location.utils.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.location.location.R
import com.location.location.models.NotificationModel
import com.location.location.ui.splash.SplashActivity
import org.apache.commons.text.StringEscapeUtils

/*import org.apache.commons.text.StringEscapeUtils*/


/**
 * Notification type
 * 1. Chat
 */
class MyFcmListenerService : FirebaseMessagingService() {
    private val TAG = javaClass.simpleName
    private var NOTIFICATION_TYPE_CHAT = "1"
    var data: Map<String, String>? = null

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val preferenceManager = PreferenceManager(this)
        preferenceManager.setFCMToken(newToken)
        Log.e(TAG, "token=$newToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            message.data.let {
                val preferenceManager = PreferenceManager(this)
                // if user not login then return from here
                if (!preferenceManager.getUserLogin()) {
                    return
                }

                Log.e("TAG", "FCM MSG ==" + message.data.toString())
                // Parse the notification data
                val notificationDataJson = it["send_data"]
                val notificationData = Gson().fromJson(notificationDataJson, NotificationModel::class.java)

                // Extract notification details
                val title = StringEscapeUtils.unescapeJava(notificationData.title)
                val body = StringEscapeUtils.unescapeJava(notificationData.notification_message)

                // Create intent to open Main Activity
                val intent = Intent(this, SplashActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                showNotification(this, title, body, intent)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "error==" + e.message)
        }
    }

    private fun showNotification(
        context: Context,
        title: String?,
        body: String?,
        intent: Intent?
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_id)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val mChannel = NotificationChannel(
            channelId, channelName, importance
        )
        mChannel.enableVibration(true)
        mChannel.enableLights(true)
        mChannel.setSound(
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
        )
        notificationManager.createNotificationChannel(mChannel)

        val mBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setColor(ContextCompat.getColor(context, R.color.colorGreen))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)

        val stackBuilder = TaskStackBuilder.create(context)
        intent?.setPackage(context.packageName)

        stackBuilder.addNextIntentWithParentStack(intent!!)
//        stackBuilder.addParentStack(HomeActivity::class.java)
        stackBuilder.addParentStack(SplashActivity::class.java)

        var id = PreferenceManager(context).getIntPreference(PreferenceManager.NOTIFICATION_ID)
        if (id >= Integer.MAX_VALUE) id = 0
        PreferenceManager(context).setIntPreference(PreferenceManager.NOTIFICATION_ID, id + 1)

        val resultPendingIntent = stackBuilder.getPendingIntent(
            id,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)
        notificationManager.notify(id, mBuilder.build())
    }
}
