package com.grzeluu.habittracker.activity.ui.state

import com.grzeluu.habittracker.component.settings.domain.model.Settings

data class MainActivityStateData(
    val settings: Settings?,
    val isSettingsDefinedOnAppStart: Boolean
)