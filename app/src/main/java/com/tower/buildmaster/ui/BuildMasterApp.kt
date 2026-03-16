package com.tower.buildmaster.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tower.buildmaster.navigation.BuildMasterTheme
import com.tower.buildmaster.navigation.NavigationGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BuildMasterApp() {
    val navController = rememberNavController()
    BuildMasterTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            NavigationGraph(
                navController = navController
            )

        }
    }
}