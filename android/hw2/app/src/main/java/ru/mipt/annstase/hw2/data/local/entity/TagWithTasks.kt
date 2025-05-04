package ru.mipt.annstase.hw2.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


class TagWithTasks(
    @Embedded val tag: TagEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "id",
        associateBy = Junction(
            value = TaskTagCrossRef::class,
            parentColumn = "tagName",
            entityColumn = "taskId"
        )
    )
    val tasks: List<TaskEntity>
)
