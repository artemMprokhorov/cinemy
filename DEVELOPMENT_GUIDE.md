# DEVELOPMENT_GUIDE.md

**TmdbAi - Руководство по разработке**  
**Дата создания**: 2024-12-19  
**Дата обновления**: 2024-12-19  
**Версия**: 2.3.1

## 🚀 Настройка среды разработки

### 📋 Предварительные требования

- **Android Studio**: Hedgehog (2023.1.1) или новее
- **JDK**: 17 или новее
- **Android SDK**: API 36 (Android 14)
- **Gradle**: 8.10.2
- **Kotlin**: 1.9.22

### ⚙️ Настройка проекта

1. **Клонирование репозитория**
   ```bash
   git clone https://github.com/your-username/TmdbAi.git
   cd TmdbAi
   ```

2. **Настройка переменных окружения**
   ```bash
   # Создайте local.properties
   MCP_SERVER_URL=https://your-ngrok-url.ngrok.io
   TMDB_BASE_URL=https://api.themoviedb.org/3/
   TMDB_API_KEY=your_tmdb_api_key_here
   ```

3. **Синхронизация Gradle**
   ```bash
   ./gradlew --refresh-dependencies
   ```

4. **Проверка сборки**
   ```bash
   # Dummy version (mock data)
   ./gradlew assembleDummyDebug
   
   # Production version (real backend)
   ./gradlew assembleProdDebug
   ```

### 🔧 Конфигурация Android Studio

1. **Kotlin Plugin**: Убедитесь, что установлен Kotlin plugin
2. **Compose Preview**: Включите Compose Preview в настройках
3. **Code Style**: Настройте Kotlin code style
4. **Live Templates**: Настройте live templates для MVI

### 🏗️ Build Variants

Проект поддерживает три варианта сборки:

| Variant | Purpose | Data Source | Package ID |
|---------|---------|-------------|------------|
| **dummyDebug** | Development | Mock data only | `com.example.tmdbai.dummy.debug` |
| **prodDebug** | Testing | Real backend + fallback | `com.example.tmdbai.debug` |
| **prodRelease** | Production | Real backend only | `com.example.tmdbai` |

#### Установка и запуск

```bash
# Dummy version (mock data)
./gradlew installDummyDebug
adb shell am start -n com.example.tmdbai.dummy.debug/com.example.tmdbai.MainActivity

# Production version (real backend)
./gradlew installProdDebug
adb shell am start -n com.example.tmdbai.debug/com.example.tmdbai.MainActivity
```

## 🎨 Edge-to-Edge Display

### 📱 Настройка Edge-to-Edge

Проект поддерживает полноэкранный режим (edge-to-edge) на всех версиях Android 5.0+:

#### 1. **VersionUtils Configuration**
```kotlin
// VersionUtils.kt
object Versions {
    const val ANDROID_5 = Build.VERSION_CODES.LOLLIPOP // API 21 - Minimum for edge-to-edge
}

fun safeEnableEdgeToEdge(activity: ComponentActivity) {
    if (Build.VERSION.SDK_INT >= Versions.ANDROID_5) {
        activity.enableEdgeToEdge()
    }
}
```

#### 2. **MainActivity Setup**
```kotlin
// MainActivity.kt
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Enable edge-to-edge on supported versions
    VersionUtils.safeEnableEdgeToEdge(this)
    
    setContent {
        // Your UI content
    }
}
```

#### 3. **UI Screen Implementation**
```kotlin
// All main screens should use systemBarsPadding()
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(SplashBackground)
        .systemBarsPadding() // ← This is crucial for proper edge-to-edge
) {
    // Your content
}
```

### ✅ **Best Practices**

1. **Always use `systemBarsPadding()`** on main containers
2. **Test on different Android versions** (API 21+)
3. **Verify content doesn't overlap** with system bars
4. **Use `VersionUtils.safeEnableEdgeToEdge()`** for compatibility

### 🚨 **Common Issues**

- **Content overlapping status bar**: Missing `systemBarsPadding()`
- **Not working on older devices**: Check API level requirements
- **Inconsistent behavior**: Ensure all screens use the same pattern

## 📏 Правила работы с кодом

### 🏗️ Архитектурные принципы

