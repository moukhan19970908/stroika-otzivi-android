package com.contractors.app.presentation.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.screen.comment.CommentBlock1
import com.contractors.app.presentation.ui.screen.comment.FinalBtn
import com.contractors.app.presentation.ui.screen.comment.GradeBlock

@Composable
fun NewProfileCommentView(success: () -> Unit) {

    var exp by remember { mutableStateOf("") }
    var ad by remember { mutableStateOf("") }
    var grade1 by remember { mutableStateOf(0) }
    var grade2 by remember { mutableStateOf(0) }

    val imgPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CommentBlock1(
            stringResource(R.string.feedbackMaster1),
            "",
            exp,
            { exp = it },
            Modifier.padding(top = 10.dp)
        )
        CommentBlock1(
            stringResource(R.string.feedbackMaster2),
            "",
            ad,
            { ad= it },
            Modifier.padding(top = 10.dp)
        )
        GradeBlock(
            R.string.feedbackMaster3,
            grade1,
            onChange = { grade1 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10
        )
        GradeBlock(
            R.string.feedbackMaster4,
            grade2,
            onChange = { grade2 = it },
            Modifier.padding(top = 10.dp),
            textSize = 10
        )

        FinalBtn(success = success)
    }

}