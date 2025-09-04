# Basic Android rules
-keep public class * extends android.app.Application
-keep public class * extends androidx.activity.ComponentActivity

# Keep data models  
-keep class com.example.tmdbai.data.model.** { *; }
-keep class com.example.tmdbai.data.remote.dto.** { *; }

# Gson
-keepattributes Signature
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# SLF4J logging
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *; }

# Ktor and networking
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Remove debug logs in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Compose
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }