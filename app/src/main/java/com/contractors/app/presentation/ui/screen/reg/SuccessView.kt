package com.contractors.app.presentation.ui.screen.reg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.elements.CircularProgressBar
import com.contractors.app.presentation.ui.elements.components.DefButton
import com.contractors.app.presentation.ui.elements.components.DefImage
import com.contractors.app.presentation.ui.elements.components.DefText
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.theme.Gray

@Composable
fun SuccessReg() {

    val navController = LocalNavController.current

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefImage(R.drawable.check, Modifier.size(100.dp))
        DefText(stringResource(R.string.successReg), weight = FontWeight.Bold)
        DefButton(
            stringResource(R.string.toMain),
            modifier = Modifier.padding(top = 30.dp)
        ) { navController.navigate(Screen.Profile.name) }
    }
}

@Composable
fun SuccessPay(
    toStatus: () -> Unit,
    toMain: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefImage(R.drawable.check, Modifier.size(100.dp))
        DefText(stringResource(R.string.successPay), weight = FontWeight.Bold)
        DefButton(
            stringResource(R.string.payStatus),
            modifier = Modifier.padding(top = 30.dp)
        ) { toStatus() }
        DefButton(
            stringResource(R.string.toMain),
            modifier = Modifier.padding(top = 10.dp)
        ) { toMain() }
    }
}

@Composable
fun PayStatus(
    updatePage: () -> Unit,
    toMain: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressBar(
            totalSegments = 8,
            strokeWidth = 30f,
            radius = 100f
        )

        DefText(
            stringResource(R.string.inProcess),
            weight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        Row(
            Modifier
                .padding(horizontal = 50.dp, vertical = 50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                DefText(stringResource(R.string.payDate), weight = FontWeight.Bold, size = 12)
                DefText("31 Mar - 1 Apr 2024", size = 12)
                DefText(stringResource(R.string.payTarget), weight = FontWeight.Bold, size = 12)
                DefText("Оплата доступа", size = 12, color = Gray)
            }
        }
        DefButton(
            stringResource(R.string.updatePage),
            modifier = Modifier.padding(top = 100.dp)
        ) { updatePage() }
        DefButton(
            stringResource(R.string.toMain),
            modifier = Modifier.padding(top = 10.dp)
        ) { toMain() }
    }
}

@Composable
fun SuccessStatus(
    toMain: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefImage(R.drawable.check, Modifier.size(100.dp))
        DefText(stringResource(R.string.createFeedbackSuccess), weight = FontWeight.Bold)
        DefButton(
            stringResource(R.string.toMain),
            modifier = Modifier.padding(top = 100.dp)
        ) { toMain() }
    }
}