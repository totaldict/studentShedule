package com.example.studentshedule.util.rest

import com.example.studentshedule.model.CLesson
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface IServerAPITemplate {
    @GET("/lessons")
    suspend fun getAllLessons(): List<CLesson>

    @POST("/lessons")
    suspend fun saveLesson(@Body lesson: CLesson)

    @DELETE("/lessons")
    suspend fun deleteLesson(@Query(value="id") id: UUID)
}