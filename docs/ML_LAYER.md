# ML Layer Implementation

Machine Learning layer for Cinemy Android application providing AI-powered sentiment analysis with hybrid model support.

## 📁 Structure

```
ml/
├── model/                                  # Data classes and models
│   ├── SentimentResult.kt                 # Sentiment analysis result → см. код
│   ├── ModelInfo.kt                       # Model information → см. код
│   ├── KeywordSentimentModel.kt           # Keyword-based model → см. код
│   ├── AlgorithmConfig.kt                 # Algorithm configuration → см. код
│   ├── ContextBoosters.kt                # Context boosters → см. код
│   ├── EnhancedModelData.kt               # Enhanced model data → см. код
│   ├── ProductionModelData.kt             # Production model data → см. код
│   └── TensorFlowConfig.kt                # TensorFlow configuration → см. код
├── mlfactory/                             # Factory classes for ML components
│   ├── KeywordFactory.kt                  # Multilingual keyword creation → см. код
│   ├── ContextBoostersFactory.kt         # Context boosters factory → см. код
│   ├── IntensityModifiersFactory.kt      # Intensity modifiers factory → см. код
│   ├── Algorithm.kt                       # Algorithm configuration factory → см. код
│   └── SimpleKeywordModelFactory.kt      # Simple keyword model factory → см. код
├── mltools/                               # ML utility classes
│   └── HardwareDetection.kt               # Hardware capability detection → см. код
├── mlmodels/                              # ML model implementations
│   ├── LiteRTSentimentModel.kt            # LiteRT implementation → см. код
│   └── TensorFlowSentimentModel.kt       # TensorFlow Lite model → см. код
├── di/                                    # ML dependency injection
│   └── MLModule.kt                        # ML module configuration → см. код
├── SentimentAnalyzer.kt                   # Main hybrid sentiment analyzer → см. код
├── AdaptiveMLRuntime.kt                   # Adaptive ML runtime selector → см. код
└── MLConstants.kt                         # Centralized ML constants → см. код
```

## 🔧 Key Components

### SentimentAnalyzer
- **Роль**: Main hybrid sentiment analyzer with TensorFlow + Keyword fallback
- **Файл**: `app/src/main/java/org/studioapp/cinemy/ml/SentimentAnalyzer.kt`
- **API**: см. KDoc комментарии в коде
- **Тесты**: `app/src/test/java/org/studioapp/cinemy/ml/SentimentAnalyzerTest.kt`

### AdaptiveMLRuntime
- **Роль**: Adaptive ML runtime selector for optimal performance
- **Файл**: `app/src/main/java/org/studioapp/cinemy/ml/AdaptiveMLRuntime.kt`
- **API**: см. KDoc комментарии в коде
- **Тесты**: `app/src/test/java/org/studioapp/cinemy/ml/HybridSystemCompatibilityTest.kt`

### TensorFlowSentimentModel
- **Роль**: TensorFlow Lite model implementation for sentiment analysis
- **Файл**: `app/src/main/java/org/studioapp/cinemy/ml/mlmodels/TensorFlowSentimentModel.kt`
- **API**: см. KDoc комментарии в коде
- **Тесты**: `app/src/test/java/org/studioapp/cinemy/ml/TensorFlowIntegrationTest.kt`

### LiteRTSentimentModel
- **Роль**: LiteRT model implementation for lightweight sentiment analysis
- **Файл**: `app/src/main/java/org/studioapp/cinemy/ml/mlmodels/LiteRTSentimentModel.kt`
- **API**: см. KDoc комментарии в коде

### KeywordFactory
- **Роль**: Multilingual keyword creation and management
- **Файл**: `app/src/main/java/org/studioapp/cinemy/ml/mlfactory/KeywordFactory.kt`
- **API**: см. KDoc комментарии в коде

## 🚀 Quick Start

```kotlin
// Initialize SentimentAnalyzer
val analyzer = SentimentAnalyzer.getInstance(context)
analyzer.initialize()

// Analyze sentiment
val result = analyzer.analyzeSentiment("This movie is amazing!")
println("Sentiment: ${result.sentiment}, Confidence: ${result.confidence}")
```

**Для детальных примеров см. тесты в `app/src/test/java/org/studioapp/cinemy/ml/`**

## 🏗️ Architectural Decisions

### Hybrid System
- **TensorFlow + Keyword fallback**: TensorFlow for accuracy, keyword model for reliability
- **Adaptive Runtime**: Automatic selection between TensorFlow Lite and LiteRT based on device capabilities
- **Fallback Strategy**: Seamless fallback to keyword model when TensorFlow fails

### Adaptive Runtime Logic
- **Hardware Detection**: `HardwareDetection.kt` analyzes device capabilities
- **Performance Optimization**: Runtime selection based on CPU, memory, and ML acceleration support
- **Graceful Degradation**: Automatic fallback to simpler models on low-end devices

### Model Configuration
- **Production Models**: `ProductionModelData.kt` for multilingual keyword models
- **Enhanced Models**: `EnhancedModelData.kt` for advanced sentiment analysis
- **TensorFlow Config**: `TensorFlowConfig.kt` for model-specific settings

## ⚙️ Configuration

### Model Files
- **Production Model**: `app/src/main/assets/mock_movie_details.json`
- **Enhanced Model**: `app/src/main/assets/mock_movies.json`
- **TensorFlow Model**: `app/src/main/assets/ml_models/production_sentiment_full_manual.tflite`

### Build Variants
- **dummy**: Mock data for testing
- **prod**: Real backend integration

### Dependency Injection
- **MLModule**: `app/src/main/java/org/studioapp/cinemy/ml/di/MLModule.kt`
- **Configuration**: See Koin module setup in `MLModule.kt`

## 📚 Documentation

- **API Reference**: All methods documented with KDoc comments in source code
- **Test Examples**: Comprehensive test cases in `app/src/test/java/org/studioapp/cinemy/ml/`
- **Integration Guide**: See `docs/ADAPTIVE_ML_RUNTIME.md` for detailed runtime configuration

## 🔍 Troubleshooting

| Issue | Solution | File |
|-------|----------|------|
| Model loading fails | Check asset files and TensorFlow configuration | `TensorFlowConfig.kt` |
| Runtime selection issues | Verify hardware detection | `HardwareDetection.kt` |
| Fallback not working | Check keyword model initialization | `KeywordFactory.kt` |
| Performance issues | Review adaptive runtime logic | `AdaptiveMLRuntime.kt` |

---

**Note**: This documentation provides navigation and architecture overview. For detailed API reference, see KDoc comments in source code files.