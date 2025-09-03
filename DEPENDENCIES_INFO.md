# DEPENDENCIES_INFO.md

**TmdbAi - Информация о зависимостях**  
**Дата создания**: 2024-12-19  
**Версия**: 1.0.0-dev

## 📦 Обзор зависимостей

Проект использует современный стек технологий для Android разработки с акцентом на производительность, тестируемость и масштабируемость.

## 🏗️ Core Dependencies

### 📱 Android Core

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.core:core-ktx` | 1.12.0 | Базовые Android компоненты | Стандартная библиотека для Kotlin Android |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.7.0 | Lifecycle компоненты | Управление жизненным циклом компонентов |
| `androidx.activity:activity-compose` | 1.8.2 | Compose Activity | Интеграция Compose с Activity |

### 🎨 Jetpack Compose

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.compose:compose-bom` | 2025.01.00 | Compose BOM | Управление версиями Compose |
| `androidx.compose.ui:ui` | BOM | UI компоненты | Основные UI элементы |
| `androidx.compose.material3:material3` | BOM | Material Design 3 | Современная система дизайна |
| `androidx.compose.ui:ui-tooling-preview` | BOM | Preview инструменты | Предварительный просмотр UI |
| `androidx.compose.ui:ui-tooling` | BOM | Debug инструменты | Отладка Compose UI |

**Обоснование выбора**: Compose - современный декларативный UI фреймворк, который заменяет традиционный View system. Material Design 3 обеспечивает современный внешний вид.

### 🧭 Navigation

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.navigation:navigation-compose` | 2.9.3 | Compose навигация | Type-safe навигация между экранами |
| `androidx.navigation:navigation-runtime-ktx` | 2.9.3 | Runtime навигация | Базовые компоненты навигации |

**Обоснование выбора**: Navigation Compose обеспечивает type-safe навигацию с поддержкой Compose. Версия 2.9.3 - последняя стабильная версия.

### 🔄 State Management

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.7.0 | ViewModel Compose | Интеграция ViewModel с Compose |
| `androidx.lifecycle:lifecycle-runtime-compose` | 2.7.0 | Runtime Compose | Runtime интеграция |

**Обоснование выбора**: ViewModel Compose обеспечивает правильную интеграцию ViewModel с Compose UI, включая автоматическое управление жизненным циклом.

## 🗜️ Dependency Injection

### 🎯 Koin

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `io.insert-koin:koin-android` | 3.5.3 | Android Koin | DI для Android |
| `io.insert-koin:koin-androidx-compose` | 3.5.3 | Compose Koin | DI для Compose |
| `io.insert-koin:koin-core` | 3.5.3 | Core Koin | Базовые DI компоненты |

**Обоснование выбора**: Koin выбран за простоту использования, отсутствие аннотаций и хорошую интеграцию с Kotlin. Альтернатива Hilt требует больше настройки.

## 🌐 Networking & Data

### 🚀 Ktor Client

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `io.ktor:ktor-client-core` | 2.1.3 | Core HTTP клиент | Базовый HTTP функционал |
| `io.ktor:ktor-client-android` | 2.1.3 | Android HTTP клиент | Android-специфичные функции |
| `io.ktor:ktor-client-content-negotiation` | 2.1.3 | Content negotiation | Автоматическая сериализация |
| `io.ktor:ktor-serialization-kotlinx-json` | 2.1.3 | JSON сериализация | JSON обработка |
| `io.ktor:ktor-client-logging` | 2.1.3 | HTTP логирование | Отладка сетевых запросов |
| `io.ktor:ktor-client-auth` | 2.1.3 | Аутентификация | HTTP аутентификация |

**Обоснование выбора**: Ktor - современный HTTP клиент от JetBrains, написанный на Kotlin. Обеспечивает лучшую интеграцию с Kotlin и корутинами по сравнению с Retrofit.

### 📊 JSON Processing

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | 1.6.2 | JSON сериализация | Основная JSON библиотека |
| `com.google.code.gson:gson` | 2.10.1 | Gson (совместимость) | Обратная совместимость |

**Обоснование выбора**: Kotlinx Serialization - нативная Kotlin библиотека для сериализации. Gson оставлен для обратной совместимости во время миграции.

## 🖼️ Image Loading

### 🎨 Coil

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `io.coil-kt:coil-compose` | 2.5.0 | Compose интеграция | Загрузка изображений в Compose |
| `io.coil-kt:coil-gif` | 2.5.0 | GIF поддержка | Анимированные изображения |
| `io.coil-kt:coil-svg` | 2.5.0 | SVG поддержка | Векторные изображения |

**Обоснование выбора**: Coil - современная библиотека для загрузки изображений, написанная на Kotlin. Обеспечивает лучшую производительность по сравнению с Glide и Picasso.

## ⚡ Asynchronous Programming

### 🔄 Coroutines

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.8.0 | Android корутины | Асинхронное программирование |
| `org.jetbrains.kotlinx:kotlinx-coroutines-core` | 1.8.0 | Core корутины | Базовые корутины |

**Обоснование выбора**: Kotlin Coroutines - нативный способ асинхронного программирования в Kotlin. Обеспечивает лучшую читаемость кода по сравнению с RxJava.

## 📱 UI Enhancements

### 🎨 Accompanist

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `com.google.accompanist:accompanist-systemuicontroller` | 0.34.0 | System UI контроллер | Управление системным UI |
| `com.google.accompanist:accompanist-permissions` | 0.34.0 | Permissions | Управление разрешениями |

**Обоснование выбора**: Accompanist предоставляет дополнительные компоненты для Compose, которые еще не включены в основной фреймворк.

### 🔄 Paging

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.paging:paging-runtime-ktx` | 3.2.1 | Paging runtime | Пагинация данных |
| `androidx.paging:paging-compose` | 3.2.1 | Compose paging | UI для пагинации |

