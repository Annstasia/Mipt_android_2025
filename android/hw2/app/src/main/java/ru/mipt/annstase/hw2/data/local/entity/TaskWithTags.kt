package ru.mipt.annstase.hw2.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class TaskWithTags(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(
            value = TaskTagCrossRef::class,
            parentColumn = "taskId",
            entityColumn = "tagName"
        )
    )
    val tags: List<TagEntity>
)
