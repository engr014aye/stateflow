# SlateFlow — Production-Ready Offline-First Android App

SlateFlow is a production-grade, privacy-first Android application designed with an ultra-modern **Liquid Glass (Glassmorphism)** aesthetic, fluid spring physics, and clean **MVVM Architecture**. It unites intuitive note-taking (**Flow Notes**) and actionable task management (**Action Slate**) in a single fluid interface with **zero internet permissions**, guaranteeing 100% offline data confidentiality.

---

## 🏛 Clean Architecture & Tech Stack

- **UI Framework**: 100% Jetpack Compose with Material 3.
- **Language**: Kotlin 2.0.21 with Coroutines & StateFlow reactive streaming.
- **Local Persistence**: Room SQLite Database 2.6.1 compiled via Kotlin Symbol Processing (KSP).
- **Architecture**: Clean MVVM (Data, Domain, UI layers) with decoupled Dependency Injection (`AppContainer` and `SlateFlowViewModelFactory`).
- **Build System**: Gradle 8.9 with Gradle Kotlin DSL (`build.gradle.kts`) and Version Catalogs (`libs.versions.toml`).
- **Edge-to-Edge**: Full screen display (`enableEdgeToEdge()`) with defensive `WindowInsets` padding (`statusBarsPadding`, `navigationBarsPadding`, `imePadding`).

---

## 💎 Liquid Glass UI & Fluid Spring Physics

1. **Animated Mesh Gradient Background**:
   - Multi-orb canvas rendering subtle ambient orbs (Electric Cyan `#00F5D4`, Indigo `#6366F1`, Violet `#8B5CF6`) floating over Deep Obsidian `#070B12` and Midnight Navy `#0F172A`.
2. **LiquidGlassCard Component**:
   - Translucent glass fill with 24.dp rounded corners and vertical gradient borders (`Color.White.copy(alpha = 0.22f)` to `0.04f`).
3. **Tactile Spring Physics (`bouncyClickable`)**:
   - Tactile spring physics via `animateFloatAsState` and `spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)` with automatic 300ms click debouncing to prevent multi-tap races.
4. **Liquid Segmented Control**:
   - Sliding frosted glass pill indicator gliding between "Flow Notes" and "Action Slate".
5. **Interactive Bouncy Checkboxes**:
   - Spring-bouncing checkmarks with real-time strike-through animations and automatic movement to collapsible "Completed" section.

---

## 🛡 100% Offline Privacy Guarantee

- **Zero Network Calls**: `AndroidManifest.xml` contains strictly **ZERO** internet permissions.
- **Zero Third-Party SDKs**: No trackers, no ad networks, no analytics.
- **Local-Only SQLite**: 100% of user data remains on the physical device.
- **In-App Privacy Statement**: Accessible via the top bar security shield icon at any time.

---

## 📁 Project Structure

```
D:\Antigravity\SlateFlow\
├── gradle\
│   ├── libs.versions.toml             # Version Catalog
│   └── wrapper\                      # Gradle 8.9 wrapper
├── app\
│   ├── build.gradle.kts              # Application Gradle script
│   ├── proguard-rules.pro            # R8 rules
│   └── src\main\
│       ├── AndroidManifest.xml       # Zero-permission offline manifest
│       ├── java\com\saddiqueini\slateflow\
│       │   ├── SlateFlowApp.kt        # Application & Dependency Container
│       │   ├── MainActivity.kt        # Edge-to-Edge Host Activity
│       │   ├── data\
│       │   │   ├── local\             # Room Database, DAOs & Converters
│       │   │   ├── model\             # NoteEntity, TaskEntity, Priority, GlassTint
│       │   │   └── repository\        # NoteRepository, TaskRepository
│       │   ├── domain\                # NoteUseCases, TaskUseCases
│       │   └── ui\
│       │       ├── theme\             # Liquid Glass Colors, Typography, Theme
│       │       ├── components\        # Mesh Canvas, LiquidGlassCard, Bouncy Modifiers, Search Bar, Dialogs
│       │       ├── notes\             # NotesScreen, NotesViewModel, NoteEditorDialog
│       │       ├── tasks\             # TasksScreen, TasksViewModel, TaskEditorDialog
│       │       ├── common\            # SlateFlowViewModelFactory
│       │       └── MainSlateScreen.kt # Root screen orchestrator
│       └── res\                       # Drawables, Mipmaps, Strings, Theme, Backup Rules
```

---

## 🎨 Google Play Store Assets

Stored inside `D:\Antigravity\store listing\SlateFlow`:
1. **`app_icon_512x512.png`**: Exact 512x512 32-bit PNG, flat 2D geometric vector matching the app's adaptive launcher icon, full bleed, no drop shadows, strictly non-3D.
2. **`feature_graphic_1024x500.png`**: Exact 1024x500 PNG with deep slate liquid gradient, 2D vector mark, safe text margins, and typography reading *"SlateFlow - Offline Notes & Tasks"*.

---

## 🚀 Building & Packaging

```bash
# Debug APK
./gradlew.bat assembleDebug

# Release APK (R8 Minified & Signed)
./gradlew.bat assembleRelease
```
Both outputs verify with zero compilation and zero linking errors.
