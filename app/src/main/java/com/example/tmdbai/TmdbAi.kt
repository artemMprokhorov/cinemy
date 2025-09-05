package com.example.tmdbai

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tmdbai.data.di.dataModule
import com.example.tmdbai.navigation.AppNavigation
import com.example.tmdbai.presentation.di.presentationModule
import com.example.tmdbai.ui.theme.TmdbAiTheme
import com.example.tmdbai.utils.VersionUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TmdbAiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TmdbAiApplication)
            modules(dataModule, presentationModule)
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safely enable edge-to-edge display only on supported Android versions
        // This prevents UI issues on older devices that don't support this feature
        VersionUtils.safeEnableEdgeToEdge(this)

        setContent {
            TmdbAiTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    TmdbAiTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}