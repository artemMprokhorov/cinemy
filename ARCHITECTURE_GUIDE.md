# ARCHITECTURE_GUIDE.md

**TmdbAi - Руководство по архитектуре**  
**Дата создания**: 2024-12-19  
**Версия**: 1.0.0-dev

## 🏗️ Обзор архитектуры

TmdbAi построен на принципах **Clean Architecture** с использованием **MVI (Model-View-Intent)** паттерна. Архитектура обеспечивает четкое разделение ответственности, тестируемость и масштабируемость.

## 🎯 MVI (Model-View-Intent) Pattern

### 📖 Основные принципы

MVI - это архитектурный паттерн, который обеспечивает **однонаправленный поток данных** и **предсказуемое состояние** приложения.

```
┌─────────────┐    Intent    ┌─────────────┐    State    ┌─────────────┐
│    View     │ ──────────→ │ ViewModel   │ ──────────→ │    View     │
│             │              │             │             │             │
└─────────────┘              └─────────────┘             └─────────────┘
       │                            │                           │
       │                            ▼                           │
       │                    ┌─────────────┐                     │
       │                    │ Repository  │                     │
       │                    │             │                     │
       │                    └─────────────┘                     │
       │                            │                           │
       └────────────────────────────┼───────────────────────────┘
                                    │
                                    ▼
                            ┌─────────────┐
                            │   MCP       │
                            │  Client     │
                            └─────────────┘
```

### 🔄 Data Flow

1. **User Action** → Пользователь выполняет действие
2. **Intent** → Создается Intent объект
3. **ViewModel.processIntent()** → Обработка Intent
4. **Repository Call** → Вызов репозитория
5. **MCP Client** → Взаимодействие с AI сервером
6. **State Update** → Обновление состояния
7. **UI Update** → Автоматическое обновление UI

## 🏛️ Clean Architecture Layers

### 📱 Presentation Layer (UI + ViewModels)

**Назначение**: Управление UI состоянием и пользовательскими взаимодействиями

**Компоненты**:
- `MoviesListViewModel` - Управление списком фильмов
- `MovieDetailViewModel` - Управление деталями фильма
- `MoviesListIntent` - Intent'ы для списка фильмов
- `MovieDetailIntent` - Intent'ы для деталей фильма
- `MoviesListState` - Состояние списка фильмов
- `MovieDetailState` - Состояние деталей фильма

**Принципы**:
- ViewModels не знают о деталях UI
- Состояние представлено как immutable data class
- Intent'ы описывают все возможные действия пользователя

### 🎯 Domain Layer (Use Cases + Models)

**Назначение**: Бизнес-логика и доменные модели

**Компоненты**:
- `MovieRepository` - Интерфейс репозитория
- Domain Models - Бизнес-модели
- Use Cases - Бизнес-операции

**Принципы**:
- Независимость от внешних слоев
- Чистая бизнес-логика
- Тестируемость

### 💾 Data Layer (Repository + Data Sources)

**Назначение**: Управление данными и внешними API

**Компоненты**:
- `MovieRepositoryImpl` - Реализация репозитория
- `McpClient` - MCP клиент для AI интеграции
- `McpHttpClient` - HTTP клиент
- Data Models - DTO модели
- Mappers - Преобразование данных

**Принципы**:
- Repository pattern
- Абстракция источников данных
- Кэширование и синхронизация

## 📁 Структура папок

```
app/src/main/java/com/example/tmdbai/
├── TmdbAi.kt                    # Главный класс приложения
├── navigation/                   # Навигация
│   ├── Navigation.kt            # Основная навигация
│   └── Screen.kt                # Определения экранов
├── ui/                          # UI Layer
│   ├── components/              # Переиспользуемые компоненты
│   ├── movieslist/              # Экран списка фильмов
│   ├── moviedetail/             # Экран деталей фильма
│   ├── splash/                  # Экран загрузки
│   └── theme/                   # Тема и стили
├── presentation/                 # Presentation Layer
│   ├── di/                      # DI модули
│   ├── commons/                 # Общие компоненты
│   ├── movieslist/              # MoviesList ViewModel
│   └── moviedetail/             # MovieDetail ViewModel
├── data/                        # Data Layer
│   ├── di/                      # Data DI модули
│   ├── mcp/                     # MCP клиент и модели
│   ├── model/                   # Data модели
│   ├── mapper/                  # Мапперы данных
│   ├── remote/                  # Remote data sources
│   └── repository/              # Репозитории
└── utils/                       # Утилиты
```

