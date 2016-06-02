package com.szymkowski.personaltrainercompanion.core

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

object DateFormatter {

    @JvmStatic
    fun getDateFormatter(pattern : String) : DateTimeFormatter {
        return DateTimeFormat.forPattern(pattern).withLocale(Locale.getDefault())
    }
}