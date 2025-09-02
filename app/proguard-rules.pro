# TmdbAi ProGuard Rules
# Comprehensive obfuscation and optimization rules for production builds

# =============================================================================
# BASIC CONFIGURATION
# =============================================================================

# Keep source file names for better crash reports
-keepattributes SourceFile,LineNumberTable

# Keep generic signatures for better reflection support
-keepattributes Signature

# Keep annotations for runtime processing
-keepattributes *Annotation*

# Keep inner classes for Compose
-keepattributes InnerClasses

# Keep synthetic methods for Compose
-keepattributes Synthetic

# =============================================================================
# ANDROID FRAMEWORK
# =============================================================================

# Keep Android framework classes
-keep class android.** { *; }
-keep interface android.** { *; }

# Keep Android support libraries
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Keep Android lifecycle components
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# =============================================================================
# JETPACK COMPOSE
# =============================================================================

# Keep Compose runtime classes
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.animation.** { *; }

# Keep Compose compiler generated classes
-keep class androidx.compose.compiler.** { *; }

# Keep Compose preview classes
-keep class androidx.compose.ui.tooling.preview.** { *; }

# =============================================================================
# KOIN DEPENDENCY INJECTION
# =============================================================================

# Keep Koin classes
-keep class org.koin.** { *; }
-keep interface org.koin.** { *; }

# Keep Koin modules
-keep class * extends org.koin.core.module.Module { *; }

# Keep Koin component interfaces
-keep interface org.koin.core.component.KoinComponent { *; }

# =============================================================================
# KTOR NETWORKING
# =============================================================================

# Keep Ktor client classes
-keep class io.ktor.** { *; }
-keep interface io.ktor.** { *; }

# Keep Ktor serialization
-keep class io.ktor.serialization.** { *; }
-keep class io.ktor.serialization.kotlinx.** { *; }

# Keep Ktor content negotiation
-keep class io.ktor.client.plugins.contentnegotiation.** { *; }

# =============================================================================
# KOTLIN SERIALIZATION
# =============================================================================

# Keep Kotlin serialization classes
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }

# Keep serializable classes
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Keep serialization annotations
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# =============================================================================
# COIL IMAGE LOADING
# =============================================================================

# Keep Coil classes
-keep class coil.** { *; }
-keep interface coil.** { *; }

# Keep Coil image loaders
-keep class coil.ImageLoader { *; }
-keep class coil.request.ImageRequest { *; }

# =============================================================================
# PAGING
# =============================================================================

# Keep Paging classes
-keep class androidx.paging.** { *; }
-keep interface androidx.paging.** { *; }

# Keep Paging data sources
-keep class * extends androidx.paging.PagingSource { *; }
-keep class * extends androidx.paging.Pager { *; }

# =============================================================================
# DATASTORE
# =============================================================================

# Keep DataStore classes
-keep class androidx.datastore.** { *; }
-keep interface androidx.datastore.** { *; }

# Keep DataStore preferences
-keep class androidx.datastore.preferences.** { *; }

# =============================================================================
# TIMBER LOGGING
# =============================================================================

# Keep Timber classes
-keep class timber.log.** { *; }

# Keep Timber tags
-keepclassmembers class * {
    @timber.log.TimberTag *;
}

# =============================================================================
# ACCOMPANIST
# =============================================================================

# Keep Accompanist classes
-keep class com.google.accompanist.** { *; }
-keep interface com.google.accompanist.** { *; }

# =============================================================================
# COROUTINES
# =============================================================================

# Keep Coroutines classes
-keep class kotlinx.coroutines.** { *; }
-keep interface kotlinx.coroutines.** { *; }

# Keep Coroutines flow
-keep class kotlinx.coroutines.flow.** { *; }

# =============================================================================
# KOTLIN DATETIME
# =============================================================================

# Keep Kotlin datetime classes
-keep class kotlinx.datetime.** { *; }
-keep interface kotlinx.datetime.** { *; }

# =============================================================================
# PROJECT SPECIFIC CLASSES
# =============================================================================

# Keep your app's main classes
-keep class com.example.tmdbai.MainActivity { *; }
-keep class com.example.tmdbai.TmdbAiApplication { *; }

# Keep ViewModels
-keep class com.example.tmdbai.presentation.**.ViewModel { *; }
-keep class com.example.tmdbai.presentation.**.ViewModel$* { *; }

# Keep data models
-keep class com.example.tmdbai.data.model.** { *; }
-keep class com.example.tmdbai.data.remote.dto.** { *; }

# Keep repositories
-keep class com.example.tmdbai.data.repository.** { *; }

# Keep API services
-keep class com.example.tmdbai.data.remote.api.** { *; }

# Keep MCP client
-keep class com.example.tmdbai.data.mcp.** { *; }

# Keep mappers
-keep class com.example.tmdbai.data.mapper.** { *; }

# Keep navigation
-keep class com.example.tmdbai.navigation.** { *; }

# Keep UI components
-keep class com.example.tmdbai.ui.** { *; }

# Keep utilities
-keep class com.example.tmdbai.utils.** { *; }

# =============================================================================
# SERIALIZATION AND REFLECTION
# =============================================================================

# Keep classes that use reflection
-keepclassmembers class * {
    @kotlin.jvm.JvmStatic *;
}

# Keep companion objects
-keepclassmembers class * {
    static final *;
}

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# =============================================================================
# WEBVIEW (if used)
# =============================================================================

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# =============================================================================
# OPTIMIZATION RULES
# =============================================================================

# Remove unused code
-dontwarn **
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Remove Timber logging in release builds
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# =============================================================================
# KEEP RULES FOR EXTERNAL LIBRARIES
# =============================================================================

# Keep Gson classes if used
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# Keep Retrofit classes if used
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Keep OkHttp classes if used
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# =============================================================================
# DEBUGGING AND TESTING
# =============================================================================

# Keep test classes in debug builds
-keep class *Test { *; }
-keep class *Tests { *; }

# Keep test runner classes
-keep class androidx.test.** { *; }
-keep class junit.** { *; }

# =============================================================================
# FINAL NOTES
# =============================================================================

# These rules provide comprehensive protection for your TmdbAi app
# while maintaining compatibility with all dependencies
# 
# If you encounter any issues with specific features, you may need to
# add additional -keep rules for those specific classes
# 
# Test thoroughly in release builds to ensure all functionality works
# correctly after obfuscation