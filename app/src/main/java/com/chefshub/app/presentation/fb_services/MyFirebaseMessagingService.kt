package com.chefshub.app.presentation.fb_services

import android.util.Log
import com.chefshub.app.R
import com.chefshub.app.presentation.main.MainActivity
import com.chefshub.utils.ext.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "MyFirebaseMessagingServ"

class MyFirebaseMessagingService :
    FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: notification ${message.notification}")
        Log.d(TAG, "onMessageReceived: data ${message.data}")

        if (message.data.isEmpty().not()) {

            message.data.let {
                sendNotification(
                    title = it["title"].toString(),
                    body = it["body"].toString(),
                    channelId = getString(R.string.default_notification_channel_id),
                    notID = 33,
                    icon = R.drawable.ic_arrow_left,
                    classz = MainActivity::class.java,
                    fragmentId = null,
                    map = emptyMap()

                )
            }
        } else
            message.notification?.let {
                sendNotification(
                    title = it.title.toString(),
                    it.body.toString(),
                    clickAction = null,
                    getString(R.string.default_notification_channel_id),
                    notID = 33,
                    icon = R.drawable.ic_arrow_left,
                    classz = MainActivity::class.java
                )

            }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        FirebaseMessaging.getInstance().subscribeToTopic("customersCampaignAndroid")
    }
}