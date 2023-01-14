package com.chefshub.utils.ext

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

fun Context.sendNotification(
    title: String,
    body: String,
    clickAction: String? = null,
    channelId: String,
    icon: Int,
    notID: Int,
    classz: Class<*>,
    pendingInt: PendingIntent? = null
) {
    var pendingIntent: PendingIntent? = pendingInt
    if (pendingIntent == null) {
        val intent = Intent(this, classz)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                applicationContext, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                applicationContext, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(notID, notificationBuilder.build())
}

fun Context.sendNotification(
    title: String,
    body: String,
    channelId: String,
    icon: Int,
    notID: Int,
    fragmentId: Int?=null,
    classz: Class<*>,
    map: Map<String, Long>
) {

    val intent = Intent(this, classz)
    intent.apply {
        intent.putExtra("fragID", fragmentId)
        map.forEach { it ->
            intent.putExtra(it.key, it.value)
        }
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            applicationContext, 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getActivity(
            applicationContext, 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
val notificationBuilder = NotificationCompat.Builder(this, channelId)
    .setSmallIcon(icon)
    .setContentTitle(title)
    .setContentText(body)
    .setAutoCancel(true)
    .setSound(defaultSoundUri)
    .setContentIntent(pendingIntent)

val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        channelId,
        "Channel human readable title",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    notificationManager.createNotificationChannel(channel)
}

notificationManager.notify(notID, notificationBuilder.build())
}