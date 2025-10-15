# Accessibility Guide

**Cinemy - Accessibility Guide**  
**Last Updated**: 2025-01-27  
**Version**: 3.0.0

## 📁 Structure Tree

```
app/src/main/java/org/studioapp/cinemy/
├── ui/components/
│   ├── ConfigurableText.kt          # Accessible text component
│   ├── ConfigurableMovieCard.kt    # Accessible movie card
│   ├── SentimentAnalysisCard.kt    # Accessible sentiment card
│   └── AdaptiveLayout.kt           # Accessible adaptive layout
├── utils/
│   └── TestUtils.kt               # QA testing utilities
└── res/values/
    └── strings.xml                # Accessibility strings
```

## 🔄 Flow Diagram

```
User Interaction
    ↓
Screen Reader Detection
    ↓
Content Description Application
    ↓
Semantic Role Assignment
    ↓
Focus Management
    ↓
State Announcement
    ↓
Accessible UI Rendering
```

## 🚀 Quick Start

```kotlin
@Composable
fun AccessibleMovieCard(movie: Movie) {
    ConfigurableMovieCard(
        movie = movie,
        contentDescription = "Movie: ${movie.title}, Rating: ${movie.rating}",
        testTag = "movie_card"
    )
}
```

## 🏗️ Architecture Decisions

### **WHY**: Accessibility-First Design
- **Screen Reader Support**: Full TalkBack/VoiceOver compatibility for inclusive UX
- **Semantic Roles**: Proper roles ensure assistive technology understanding
- **Content Descriptions**: Descriptive text enables non-visual navigation
- **Focus Management**: Keyboard navigation support for accessibility compliance

### **WHY**: WCAG 2.1 AA Compliance
- **Perceivable**: Multi-sensory content delivery
- **Operable**: Multiple input method support
- **Understandable**: Clear interaction patterns
- **Robust**: Assistive technology compatibility

## ⚙️ Configuration

### **Core Files**
- `ConfigurableText.kt` - Accessible text component
- `ConfigurableMovieCard.kt` - Accessible movie card
- `SentimentAnalysisCard.kt` - Accessible sentiment analysis
- `AdaptiveLayout.kt` - Device-adaptive accessibility
- `TestUtils.kt` - QA testing support

### **Resources**
- `strings.xml` - Accessibility strings
- `themes.xml` - Accessibility themes

## ❓ FAQ

**Q: How to add accessibility to new components?**  
A: Use `ConfigurableText`/`ConfigurableMovieCard` with `contentDescription` parameter.

**Q: How to test accessibility?**  
A: Use `testTag()` and `testId()` parameters with Espresso/UI Automator.

**Q: How to support screen readers?**  
A: Set `contentDescription` and use `semantics` modifier for proper announcements.

**Q: How to handle different device types?**  
A: Use `AdaptiveLayout` component with device-specific accessibility configurations.
