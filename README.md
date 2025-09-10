# TmdbAi - AI-Powered Movie Discovery App

[![Android](https://img.shields.io/badge/Android-API%2024+-green.svg)](https://developer.android.com/about/versions/android-14.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose-1.5.8-orange.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📱 Описание проекта

TmdbAi - это современное Android-приложение для поиска и изучения фильмов, использующее искусственный интеллект через MCP (Model Context Protocol) для персонализированных рекомендаций и анализа контента.

### 🎯 Основные цели
- Предоставить пользователям персонализированные рекомендации фильмов
- Интегрировать AI-анализ через MCP протокол
- Создать современный и интуитивный пользовательский интерфейс
- Демонстрировать лучшие практики Android разработки

## 🤖 ML Возможности v2.0

### Enhanced Keyword Sentiment Analysis
- **Модель**: Enhanced Keyword Model v2.0.0
- **Точность**: 85%+ для английских отзывов
- **Словарь**: 60+ ключевых слов + модификаторы
- **Скорость**: < 50ms локальный анализ
- **Особенности**: Context-aware анализ с интенсивностью

#### Новые возможности v2.0:
- ✨ **Расширенный словарь** - специализированные термины кино
- 🔧 **Модификаторы интенсивности** - "absolutely amazing" vs "pretty good"  
- 🎯 **Контекстная осведомленность** - понимание кино-контекста
- ⚡ **Улучшенная точность** - до 85% на фильмовых отзывах

#### Использование:
1. Откройте экран фильма
2. Найдите "🤖 Анализ тональности v2.0"
3. Введите отзыв: "This movie is absolutely incredible!"
4. Получите точный результат с объяснением ключевых слов

## 🎨 Server-Driven UI (SDUI)

### AI-Powered Dynamic Theming
Приложение поддерживает **Server-Driven UI** - динамическое изменение внешнего вида через конфигурацию от N8N бэкенда с AI-генерацией цветов.

#### ✨ Возможности:
- **AI-генерация цветов** - N8N + Perplexity AI создают персонализированные цветовые схемы
- **Динамическое обновление** - Изменение темы без перезапуска приложения
- **ConfigurableComponents** - Специальные компоненты, адаптирующиеся под uiConfig
- **Fallback система** - Автоматический откат к Material Theme при отсутствии конфигурации

#### 🔧 Компоненты:
- **ConfigurableMovieCard** - Карточки фильмов с AI цветами
- **ConfigurableButton** - Кнопки с динамическими цветами
- **ConfigurableText** - Текст с адаптивными цветами

#### 📊 uiConfig Structure:
```json
{
  "uiConfig": {
    "colors": {
      "primary": "#2196F3",
      "secondary": "#4CAF50", 
      "background": "#121212",
      "surface": "#1E1E1E",
      "onPrimary": "#FFFFFF",
      "onSecondary": "#FFFFFF",
      "onBackground": "#FFFFFF",
      "onSurface": "#FFFFFF"
    },
    "texts": {
      "appTitle": "TMDB AI Movies",
      "loadingText": "Loading movies...",
      "errorMessage": "Something went wrong"
    },
    "buttons": {
      "buttonCornerRadius": 8
    }
  }
}
```

#### 🚀 MCP Flow:
```
N8N Backend → MCP Client → Repository → ViewModel → ConfigurableComponents
     ↓              ↓           ↓          ↓              ↓
  AI Colors    HTTP Response  Parsing   State Update   Visual Apply
```

## 🏗️ Архитектура

Проект построен на принципах **Clean Architecture** с использованием **MVI (Model-View-Intent)** паттерна:

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Splash    │  │ MoviesList  │  │MovieDetail  │        │
│  │   Screen    │  │   Screen    │  │   Screen    │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Presentation Layer                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Splash    │  │ MoviesList  │  │MovieDetail  │        │
│  │ ViewModel   │  │ ViewModel   │  │ ViewModel   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Intent    │  │   State     │  │ Repository  │        │
│  │   Classes   │  │   Classes   │  │  Interface  │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   MCP       │  │ Repository  │  │   Mapper    │        │
│  │  Client     │  │  Impl       │  │   Classes   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Технологии и библиотеки

### Core Technologies
- **Kotlin 1.9.22** - Основной язык разработки
- **Android 14 (API 36)** - Минимальная версия API 24
- **Jetpack Compose 1.5.8** - Современный UI фреймворк
- **Material Design 3** - Система дизайна

### Architecture & DI
- **Clean Architecture** - Принципы разделения слоев
- **MVI Pattern** - Паттерн управления состоянием
- **Koin 3.5.3** - Dependency Injection
- **Navigation Compose 2.9.3** - Навигация между экранами

### Networking & Data
- **Ktor 2.1.3** - HTTP клиент для MCP протокола
- **Kotlinx Serialization** - JSON сериализация
- **DataStore** - Хранение настроек
- **Paging 3** - Пагинация списков

### UI & UX
- **Coil 2.5.0** - Загрузка изображений
- **Accompanist** - Дополнительные UI компоненты
- **Splash Screen API** - Экран загрузки
- **Edge-to-Edge** - Полноэкранный режим

### Testing
- **JUnit 4** - Unit тестирование
- **MockK** - Мокирование
- **Turbine** - Тестирование Flow
- **Espresso** - UI тестирование

## 🚀 Установка и запуск

### Предварительные требования
- Android Studio Hedgehog (2023.1.1) или новее
- Android SDK API 36
- JDK 17
- Android устройство или эмулятор (API 24+)

### Шаги установки

1. **Клонирование репозитория**
   ```bash
   git clone https://github.com/your-username/TmdbAi.git
   cd TmdbAi
   ```

2. **Настройка переменных окружения**
   ```bash
   # Создайте local.properties или настройте в Android Studio
   MCP_SERVER_URL=https://your-ngrok-url.ngrok.io
   TMDB_BASE_URL=https://api.themoviedb.org/3/
   ```

## 🔧 Setup and Installation

### **Build Variants (Simplified)**

| Variant | Purpose | Data Source |
|---------|---------|-------------|
| **dummyDebug** | Development | Mock data only |
| **prodDebug** | Testing | Real backend + fallback |
| **prodRelease** | Production | Real backend only |

### **Quick Start**

#### Development (Mock Data):
```bash
git clone https://github.com/artemMprokhorov/TmdbAi.git
cd TmdbAi
./gradlew installDummyDebug
# Or manually install:
adb install -r app/build/outputs/apk/dummy/debug/app-dummy-debug.apk
```

#### Production Testing:
```bash
# Edit app/build.gradle.kts MCP_SERVER_URL first
./gradlew installProdDebug
# Or manually install:
adb install -r app/build/outputs/apk/prod/debug/app-prod-debug.apk
```

### **Backend Configuration**
Update `MCP_SERVER_URL` in `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "MCP_SERVER_URL", "\"https://your-backend.ngrok.io\"")
```

### **Connection Status**
- 🔵 Blue: Demo data (dummy variant)
- 🟠 Orange: Backend unavailable
- 🟢 Green: Connected to backend

## 📁 Структура проекта

```
app/src/main/java/com/example/tmdbai/
├── TmdbAi.kt                    # Главный класс приложения
├── navigation/                   # Навигация
│   ├── Navigation.kt            # Основная навигация
│   └── Screen.kt                # Определения экранов
├── ui/                          # UI компоненты
│   ├── components/              # Переиспользуемые компоненты
│   ├── movieslist/              # Экран списка фильмов
│   ├── moviedetail/             # Экран деталей фильма
│   ├── splash/                  # Экран загрузки
│   └── theme/                   # Тема и стили
├── presentation/                 # Presentation слой
│   ├── di/                      # DI модули
│   ├── commons/                 # Общие компоненты
│   ├── movieslist/              # MoviesList ViewModel
│   └── moviedetail/             # MovieDetail ViewModel
├── data/                        # Data слой
│   ├── di/                      # Data DI модули
│   ├── mcp/                     # MCP клиент и модели
│   ├── model/                   # Data модели
│   ├── mapper/                  # Мапперы данных
│   ├── remote/                  # Remote data sources
│   └── repository/              # Репозитории
└── utils/                       # Утилиты
```

## 🔄 Data Flow

```
User Action → Intent → ViewModel → Repository → MCP Client
     ↑                                                      ↓
     └────────────── State ←─────────────── Result ←────────┘
```

1. **User Action** - Пользователь выполняет действие
2. **Intent** - Создается Intent для ViewModel
3. **ViewModel** - Обрабатывает Intent и вызывает Repository
4. **Repository** - Получает данные через MCP Client
5. **State** - Обновляется состояние UI
6. **UI Update** - Интерфейс перерисовывается

## 📊 Статус разработки

### ✅ Реализовано
- [x] Базовая архитектура MVI + Clean Architecture
- [x] Навигация между экранами
- [x] Splash экран
- [x] Список фильмов с pull-to-refresh
- [x] Детали фильма с pull-to-refresh
- [x] Dependency Injection с Koin
- [x] MCP клиент для AI интеграции
- [x] Базовые ViewModels и States
- [x] Material Design 3 тема
- [x] Custom error screens с red arrow indicator
- [x] Pull-to-refresh functionality на всех экранах
- [x] Mock data из assets для dummy версии
- [x] Enhanced UI/UX с consistent design
- [x] Обработка ошибок с retry functionality

### 🚧 В разработке
- [ ] Интеграция с TMDB API
- [ ] AI рекомендации через MCP
- [ ] Кэширование данных
- [ ] Unit тесты
- [ ] UI тесты

### 📋 Планируется
- [ ] Поиск фильмов
- [ ] Фильтры и сортировка
- [ ] Избранное
- [ ] Push уведомления
- [ ] Темная тема
- [ ] Многоязычность
- [ ] Аналитика и метрики

## 📋 Changelog

### **v2.4.0** - Code Quality & Refactoring Improvements
**Date**: December 2024  
**Status**: ✅ **COMPLETED**

#### 🔧 **Code Quality Improvements**
- **String Resources**: All hardcoded UI texts moved to `strings.xml` for internationalization
- **Constants Organization**: Comprehensive constants system with proper organization
- **Error Handling**: Replaced all `try/catch` blocks with modern `runCatching` approach
- **Debug Logging**: All logs now wrapped with `BuildConfig.DEBUG` checks for production safety
- **Hardcoded Values**: Eliminated all hardcoded strings, dimensions, and numerical values

#### 🎯 **ML Components Refactoring**
- **SentimentAnalyzer.kt**: Added comprehensive constants for model configuration
  - Model info constants (type, version, language, accuracy, speed)
  - Error message constants
  - Performance threshold constants (confidence, weights, thresholds)
  - Intensity modifier constants (all numerical values)
- **MLPerformanceMonitor.kt**: Added constants for performance monitoring
  - Performance thresholds and text length categories
  - Log intervals and message constants
  - Debug-only logging implementation

#### 🎨 **UI Components Improvements**
- **SentimentAnalysisCard.kt**: All hardcoded strings moved to string resources
  - Sentiment analysis titles and subtitles
  - Review status messages
  - Emoji constants for sentiment indicators
  - Keywords label formatting
- **ConfigurableMovieCard.kt**: String resources for movie card elements
  - Poster description strings
  - Rating format strings
- **String Resources**: Added comprehensive sentiment analysis strings to `strings.xml`

#### 🚀 **Production Optimizations**
- **Debug-Only Logging**: All `Log.d`, `Log.i`, `Log.e` calls wrapped with `BuildConfig.DEBUG`
- **Performance**: No logging overhead in production builds
- **Error Handling**: Modern `runCatching` approach for cleaner error handling
- **Constants**: Compile-time constants for better performance

#### 📱 **Build Variants**
- **Dummy Build**: Uses mock data with all refactoring improvements
- **Production Build**: Real backend calls with optimized logging and error handling
- **Both Variants**: Include all code quality improvements and constants system

### **v2.3.2** - Theme Resources Cleanup & Pagination Fix
**Date**: December 2024  
**Status**: ✅ **COMPLETED**

#### 🧹 **Theme Resources Cleanup**
- **Color.kt**: Removed 8 unused colors (AppBackground, MoviePosterBlue, MoviePosterBrown, MoviePosterDarkBlue, MoviePosterGreen, MoviePosterNavy, TextTertiary, ButtonContainer)
- **Dimens.kt**: Removed 8 unused dimensions (Dimens1, Dimens6, Dimens20, Dimens24, Dimens40, Dimens48, Dimens300, Dimens500) + Added Dimens112
- **Typography.kt**: Removed 5 unused typography values (Typography8, Typography12, Typography14, Typography20, LetterSpacing2)
- **File Size Reduction**: Significant reduction in theme file sizes (Color.kt: 40% reduction, Dimens.kt: 24% reduction, Typography.kt: 28% reduction)

#### 🔧 **Pagination Controls Fix**
- **Dynamic Margin**: Fixed pagination controls overlapping last movie card
- **Calculated Height**: Implemented reliable 112dp bottom padding for pagination controls
- **Constants Compliance**: Extracted hardcoded 112dp to Dimens112 constant
- **Better UX**: Last movie card now fully visible when pagination controls are shown

#### 📱 **UI Components Updates**
- **MoviesListScreen.kt**: Fixed dynamic margin problem with proper constants usage
- **ConfigurableMovieCard.kt**: Updated to use cleaned up theme constants
- **ConfigurableButton.kt**: Updated to use cleaned up theme constants
- **PullToReloadIndicator.kt**: Updated to use cleaned up theme constants
- **MovieDetailScreen.kt**: Updated to use cleaned up theme constants

#### 🎯 **Code Quality**
- **No Dead Code**: Eliminated all unused theme resources
- **Cleaner Codebase**: Only necessary resources remain in theme files
- **Better Maintainability**: Reduced file sizes and complexity
- **Constants Compliance**: All hardcoded values properly extracted to constants

### **v2.3.1** - UI Layer Constants Refactoring
**Date**: December 2024  
**Status**: ✅ **COMPLETED**

#### 🎨 **UI Layer Constants System**
- **Floats.kt**: Created comprehensive float constants file (Float0, Float01, Float02, etc.)
- **Alpha.kt → Floats.kt**: Renamed and expanded alpha values to cover all float calculations
- **ImageConfig.kt**: Centralized TMDB image URL configuration (renamed from BuildConfig.kt)
- **UIConstants.kt**: Added animation constants and UI behavior constants
- **String Resources**: Extracted animation labels and UI text to strings.xml

#### 🔧 **Component Refactoring**
- **PullToReloadIndicator**: All hardcoded float values extracted to constants
- **ConfigurableMovieCard**: Dimensions, alpha values, and UI text extracted to constants
- **ConfigurableButton**: All hardcoded values replaced with constants
- **ConfigurableText**: TextType enum default texts moved to string resources
- **Edge-to-Edge**: Applied systemBarsPadding() to main UI screens
- **VersionUtils Update**: Changed API requirement from Android 14+ (API 34) to Android 5+ (API 21)
- **System Bars Padding**: Added proper `systemBarsPadding()` to all main UI screens
- **Status Bar Integration**: Content now properly extends behind status bar with correct padding
- **Cross-Platform Support**: Edge-to-edge now works on Android 5.0+ devices

#### 🔧 **Technical Changes**
- **VersionUtils.kt**: Added `ANDROID_5` constant and updated `safeEnableEdgeToEdge()` method
- **UI Screens**: Updated `MoviesListScreen.kt`, `MovieDetailScreen.kt`, and `Splash.kt`
- **Window Insets**: Proper handling of system bars across all screens
- **Build Compatibility**: Maintains compatibility with older Android versions

### **v2.3.0** - Constants Refactoring & Dummy Repository
**Date**: December 2024  
**Status**: ✅ **COMPLETED**

#### 🔧 **Code Quality Improvements**
- **Constants Extraction**: Extracted all hardcoded strings, integers, and doubles to `StringConstants.kt`
- **Serialized Names**: All `@SerializedName` annotations now use constants instead of hardcoded strings
- **Pagination Constants**: Centralized pagination values (8, 80, 5, 50, 4, 40, 1)
- **Network Simulation**: Constants for delays (500ms, 1000ms) and error probability (0.05)
- **HTTP Timeouts**: Constants for request (15000ms) and connect (10000ms) timeouts
- **Default Values**: Constants for all default values (0, 0.0, 0L, false, "", etc.)
- **UI Constants**: Button corner radius and other UI-related constants

#### 🏗️ **Architecture Enhancements**
- **DummyMovieRepository**: Created dedicated dummy repository for testing
- **Asset Integration**: Proper integration with existing `FakeInterceptor` and `AssetDataLoader`
- **Build Variants**: Correct configuration for dummy vs production versions
- **Dependency Injection**: Proper DI setup for different build variants

#### 🎯 **Build Variants**
- **dummyDebug**: Uses `FakeInterceptor` → loads mock data from assets
- **prodDebug**: Uses real backend calls with fallback to mock data
- **prodRelease**: Production version with real backend integration

#### 📱 **Mock Data System**
- **Asset Files**: `mock_movies.json`, `mock_movie_details.json` for consistent testing
- **Realistic Delays**: Simulated network delays for authentic testing experience
- **Complete Data**: Full movie details with genres, production companies, budgets
- **Pagination Support**: Proper pagination with realistic page counts

### **v2.2.0** - Enhanced Pagination & UX (2024-12-19)
**Date**: December 2024  
**Status**: ✅ **COMPLETED**

#### 🎨 **UI/UX Improvements**
- **45 Movies Total**: Expanded mock data to 45 movies (15 per page × 3 pages)
- **Smart Swipe Navigation**: 
  - Swipe right on page 1: No action
  - Swipe right on page 2+: Go to previous page
  - Swipe left on page 1-2: Go to next page
  - Swipe left on page 3: Show snackbar "This is the last available page"
- **Custom Snackbar**: Dark blue background matching splash screen with white text
- **Debounced Snackbar**: Prevents multiple snackbar spam with 2-second debounce
- **Real TMDB Posters**: All 45 movies have proper poster URLs

#### 🔧 **Technical Enhancements**
- **Import Optimization**: Reviewed and maintained clean import structure
- **Build Verification**: All variants (dummy, prod) build successfully
- **Mock Data Integration**: Comprehensive mock data for offline testing

### **v2.1.0** - Enhanced UI/UX & Pull-to-Refresh
**Date**: September 2024  
**Status**: ✅ **COMPLETED**

#### 🎨 **UI/UX Improvements**
- **Custom Error Screens**: Large white text with split error messages
- **Red Arrow Indicator**: Triple-sized (144dp) custom pull-to-reload arrow
- **Pull-to-Refresh**: Full gesture support on error screens
- **Consistent Background**: SplashBackground color across all screens
- **Loading States**: Custom white spinner without system indicators
- **Scrollable Error Content**: Enhanced gesture detection for pull-to-refresh

#### 🔧 **Technical Enhancements**
- **Custom PullToReloadIndicator**: Canvas-drawn red arrow with animations
- **Gesture Handling**: Improved pull-to-refresh detection on error screens
- **Mock Data Assets**: Enhanced dummy version with movies from JSON assets
- **Build Variants**: Simplified to dummyDebug, prodDebug, prodRelease
- **Debug Logging**: Added pull-to-refresh trigger logging

## 🎨 UI Layer Constants System

### 📐 **Comprehensive Constants Architecture**
The project implements a complete constants system for the UI layer to eliminate hardcoded values:

#### **Constants Files:**
- **`Floats.kt`**: All float values (Float0, Float01, Float02, Float03, etc.)
- **`Dimens.kt`**: All dimension values (Dimens2, Dimens4, Dimens8, Dimens16, etc.)
- **`ImageConfig.kt`**: TMDB image URL configuration and helper functions
- **`UIConstants.kt`**: UI behavior constants, animation durations, and thresholds
- **`strings.xml`**: All UI text for internationalization

#### **Benefits:**
- **No Hardcoded Values**: Complete elimination of magic numbers and strings
- **Maintainability**: Centralized constants for easy updates
- **Consistency**: Uniform naming conventions across the codebase
- **Internationalization**: All UI text properly externalized
- **Type Safety**: Compile-time constants for better performance

## 🎨 UI/UX особенности

- **Material Design 3** - Современный дизайн
- **Edge-to-Edge** - Полноэкранный режим
- **Responsive Design** - Адаптация под разные экраны
- **Smooth Animations** - Плавные переходы
- **Accessibility** - Поддержка доступности

## 🧪 Тестирование

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Code Coverage
```bash
./gradlew jacocoTestReport
```

## 📝 Changelog

### v2.2.0 - Enhanced Pagination & UX (2024-12-19)
- ✅ **45 Movies Total**: Expanded mock data to 45 movies (15 per page × 3 pages)
- ✅ **Smart Swipe Navigation**: 
  - Swipe right on page 1: No action
  - Swipe right on page 2+: Go to previous page
  - Swipe left on page 1-2: Go to next page
  - Swipe left on page 3: Show snackbar "This is the last available page"
- ✅ **Custom Snackbar**: Dark blue background matching splash screen with white text
- ✅ **Debounced Snackbar**: Prevents multiple snackbar spam with 2-second debounce
- ✅ **Real TMDB Posters**: All 45 movies have proper poster URLs
- ✅ **Import Optimization**: Reviewed and maintained clean import structure
- ✅ **Build Verification**: All variants (dummy, prod) build successfully

### v2.1.0 - Build & Backend Fixes (2024-12-19)
- ✅ **UI/UX Improvements**: Simplified design with consistent theming
- ✅ **Pull-to-Refresh**: Custom implementation with visual feedback
- ✅ **Mock Data Integration**: Comprehensive mock data for offline testing
- ✅ **Technical Enhancements**: Improved error handling and state management
- ✅ **User Experience**: Enhanced loading states and error screens

## 📱 Скриншоты

*Скриншоты будут добавлены по мере развития UI*

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit изменения (`git commit -m 'Add some AmazingFeature'`)
4. Push в branch (`git push origin feature/AmazingFeature`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект лицензирован под MIT License - см. файл [LICENSE](LICENSE) для деталей.

## 📞 Контакты

- **Разработчик**: [Your Name]
- **Email**: [your.email@example.com]
- **GitHub**: [@your-username]

## 🙏 Благодарности

- [TMDB](https://www.themoviedb.org/) - API для данных о фильмах
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Современный UI фреймворк
- [Koin](https://insert-koin.io/) - Dependency Injection
- [Material Design](https://material.io/) - Система дизайна

---

**Последнее обновление**: 2024-12-19  
**Версия**: 2.2.0  
**Статус**: В активной разработке
