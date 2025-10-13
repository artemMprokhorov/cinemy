# Adaptive ML Runtime System

## Overview

The Adaptive ML Runtime System is an intelligent ML runtime selection framework that automatically chooses the optimal ML runtime based on device hardware capabilities. This system provides the best possible performance for sentiment analysis while maintaining reliability through fallback mechanisms.

## Architecture

### Core Components

1. **HardwareDetection** - Detects device hardware capabilities
2. **AdaptiveMLRuntime** - Selects and manages optimal ML runtime
3. **SentimentAnalyzer** - Integrates adaptive runtime for sentiment analysis

### Runtime Selection Priority

The system follows this priority order for runtime selection:

1. **LiteRT with GPU** - Best performance, requires Play Services
2. **LiteRT with NPU** - Good performance, requires Play Services  
3. **TensorFlow Lite with GPU** - Good performance, no Play Services dependency
4. **TensorFlow Lite with NNAPI** - Decent performance
5. **TensorFlow Lite CPU** - Basic performance
6. **Keyword Fallback** - Last resort

## Hardware Detection

### Capabilities Detected

- **GPU Support** - GPU acceleration for ML inference
- **NNAPI Support** - Neural Networks API acceleration
- **XNNPACK Support** - CPU optimization library
- **LiteRT Support** - Google's ML runtime via Play Services
- **Play Services** - Google Play Services availability

### Performance Scoring

The system calculates a performance score (0-100) based on:

- Base CPU performance: 10 points
- GPU acceleration: +30 points
- NNAPI acceleration: +25 points
- XNNPACK optimization: +15 points
- LiteRT with hardware: +40/+35 points
- Play Services: +5 points

## Usage

### Basic Usage

```kotlin
// Initialize adaptive runtime
val adaptiveRuntime = AdaptiveMLRuntime.getInstance(context)
adaptiveRuntime.initialize()

// Analyze sentiment
val result = adaptiveRuntime.analyzeSentiment("This movie is amazing!")
```

### Integration with SentimentAnalyzer

```kotlin
// SentimentAnalyzer automatically uses adaptive runtime
val analyzer = SentimentAnalyzer.getInstance(context)
analyzer.initialize()

// Analyze sentiment with optimal runtime
val result = analyzer.analyzeSentiment("Great film!")
```

### Runtime Information

```kotlin
// Get current runtime info
val runtimeInfo = analyzer.getRuntimeInfo()
println(runtimeInfo)

// Get performance recommendations
val recommendations = analyzer.getPerformanceRecommendations()
recommendations.forEach { println(it) }

// Check if setup is optimal
val isOptimal = analyzer.isOptimalForSentimentAnalysis()
```

## Performance Expectations

### Expected Inference Times

- **LiteRT with GPU**: ~50ms
- **LiteRT with NPU**: ~80ms
- **TensorFlow Lite with GPU**: ~70ms
- **TensorFlow Lite with NNAPI**: ~100ms
- **TensorFlow Lite CPU**: ~200ms
- **Keyword Fallback**: ~10ms (lower accuracy)

### Performance Recommendations

The system provides actionable recommendations:

- ✅ **Optimal**: Using LiteRT with GPU acceleration
- ✅ **Good**: Using TensorFlow Lite with hardware acceleration
- ⚠️ **Basic**: Using CPU-only inference
- ❌ **Fallback**: Using keyword-based model

## Hardware Requirements

### Minimum Requirements

- Android API 24+
- 2GB RAM
- CPU with XNNPACK support

### Recommended Requirements

- Android API 28+
- 4GB RAM
- GPU with OpenGL ES 3.1+
- Google Play Services
- NPU support (optional)

## Configuration

### Hybrid System Configuration

The system uses `HybridSystemConfig` for configuration:

```kotlin
data class HybridSystemConfig(
    val enableAdaptiveRuntime: Boolean = true,
    val enableHardwareDetection: Boolean = true,
    val performanceThreshold: Int = 50,
    val fallbackEnabled: Boolean = true
)
```

### Runtime Selection Logic

