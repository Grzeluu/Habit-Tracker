package com.grzeluu.habittracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HabitTrackerApp @Inject constructor() : Application() {

}