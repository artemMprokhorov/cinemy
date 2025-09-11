# Utils Package

This package contains utility classes and helper functions for the Cinemy Android application.

## VersionUtils

The `VersionUtils` class provides safe handling of Android version-specific features, particularly
for the `enableEdgeToEdge()` function.

### Problem Solved

The `enableEdgeToEdge()` function was introduced in Android 14 (API level 34) and can cause UI
issues or crashes when called on older Android versions. This utility provides a safe way to enable
edge-to-edge display only on supported devices.

### Key Features

#### ✅ **Safe Edge-to-Edge Support**

- Only enables edge-to-edge on Android 14+ (API 34+)
- Prevents UI issues on older devices
- Includes exception handling for partial support scenarios

#### ✅ **Version Constants**

- Readable constants for Android versions
- Easy to understand and maintain
- Centralized version checking logic

#### ✅ **Utility Functions**

- `safeEnableEdgeToEdge()`: Safely enables edge-to-edge display
- `supportsEdgeToEdge()`: Checks if device supports the feature
- `getAndroidVersionName()`: Human-readable version names
- `getApiLevel()`: Current API level

### Usage Example

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safely enable edge-to-edge display only on supported Android versions
        // This prevents UI issues on older devices that don't support this feature
        VersionUtils.safeEnableEdgeToEdge(this)

        setContent {
            // Your app content
        }
    }
}
```

### Version Support Matrix

| Android Version | API Level | Edge-to-Edge Support |
|-----------------|-----------|----------------------|
| Android 14+     | 34+       | ✅ Supported          |
| Android 13      | 33        | ❌ Not Supported      |
| Android 12      | 32        | ❌ Not Supported      |
| Android 11      | 30        | ❌ Not Supported      |
| Android 10      | 29        | ❌ Not Supported      |

### Benefits

1. **Crash Prevention**: Prevents app crashes on unsupported devices
2. **UI Consistency**: Maintains consistent UI behavior across Android versions
3. **Future-Proof**: Easy to update when new Android versions are released
4. **Maintainable**: Centralized version checking logic
5. **Reusable**: Can be used across multiple activities

### Implementation Details

The utility uses:

- **Build.VERSION.SDK_INT**: To check the current Android API level
- **Build.VERSION_CODES.UPSIDE_DOWN_CAKE**: Android 14 constant (API 34)
- **Exception Handling**: `runCatching` with `onFailure` for idiomatic Kotlin error handling
- **Extension Function**: Proper usage of `enableEdgeToEdge()` extension function

### Error Handling

The utility includes comprehensive error handling:

- **Version Check**: Only calls `enableEdgeToEdge()` on supported versions
- **Exception Handling**: Uses `runCatching` with `onFailure` for idiomatic Kotlin error handling
- **Graceful Fallback**: Continues app execution even if edge-to-edge fails

This ensures the app remains stable and functional across all supported Android versions.