#### ✅ Обязательные правила

1. **Следуйте MVI паттерну**
   - Каждый экран имеет свой ViewModel
   - ViewModel обрабатывает Intent'ы
   - UI реагирует на изменения State

2. **Используйте Clean Architecture**
   - Разделяйте код на слои
   - Зависимости направлены внутрь
   - Внешние слои не знают о внутренних

3. **Применяйте Dependency Injection**
   - Используйте Koin для DI
   - Не создавайте объекты напрямую
   - Используйте интерфейсы для абстракции

4. **Используйте константы вместо hardcoded значений**
   - Все строки, числа и булевы значения должны быть в `StringConstants.kt`
   - Используйте константы в `@SerializedName` аннотациях
   - Не используйте константы для log сообщений (только inline строки)

5. **UI Layer Constants Rules**
   - **Dimensions**: Все размеры (16.dp, 8.dp, etc.) → `Dimens.kt`
   - **Float Values**: Все float значения (0.1f, 0.7f, 2.0f, etc.) → `Floats.kt`
   - **UI Text**: Все пользовательские строки → `strings.xml`
   - **Image URLs**: Все URL изображений → `ImageConfig.kt`
   - **UI Constants**: Числовые константы (пороги, лимиты) → `UIConstants.kt`
   - **No Hardcoded Values**: Никаких hardcoded значений в UI компонентах

#### 🔤 Naming Conventions

```kotlin
// ✅ Правильно
class MoviesListViewModel : ViewModel()
sealed class MoviesListIntent
data class MoviesListState
interface MovieRepository

// ❌ Неправильно
class MoviesListVM : ViewModel()
class MoviesListActions
class MoviesListData
class MovieRepo
```

#### 📁 Структура файлов

```
presentation/movieslist/
├── MoviesListViewModel.kt      # ViewModel
├── MoviesListIntent.kt         # Intent классы
└── MoviesListState.kt          # State классы

ui/movieslist/
└── MoviesListScreen.kt         # UI экран

data/repository/
├── MovieRepository.kt           # Интерфейс
└── MovieRepositoryImpl.kt      # Реализация

data/model/
└── StringConstants.kt          # Все константы проекта
```

#### 🔧 Constants Usage

```kotlin
// ✅ Правильно - использование констант
data class MovieDto(
    @SerializedName(StringConstants.SERIALIZED_ID)
    val id: Int,
    @SerializedName(StringConstants.SERIALIZED_TITLE)
    val title: String,
    @SerializedName(StringConstants.SERIALIZED_OVERVIEW)
    val description: String
)

// В коде
val pagination = PaginationDto(
    page = page,
    totalPages = StringConstants.PAGINATION_TOP_RATED_TOTAL_PAGES,
    totalResults = StringConstants.PAGINATION_TOP_RATED_TOTAL_RESULTS
)

// ❌ Неправильно - hardcoded значения
data class MovieDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)

val pagination = PaginationDto(
    page = page,
    totalPages = 8,
    totalResults = 80
)
```

#### 🎨 UI Layer Constants Usage

```kotlin
// ✅ Правильно - используйте Dimens.kt
Box(
    modifier = Modifier
        .padding(Dimens16)
        .height(Dimens200)
)

// ✅ Правильно - используйте Floats.kt
Color.White.copy(alpha = Float02)
val centerX = size / Float2
val progress = state.progress.coerceIn(Float0, Float10)

// ✅ Правильно - используйте ImageConfig.kt
val imageUrl = ImageConfig.buildBackdropUrl(movie.backdropPath)

// ✅ Правильно - используйте UIConstants.kt
if (movie.popularity > UIConstants.POPULARITY_THRESHOLD) {
    // Show popularity
}

// ✅ Правильно - используйте strings.xml
Text(text = stringResource(R.string.no_image))

// ✅ Правильно - используйте BuildConfig.kt
val imageUrl = BuildConfig.buildBackdropUrl(movie.backdropPath)

// ❌ Неправильно - hardcoded значения
Box(
    modifier = Modifier
        .padding(16.dp)
        .height(200.dp)
)

// ❌ Неправильно - hardcoded значения
Color.White.copy(alpha = 0.2f)

// ❌ Неправильно - hardcoded значения
Text(text = "No Image")

// ❌ Неправильно - hardcoded значения
val imageUrl = "https://image.tmdb.org/t/p/w500${movie.backdropPath}"
```

