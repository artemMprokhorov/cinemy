# Adaptive ML Runtime System

**Layer**: ML Runtime  
**Purpose**: Intelligent ML runtime selection based on device capabilities  
**Files**: `ml/adaptive/`, `ml/sentiment/`  
**Status**: ✅ Production Ready

## Structure

```
ml/
├── adaptive/
│   ├── AdaptiveMLRuntime.kt          # Main runtime selector
│   ├── HardwareDetection.kt          # Device capability detection
│   └── MLRuntime.kt                  # Runtime type definitions
├── sentiment/
│   ├── SentimentAnalyzer.kt          # Sentiment analysis interface
│   ├── TensorFlowSentimentModel.kt   # TensorFlow Lite implementation
│   └── KeywordSentimentModel.kt      # Fallback implementation
└── models/
    ├── production_sentiment_full_manual.tflite
    └── vocab.json
```

## Flow Diagram

```
Device Startup
    ↓
HardwareDetection.detectCapabilities()
    ↓
AdaptiveMLRuntime.selectOptimalRuntime()
    ↓
┌─────────────────────────────────────┐
│ Runtime Selection Priority:         │
│ 1. LiteRT + GPU (best)             │
│ 2. LiteRT + NPU                    │
│ 3. TensorFlow Lite + GPU           │
│ 4. TensorFlow Lite + NNAPI         │
│ 5. TensorFlow Lite CPU             │
│ 6. Keyword Fallback (last resort)  │
└─────────────────────────────────────┘
    ↓
SentimentAnalyzer.initialize()
    ↓
Ready for sentiment analysis
```

## Quick Start

```kotlin
// Initialize in Application.onCreate()
val analyzer = SentimentAnalyzer.getInstance(this)
analyzer.initialize()

// Analyze sentiment with optimal runtime
val result = analyzer.analyzeSentiment("Great movie!")
```

## Architecture Decisions

### Why Adaptive Runtime Selection?
- **Device Diversity**: Android devices vary significantly in ML capabilities
- **Performance Optimization**: Hardware acceleration can provide 4x speedup
- **Reliability**: Fallback mechanisms ensure functionality on all devices

### Why This Priority Order?
- **LiteRT + GPU**: Best performance, requires Play Services
- **TensorFlow Lite**: Universal compatibility, no Play Services dependency
- **Keyword Fallback**: Ensures functionality on low-end devices

### Why Performance Scoring?
- **Quantified Selection**: Objective runtime selection criteria
- **Hardware Awareness**: Considers GPU, NNAPI, XNNPACK capabilities
- **Future-Proof**: Adapts to new hardware capabilities

## Configuration

### Core Files
- `AdaptiveMLRuntime.kt` - Runtime selection logic
- `HardwareDetection.kt` - Device capability detection
- `SentimentAnalyzer.kt` - Main interface
- `HybridSystemConfig.kt` - Configuration options

### Model Files
- `production_sentiment_full_manual.tflite` - TensorFlow Lite model
- `vocab.json` - Vocabulary for keyword fallback
- `android_integration_config.json` - Model configuration

## FAQ

**Q: Why not use ML Kit directly?**  
A: ML Kit has limited customization. Our system provides fine-grained control over runtime selection and fallback mechanisms.

**Q: How does performance scoring work?**  
A: See `HardwareDetection.calculatePerformanceScore()` - combines GPU, NNAPI, XNNPACK, and LiteRT capabilities.

**Q: What if no hardware acceleration is available?**  
A: System falls back to CPU inference, then keyword-based analysis as last resort.

**Q: How to debug runtime selection?**  
A: Use `SentimentAnalyzer.getRuntimeInfo()` and `getDeviceInfo()` for detailed diagnostics.
