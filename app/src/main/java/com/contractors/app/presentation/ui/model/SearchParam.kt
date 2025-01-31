package com.contractors.app.presentation.ui.model

import com.contractors.app.presentation.ui.navigation.Screen

data class SearchParam(
    val text: String = "",
    val title: String = "",
    val subTitle: String = "",
    val returnId: String = Screen.Profile.name,
    val clickItem: (Post) -> Unit = {},
    val back: () -> Unit = {}
)