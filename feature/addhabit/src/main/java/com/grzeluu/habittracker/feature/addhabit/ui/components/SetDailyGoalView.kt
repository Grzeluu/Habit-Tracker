package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.dropdown.FormDropdown
import com.grzeluu.habittracker.common.ui.mapper.MappingType
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.util.enums.EffortUnit

@Composable
fun SetDailyGoalView(
    goalTextState: String,
    onTextChanged: (String) -> Unit,
    selectedEffortUnit: EffortUnit,
    onChangeEffortUnit: (EffortUnit) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextField(
            modifier = Modifier.weight(0.5f),
            value = goalTextState,
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
            withClearTextOption = false,
            onValueChange = {
                if (it.isEmpty() || it.toFloatOrNull() != null) onTextChanged(it)
            },
            alignment = TextAlign.End
        )
        Spacer(modifier = Modifier.width(12.dp))
        FormDropdown(
            modifier = Modifier.weight(0.5f),
            selectedItem = selectedEffortUnit,
            options = EffortUnit.entries,
            getTextValue = {
                it.mapToUiText(
                    if (goalTextState.toFloatOrNull() == 1.0f) MappingType.SINGULAR else MappingType.PLURAL
                ).asString() ?: ""
            },
            onSelectOption = onChangeEffortUnit

        )
    }
}