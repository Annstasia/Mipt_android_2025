package ru.mipt.annstase.hw2.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mipt.annstase.hw2.data.local.AppDatabase
import ru.mipt.annstase.hw2.data.local.entity.TagEntity
import ru.mipt.annstase.hw2.data.local.entity.TaskEntity
import ru.mipt.annstase.hw2.data.local.entity.TaskTagCrossRef
import ru.mipt.annstase.hw2.data.local.entity.TaskWithTags
import ru.mipt.annstase.hw2.domain.model.TaskModel

class TaskRepository private constructor(private val db: AppDatabase) {

    private val taskDao = db.taskDao()
    private val tagDao  = db.tagDao()

    fun getTasks(): LiveData<List<TaskModel>> {
        return taskDao.getAllWithTags()
            .map { list ->
                list.map { twt ->
                    TaskModel(
                        id          = twt.task.id,
                        title       = twt.task.title,
                        description = twt.task.description,
                        deadline    = twt.task.deadline,
                        urgency     = twt.task.urgency,
                        tags        = twt.tags.map { it.name },
                        done        = twt.task.done
                    )
                }
            }
    }

    fun getTaskById(taskId: Long): LiveData<TaskModel?> {
        return taskDao.getTaskWithTagsById(taskId)
            .map { twt ->
                twt?.let {
                    TaskModel(
                        id          = it.task.id,
                        title       = it.task.title,
                        description = it.task.description,
                        deadline    = it.task.deadline,
                        urgency     = it.task.urgency,
                        tags        = it.tags.map { tag -> tag.name },
                        done        = it.task.done
                    )
                }
            }
    }

    suspend fun insertOrUpdate(task: TaskModel) {
        withContext(Dispatchers.IO) {
            val entity = TaskEntity(
                id          = task.id,
                title       = task.title,
                description = task.description,
                deadline    = task.deadline,
                urgency     = task.urgency,
                done        = task.done
            )
            val newId = if (task.id == 0L) {
                taskDao.insert(entity)
            } else {
                taskDao.update(entity)
                task.id
            }

            tagDao.deleteCrossRefsForTask(newId)
            task.tags.forEach { tagName ->
                tagDao.insertTag(TagEntity(name = tagName))
                tagDao.insertCrossRef(TaskTagCrossRef(taskId = newId, tagName = tagName))
            }
        }
    }

    suspend fun delete(taskId: Long) {
        withContext(Dispatchers.IO) {
            taskDao.getById(taskId)?.let { entity ->
                taskDao.delete(entity)
            }
        }
    }

    companion object {
        @Volatile private var INSTANCE: TaskRepository? = null

        fun getInstance(db: AppDatabase): TaskRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TaskRepository(db).also { INSTANCE = it }
            }
    }
}
