package com.tubetoast.envelopes.ui.presentation.ui.views

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tubetoast.envelopes.common.domain.models.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(label: String) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis
        ?.let { Date.fromMillis(it) }
        ?.toString()
        .orEmpty()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (showDatePicker) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun DatePickerFieldToModal(
    label: String,
    selectedDate: Date?,
    modifier: Modifier = Modifier,
    onDateSelected: (Date?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        readOnly = true,
        value = selectedDate?.toString().orEmpty(),
        onValueChange = { },
        label = { Text(label) },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = onDateSelected
        ) {
            showModal = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    selectedDate: Date? = null, // todo set start value
    onDateSelected: (Date?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis?.let { Date.fromMillis(it) })
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
