# AI ASSISTANT GUIDE - TmdbAi Project

> **ВАЖНО:** Этот документ является reference guide для взаимодействия с AI ассистентом по проекту TmdbAi. Копируйте ссылку на этот файл в начале каждого нового чата с AI.

## ПРОЕКТ OVERVIEW

**Репозиторий:** https://github.com/artemMprokhorov/TmdbAi.git  
**Тип проекта:** Обучающий Android проект (без публикации в Play Store)  
**Цель:** Демонстрация современной Android архитектуры с AI-интеграцией

## ТЕХНИЧЕСКИЕ ТРЕБОВАНИЯ

### Android App Stack:

- **Архитектура:** Clean Architecture + MVI pattern (БЕЗ domain слоя)
- **UI:** Jetpack Compose
- **Навигация:** Jetpack Navigation 3.0 (type-safe)
- **Коммуникация:** AI MCP protocol для всех backend вызовов
- **Изображения:** Jetpack Coil для загрузки
- **DI:** Koin
- **Тесты:** Unit tests coverage обязательно
- **UI Стандартизация:** Переиспользуемые компоненты в отдельном пакете
- **Network:** Стандартизированный слой в отдельном пакете
- **Server-Driven UI:** Рендеринг UI с параметрами (цвета, тексты) с backend через MCP

### Backend Stack:

- **Туннель:** Ngrok для доступа к локальному серверу
- **Контейнеры:** Docker для изоляции сервисов
- **Workflow:** N8N для автоматизации
- **AI:** Локальная AI модель для обработки MCP calls
- **Протокол:** MCP для коммуникации с Android app

### DevOps:

- **IDE:** Cursor для AI-assisted разработки
- **CI/CD:** GitHub Actions
- **Артефакты:** Генерация подписанного APK
- **Безопасность:** Хранение ключей в GitHub Secrets

## АРХИТЕКТУРА ПРИЛОЖЕНИЯ

### App Flow:

```
Splash Screen → Principal Page → Movie Detail Page
     ↓              ↓                ↓
  MCP Call    MCP + Images     MCP + Images
     ↓              ↓                ↓
 AI Processing  TMDB Images    Detail Data
```

### MVI без Domain слоя:

```
data/
├── model/           # Модели и Result types
├── repository/      # Repository интерфейсы и реализации  
├── remote/          # MCP client и network
├── mcp/             # MCP protocol implementation
└── di/              # Dependency injection

presentation/
├── commons/         # Базовые классы
├── splash/          # Splash screen logic
├── movieslist/      # Movies list MVI logic
├── moviedetail/     # Movie detail MVI logic
└── di/              # Presentation DI

ui/
├── components/      # 🆕 Переиспользуемые компоненты
├── network/         # 🆕 Стандартизированный network слой
├── splash/          # Splash UI
├── movieslist/      # Movies list UI
├── moviedetail/     # Movie detail UI
└── theme/           # Dynamic theming
```

## КЛЮЧЕВЫЕ ОСОБЕННОСТИ РЕАЛИЗАЦИИ

### 1. MCP Protocol Integration

- Все backend коммуникации только через MCP
- Локальная AI модель обрабатывает MCP вызовы
- JSON responses с UI конфигурацией

### 2. Server-Driven UI

- Backend отправляет цвета для кнопок, текстов, фонов
- Dynamic theming на основе AI анализа контента
- Адаптивные стили компонентов

### 3. Hybrid Data Loading

- Метаданные фильмов через MCP (с AI обработкой)
- Изображения напрямую с TMDB через Coil
- Кэширование и оптимизация загрузки

### 4. AI Enhancement

- Локальная модель анализирует данные фильмов
- Генерирует персонализированные UI конфигурации
- Обогащает контент дополнительной информацией

## ЭТАПЫ РАЗРАБОТКИ

### ФАЗА 1: FOUNDATION (1-2 недели)

- [ ] Cursor IDE настройка и AI configuration
- [ ] GitHub Actions для APK generation
- [ ] Keystore generation и secrets setup
- [ ] Базовая MCP client архитектура

### ФАЗА 2: BACKEND INFRASTRUCTURE (1-2 недели)

- [ ] Docker + N8N environment
- [ ] Ngrok tunnel configuration
- [ ] Local AI model setup (Ollama/Llama)
- [ ] MCP server implementation
- [ ] TMDB API integration в AI agent

### ФАЗА 3: ANDROID CORE (2-3 недели)

