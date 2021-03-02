package kz.adamant.data.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    const val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
}

fun String.toDate(): Date {
    val parser = DateConverter.dateFormatter
    parser.timeZone = DateConverter.timeZone
    return parser.parse(this) ?: Calendar.getInstance().time
}

fun Date.formatTo(): String {
    val formatter = DateConverter.dateFormatter
    formatter.timeZone = DateConverter.timeZone
    return formatter.format(this)
}