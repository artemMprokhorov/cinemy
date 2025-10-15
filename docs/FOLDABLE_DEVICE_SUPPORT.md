# Foldable Device Support

**Cinemy - Foldable Device Support**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

## 📁 Structure Tree

```
app/src/main/java/org/studioapp/cinemy/
├── utils/
│   └── DeviceUtils.kt              # Device type detection
├── ui/components/
│   ├── AdaptiveLayout.kt          # Adaptive layout components
│   └── FoldableInsets.kt          # Window insets handling
├── res/
│   ├── values-sw600dp/            # Large screen resources
│   ├── values-w600dp/             # Wide screen resources
│   └── values-land/               # Landscape resources
└── AndroidManifest.xml            # Foldable device configuration
```

## 🔄 Flow Diagram

```
Device Detection
    ↓
Screen Size Analysis
    ↓
Device Type Classification
    ↓
Layout Strategy Selection
    ↓
Dual Pane / Single Pane
    ↓
Window Insets Application
    ↓
Adaptive UI Rendering
```

## 🚀 Quick Start

```kotlin
@Composable
fun MoviesScreen() {
    AdaptiveLayout(
        leftPane = { MoviesList() },
        rightPane = { MovieDetails() }
    )
}
```

## 🏗️ Architecture Decisions

### **WHY**: Foldable-First Design
- **Device Detection**: Automatic classification for optimal UX across device types
- **Dual Pane Layout**: Efficient content consumption on large screens
- **Configuration Changes**: Seamless transitions between folded/unfolded states
- **Window Insets**: Proper content positioning on foldable devices

### **WHY**: Adaptive Layout Strategy
- **Single Pane**: Phone-optimized navigation for small screens
- **Dual Pane**: Tablet-optimized content for large screens
- **Flexible Sizing**: Dynamic pane sizing based on device capabilities
- **Content Optimization**: Device-specific content density and spacing

## ⚙️ Configuration

### **Core Files**
- `DeviceUtils.kt` - Device type detection and classification
- `AdaptiveLayout.kt` - Dual pane and single pane layouts
- `FoldableInsets.kt` - Window insets handling
- `MainActivity.kt` - Configuration change handling

### **Resources**
- `values-sw600dp/` - Large screen resources
- `values-w600dp/` - Wide screen resources
- `values-land/` - Landscape resources
- `AndroidManifest.xml` - Foldable device configuration

## ❓ FAQ

**Q: How to detect foldable devices?**  
A: Use `DeviceUtils.getDeviceType()` and `DeviceUtils.isFoldableDevice()`.

**Q: How to implement dual pane layout?**  
A: Use `AdaptiveLayout` component with `leftPane` and `rightPane` parameters.

**Q: How to handle configuration changes?**  
A: Override `onConfigurationChanged()` in MainActivity and use `DeviceUtils` for detection.

**Q: How to handle window insets?**  
A: Use `FoldableInsets.kt` utilities with `foldableWindowInsetsPadding()` modifier.
