package ru.mipt.annstase.hw2.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.mipt.annstase.hw2.data.local.AppDatabase
import ru.mipt.annstase.hw2.data.repository.TaskRepository
import ru.mipt.annstase.hw2.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "tasks_db"
        ).build()

    @Provides
    @Singleton
    fun provideTaskRepository(db: AppDatabase): TaskRepository =
        TaskRepository.getInstance(db)

    @Provides fun provideGetTasksUseCase(
        repo: TaskRepository
    ): GetTasksUseCase =
        GetTasksUseCase(repo)

    @Provides fun provideInsertOrUpdateTaskUseCase(
        repo: TaskRepository
    ): InsertOrUpdateTaskUseCase =
        InsertOrUpdateTaskUseCase(repo)

    @Provides fun provideDeleteTaskUseCase(
        repo: TaskRepository
    ): DeleteTaskUseCase =
        DeleteTaskUseCase(repo)

    @Provides fun provideGetTaskByIdUseCase(
        repo: TaskRepository
    ): GetTaskByIdUseCase =
        GetTaskByIdUseCase(repo)
}
