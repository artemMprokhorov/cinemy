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

3. **Сборка проекта**
   ```bash
   # Development версия
   ./gradlew assembleDevelopmentDebug
   
   # Или Production версия
   ./gradlew assembleProductionDebug
   ```

4. **Установка на устройство**
   ```bash
   # Development версия
   ./gradlew installDevelopmentDebug
   
   # Или Production версия
   ./gradlew installProductionDebug
   ```

5. **Запуск приложения**
   ```bash
   adb shell am start -n com.example.tmdbai/.MainActivity
   ```

### Build Variants
- **development** - Отладочная версия с логированием
- **staging** - Тестовая версия
- **production** - Продакшн версия

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
- [x] Список фильмов (базовая структура)
- [x] Детали фильма (базовая структура)
- [x] Dependency Injection с Koin
- [x] MCP клиент для AI интеграции
- [x] Базовые ViewModels и States
- [x] Material Design 3 тема

### 🚧 В разработке
- [ ] Интеграция с TMDB API
- [ ] AI рекомендации через MCP
- [ ] Кэширование данных
- [ ] Обработка ошибок
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
**Версия**: 1.0.0-dev  
**Статус**: В активной разработке
