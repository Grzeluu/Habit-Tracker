package com.grzeluu.habittracker.common.ui.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormDropdown(
    modifier: Modifier = Modifier,
    selectedItem: T,
    options: List<T>,
    getTextValue: @Composable (T) -> String,
    onSelectOption: (T) -> Unit,
    label: String? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = getTextValue(selectedItem),
            readOnly = true,
            label = label,
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painterResource(
                        id = when (isExpanded) {
                            true -> R.drawable.ic_arrow_up
                            else -> R.drawable.ic_arrow_down
                        }
                    ),
                    contentDescription = null
                )
            },
            maxLines = 1
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = getTextValue(option)) },
                    onClick = {
                        isExpanded = !isExpanded
                        onSelectOption(option)
                    }
                )
            }
        }
    }
}