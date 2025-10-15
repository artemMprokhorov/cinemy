# QA Testing Guide

**Cinemy - QA Testing Guide**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

## 📁 Structure Tree

```
app/src/main/java/org/studioapp/cinemy/
├── ui/components/
│   ├── ConfigurableText.kt          # Test-enabled text component
│   ├── ConfigurableMovieCard.kt    # Test-enabled movie card
│   └── SentimentAnalysisCard.kt    # Test-enabled sentiment card
├── utils/
│   └── TestUtils.kt               # QA testing utilities
├── test/
│   ├── espresso/                  # Espresso tests
│   ├── ui-automator/             # UI Automator tests
│   └── appium/                   # Appium tests
└── res/values/
    └── strings.xml               # Test strings
```

## 🔄 Flow Diagram

```
Test Execution
    ↓
Element Identification
    ↓
Test Tag/ID Resolution
    ↓
Action Execution
    ↓
Assertion Verification
    ↓
Test Result Reporting
```

## 🚀 Quick Start

```kotlin
@Composable
fun TestableMovieCard(movie: Movie) {
    ConfigurableMovieCard(
        movie = movie,
        testTag = "movie_card",
        testId = "movie_123",
        testData = mapOf("movieId" to "123")
    )
}
```

## 🏗️ Architecture Decisions

### **WHY**: Comprehensive Test Automation
- **Test Tags**: Semantic identification for reliable element targeting
- **Test IDs**: Unique identifiers for precise element selection
- **Test Data**: Contextual information for complex test scenarios
- **Framework Support**: Multi-framework compatibility for diverse testing needs

### **WHY**: Accessibility-First Testing
- **Screen Reader Support**: Full TalkBack/VoiceOver testing compatibility
- **Content Descriptions**: Descriptive text for accessibility testing
- **Semantic Roles**: Proper roles for assistive technology testing
- **Cross-Platform**: Consistent testing across different devices and orientations

## ⚙️ Configuration

### **Core Files**
- `TestUtils.kt` - QA testing utilities and test attributes
- `ConfigurableText.kt` - Test-enabled text component
- `ConfigurableMovieCard.kt` - Test-enabled movie card
- `SentimentAnalysisCard.kt` - Test-enabled sentiment analysis

### **Test Frameworks**
- `espresso/` - Android UI testing
- `ui-automator/` - Cross-app UI testing
- `appium/` - Cross-platform mobile testing
- `detox/` - React Native testing

## ❓ FAQ

**Q: How to add test tags to components?**  
A: Use `testTag` parameter in `ConfigurableText`/`ConfigurableMovieCard` components.

**Q: How to test with Espresso?**  
A: Use `onView(withTestTag("tag_name"))` and `onView(withId("test_id"))` for element selection.

**Q: How to test accessibility?**  
A: Use `contentDescription` parameter and test with screen readers enabled.

**Q: How to test on different devices?**  
A: Use `testData` parameter with device-specific attributes and test on real devices.
