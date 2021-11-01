package com.example.studentshedule.util.rest

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class CConverterUUID {
    @FromJson
    fun fromJson(value: String): UUID {
        return UUID.fromString(value)
    }

    @ToJson
    fun toJson(value: UUID): String {
        return value.toString()
    }
}