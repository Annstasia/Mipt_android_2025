package ru.mipt.annstase.hw2.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mipt.annstase.hw2.data.local.entity.TagEntity
import ru.mipt.annstase.hw2.data.local.entity.TagWithTasks
import ru.mipt.annstase.hw2.data.local.entity.TaskTagCrossRef

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: TaskTagCrossRef)

    @Query("DELETE FROM task_tag_cross_ref WHERE taskId = :taskId")
    suspend fun deleteCrossRefsForTask(taskId: Long)

    @Transaction
    @Query("SELECT * FROM tags")
    fun getTagsWithTasks(): Flow<List<TagWithTasks>>
}
