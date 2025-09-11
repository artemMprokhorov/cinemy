# Cinemy Project Rules

## 📋 Documentation Rules

### Single README.md Rule
- **ONLY ONE** file named `README.md` in the entire project
- **Location**: Root directory (`./README.md`)
- **Purpose**: Main project documentation and overview
- **All other documentation files** must be renamed thematically:

#### Thematic Documentation Files:
- `.github/GITHUB_ACTIONS.md` - GitHub Actions and CI/CD documentation
- `.github/workflows/WORKFLOWS.md` - Detailed workflows documentation
- `app/src/main/java/org/studioapp/cinemy/ui/UI_COMPONENTS.md` - UI components documentation
- `app/src/main/java/org/studioapp/cinemy/utils/UTILITIES.md` - Utilities documentation
- `app/src/main/java/org/studioapp/cinemy/data/DATA_LAYER.md` - Data layer documentation
- `app/src/main/java/org/studioapp/cinemy/presentation/PRESENTATION_LAYER.md` - Presentation layer documentation

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

**Last Updated**: 2025-01-XX  
**Status**: Active Rule
