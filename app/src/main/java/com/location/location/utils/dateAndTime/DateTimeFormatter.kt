package com.location.location.utils.dateAndTime

import android.text.format.DateUtils
import com.location.location.utils.LogHelper
/*import org.apache.commons.lang3.time.DateUtils.isSameDay*/
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateTimeFormatter {
    val TAG = this.javaClass.simpleName

    const val UTC_FORMAT = "yyyy-MM-dd HH:mm:ss"//2022-07-15 05:15:47
    const val UTC_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'"
    const val UTC_FORMAT3 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val USER_USAGE_DATE_FORMAT = "dd-MM-yyyy HH:mm"//:ss
    const val USER_USAGE_DATE_FORMAT1 = "HH:mm:ss, dd/MM/yyyy"
    const val USER_USAGE_DATE_FORMAT2 = "dd-MM-yyyy | hh:mm"
    const val USER_USAGE_DATE_FORMAT3 = "dd MMM yyyy | hh:mm"
    const val USER_USAGE_DATE_FORMAT4 = "yyyy-MM-dd"
    const val USER_USAGE_DATE_FORMAT5 = "MMM yyyy"
    const val USER_USAGE_DATE_FORMAT6 = "MM-dd-yyyy"
    const val USER_USAGE_DATE_FORMAT7 = "YYYY-MM-DD"
    const val USER_USAGE_DATE_FORMAT8 = "dd-MM-yyyy"
    const val USER_USAGE_DATE_FORMAT9 = "MM/yyyy"
    const val DATE_FORMAT_D = "d"
    const val TIME_FORMAT_12_HOUR = "hh:mm aa"
    const val TIME_FORMAT_24_HOUR = "HH:mm"
    const val DEFAULT_PATTERN_DATE = "dd, MMM yyyy"
    const val DEFAULT_PATTERN_DATE1 = "yyyy-MM-dd"
    const val FORMAT1 = "MMM dd, hh:mm aa"
    const val CHAT_DATE_FORMAT1 = "hh:mm aa"
    const val CHAT_DATE_FORMAT2 = "dd-MM-yyyy hh:mm aa"
    const val CHAT_DATE_FORMAT3 = "dd-MM-yyyy"
    const val FORMAT5_REQUIRED = "dd MMM yyyy, hh:mm aa"
    const val FORMAT6_REQUIRED = "dd MMM yy"

    fun getFormattedDate(calendar: Calendar, format: String): String {
        val dateFormatter = SimpleDateFormat(format, Locale.US)
        return dateFormatter.format(calendar.timeInMillis)
    }

    fun convertDateToDefaultPatern(date1: String?): String? {
        var finlDate: String? = null
        var myDate: Date? = null
        var formatter = SimpleDateFormat(DEFAULT_PATTERN_DATE1, Locale.US)
        try {
            myDate = formatter.parse(date1)
            formatter = SimpleDateFormat(DEFAULT_PATTERN_DATE, Locale.US)
            finlDate = formatter.format(myDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return finlDate
    }

    fun getFormattedDateWithSuffix(calendar: Calendar): String {
        val dayNumberSuffix = getDayNumberSuffix(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        val dateFormatter = SimpleDateFormat("d'$dayNumberSuffix' MMMM,yyyy", Locale.US)
        return dateFormatter.format(calendar.timeInMillis)
    }

    fun getFormattedDateWithSuffix(date: String): String {
        val date1 = SimpleDateFormat(USER_USAGE_DATE_FORMAT4, Locale.US).parse(date)
        val day = SimpleDateFormat(DATE_FORMAT_D, Locale.US).format(date1)

        val dayNumberSuffix = getDayNumberSuffix(day.toInt())
        val dateFormatter = SimpleDateFormat("d'$dayNumberSuffix' MMMM yyyy", Locale.US)
        return dateFormatter.format(date1)
    }

    private fun getDayNumberSuffix(day: Int): String {
        /*
          //We are only using suffix not superscript that's why this code is commented.
          if (day in 11..13) {
              return "<sup>th</sup>"
          }
          return when (day % 10) {
              1 -> "<sup>st</sup>"
              2 -> "<sup>nd</sup>"
              3 -> "<sup>rd</sup>"
              else -> "<sup>th</sup>"
          }*/

        if (day in 11..13) {
            return "th"
        }
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun getConvertedDateToLocalFromUTC(date: String?): String {
        if (date != null) {
            println(date.length)

            val inputFormat: DateFormat
            inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)
            var localDateTime: String
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            val parsed: Date
            try {
                parsed = inputFormat.parse(date)
                val dateFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT, Locale.US)
                localDateTime = dateFormat.format(parsed)
            } catch (e: Exception) {
                LogHelper.printStackTrace(e)
                localDateTime = ""
            }

            return localDateTime
        } else {
            return ""
        }
    }

    fun getConvertedToLocalFromUTC(date: String?): String {
        if (date != null) {
            println(date.length)

            val inputFormat: DateFormat
            inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)
            var localDateTime: String
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            val parsed: Date
            try {
                parsed = inputFormat.parse(date)
                val dateFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT, Locale.US)
                localDateTime = dateFormat.format(parsed)
            } catch (e: Exception) {
                LogHelper.printStackTrace(e)
                localDateTime = ""
            }

            return localDateTime
        } else {
            return ""
        }
    }

    fun getConvertedToLocalFromUTC1(date: String?): String {
        if (date != null) {
            println(date.length)

            val inputFormat: DateFormat
            inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)
            var localDateTime: String
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
            val parsed: Date
            try {
                parsed = inputFormat.parse(date)
                val dateFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT2, Locale.US)
                localDateTime = dateFormat.format(parsed)
            } catch (e: Exception) {
                LogHelper.printStackTrace(e)
                localDateTime = ""
            }

            return localDateTime
        } else {
            return ""
        }
    }

    fun getConvertedDateToLocalFromUTC(date: String, outputFormat: String): String {

        val inputFormat: DateFormat
        inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)
        var localDateTime: String
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val parsed: Date
        try {
            parsed = inputFormat.parse(date)
            val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
            localDateTime = dateFormat.format(parsed)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
            localDateTime = ""
        }

        return localDateTime
    }

    fun getConvertedDateToLocalFromUTC(date: String, dateCurrentFormat:String, outputFormat: String): String {

        val inputFormat: DateFormat
        inputFormat = SimpleDateFormat(dateCurrentFormat, Locale.US)
        var localDateTime: String
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val parsed: Date
        try {
            parsed = inputFormat.parse(date)
            val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
            localDateTime = dateFormat.format(parsed)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
            localDateTime = ""
        }

        return localDateTime
    }


    fun getCurrentDateTimeInUTC(): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US)
        val yourDate =
            android.text.format.DateFormat.format(USER_USAGE_DATE_FORMAT, calendar).toString()
        return yourDate
    }

    fun getCurrentUTCDateTime(outputFormat: String): String {
        val dateFormat = SimpleDateFormat(outputFormat)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun currentMilisecondToFormat(millisecond: Long, format: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millisecond
        return android.text.format.DateFormat.format(format, calendar).toString()
    }

    fun getMilliSecondsFromDate(date: String, currentFormat: String): Long {
        return try {
            val mDate = SimpleDateFormat(currentFormat, Locale.US).parse(date)
            val timeInMilliSeconds = mDate.time
            timeInMilliSeconds
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }

    }

    fun convertDateString(strDate: String, inputFormat: String, outputFormat: String): Date? {
        var date: Date? = null
        try {
            val df = SimpleDateFormat(outputFormat, Locale.US)
//            date = df.format(SimpleDateFormat(inputFormat, Locale.US).parse(strDate))
            //date = SimpleDateFormat(inputFormat, Locale.US).parse(strDate)
            date = SimpleDateFormat(inputFormat, Locale.US).parse(strDate)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
        return date
    }

    fun getConvertedDate(date: String, outputFormat: String): String {
        val inputFormat: DateFormat
        inputFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT4, Locale.US)
        var localDateTime: String
        val parsed: Date
        try {
            parsed = inputFormat.parse(date)
            val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
            localDateTime = dateFormat.format(parsed)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
            localDateTime = ""
        }

        return localDateTime
    }

    fun getConvertedDate1(date: String, outputFormat: String): String {
        val inputFormat: DateFormat
        inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)
        var localDateTime: String
        val parsed: Date
        try {
            parsed = inputFormat.parse(date)
            val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
            localDateTime = dateFormat.format(parsed)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
            localDateTime = ""
        }

        return localDateTime
    }

    fun getMilliSecondsFromDate(date: String): Long {
        return try {
            val mDate = SimpleDateFormat(USER_USAGE_DATE_FORMAT4, Locale.US).parse(date)
            val timeInMilliSeconds = mDate.time
            timeInMilliSeconds
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun convert24HoursTo12Hours(time: String): String {
        return try {
            //val s = "12:18:00"
            val f1: DateFormat = SimpleDateFormat("HH:mm:ss") //HH for hour of the day (0 - 23)
            val date = f1.parse(time)
            val f2: DateFormat = SimpleDateFormat(TIME_FORMAT_12_HOUR)
            f2.format(date).uppercase(Locale.getDefault()) // "12:18 AM"
        } catch (e: ParseException) {
            e.printStackTrace()
            e.message.toString()
        }
    }

    fun convert12HoursTo24Hours(time: String): String {
        return try {
            //val s = "12:18:00"
            val f1: DateFormat = SimpleDateFormat("HH:mm a") //HH for hour of the day (0 - 23)
            val date = f1.parse(time)
            val f2: DateFormat = SimpleDateFormat("HH:mm")
            f2.format(date).uppercase(Locale.getDefault()) // "12:18 AM"
        } catch (e: ParseException) {
            e.printStackTrace()
            e.message.toString()
        }
    }

    fun convert12HoursTo24Hours2(time: String): String {
        val df: DateFormat = SimpleDateFormat("HH:mm")
        var mCalendar = Calendar.getInstance()
        mCalendar.time = df.parse(time)
        return android.text.format.DateFormat.format(TIME_FORMAT_24_HOUR, mCalendar).toString()

    }

    fun isToMinuteLessOrEqual(mCalanderFrom: Calendar, mCalanderTo: Calendar, minDiffMinute: Int): Boolean {
        return mCalanderTo.get(Calendar.MINUTE) <= mCalanderFrom.get(Calendar.MINUTE) + minDiffMinute
    }

    fun isToMinuteLessOrEqual(fromTime: String, toTime: String, minDiffMinute: Int): Boolean {
        //val f: DateFormat = SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)

        val df: DateFormat = SimpleDateFormat("HH:mm")
        val calFrom = Calendar.getInstance()
        val calTo = Calendar.getInstance()
        val tmpCal = Calendar.getInstance()
        tmpCal.timeInMillis = System.currentTimeMillis()
        calFrom.time = df.parse(fromTime)
        calTo.time = df.parse(toTime)
        calFrom.set(
            tmpCal.get(Calendar.YEAR),
            tmpCal.get(Calendar.MONTH),
            tmpCal.get(Calendar.DATE)
        )
        calTo.set(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH), tmpCal.get(Calendar.DATE))
        //return calTo.get(Calendar.MINUTE) <= calFrom.get(Calendar.MINUTE) + minDiffMinute

        /*var isInvalid = false
        if (calTo.get(Calendar.HOUR) < calFrom.get(Calendar.HOUR)) {
            isInvalid = true
        } else if (calTo.get(Calendar.HOUR) == calFrom.get(Calendar.HOUR) && calTo.get(Calendar.MINUTE) <= calFrom.get(Calendar.MINUTE)) {
            isInvalid = true
        } else {
            isInvalid = false
        }
        return isInvalid*/

        return calTo.timeInMillis < calFrom.timeInMillis
    }

    fun getCurrentDateTime(outputFormat: String): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat(outputFormat)
        // you can get seconds by adding  "...:ss" to it
        date.timeZone = TimeZone.getTimeZone("GMT+1:00")
        val currentDateTime = date.format(currentLocalTime)
        return currentDateTime

    }

    /*
    *  Chat message header time format
    * */

    fun chatMsgHeaderTimeFormat(mDateTime: String): String {
        val dateFormatter = SimpleDateFormat(UTC_FORMAT)
        //LogHelper.d("test_message_local: ", getConvertedDateToLocalFromUTC(mDateTime, USER_USAGE_DATE_FORMAT))
        val msgDate = dateFormatter.parse(getConvertedDateToLocalFromUTC(mDateTime, UTC_FORMAT))
        val now = dateFormatter.parse(dateFormatter.format(System.currentTimeMillis()))
        val diff = now.time - msgDate.time
        val day = TimeUnit.MILLISECONDS.toDays(diff)
        val week = day / 7

        val dateFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT8)
        val weekFormat = SimpleDateFormat("EEEE")
        val timeFormat = SimpleDateFormat("h:mm a")

        return when {
            DateUtils.isToday(msgDate.time) -> {
                "Today"
            }

            DateUtils.isToday(msgDate.time + DateUtils.DAY_IN_MILLIS) -> {
                "Yesterday"
            }

            day > 6 -> {
                dateFormat.format(msgDate)
            }

            else -> {
                weekFormat.format(msgDate)
            }
        }
    }

    fun calculateTimeDifferenceInMinutes(time1: Long, time2: Long): Long {
        // Calculate the time difference in seconds
        val timeDifferenceSeconds = Math.abs(time1 - time2)

        // Convert the time difference to minutes
        val minutesDifference = TimeUnit.SECONDS.toMinutes(timeDifferenceSeconds)

        return minutesDifference
    }

    private fun isSameDay(currentTimeMillis: Long, timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = currentTimeMillis

        val otherCalendar = Calendar.getInstance()
        otherCalendar.timeInMillis = timestamp // Convert Unix timestamp to milliseconds

        return (currentCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.DAY_OF_YEAR) == otherCalendar.get(Calendar.DAY_OF_YEAR))
    }

    fun getChatDateTimeFormat(timestamp: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val timeZone = TimeZone.getDefault()
        val dateFormat = if (isSameDay(currentTimeMillis, timestamp)) {
            // Same day, format as 09:28 AM
            CHAT_DATE_FORMAT1
        } else {
            // Different day, format as 13-10-2023 09:28 AM
            CHAT_DATE_FORMAT2
        }

        val date = Date(timestamp * 1000) // Convert Unix timestamp to milliseconds
        val simpleDateFormat = SimpleDateFormat(dateFormat)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.format(date)
    }

    fun getConversationDateTimeFormat(mDateTime: String): String {
        try {
            val msgDate = getConvertedDateToLocalFromUTC(mDateTime, UTC_FORMAT)
            val timestamp = getMilliSecondsFromDate(msgDate, UTC_FORMAT)
            val currentTimeMillis = System.currentTimeMillis()
            val dateFormat = if (isSameDay(currentTimeMillis, timestamp)) {
                // Same day, format as 09:28 AM
                CHAT_DATE_FORMAT1
            } else {
                // Different day, format as 13-10-2023
                CHAT_DATE_FORMAT3
            }

            return getConvertedDate1(msgDate.toString(), dateFormat)
        } catch (e: Exception) {
            LogHelper.println(e)
            return ""
        }
    }

    fun getChatDateFormat(dateTime: String): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat(USER_USAGE_DATE_FORMAT8)
        // you can get seconds by adding  "...:ss" to it
        date.timeZone = TimeZone.getTimeZone("UTC")
        val currentDateTime = date.format(currentLocalTime)
        val msgDateTime = getConvertedDate(dateTime, USER_USAGE_DATE_FORMAT8)
        return if (currentDateTime == msgDateTime) {
            getConvertedDateToLocalFromUTC(dateTime, UTC_FORMAT, CHAT_DATE_FORMAT1)
        } else {
            //getConvertedDateToLocalFromUTC(dateTime, CHAT_DATE_FORMAT2)
            getConvertedDateToLocalFromUTC(
                dateTime, UTC_FORMAT,
                CHAT_DATE_FORMAT1
            ) // after display date in header display only time
        }
    }

    fun getStarredMessageDateFormat(dateTime: String): String {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat(UTC_FORMAT)
        // you can get seconds by adding  "...:ss" to it
        date.timeZone = TimeZone.getTimeZone("UTC")
        val currentDateTime = date.format(currentLocalTime)
        val msgDateTime = getConvertedDate(dateTime, USER_USAGE_DATE_FORMAT8)
        return if (currentDateTime == msgDateTime) {
            getConvertedDateToLocalFromUTC(dateTime, UTC_FORMAT, UTC_FORMAT)
        } else {
            //getConvertedDateToLocalFromUTC(dateTime, CHAT_DATE_FORMAT2)
            getConvertedDateToLocalFromUTC(
                dateTime, UTC_FORMAT,
                CHAT_DATE_FORMAT1
            ) // after display date in header display only time
        }
    }

    fun formatDateTime(inputDateString: String): Pair<String, String> {
        val inputDate = SimpleDateFormat(UTC_FORMAT, Locale.getDefault()).parse(inputDateString)
            ?: return Pair("", "")

        val currentDate = Calendar.getInstance()
        currentDate.time = Date()

        val inputCalendar = Calendar.getInstance()
        inputCalendar.time = inputDate

        return when {
            DateUtils.isToday(inputDate.time) -> {
                // Today: Return time
                Pair(
                    "Today ", convertDateFromUTC(
                        inputDateString,
                        "hh:mm a"
                    )
                )
            }

            isYesterday(currentDate, inputCalendar) -> {
                // Yesterday: Return "Yesterday" and time
                Pair("Yesterday ", convertDateFromUTC(inputDateString, "hh:mm a"))
            }

            else -> {
                // Other days: Return date and time
                val formattedDate =
                    SimpleDateFormat(USER_USAGE_DATE_FORMAT8, Locale.getDefault()).format(inputDate)
                Pair(
                    formattedDate, convertDateFromUTC(
                        inputDateString,
                        USER_USAGE_DATE_FORMAT8
                    )
                )
            }
        }
    }

    fun isYesterday(currentDate: Calendar, inputCalendar: Calendar): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.time = Date()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        return yesterday.get(Calendar.YEAR) == inputCalendar.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == inputCalendar.get(Calendar.DAY_OF_YEAR)
    }

    fun convertDateFromUTC(date: String, outputFormat: String): String {
        var localDateTime: String

        try {
            val inputFormat = SimpleDateFormat(UTC_FORMAT, Locale.US)  // utc input formate
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsed = inputFormat.parse(date)

            val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
            dateFormat.timeZone = TimeZone.getDefault()
            localDateTime = dateFormat.format(parsed)

        } catch (e: Exception) {
            e.printStackTrace()
            localDateTime = ""
        }

        return localDateTime
    }

    fun getCurrentDateTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}