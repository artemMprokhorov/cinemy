package org.studioapp.cinemy

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.studioapp.cinemy.data.di.dataModule
import org.studioapp.cinemy.di.mlModule
import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.studioapp.cinemy.navigation.AppNavigation
import org.studioapp.cinemy.presentation.di.presentationModule
import org.studioapp.cinemy.ui.theme.CinemyTheme
import org.studioapp.cinemy.utils.VersionUtils

class CinemyApplication : Application() {

    private val sentimentAnalyzer: SentimentAnalyzer by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CinemyApplication)
            modules(dataModule, presentationModule, mlModule)
        }

        // Initialize ML analyzer
        CoroutineScope(Dispatchers.IO).launch {
            sentimentAnalyzer.initialize()
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
            CinemyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    CinemyTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}