**Обоснование выбора**: Paging 3 обеспечивает эффективную загрузку и отображение больших списков данных с поддержкой Compose.

## 💾 Data Storage

### 📊 DataStore

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.datastore:datastore-preferences` | 1.0.0 | Preferences DataStore | Хранение настроек |

**Обоснование выбора**: DataStore - современная замена SharedPreferences, обеспечивающая type-safe API и поддержку корутин.

## 🧪 Testing

### 📊 Unit Testing

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `junit:junit` | 4.13.2 | JUnit 4 | Базовое unit тестирование |
| `org.jetbrains.kotlinx:kotlinx-coroutines-test` | 1.8.0 | Coroutines тестирование | Тестирование корутин |
| `io.mockk:mockk` | 1.13.8 | Mocking | Создание моков |
| `app.cash.turbine:turbine` | 1.0.0 | Flow тестирование | Тестирование Flow |
| `io.insert-koin:koin-test` | 3.5.3 | Koin тестирование | Тестирование DI |

**Обоснование выбора**: MockK - современная библиотека для мокирования в Kotlin. Turbine специально разработан для тестирования Flow.

### 📱 Android Testing

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `androidx.test.ext:junit` | 1.1.5 | Android JUnit | JUnit для Android |
| `androidx.test.espresso:espresso-core` | 3.5.1 | Espresso | UI тестирование |
| `androidx.compose.ui:ui-test-junit4` | BOM | Compose тестирование | Тестирование Compose UI |

**Обоснование выбора**: Espresso - стандартная библиотека для UI тестирования Android. Compose UI Test обеспечивает тестирование Compose компонентов.

## 🔧 Development Tools

### 📝 Logging

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `com.jakewharton.timber:timber` | 5.0.1 | Логирование | Структурированное логирование |

**Обоснование выбора**: Timber - популярная библиотека для логирования, обеспечивающая удобный API и автоматическое управление тегами.

### 🎨 Debug Tools

| Зависимость | Версия | Назначение | Обоснование |
|-------------|--------|------------|-------------|
| `com.squareup.leakcanary:leakcanary-android` | 2.12 | Memory leak detection | Отладка утечек памяти |

**Обоснование выбора**: LeakCanary - инструмент для обнаружения утечек памяти в Android приложениях.

## 📋 Version Management

### 🔄 Version Catalog

Проект использует Gradle Version Catalog (`gradle/libs.versions.toml`) для централизованного управления версиями зависимостей.

**Преимущества**:
- Централизованное управление версиями
- Легкое обновление зависимостей
- Консистентность версий
- Упрощение миграции

### 📊 Version Compatibility

| Компонент | Версия | Совместимость |
|-----------|--------|---------------|
| AGP | 8.8.0 | Android Studio Hedgehog+ |
| Kotlin | 1.9.22 | Стабильная версия |
| Compose Compiler | 1.5.8 | Совместим с Kotlin 1.9.22 |
| Compose BOM | 2025.01.00 | Последняя стабильная версия |

## 🚀 Build Configuration

### 🔧 Build Variants

```kotlin
flavorDimensions += "environment"
productFlavors {
    create("development") {
        dimension = "environment"
        applicationIdSuffix = ".dev"
        versionNameSuffix = "-dev"
    }
    create("staging") {
        dimension = "environment"
        applicationIdSuffix = ".staging"
        versionNameSuffix = "-staging"
    }
    create("production") {
        dimension = "environment"
    }
}
```

### 📱 Build Types

```kotlin
buildTypes {
    debug {
        isMinifyEnabled = false
        isDebuggable = true
    }
    release {
        isMinifyEnabled = true
        isDebuggable = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
```

## 🔒 Security Considerations

### 🛡️ API Keys

```kotlin
buildConfigField("String", "MCP_SERVER_URL", "\"https://your-ngrok-url.ngrok.io\"")
buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
```

**Рекомендации**:
- Не коммитьте API ключи в репозиторий
- Используйте переменные окружения
- Храните секреты в безопасном месте

### 🔐 Signing Configuration

```kotlin
signingConfigs {
    create("release") {
        keyAlias = System.getenv("KEY_ALIAS") ?: "tmdbai"
        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
        storeFile = file("tmdbai-release-key.jks")
        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
    }
}
```

## 📈 Performance Optimization

### ⚡ Compose Optimizations

```kotlin
composeOptions {
    kotlinCompilerExtensionVersion = "1.5.8"
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9"
        }
    }
}
```

### 🧹 ProGuard Configuration

```kotlin
release {
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}
```

## 🔄 Dependency Updates

### 📅 Update Schedule

- **Еженедельно**: Проверка обновлений безопасности
- **Ежемесячно**: Обновление minor версий
- **Ежеквартально**: Обновление major версий

### 🚨 Breaking Changes

При обновлении зависимостей:
1. Проверьте changelog
2. Протестируйте на dev ветке
3. Обновите документацию
4. Проведите code review

## 📚 Alternative Libraries

### 🔄 Considered Alternatives

| Текущая | Альтернатива | Причина выбора |
|---------|--------------|----------------|
| Koin | Hilt | Простота настройки |
| Ktor | Retrofit | Kotlin-first подход |
| Coil | Glide | Современность и производительность |
| StateFlow | LiveData | Kotlin-first подход |

## 🎯 Future Dependencies

### 📋 Planned Additions

- **Room**: Локальная база данных
- **Hilt**: Замена Koin (если потребуется)
- **WorkManager**: Фоновые задачи
- **DataStore**: Замена SharedPreferences
- **Navigation Testing**: Тестирование навигации

### 🔮 Experimental Features

- **Compose Multiplatform**: Кроссплатформенная разработка
- **Kotlin Multiplatform**: Общий код для Android и iOS
- **Jetpack Compose for Desktop**: Desktop приложения

---

**Последнее обновление**: 2024-12-19  
**Версия документа**: 1.0.0  
**Статус**: Актуально
