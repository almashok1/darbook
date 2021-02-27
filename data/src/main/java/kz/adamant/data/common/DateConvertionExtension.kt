package kz.adamant.data.common

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val dateFormatter = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

fun String.toDate(dateFormat: String = dateFormatter, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this) ?: Calendar.getInstance().time
}

fun Date.formatTo(dateFormat: String = dateFormatter, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}