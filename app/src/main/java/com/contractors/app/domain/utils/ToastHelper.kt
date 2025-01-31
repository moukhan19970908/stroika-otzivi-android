package com.contractors.app.domain.utils

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToastHelper(private val snackbarHostState: SnackbarHostState) {

    fun show(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(text, duration = SnackbarDuration.Short)
        }
    }
}