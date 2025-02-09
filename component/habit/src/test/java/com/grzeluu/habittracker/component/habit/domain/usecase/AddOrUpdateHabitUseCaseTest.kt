package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.component.habit.domain.error.HabitValidationError
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
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
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddOrUpdateHabitUseCaseTest {

    private lateinit var useCase: AddOrUpdateHabitUseCase
    private val habitRepository: HabitRepository = mockk()
    private val notificationRepository: NotificationRepository = mockk()
    private val notificationManager: NotificationManager = mockk()
    private val notificationScheduler: NotificationScheduler = mockk()

    @BeforeEach
    fun setUp() {
        useCase = AddOrUpdateHabitUseCase(
            habitRepository,
            notificationRepository,
            notificationManager,
            notificationScheduler
        )
    }

    @Test
    fun `should return error when name is empty`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { habit.name } returns ""

        // Act
        val result = useCase.execute(habit)

        // Assert
        assertEquals(Result.Error<BaseError>(HabitValidationError.EMPTY_NAME), result)
    }

    @Test
    fun `should return error when desirableDays is empty`() = runTest {
        // Arrange
        val habit = mockk<Habit>()
        every { habit.name } returns "Test"
        every { habit.desirableDays } returns emptyList()

        // Act
        val result = useCase.execute(habit)

        // Assert
        assertEquals(Result.Error<BaseError>(HabitValidationError.EMPTY_DAYS), result)
    }

    @Test
    fun `should save habit and not schedule notification when notification is disabled`() = runTest {
        // Arrange
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
        coEvery { habitRepository.addOrUpdateHabit(habit) } returns 1

        // Act
        val result = useCase.execute(habit)

        // Assert
        coVerify { habitRepository.addOrUpdateHabit(habit) }
        coVerify(exactly = 0) { notificationManager.calculateNextNotificationForHabit(any()) }
        coVerify(exactly = 0) { notificationRepository.addOrUpdateHabitNotification(any()) }
        coVerify(exactly = 0) { notificationScheduler.scheduleNotification(any()) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should save habit and schedule notification when notification is enabled`() = runTest {
        // Arrange
        val notificationTime = LocalTime(17, 0)
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
        val savedHabit = habit.copy(id = 1)
        val notification = HabitNotification(
            dateTime = LocalDateTime(LocalDate(2025, 2, 9), notificationTime),
            habit = habit.copy(1)
        )

        coEvery { habitRepository.addOrUpdateHabit(habit) } returns 1
        coEvery { notificationManager.calculateNextNotificationForHabit(savedHabit) } returns notification
        coEvery { notificationRepository.addOrUpdateHabitNotification(notification) } just Runs
        coEvery { notificationScheduler.scheduleNotification(notification) } just Runs

        // Act
        val result = useCase.execute(habit)

        // Assert
        coVerify { habitRepository.addOrUpdateHabit(habit) }
        coVerify { notificationManager.calculateNextNotificationForHabit(savedHabit) }
        coVerify { notificationRepository.addOrUpdateHabitNotification(notification) }
        coVerify { notificationScheduler.scheduleNotification(notification) }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `should return save error when exception occurs`() = runTest {
        // Arrange
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
        coEvery { habitRepository.addOrUpdateHabit(habit) } throws RuntimeException("Database error")

        // Act
        val result = useCase.execute(habit)

        // Assert
        assertEquals(Result.Error<BaseError>(BaseError.SAVE_ERROR), result)
    }
}