- [ ] Clean Architecture без domain слоя
- [ ] MVI pattern implementation
- [ ] Navigation 3.0 type-safe setup
- [ ] Koin DI configuration
- [ ] Base UI components package

### ФАЗА 4: UI & UX (2-3 недели)

- [ ] Server-driven UI implementation
- [ ] Dynamic theming system
- [ ] Jetpack Coil image loading
- [ ] Standardized network layer
- [ ] Responsive design для всех экранов

### ФАЗА 5: AI INTEGRATION (1-2 недели)

- [ ] MCP protocol полная интеграция
- [ ] AI model fine-tuning для UI generation
- [ ] Performance optimization
- [ ] Error handling и fallbacks

### ФАЗА 6: TESTING & POLISH (1-2 недели)

- [ ] Unit tests coverage (минимум 80%)
- [ ] UI tests для критических flows
- [ ] Performance testing и optimization
- [ ] APK generation и signing verification

## AI MODELS TRAINING ROADMAP

### Базовые модели для изучения:

1. **Ollama + Llama 3.1** - основная модель для MCP
1. **Color Palette Extraction** - CV модель для анализа постеров
1. **UI Configuration Generator** - fine-tuned модель для стилей
1. **Content Enhancement** - NLP модель для обогащения описаний

### Обучающие материалы:

- Создание custom datasets из TMDB данных
- Fine-tuning transformer моделей для UI задач
- RAG системы для контекстных ответов
- MLOps для мобильных приложений

## CURSOR AI PROMPTS ШАБЛОНЫ

### Для архитектурных компонентов:

```
"Generate MVI ViewModel for [Feature] following Clean Architecture without domain layer, using StateFlow for state management and Koin for DI"
```

### Для UI компонентов:

```
"Create Jetpack Compose component for [Component] with server-driven styling support, accepting UiConfig parameter for dynamic theming"
```

### Для MCP integration:

```
"Implement MCP client method for [endpoint] with proper error handling using Result sealed class and Kotlin coroutines"
```

## TROUBLESHOOTING GUIDE

### Общие проблемы и решения:

- **MCP Connection Issues:** Проверить Ngrok tunnel и Docker containers
- **UI Config Not Applied:** Валидация JSON parsing и color conversion
- **Image Loading Problems:** Coil configuration и TMDB URL validation
- **Navigation Issues:** Type-safe routes и proper serialization

## КАЧЕСТВЕННЫЕ МЕТРИКИ

### Code Quality Targets:

- **Test Coverage:** > 80%
- **Build Time:** < 2 минуты
- **APK Size:** < 50MB
- **Startup Time:** < 3 секунды
- **Memory Usage:** < 150MB peak

### AI Model Performance:

- **UI Config Generation:** < 500ms response time
- **Content Enhancement:** > 85% user satisfaction
- **Color Palette Accuracy:** > 90% aesthetic approval

## CONTACT POINTS С AI ASSISTANT

### Для новых чатов используйте формат:

```
🎯 ПРОЕКТ: TmdbAi Android App с AI MCP Backend
📋 ФАЗА: [Текущая фаза разработки]
🔗 REFERENCE: [Ссылка на этот файл в GitHub]
❓ ЗАДАЧА: [Конкретный вопрос/задача]
```

### Типы взаимодействия:

1. **Architectural Reviews:** Анализ через GitHub URLs
1. **Code Generation:** Промпты для Cursor + direct code помощь
1. **Debugging:** Прямые файлы и error logs
1. **AI Training:** Datasets, model configs, training scripts
1. **Progress Tracking:** Regular checkpoint reviews

## РЕСУРСЫ И ССЫЛКИ

### Документация:

- [MCP Protocol Spec](https://spec.modelcontextprotocol.io/)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Navigation 3.0 Guide](https://developer.android.com/guide/navigation)
- [Koin Documentation](https://insert-koin.io/)

### AI/ML Resources:

- [Ollama Documentation](https://ollama.ai/docs)
- [Hugging Face Transformers](https://huggingface.co/transformers)
- [MLflow for Experiment Tracking](https://mlflow.org/)

-----

## 📝 CHANGELOG

- **2025-09-02:** Initial project setup and requirements definition
- **[ДАТА]:** [Добавляйте изменения по мере разработки]

-----

*Этот документ обновляется по мере развития проекта. Всегда ссылайтесь на latest version в GitHub.*