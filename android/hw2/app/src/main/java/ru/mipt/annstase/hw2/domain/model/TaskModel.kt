package ru.mipt.annstase.hw2.domain.model

data class TaskModel(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: Long,
    val urgency: Int,
    val tags: List<String>,
    val done: Boolean
)