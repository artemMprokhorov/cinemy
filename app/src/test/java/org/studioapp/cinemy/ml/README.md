# ML Tests for Cinemy

This directory contains comprehensive tests for the Machine Learning components in Cinemy, including
the hybrid sentiment analysis system with TensorFlow Lite integration.

## Test Files

### Core Tests

- **`SentimentAnalyzerTest.kt`** - Tests for the original keyword-based sentiment analysis model
- **`TensorFlowIntegrationTest.kt`** - Comprehensive tests for TensorFlow Lite integration and
  hybrid system
- **`HybridSystemCompatibilityTest.kt`** - Backward compatibility tests ensuring existing
  functionality works
- **`TestUtils.kt`** - Utility functions and mock objects for testing

## Test Coverage

### TensorFlowIntegrationTest.kt

- ✅ TensorFlow Lite model initialization
- ✅ Hybrid system initialization
- ✅ Model selection logic (simple vs complex text)
- ✅ Fallback mechanisms
- ✅ Batch analysis performance
- ✅ Error handling and recovery
- ✅ Resource cleanup
- ✅ Concurrent access safety

### HybridSystemCompatibilityTest.kt

- ✅ Backward compatibility with existing API
- ✅ Batch analysis compatibility
- ✅ Model info compatibility
- ✅ Error handling compatibility
- ✅ Performance characteristics
- ✅ Singleton pattern compatibility
- ✅ Edge cases handling

### TestUtils.kt

- ✅ Mock Context creation with assets
- ✅ Test data for different complexity levels
- ✅ Performance measurement utilities
- ✅ Result validation helpers
- ✅ Model selection logic testing

## Running Tests

### Run All ML Tests

```bash
./gradlew test --tests "org.studioapp.cinemy.ml.*"
```

### Run Specific Test Classes

```bash
# TensorFlow integration tests
./gradlew test --tests "org.studioapp.cinemy.ml.TensorFlowIntegrationTest"

# Compatibility tests
./gradlew test --tests "org.studioapp.cinemy.ml.HybridSystemCompatibilityTest"

# Original sentiment analyzer tests
./gradlew test --tests "org.studioapp.cinemy.ml.SentimentAnalyzerTest"
```

### Run Individual Test Methods

```bash
./gradlew test --tests "org.studioapp.cinemy.ml.TensorFlowIntegrationTest.testTensorFlowModelInitialization"
```

## Test Data

### Text Complexity Levels

**Simple Texts** (use keyword model):

- "This movie is amazing and fantastic!"
- "This movie is terrible and awful."
- "This movie is okay and decent."

**Complex Texts** (use TensorFlow model):

- Long reviews with complex sentence structures
- Texts with ambiguous sentiment indicators
- Reviews with multiple conflicting opinions

**Ambiguous Texts**:

- "This movie is interesting and different, but I'm not sure if I liked it or not."

## Mock Objects

### MockContext

- Provides mock Android Context for testing
- Includes mock AssetManager with predefined assets
- Supports both successful and failing scenarios

### Test Assets

- `production_sentiment_full_manual.tflite` - Production TensorFlow Lite model
- `android_integration_config.json` - Configuration for hybrid system
- `multilingual_sentiment_production.json` - Production keyword model data

## Performance Testing

Tests include performance measurements to ensure:

- Analysis completes within reasonable time (< 1 second)
- Batch processing is efficient
- Memory usage is controlled
- Resource cleanup works properly

## Error Scenarios

Tests cover various error scenarios:

- Asset loading failures
- TensorFlow model initialization errors
- Network connectivity issues (for future MCP integration)
- Invalid input data
- Resource exhaustion

## Dependencies

Tests use the following testing frameworks:

- **JUnit 4** - Basic test framework
- **MockK** - Mocking framework for Kotlin
- **Kotlin Coroutines** - For async testing
- **TensorFlow Lite** - For ML model testing

## Best Practices

1. **Isolation**: Each test is independent and doesn't affect others
2. **Mocking**: Use mock objects to avoid external dependencies
3. **Performance**: Include performance assertions where relevant
4. **Error Handling**: Test both success and failure scenarios
5. **Documentation**: Clear test names and comments explain what's being tested

## Future Enhancements

- Add integration tests with real TensorFlow Lite models
- Include performance benchmarking tests
- Add memory usage monitoring
- Create stress tests for high-load scenarios
- Add tests for model versioning and updates
