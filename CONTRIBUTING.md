# Contributing to x-DevPocket

Thanks for considering a contribution. This project stays small and offline-first on purpose, so please read the "Is this a good fit?" section before writing code.

## Is this a good fit?

x-DevPocket ships **zero required network dependencies** — every tool works in airplane mode, nothing is ever uploaded or logged. Before proposing a feature, check that it can be implemented as pure on-device logic. If it fundamentally needs a network call (e.g. "check the latest version of a package on npm"), open an issue first to discuss it rather than sending a PR — these are evaluated case by case against the app's privacy/offline principles.

## Local setup

1. Fork and clone the repo.
2. Open the project in Android Studio (Koala or newer).
3. Let Gradle sync.
4. Run on a device or emulator (minSdk 26 / Android 8.0+).

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

## Project conventions

- **Business logic lives in `domain/utilities/`**, as pure, side-effect-free Kotlin objects that return a sealed result type (`TextOpResult.Success` / `TextOpResult.Error`). Composables and ViewModels should never contain parsing/formatting/crypto logic directly.
- **MVVM + Repository**: each screen has one `ViewModel` (StateFlow-based) and reads/writes through `DeveloperRepository`. There's no DI framework — `AppViewModelFactory` is a hand-rolled `ViewModelProvider.Factory`; add new ViewModels there.
- **No networking, no analytics, no logging of sensitive input.** This is a hard rule, not a style preference — see the Privacy section of the README.

## Adding a new tool

A new tool touches five places:

1. `domain/utilities/YourToolUtility.kt` — the actual logic, pure Kotlin, unit-testable in isolation.
2. `domain/model/Tool.kt` — register a `Tool` entry (id, title, subtitle, category, icon, route).
3. `presentation/navigation/Destinations.kt` — add a `Destination` for the route.
4. `presentation/navigation/NavGraph.kt` and `presentation/navigation/ViewModelFactory.kt` — wire the screen and ViewModel in.
5. `presentation/screens/yourtool/` — `YourToolScreen.kt` (Compose UI) + `YourToolViewModel.kt` (StateFlow-based state).

Look at an existing simple tool (e.g. `randomstring/` or `gitignore/`) as a template — the pattern is consistent across all tools.

## Tests

Add unit tests for new/changed logic under `app/src/test/java/.../domain/utilities/`. Tests are plain JUnit4 (Robolectric only where a utility touches an Android class). Each test file doubles as usage documentation for its utility, so favor clear, descriptive test names.

## Pull requests

- Keep PRs focused — one tool or fix per PR is easier to review than a bundle of unrelated changes.
- Fill out the PR template checklist.
- Make sure `./gradlew lintDebug` and `./gradlew testDebugUnitTest` pass before requesting review; CI will also check this.
- Update the README (feature list and/or "Future Improvements" checklist) if you're adding or removing a tool.

## Reporting bugs / requesting features

Use the issue templates — they ask for the details that make triage fast (repro steps, device info for bugs; the offline-fit question for feature requests).

## Security issues

Please don't open a public issue for a security vulnerability — see [SECURITY.md](SECURITY.md).
