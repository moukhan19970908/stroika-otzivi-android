package com.contractors.app.presentation.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.contractors.app.R
import com.contractors.app.presentation.ui.navigation.LocalLoginViewModel
import com.contractors.app.presentation.ui.navigation.LocalNavController
import com.contractors.app.presentation.ui.navigation.Screen
import com.contractors.app.presentation.ui.theme.Brand

@Composable
fun Footer(
    startId: Int = 0,
    modifier: Modifier = Modifier,
) {
    val navController = LocalNavController.current
    val loginViewModel = LocalLoginViewModel.current
    val id by remember { mutableIntStateOf(startId) }

    Row(
        modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Brand)
    ) {
        if (loginViewModel.state.token.isEmpty()) {
            FooterItem(
                modifier = Modifier.weight(1f),
                isActive = id == 0,
                iconId = R.drawable.icon_navbar,
                text = stringResource(id = R.string.start)
            ) {
                navController.navigate(Screen.Reg.name)
            }

        }
//        Главного экрана нету
//        FooterItem(Modifier.weight(1f), id == 0, R.drawable.grid, stringResource(R.string.main)) {
//            navController.navigate(Screen.Main.name)
//        }
        if (loginViewModel.state.token.isNotEmpty()) {
            FooterItem(
                Modifier.weight(1f),
                id == 1,
                R.drawable.search,
                stringResource(R.string.search)
            ) {
                navController.navigate(Screen.Search.name)
            }

        }
        FooterItem(
            modifier = Modifier.weight(1f),
            isActive = id == 2,
            iconId = R.drawable.account_circle,
            text = stringResource(if (loginViewModel.state.token.isNotEmpty()) R.string.profile else R.string.login)
        ) {
            if (loginViewModel.state.token.isNotEmpty())
                navController.navigate(Screen.Profile.name)
            else
                navController.navigate(Screen.Login.name)
        }

        if (loginViewModel.state.token.isNotEmpty() && loginViewModel.state.userInfo.userTypeId != 3)
            FooterItem(
                Modifier.weight(1f),
                isActive = id == 3,
                R.drawable.blog,
                stringResource(R.string.blog)
            ) {
                navController.navigate(Screen.Blog.name)
            }
//        if (loginViewModel.state.token.isNotEmpty() && loginViewModel.state.userInfo.userTypeId != 2)
//            FooterItem(Modifier.weight(1f), id == 2, R.drawable.objects, "Мои объекты") {
//                navController.navigate(Screen.MyObjects.name)
//            }
    }
}