### 📱 UI Layer

**Назначение**: Отображение UI и обработка пользовательских взаимодействий

**Принципы**:
- Только UI логика
- Реакция на изменения состояния
- Отправка Intent'ов в ViewModel

**Компоненты**:
- `MoviesListScreen` - Экран списка фильмов
- `MovieDetailScreen` - Экран деталей фильма
- `MovieAppSplashScreen` - Экран загрузки
- `components/` - Переиспользуемые UI компоненты

### 🎭 Presentation Layer

**Назначение**: Управление состоянием и бизнес-логика UI

**Принципы**:
- Обработка Intent'ов
- Управление состоянием
- Взаимодействие с репозиториями

**Компоненты**:
- ViewModels - Управление состоянием
- Intent Classes - Описание действий пользователя
- State Classes - Описание состояния UI

### 💾 Data Layer

**Назначение**: Управление данными и внешними API

**Принципы**:
- Repository pattern
- Абстракция источников данных
- Кэширование

**Компоненты**:
- `MovieRepository` - Интерфейс репозитория
- `MovieRepositoryImpl` - Реализация репозитория
- `McpClient` - MCP клиент
- `McpHttpClient` - HTTP клиент

## 🔄 Dependency Injection (Koin)

### 📦 Модули

#### Presentation Module
```kotlin
val presentationModule = module {
    // ViewModels
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}
```

#### Data Module
```kotlin
val dataModule = module {
    // MCP Client
    single<McpClient> { McpClient() }
    
    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
```

### 🔗 Dependency Graph

```
MainActivity
    ↓
AppNavigation
    ↓
Screens → ViewModels → Repository → MCP Client
    ↓
Koin Container
```

## 🧭 Navigation

### 📱 Навигационная структура

```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            MovieAppSplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.MoviesList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MoviesList.route) {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail(movieId).createRoute())
                }
            )
        }

        composable(
            route = Screen.MovieDetail(0).route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 1
            MovieDetailScreen(movieId = movieId)
        }
    }
}
```

### 🎯 Type-Safe Routes

```kotlin
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MoviesList : Screen("movies_list")
    data class MovieDetail(val movieId: Int) : Screen("movie_detail/{movieId}") {
        fun createRoute() = "movie_detail/$movieId"
    }
}
```

## 🔄 State Management

### 📊 State Classes

#### MoviesListState
```kotlin
data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
)
```

