# 🎯 Cursor Integration Guide

This guide explains how to effectively use **Cursor IDE** with the Cinemy project, leveraging its AI capabilities and the comprehensive documentation structure.

## 🚀 Getting Started with Cursor

### 1. **Open the Project**
```bash
# Clone the repository
git clone <repository-url>
cd Cinemy

# Open in Cursor
cursor .
```

### 2. **Understand the Documentation Structure**
The project uses a centralized documentation approach:
- **📁 `docs/`** - All documentation files
- **📄 `README.md`** - Main project overview (in root)
- **🔗 Links** - All documentation links point to `docs/` folder

## 🤖 Leveraging Cursor's AI Features

### **Code Understanding**
Use Cursor's AI to understand the codebase by referencing documentation:

```
@docs/ARCHITECTURE_GUIDE.md How does the MVI pattern work in this project?
@docs/DEVELOPMENT_GUIDE.md What are the build variants and how to use them?
@docs/FOLDABLE_DEVICE_SUPPORT.md How is foldable device support implemented?
```

### **Development Workflow**
```
@docs/DEVELOPMENT_GUIDE.md How do I build the prod version without leak detection?
@docs/QA_TESTING_GUIDE.md What testing strategies should I follow?
@docs/TROUBLESHOOTING.md I'm getting build errors, what should I check?
```

### **Architecture Questions**
```
@docs/ARCHITECTURE_GUIDE.md Explain the data layer architecture
@docs/ARCHITECTURE_GUIDE.md How does the presentation layer work?
@docs/ARCHITECTURE_GUIDE.md What is the MVI pattern implementation?
```

### **Layer-Specific Questions**
```
@docs/DATA_LAYER.md How does the repository pattern work?
@docs/ML_LAYER.md How does sentiment analysis work?
@docs/NAVIGATION_LAYER.md How is navigation implemented?
@docs/PRESENTATION_LAYER.md How do ViewModels work?
@docs/UI_COMPONENTS_LAYER.md How are UI components structured?
@docs/UTILS_LAYER.md How does device detection work?
```

## 📚 Documentation Navigation

### **Start Here**
1. **📋 [Documentation Overview](./DOCUMENTATION_OVERVIEW.md)** - Complete roadmap
2. **🏗️ [Architecture Guide](./ARCHITECTURE_GUIDE.md)** - Technical architecture
3. **👨‍💻 [Development Guide](./DEVELOPMENT_GUIDE.md)** - Setup and workflow

### **Feature-Specific Documentation**
- **📱 [Foldable Device Support](./FOLDABLE_DEVICE_SUPPORT.md)** - Foldable implementation
- **♿ [Accessibility Guide](./ACCESSIBILITY_GUIDE.md)** - Accessibility features
- **🔧 [Dependencies Info](./DEPENDENCIES_INFO.md)** - Dependency management
- **🧪 [QA Testing Guide](./QA_TESTING_GUIDE.md)** - Testing procedures

### **Layer-Specific Documentation**
- **🗄️ [Data Layer](./DATA_LAYER.md)** - Data layer architecture and implementation
- **🤖 [ML Layer](./ML_LAYER.md)** - Machine learning and sentiment analysis
- **🧭 [Navigation Layer](./NAVIGATION_LAYER.md)** - Navigation and routing system
- **🎨 [Presentation Layer](./PRESENTATION_LAYER.md)** - ViewModels and state management
- **🖼️ [UI Components Layer](./UI_COMPONENTS_LAYER.md)** - UI components and theming
- **🔧 [Utils Layer](./UTILS_LAYER.md)** - Utility classes and helper functions

### **Project Management**
- **📝 [Changelog](./CHANGELOG.md)** - Project history
- **❓ [Troubleshooting](./TROUBLESHOOTING.md)** - Common issues
- **🔒 [Security & Gitignore](./SECURITY_AND_GITIGNORE.md)** - Security considerations

## 🎯 Cursor-Specific Tips

### **1. Use @docs/ References**
When asking Cursor about the project, reference specific documentation:
```
@docs/ARCHITECTURE_GUIDE.md @app/src/main/java/org/studioapp/cinemy/presentation/ How does the ViewModel work?
```

