# Cinemy Project Rules

## 📋 Documentation Rules

### Single README.md Rule
- **ONLY ONE** file named `README.md` in the entire project
- **Location**: Root directory (`./README.md`)
- **Purpose**: Main project documentation and overview
- **All other documentation files** must be renamed thematically:

#### Thematic Documentation Files:
- `docs/app_layers/APPLICATION_LAYER.md` - Application layer documentation
- `docs/app_layers/UI_COMPONENTS_LAYER.md` - UI components documentation
- `docs/app_layers/UTILS_LAYER.md` - Utilities documentation
- `docs/app_layers/DATA_LAYER.md` - Data layer documentation
- `docs/app_layers/PRESENTATION_LAYER.md` - Presentation layer documentation
- `docs/app_layers/NAVIGATION_LAYER.md` - Navigation layer documentation
- `docs/app_layers/ML_LAYER.md` - Machine Learning layer documentation
- `docs/guides/ARCHITECTURE_GUIDE.md` - Architecture guide
- `docs/guides/DEVELOPMENT_GUIDE.md` - Development guide
- `docs/guides/PROJECT_GUIDE.md` - Project guide
- `docs/guides/CURSOR_INTEGRATION_GUIDE.md` - Cursor integration guide
- `docs/guides/QA_TESTING_GUIDE.md` - QA testing guide
- `docs/guides/QA_TESTING_TAGS_GUIDE.md` - QA testing tags guide
- `docs/guides/ACCESSIBILITY_GUIDE.md` - Accessibility guide
- `docs/guides/MCP_INTEGRATION_GUIDE.md` - MCP integration guide

### Why This Rule Exists:
1. **Prevents Confusion** - No duplicate README files
2. **Clear Structure** - Each file has a specific purpose
3. **Easy Navigation** - Developers know where to find specific documentation
4. **Maintainability** - Single source of truth for main project info

## 🚫 What NOT to Do:
- ❌ Create multiple `README.md` files in different directories
- ❌ Use generic names like `README.md` for specific documentation
- ❌ Duplicate documentation content across multiple files

## ✅ What TO Do:
- ✅ Use thematic names for documentation files
- ✅ Keep only one `README.md` in the root
- ✅ Update links when renaming documentation files
- ✅ Use descriptive names that indicate the file's purpose

## 🔧 Enforcement:
- Always check for existing `README.md` files before creating new ones
- Use `find . -name "README.md" -type f` to verify only one exists
- Rename any additional `README.md` files to thematic names
- Update all references and links after renaming

---

**Last Updated**: 2025-01-27  
**Status**: Active Rule