#### MovieDetailState
```kotlin
data class MovieDetailState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### 🔄 StateFlow

```kotlin
class MoviesListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(MoviesListState())
    val state: StateFlow<MoviesListState> = _state.asStateFlow()
    
    fun processIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadPopularMovies -> loadPopularMovies()
            is MoviesListIntent.LoadMoreMovies -> loadMoreMovies()
            is MoviesListIntent.Retry -> retry()
        }
    }
}
```

## 🎭 Intent Pattern

### 📝 Intent Classes

#### MoviesListIntent
```kotlin
sealed class MoviesListIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()
    object Retry : MoviesListIntent()
    object Refresh : MoviesListIntent()
    data class SearchMovies(val query: String) : MoviesListIntent()
}
```

#### MovieDetailIntent
```kotlin
sealed class MovieDetailIntent {
    object LoadMovieDetails : MovieDetailIntent()
    object Retry : MovieDetailIntent()
}
```

### 🔄 Intent Processing

```kotlin
fun processIntent(intent: MoviesListIntent) {
    when (intent) {
        is MoviesListIntent.LoadPopularMovies -> {
            _state.value = _state.value.copy(isLoading = true)
            loadPopularMovies()
        }
        is MoviesListIntent.LoadMoreMovies -> {
            loadMoreMovies()
        }
        is MoviesListIntent.Retry -> {
            retry()
        }
        is MoviesListIntent.Refresh -> {
            refresh()
        }
        is MoviesListIntent.SearchMovies -> {
            searchMovies(intent.query)
        }
    }
}
```

## 🌐 MCP Integration

### 🤖 Model Context Protocol

MCP (Model Context Protocol) - это протокол для взаимодействия с AI моделями.

#### MCP Client
```kotlin
class McpClient {
    suspend fun getPopularMoviesViaMcp(): Result<List<Movie>> {
        return try {
            val response = mcpHttpClient.getPopularMovies()
            Result.success(response.movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### MCP HTTP Client
```kotlin
class McpHttpClient {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    
    suspend fun getPopularMovies(): McpResponse {
        return client.get("$baseUrl/movies/popular").body()
    }
}
```

## 🧪 Testing Strategy

### 📊 Testing Pyramid

```
        /\
       /  \     UI Tests (Few)
      /____\    
     /      \   Integration Tests (Some)
    /________\  
   /          \ Unit Tests (Many)
  /____________\
```

### 🎯 Unit Tests

- **ViewModels** - Тестирование бизнес-логики
- **Repositories** - Тестирование доступа к данным
- **Use Cases** - Тестирование бизнес-операций

### 🔗 Integration Tests

- **Repository + MCP Client** - Тестирование интеграции
- **ViewModel + Repository** - Тестирование взаимодействия слоев

### 📱 UI Tests

- **Screen Navigation** - Тестирование навигации
- **User Interactions** - Тестирование пользовательских действий
- **State Updates** - Тестирование обновления UI

## 📏 Naming Conventions

### 🏷️ Классы

- **ViewModel**: `{Feature}ViewModel` (например, `MoviesListViewModel`)
- **Intent**: `{Feature}Intent` (например, `MoviesListIntent`)
- **State**: `{Feature}State` (например, `MoviesListState`)
- **Repository**: `{Feature}Repository` (например, `MovieRepository`)
- **Screen**: `{Feature}Screen` (например, `MoviesListScreen`)

### 📁 Папки

- **UI**: `ui/{feature}` (например, `ui/movieslist`)
- **Presentation**: `presentation/{feature}` (например, `presentation/movieslist`)
- **Data**: `data/{layer}` (например, `data/repository`)

### 🔤 Переменные и функции

- **ViewModel state**: `_state`, `state`
- **Intent processing**: `processIntent(intent: Intent)`
- **Repository methods**: `get{Entity}()`, `save{Entity}()`

## 🚀 Performance Considerations

### ⚡ Оптимизации

1. **StateFlow vs LiveData** - Использование StateFlow для Kotlin-first подхода
2. **Lazy Loading** - Загрузка данных по требованию
3. **Image Caching** - Кэширование изображений с Coil
4. **Pagination** - Пагинация для больших списков

### 📱 Memory Management

1. **ViewModel Scope** - Правильное управление жизненным циклом
2. **Coroutine Scope** - Отмена корутин при уничтожении ViewModel
3. **Resource Cleanup** - Освобождение ресурсов

## 🔒 Security

### 🛡️ Безопасность данных

1. **API Keys** - Хранение в BuildConfig
2. **HTTPS** - Использование защищенных соединений
3. **Input Validation** - Валидация пользовательского ввода
4. **Error Handling** - Безопасная обработка ошибок

## 📚 Best Practices

### ✅ Рекомендуется

1. **Single Responsibility** - Каждый класс имеет одну ответственность
2. **Dependency Inversion** - Зависимость от абстракций, а не от реализаций
3. **Immutable State** - Неизменяемое состояние
4. **Unidirectional Flow** - Однонаправленный поток данных
5. **Error Handling** - Обработка всех возможных ошибок

### ❌ Не рекомендуется

1. **Business Logic in UI** - Бизнес-логика в UI слое
2. **Direct API Calls** - Прямые вызовы API из ViewModel
3. **Mutable State** - Изменяемое состояние
4. **Tight Coupling** - Сильная связанность между слоями

## 🔄 Migration Guide

### 📱 From MVVM to MVI

1. **Replace LiveData with StateFlow**
2. **Create Intent classes**
3. **Create State classes**
4. **Implement processIntent method**
5. **Update UI to observe state**

### 🏗️ From Monolithic to Modular

1. **Identify feature boundaries**
2. **Create feature modules**
3. **Extract shared dependencies**
4. **Update build configuration**
5. **Test module integration**

---

**Последнее обновление**: 2024-12-19  
**Версия документа**: 1.0.0  
**Статус**: Актуально
