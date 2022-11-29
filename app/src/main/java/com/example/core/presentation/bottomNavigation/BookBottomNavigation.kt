package com.example.core.presentation.bottomNavigation

import android.annotation.SuppressLint
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core.utils.BottomItems

@SuppressLint("UnrememberedMutableState")
@Composable
fun BooksBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomItems.SEARCH_BOOK,
        BottomItems.SAVED_BOOKS
    )



    BottomNavigation(
        backgroundColor = Color.Gray,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->

            BottomNavigationItem(
                selected = currentRoute == item.route,
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title, fontSize = 9.sp)},
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Blue.copy(0.4f),
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route){
                                saveState = true
                            }

                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })

        }

    }
}