package kz.adamant.data.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    private const val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
}

fun String.toDate(): Date? {
    val parser = DateConverter.dateFormatter
    parser.timeZone = DateConverter.timeZone
    return parser.parse(this)
}

fun Date.formatTo(): String {
    val formatter = DateConverter.dateFormatter
    formatter.timeZone = DateConverter.timeZone
    return formatter.format(this)
}


fun String.toDateLong(): Long? {
    val parser = DateConverter.dateFormatter
    parser.timeZone = DateConverter.timeZone
    val date = parser.parse(this)
    return date?.time
}


fun Long.formatToDate(): Date? {
    return Date(this)
}