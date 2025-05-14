package ru.mipt.annstase.hw2.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.mipt.annstase.hw2.data.local.entity.TaskEntity
import ru.mipt.annstase.hw2.data.local.entity.TaskWithTags

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks")
    fun getAllWithTags(): LiveData<List<TaskWithTags>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    fun getTaskWithTagsById(taskId: Long): LiveData<TaskWithTags?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getById(taskId: Long): TaskEntity?
}