```kotlin
when {
    hasLiteRT && hasGpu && hasPlayServices -> MLRuntime.LITERT_GPU
    hasLiteRT && hasNnapi && hasPlayServices -> MLRuntime.LITERT_NPU
    hasGpu -> MLRuntime.TENSORFLOW_LITE_GPU
    hasNnapi -> MLRuntime.TENSORFLOW_LITE_NNAPI
    hasXnnpack -> MLRuntime.TENSORFLOW_LITE_CPU
    else -> MLRuntime.KEYWORD_FALLBACK
}
```

## Error Handling

### Fallback Mechanisms

1. **Primary**: Adaptive runtime (LiteRT or TensorFlow Lite)
2. **Secondary**: TensorFlow Lite model
3. **Tertiary**: Keyword-based model
4. **Error**: Returns error result

### Error Recovery

```kotlin
try {
    val result = adaptiveRuntime.analyzeSentiment(text)
    if (result.isSuccess) {
        return result
    }
} catch (e: Exception) {
    // Fallback to TensorFlow Lite
    return tensorFlowModel.analyzeSentiment(text)
}
```

## Debugging

### Device Information

```kotlin
val deviceInfo = analyzer.getDeviceInfo()
println(deviceInfo)
```

Output:
```
=== Device Information ===
Device: Google Pixel 6
Android: 13 (API 33)
Architecture: arm64-v8a, armeabi-v7a

=== Hardware Capabilities ===
GPU Support: true
NNAPI Support: true
XNNPACK Support: true
LiteRT Support: true
Play Services: true

=== ML Runtime Selection ===
Recommended Runtime: LITERT_GPU
Performance Score: 95/100
```

### Runtime Information

```kotlin
val runtimeInfo = analyzer.getRuntimeInfo()
println(runtimeInfo)
```

Output:
```
=== Current ML Runtime ===
Runtime: LITERT_GPU
Performance Score: 95/100

=== Hardware Capabilities ===
GPU: true
NNAPI: true
XNNPACK: true
LiteRT: true
Play Services: true

=== Performance Expectations ===
Expected inference time: ~50ms
```

## Best Practices

### Initialization

```kotlin
// Initialize in Application.onCreate()
class Cinemy : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize adaptive ML runtime
        val analyzer = SentimentAnalyzer.getInstance(this)
        analyzer.initialize()
    }
}
```

### Resource Management

```kotlin
// Clean up resources when done
analyzer.cleanup()
```

### Performance Monitoring

```kotlin
// Monitor performance
if (!analyzer.isOptimalForSentimentAnalysis()) {
    val recommendations = analyzer.getPerformanceRecommendations()
    // Log recommendations for optimization
}
```

## Troubleshooting

### Common Issues

1. **Low Performance Score**
   - Check GPU support
   - Verify Play Services installation
   - Consider device upgrade

2. **Runtime Selection Issues**
   - Verify hardware detection
   - Check model availability
   - Review fallback configuration

3. **Memory Issues**
   - Ensure proper cleanup
   - Monitor memory usage
   - Use WeakReference for singletons

### Debug Commands

```kotlin
// Check hardware capabilities
val capabilities = HardwareDetection.getInstance(context).detectHardwareCapabilities()

// Test runtime selection
val runtime = AdaptiveMLRuntime.getInstance(context)
runtime.initialize()

// Verify model availability
val isReady = analyzer.isInitialized
```

## Future Enhancements

### Planned Features

1. **Dynamic Runtime Switching** - Switch runtimes based on workload
2. **Performance Profiling** - Detailed performance metrics
3. **Model Optimization** - Automatic model optimization
4. **Hardware-Specific Tuning** - Device-specific optimizations

### Integration Opportunities

1. **ML Kit Integration** - Google ML Kit support
2. **Custom Models** - User-defined model support
3. **Cloud Fallback** - Cloud-based inference fallback
4. **Real-time Optimization** - Dynamic performance tuning

## Conclusion

The Adaptive ML Runtime System provides intelligent ML runtime selection that automatically optimizes performance based on device capabilities. This ensures the best possible sentiment analysis performance while maintaining reliability through comprehensive fallback mechanisms.

The system is designed to be transparent to the application layer, providing a unified interface regardless of the underlying ML implementation. This makes it easy to integrate and maintain while providing optimal performance across a wide range of Android devices.
