package com.grzeluu.habittracker.common.ui.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    withClearTextOption: Boolean = true,
    onValueChange: (String) -> Unit,
    label: String? = null,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    alignment: TextAlign = TextAlign.Start,
    trailingIcon: @Composable (() -> Unit)? = null
) {

    val textFieldTrailingIcon: @Composable (() -> Unit)? = when {
        trailingIcon != null -> trailingIcon
        withClearTextOption && value.isNotEmpty() -> {
            @Composable {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }
        }

        else -> null
    }

    Column(modifier = modifier) {
        label?.let { BasicLabel(text = label) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            isError = isError,
            supportingText = supportingText,
            readOnly = readOnly,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = alignment),
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            singleLine = maxLines == 1,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            trailingIcon = textFieldTrailingIcon
        )
    }
}