### **2. Context-Aware Development**
Cursor can understand the project better when you provide context:
```
@docs/DEVELOPMENT_GUIDE.md I need to add a new feature following the MVI pattern
@docs/ARCHITECTURE_GUIDE.md How should I structure a new screen component?
@docs/PRESENTATION_LAYER.md How should I implement a new ViewModel?
@docs/UI_COMPONENTS_LAYER.md How should I create a new UI component?
```

### **3. Build and Testing**
```
@docs/DEVELOPMENT_GUIDE.md How do I build the dummy version?
@docs/QA_TESTING_GUIDE.md What testing approach should I use for this feature?
```

### **4. Troubleshooting**
```
@docs/TROUBLESHOOTING.md I'm getting this error: [paste error]
@docs/DEPENDENCIES_INFO.md How do I update dependencies safely?
```

## 🔧 Development Workflow

### **1. Project Setup**
```bash
# Follow the Development Guide
@docs/DEVELOPMENT_GUIDE.md
```

### **2. Understanding Architecture**
```bash
# Study the architecture
@docs/ARCHITECTURE_GUIDE.md
```

### **3. Building and Testing**
```bash
# Build without leak detection
./gradlew clean assembleProdDebug

# Build dummy version
./gradlew clean assembleDummyDebug
```

### **4. AI-Assisted Development**
Use Cursor's AI with documentation context:
- **Code generation** - Reference architecture patterns
- **Bug fixing** - Use troubleshooting guide
- **Feature development** - Follow development guidelines
- **Testing** - Use QA testing strategies

## 📖 Best Practices

### **1. Always Reference Documentation**
When working with Cursor, always provide documentation context:
```
@docs/ARCHITECTURE_GUIDE.md How should I implement this feature?
```

### **2. Use Specific Documentation**
Be specific about which documentation to reference:
```
@docs/DEVELOPMENT_GUIDE.md (not just @docs/)
```

### **3. Combine Code and Documentation**
```
@docs/ARCHITECTURE_GUIDE.md @app/src/main/java/org/studioapp/cinemy/data/ How does the repository pattern work?
@docs/DATA_LAYER.md @app/src/main/java/org/studioapp/cinemy/data/ How does the MCP client work?
@docs/ML_LAYER.md @app/src/main/java/org/studioapp/cinemy/ml/ How does sentiment analysis work?
```

### **4. Follow Project Conventions**
```
@docs/PROJECT_RULES.md What are the coding standards for this project?
@docs/DEVELOPMENT_GUIDE.md How should I structure new components?
```

## 🎯 Quick Commands

### **Build Commands**
```bash
# Prod version (no leak detection)
./gradlew clean assembleProdDebug

# Dummy version
./gradlew clean assembleDummyDebug

# Install and launch
./gradlew installProdDebug
./gradlew installDummyDebug
```

### **Documentation Commands**
```bash
# View all documentation
ls docs/

# Open specific documentation
cursor docs/ARCHITECTURE_GUIDE.md
cursor docs/DEVELOPMENT_GUIDE.md
```

## 🚀 Getting Help

### **1. Check Documentation First**
- **📋 [Documentation Overview](./DOCUMENTATION_OVERVIEW.md)** - Complete guide
- **❓ [Troubleshooting](./TROUBLESHOOTING.md)** - Common issues

### **2. Use Cursor AI with Context**
```
@docs/TROUBLESHOOTING.md I'm having this issue: [describe issue]
```

### **3. Reference Architecture**
```
@docs/ARCHITECTURE_GUIDE.md How should I approach this problem?
```

## 🎯 Summary

The Cinemy project is designed to work seamlessly with Cursor IDE:

1. **📁 Centralized Documentation** - All docs in `docs/` folder
2. **🔗 Easy Navigation** - Clear links and structure
3. **🤖 AI Integration** - Documentation works with Cursor's AI
4. **📚 Comprehensive Coverage** - From architecture to troubleshooting
5. **🎯 Context-Aware** - Use @docs/ references for better AI responses

Start with the [Documentation Overview](./DOCUMENTATION_OVERVIEW.md) and use Cursor's AI capabilities to navigate and understand the project effectively!
