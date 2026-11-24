package dev.ell.pillora.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    // Current date: 11 Nov 2026
    fun getCurrentDate(): String {
        return SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault(),
        ).format(Date())
    }

    // Current time: 02:30 PM
    fun getCurrentTime(): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault(),
        ).format(Date())
    }

    // Convert date into readable format
    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault(),
        ).format(Date(timestamp))
    }

    // Convert timestamp into time
    fun formatTime(timestamp: Long): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault(),
        ).format(Date(timestamp))
    }

    // Get today's date in database-friendly format
    fun getDatabaseDate(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault(),
        ).format(Date())
    }

    // Get date after or before a number of days
    fun getDateAfterDays(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)

        return SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault(),
        ).format(calendar.time)
    }

    // Check whether a timestamp belongs to today
    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val date = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }

    // Get day name
    fun getDayName(timestamp: Long): String {
        return SimpleDateFormat(
            "EEEE",
            Locale.getDefault(),
        ).format(Date(timestamp))
    }

    // Convert 24-hour time to 12-hour time
    fun convertTo12Hour(time: String): String {
        return try {
            val inputFormat = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault(),
            )

            val outputFormat = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault(),
            )

            val date = inputFormat.parse(time)
                ?: return time

            outputFormat.format(date)

        } catch (e: Exception) {
            time
        }
    }

    // Get start of today
    fun getStartOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // Get end of today
    fun getEndOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }
}