### 🎭 MVI Implementation Rules

#### Intent Classes

```kotlin
// ✅ Правильно
sealed class MoviesListIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()
    object Retry : MoviesListIntent()
    data class SearchMovies(val query: String) : MoviesListIntent()
}

// ❌ Неправильно
sealed class MoviesListIntent {
    fun loadPopularMovies() // Не Intent, а функция
    val isLoading: Boolean // Не Intent, а состояние
}
```

#### State Classes

```kotlin
// ✅ Правильно
data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
)

// ❌ Неправильно
data class MoviesListState(
    var movies: List<Movie> = emptyList(), // var вместо val
    var isLoading: Boolean = false
)
```

#### ViewModel Implementation

```kotlin
// ✅ Правильно
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
    
    private fun loadPopularMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = movieRepository.getPopularMovies(1)
                _state.value = _state.value.copy(
                    movies = result.data.movies,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}
```

### 🔄 Error Handling

#### Result Pattern

```kotlin
// ✅ Используйте Result для обработки ошибок
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// В Repository
suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
    return try {
        val response = mcpClient.getPopularMovies(page)
        Result.Success(response)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

#### UI Error Handling

```kotlin
// ✅ Правильная обработка ошибок в UI
@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorView(
            message = state.error!!,
            onRetry = { viewModel.processIntent(MoviesListIntent.Retry) }
        )
        state.movies.isNotEmpty() -> MoviesList(
            movies = state.movies,
            onMovieClick = { movieId -> /* navigation */ }
        )
        else -> EmptyState()
    }
}
```

## 🆕 Добавление новых функций

### 📋 Пошаговый процесс

#### 1. Создание Data Models

```kotlin
// data/model/Movie.kt
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genreIds: List<Int>
)
```

#### 2. Создание Repository Interface

```kotlin
// data/repository/MovieRepository.kt
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<MovieListResponse>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
    suspend fun searchMovies(query: String, page: Int): Result<MovieListResponse>
}
```

#### 3. Создание Repository Implementation

```kotlin
// data/repository/MovieRepositoryImpl.kt
class MovieRepositoryImpl(
    private val mcpClient: McpClient
) : MovieRepository {
    
    override suspend fun getPopularMovies(page: Int): Result<MovieListResponse> {
        return try {
            val response = mcpClient.getPopularMovies(page)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

#### 4. Создание Intent Classes

```kotlin
// presentation/movieslist/MoviesListIntent.kt
sealed class MoviesListIntent {
    object LoadPopularMovies : MoviesListIntent()
    object LoadMoreMovies : MoviesListIntent()
    object Retry : MoviesListIntent()
    data class SearchMovies(val query: String) : MoviesListIntent()
}
```

#### 5. Создание State Classes

```kotlin
// presentation/movieslist/MoviesListState.kt
data class MoviesListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val searchQuery: String = ""
)
```

#### 6. Создание ViewModel

```kotlin
// presentation/movieslist/MoviesListViewModel.kt
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
            is MoviesListIntent.SearchMovies -> searchMovies(intent.query)
        }
    }
    
    private fun loadPopularMovies() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val result = movieRepository.getPopularMovies(1)
                when (result) {
                    is Result.Success -> {
                        _state.value = _state.value.copy(
                            movies = result.data.movies,
                            isLoading = false,
                            currentPage = 1
                        )
                    }
                    is Result.Error -> {
                        _state.value = _state.value.copy(
                            error = result.exception.message,
                            isLoading = false
                        )
                    }
                    is Result.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}
```

#### 7. Создание UI Screen

```kotlin
// ui/movieslist/MoviesListScreen.kt
@Composable
fun MoviesListScreen(
    onMovieClick: (Int) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: MoviesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.processIntent(MoviesListIntent.LoadPopularMovies)
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Popular Movies") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )
        
        // Content
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorView(
                message = state.error!!,
                onRetry = { viewModel.processIntent(MoviesListIntent.Retry) }
            )
            state.movies.isNotEmpty() -> MoviesList(
                movies = state.movies,
                onMovieClick = onMovieClick
            )
            else -> EmptyState()
        }
    }
}
```

#### 8. Добавление в Navigation

```kotlin
// navigation/Navigation.kt
composable(Screen.MoviesList.route) {
    MoviesListScreen(
        onMovieClick = { movieId ->
            navController.navigate(Screen.MovieDetail(movieId).createRoute())
        },
        onBackPressed = {
            // Handle back press
        }
    )
}
```

#### 9. Добавление в DI

```kotlin
// presentation/di/PresentationModule.kt
val presentationModule = module {
    viewModel { MoviesListViewModel(get()) }
}
```

### 🔧 Создание новых экранов

#### 1. Создание Screen Route

```kotlin
// navigation/Screen.kt
sealed class Screen(val route: String) {
    // ... existing screens
    data class MovieSearch(val query: String = "") : Screen("movie_search?query={query}") {
        fun createRoute(query: String) = "movie_search?query=$query"
    }
}
```

#### 2. Добавление в Navigation

```kotlin
// navigation/Navigation.kt
composable(
    route = Screen.MovieSearch().route,
    arguments = listOf(
        navArgument("query") {
            type = NavType.StringType
            defaultValue = ""
        }
    )
) { backStackEntry ->
    val query = backStackEntry.arguments?.getString("query") ?: ""
    MovieSearchScreen(
        initialQuery = query,
        onMovieClick = { movieId ->
            navController.navigate(Screen.MovieDetail(movieId).createRoute())
        }
    )
}
```

## 🧪 Testing Guidelines

### 📊 Unit Testing

#### ViewModel Testing

```kotlin
// test/presentation/movieslist/MoviesListViewModelTest.kt
@RunWith(MockKJUnitRunner::class)
class MoviesListViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: MoviesListViewModel
    private lateinit var mockRepository: MovieRepository
    
    @Before
    fun setup() {
        mockRepository = mockk()
        viewModel = MoviesListViewModel(mockRepository)
    }
    
    @Test
    fun `when LoadPopularMovies intent is processed, should load movies`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie"))
        val response = MovieListResponse(movies = movies, page = 1, totalPages = 1)
        coEvery { mockRepository.getPopularMovies(1) } returns Result.Success(response)
        
        // When
        viewModel.processIntent(MoviesListIntent.LoadPopularMovies)
        
        // Then
        val state = viewModel.state.first()
        assertThat(state.movies).isEqualTo(movies)
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
    }
    
    @Test
    fun `when repository returns error, should update state with error`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { mockRepository.getPopularMovies(1) } returns Result.Error(exception)
        
        // When
        viewModel.processIntent(MoviesListIntent.LoadPopularMovies)
        
        // Then
        val state = viewModel.state.first()
        assertThat(state.error).isEqualTo("Network error")
        assertThat(state.isLoading).isFalse()
    }
}
```

#### Repository Testing

```kotlin
// test/data/repository/MovieRepositoryImplTest.kt
@RunWith(MockKJUnitRunner::class)
class MovieRepositoryImplTest {
    
    private lateinit var repository: MovieRepositoryImpl
    private lateinit var mockMcpClient: McpClient
    
    @Before
    fun setup() {
        mockMcpClient = mockk()
        repository = MovieRepositoryImpl(mockMcpClient)
    }
    
    @Test
    fun `getPopularMovies should return success when MCP client succeeds`() = runTest {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie"))
        val response = MovieListResponse(movies = movies, page = 1, totalPages = 1)
        coEvery { mockMcpClient.getPopularMovies(1) } returns response
        
        // When
        val result = repository.getPopularMovies(1)
        
        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(response)
    }
}
```

### 🔗 Integration Testing

```kotlin
// androidTest/presentation/movieslist/MoviesListScreenTest.kt
@RunWith(AndroidJUnit4::class)
class MoviesListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun moviesListScreen_displaysMovies_whenDataIsLoaded() {
        // Given
        val movies = listOf(
            Movie(id = 1, title = "Test Movie 1"),
            Movie(id = 2, title = "Test Movie 2")
        )
        val state = MoviesListState(movies = movies, isLoading = false)
        
        // When
        composeTestRule.setContent {
            TmdbAiTheme {
                MoviesListScreen(
                    onMovieClick = {},
                    onBackPressed = {},
                    viewModel = FakeMoviesListViewModel(state)
                )
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("Test Movie 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Movie 2").assertIsDisplayed()
    }
}
```

### 📱 UI Testing

```kotlin
// androidTest/ui/movieslist/MoviesListScreenTest.kt
@RunWith(AndroidJUnit4::class)
class MoviesListScreenUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun clickOnMovie_navigatesToMovieDetail() {
        // Given
        val movies = listOf(Movie(id = 1, title = "Test Movie"))
        val state = MoviesListState(movies = movies, isLoading = false)
        var clickedMovieId: Int? = null
        
        // When
        composeTestRule.setContent {
            TmdbAiTheme {
                MoviesListScreen(
                    onMovieClick = { movieId -> clickedMovieId = movieId },
                    onBackPressed = {},
                    viewModel = FakeMoviesListViewModel(state)
                )
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("Test Movie").performClick()
        assertThat(clickedMovieId).isEqualTo(1)
    }
}
```

## 🐛 Troubleshooting Common Issues

### ❌ Build Errors

#### Compose Compiler Issues

```bash
# Очистка и пересборка
./gradlew clean
./gradlew assembleDevelopmentDebug
```

#### Dependency Conflicts

```bash
# Просмотр дерева зависимостей
./gradlew app:dependencies

# Принудительное разрешение версий
configurations.all {
    resolutionStrategy.force 'androidx.core:core-ktx:1.12.0'
}
```

### ❌ Runtime Errors

#### Navigation Issues

```kotlin
// ✅ Правильно - проверка аргументов
composable(
    route = Screen.MovieDetail(0).route,
    arguments = listOf(
        navArgument("movieId") {
            type = NavType.IntType
            defaultValue = 1 // Добавьте defaultValue
        }
    )
) { backStackEntry ->
    val movieId = backStackEntry.arguments?.getInt("movieId") ?: 1
    MovieDetailScreen(movieId = movieId)
}
```

#### State Management Issues

```kotlin
// ✅ Правильно - использование StateFlow
class MoviesListViewModel : ViewModel() {
    private val _state = MutableStateFlow(MoviesListState())
    val state: StateFlow<MoviesListState> = _state.asStateFlow()
    
    // Не используйте LiveData в Compose
    // private val _state = MutableLiveData<MoviesListState>()
}
```

### 🔧 Performance Issues

#### Memory Leaks

```kotlin
// ✅ Правильно - использование viewModelScope
class MoviesListViewModel : ViewModel() {
    fun loadMovies() {
        viewModelScope.launch {
            // Coroutine автоматически отменяется при уничтожении ViewModel
            val result = repository.getMovies()
            _state.value = _state.value.copy(movies = result)
        }
    }
}

// ❌ Неправильно - использование GlobalScope
fun loadMovies() {
    GlobalScope.launch {
        // Может привести к утечке памяти
    }
}
```

#### UI Performance

```kotlin
// ✅ Правильно - использование remember и derivedStateOf
@Composable
fun MoviesList(movies: List<Movie>) {
    val sortedMovies by remember(movies) {
        derivedStateOf {
            movies.sortedBy { it.title }
        }
    }
    
    LazyColumn {
        items(sortedMovies) { movie ->
            MovieItem(movie = movie)
        }
    }
}
```

## 📚 Полезные ресурсы

### 🔗 Документация

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Koin](https://insert-koin.io/)
- [Ktor](https://ktor.io/)
- [Material Design 3](https://m3.material.io/)

### 🛠️ Инструменты

- [Android Studio](https://developer.android.com/studio)
- [Layout Inspector](https://developer.android.com/studio/debug/layout-inspector)
- [Profiler](https://developer.android.com/studio/profile)
- [Lint](https://developer.android.com/studio/write/lint)

### 📱 Примеры кода

- [Android Architecture Samples](https://github.com/android/architecture-samples)
- [Jetpack Compose Samples](https://github.com/android/compose-samples)
- [Koin Samples](https://github.com/InsertKoinIO/koin-samples)

---

**Последнее обновление**: 2024-12-19  
**Версия документа**: 1.0.0  
**Статус**: Актуально
