package com.example.studentshedule.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentshedule.model.CLesson
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface IDAOLessons {
    // suspend - если хотим один раз выполнить
    @Query("SELECT * FROM lessons")
    suspend fun getAll(): List<CLesson>

    // Flow - с поддержкой отслеживания изменений
    @Query("SELECT * FROM lessons WHERE subject not like \"\"")
    fun getAllFlow(): Flow<List<CLesson>>

    @Query("SELECT * FROM lessons WHERE id = :id1")
    suspend fun findById(id1: UUID): CLesson?

    @Insert
    suspend fun insert(lesson: CLesson)

    @Update
    suspend fun update(lesson: CLesson)

    @Delete
    suspend fun delete(lesson: CLesson)
}