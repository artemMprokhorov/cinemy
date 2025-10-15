# Utils Layer

Device detection, version handling, and helper utilities for cross-platform compatibility.

## Structure

```
utils/
├── ColorUtils.kt            # Color parsing utilities for Compose
├── DeviceUtils.kt           # Device type detection and foldable support
└── VersionUtils.kt          # Android version-specific feature handling
```

## Flow Diagram

```
Context → DeviceUtils → DeviceType → Layout Decision
    ↓
VersionUtils → API Check → Safe Feature Access
    ↓
ColorUtils → Compose Color → UI Rendering
```

## Key Components

### ColorUtils
- Role: Color string parsing utilities for Compose Color objects
- File: `app/src/main/java/org/studioapp/cinemy/utils/ColorUtils.kt`
- API: см. KDoc в коде

### DeviceUtils
- Role: Device type detection and foldable device support
- File: `app/src/main/java/org/studioapp/cinemy/utils/DeviceUtils.kt`
- API: см. KDoc в коде

### VersionUtils
- Role: Android version-specific feature handling with safe API access
- File: `app/src/main/java/org/studioapp/cinemy/utils/VersionUtils.kt`
- API: см. KDoc в коде

## Quick Start

```kotlin
@Composable
fun AdaptiveLayout() {
    val deviceType = getDeviceType()
    val supportsDual = supportsDualPane()
    val spacing = getOptimalSpacing()
    // Use device-specific logic
}
```

## Architecture Decisions

### Device Detection Strategy
- **WHY**: Different devices need different layouts and spacing
- **DECISION**: Centralized device type detection with caching
- **BENEFIT**: Consistent behavior across the app

### Version-Safe API Access
- **WHY**: Prevent crashes on older Android versions
- **DECISION**: Version checking before API calls
- **BENEFIT**: Backward compatibility without crashes

### Foldable Device Support
- **WHY**: Foldable devices have unique layout requirements
- **DECISION**: Aspect ratio and screen size detection
- **BENEFIT**: Optimal experience on foldable devices

### Composable Integration
- **WHY**: Reactive UI updates on device changes
- **DECISION**: Composable extensions with LocalContext
- **BENEFIT**: Automatic UI updates on configuration changes

## Configuration

### Files
- `ColorUtils.kt` - Color parsing utilities
- `DeviceUtils.kt` - Device detection and foldable support
- `VersionUtils.kt` - Version-specific feature handling

### Dependencies
- Android Context (device information)
- WindowManager (display metrics)
- Compose Runtime (composable extensions)

## FAQ

**Q: How to detect foldable devices?**
A: см. `DeviceUtils.isFoldableDevice()`

**Q: How to safely use version-specific APIs?**
A: см. `VersionUtils.supportsEdgeToEdge()`

**Q: How to get device-specific spacing?**
A: см. `DeviceUtils.getOptimalSpacing()`

**Q: How to parse colors in Compose?**
A: см. `ColorUtils.parseColor()`
