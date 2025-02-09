package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationManager
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveHabitHistoryEntryUseCaseTest {

    private lateinit var saveHabitHistoryEntryUseCase: SaveHabitHistoryEntryUseCase
    private val habitRepository: HabitRepository = mockk()
    private val notificationRepository: NotificationRepository = mockk()
    private val notificationManager: NotificationManager = mockk()
    private val notificationScheduler: NotificationScheduler = mockk()

    @BeforeEach
    fun setUp() {
        saveHabitHistoryEntryUseCase = SaveHabitHistoryEntryUseCase(
            habitRepository,
            notificationRepository,
            notificationManager,
            notificationScheduler
        )
    }

    @Test
    fun `should add history entry and schedule notification when effort conditions are met`() = runTest {
        // Arrange
        val notificationTime = LocalTime(17, 0)
        val habitId = 1L
        val historyEntry = HabitHistoryEntry(date = LocalDate(2025, 2, 9), currentEffort = 10f)
        val habit = Habit(
            id = 0,
            name = "Test",
            desirableDays = listOf(Day.MONDAY, Day.TUESDAY),
            notification = HabitNotificationSetting.Enabled(time = notificationTime),
            icon = CardIcon.RUN,
            color = CardColor.RED,
            description = "Test habit",
            effort = HabitDesiredEffort(EffortUnit.MINUTES, 10f),
            additionDate = LocalDate(2025, 2, 9)
        )
        val notification = HabitNotification(
            dateTime = LocalDateTime(LocalDate(2025, 2, 9), notificationTime),
            habit = habit.copy(1)
        )

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)
        coEvery { habitRepository.addHabitHistoryEntry(habitId, historyEntry) } just Runs
        coEvery { notificationManager.calculateNextNotificationForHabit(habit) } returns notification
        coEvery { notificationRepository.addOrUpdateHabitNotification(notification) } just Runs
        coEvery { notificationScheduler.scheduleNotification(notification) } just Runs

        // Act
        val result = saveHabitHistoryEntryUseCase.execute(SaveHabitHistoryEntryUseCase.Request(habitId, historyEntry))

        // Assert
        coVerify { habitRepository.getHabit(habitId) }
        coVerify { habitRepository.addHabitHistoryEntry(habitId, historyEntry) }
        coVerify { notificationManager.calculateNextNotificationForHabit(habit) }
        coVerify { notificationRepository.addOrUpdateHabitNotification(notification) }
        coVerify { notificationScheduler.scheduleNotification(notification) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should add history entry but not schedule notification when effort conditions are not met`() = runTest {
        // Arrange
        val notificationTime = LocalTime(17, 0)
        val habitId = 1L
        val historyEntry = HabitHistoryEntry(date = LocalDate(2025, 2, 9), currentEffort = 5f)
        val habit = Habit(
            id = 0,
            name = "Test",
            desirableDays = listOf(Day.MONDAY, Day.TUESDAY),
            notification = HabitNotificationSetting.Enabled(time = notificationTime),
            icon = CardIcon.RUN,
            color = CardColor.RED,
            description = "Test habit",
            effort = HabitDesiredEffort(EffortUnit.MINUTES, 10f),
            additionDate = LocalDate(2025, 2, 9)
        )

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)
        coEvery { habitRepository.addHabitHistoryEntry(habitId, historyEntry) } just Runs

        // Act
        val result = saveHabitHistoryEntryUseCase.execute(SaveHabitHistoryEntryUseCase.Request(habitId, historyEntry))

        // Assert
        coVerify { habitRepository.getHabit(habitId) }
        coVerify { habitRepository.addHabitHistoryEntry(habitId, historyEntry) }
        coVerify(exactly = 0) { notificationManager.calculateNextNotificationForHabit(any()) }
        coVerify(exactly = 0) { notificationRepository.addOrUpdateHabitNotification(any()) }
        coVerify(exactly = 0) { notificationScheduler.scheduleNotification(any()) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should add history entry but not schedule notification when notification is disabled`() = runTest {
        // Arrange
        val notificationTime = LocalTime(17, 0)
        val habitId = 1L
        val historyEntry = HabitHistoryEntry(date = LocalDate(2025, 2, 9), currentEffort = 5f)
        val habit = Habit(
            id = 0,
            name = "Test",
            desirableDays = listOf(Day.MONDAY, Day.TUESDAY),
            notification = HabitNotificationSetting.Disabled,
            icon = CardIcon.RUN,
            color = CardColor.RED,
            description = "Test habit",
            effort = HabitDesiredEffort(EffortUnit.MINUTES, 10f),
            additionDate = LocalDate(2025, 2, 9)
        )

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(habit)
        coEvery { habitRepository.addHabitHistoryEntry(habitId, historyEntry) } just Runs

        // Act
        val result = saveHabitHistoryEntryUseCase.execute(SaveHabitHistoryEntryUseCase.Request(habitId, historyEntry))

        // Assert
        coVerify { habitRepository.getHabit(habitId) }
        coVerify { habitRepository.addHabitHistoryEntry(habitId, historyEntry) }
        coVerify(exactly = 0) { notificationManager.calculateNextNotificationForHabit(any()) }
        coVerify(exactly = 0) { notificationRepository.addOrUpdateHabitNotification(any()) }
        coVerify(exactly = 0) { notificationScheduler.scheduleNotification(any()) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should return error when habit is not found`() = runTest {
        // Arrange
        val habitId = 1L
        val historyEntry = HabitHistoryEntry(date = LocalDate(2025, 2, 9), currentEffort = 5f)

        coEvery { habitRepository.getHabit(habitId) } returns flowOf(null)

        // Act
        val result = saveHabitHistoryEntryUseCase.execute(SaveHabitHistoryEntryUseCase.Request(habitId, historyEntry))

        // Assert
        coVerify { habitRepository.getHabit(habitId) }
        assertInstanceOf(Result.Error::class.java, result)
        assertEquals(BaseError.READ_ERROR, (result as Result.Error).error)
    }

    @Test
    fun `should return error when repository throws exception`() = runTest {
        // Arrange
        val habitId = 1L
        val historyEntry = HabitHistoryEntry(date = LocalDate(2025, 2, 9), currentEffort = 5f)
        val exception = RuntimeException("Database error")

        coEvery { habitRepository.getHabit(habitId) } throws exception

        // Act
        val result = saveHabitHistoryEntryUseCase.execute(SaveHabitHistoryEntryUseCase.Request(habitId, historyEntry))

        // Assert
        coVerify { habitRepository.getHabit(habitId) }
        assertInstanceOf(Result.Error::class.java, result)
        assertEquals(BaseError.SAVE_ERROR, (result as Result.Error).error)
    }
}