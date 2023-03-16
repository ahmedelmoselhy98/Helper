package com.chefshub.utils.ext

import android.text.format.DateUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateExt {

    companion object{
        val simpleDateFormat= SimpleDateFormat("yyyy-mm-dd hh:mm:ss",Locale.US)
        init {
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        }



    fun formatDate(createdAt: String?): String {

        if (createdAt.isNullOrEmpty())

            return ""

        try {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(simpleDateFormat.parse(createdAt))

        } catch (ex: Exception) {

            ex.printStackTrace()

        }

        return createdAt

    }

    fun formatDateAndTime(createdAt: String?): String {

        if (createdAt.isNullOrEmpty())

            return ""

        try {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

            dateFormat.timeZone = TimeZone.getDefault()

            return dateFormat.format(simpleDateFormat.parse(createdAt))

        } catch (ex: Exception) {

            ex.printStackTrace()

        }

        return createdAt

    }


        fun format(){
            val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val past = format.parse("20/08/2015 18:10:52")
            val now = Date()

            Log.e("jjjjjjjjjjj","jjjjjjjjjj  "+TimeUnit.MILLISECONDS.toSeconds(now.time - past.time))
            println(TimeUnit.MILLISECONDS.toSeconds(now.time - past.time) )
            println(TimeUnit.MILLISECONDS.toMinutes(now.time - past.time) )
            println(TimeUnit.MILLISECONDS.toHours(now.time - past.time) )
            println(TimeUnit.MILLISECONDS.toDays(now.time - past.time) )
        }

    fun formatLastDate(createdAt: String?): String {

        if (createdAt.isNullOrEmpty())

            return ""

        try {

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateFormat.timeZone = TimeZone.getDefault()

            val date = simpleDateFormat.parse(createdAt)

            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(

                date.time,

                Calendar.getInstance().timeInMillis,

                DateUtils.MINUTE_IN_MILLIS

            ).toString()


            return niceDateStr

        } catch (ex: Exception) {

            ex.printStackTrace()

            try {

                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)

                simpleDateFormat.timeZone= TimeZone.getTimeZone("GMT")

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                dateFormat.timeZone = TimeZone.getDefault()

                val date = simpleDateFormat.parse(createdAt)

                val niceDateStr: String = DateUtils.getRelativeTimeSpanString(

                    date.time,

                    Calendar.getInstance().timeInMillis,

                    DateUtils.MINUTE_IN_MILLIS

                ).toString()

                return niceDateStr

            } catch (ex: Exception) {

                ex.printStackTrace()

            }

        }

        return createdAt

    }
}}