# x-DevPocket

[![Android CI](https://github.com/XTech-Head/x-DevPocket/actions/workflows/android-ci.yml/badge.svg)](https://github.com/XTech-Head/x-DevPocket/actions/workflows/android-ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Min SDK](https://img.shields.io/badge/minSdk-26-informational)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?logo=kotlin&logoColor=white)

**Your developer toolkit, in your pocket.**

x-DevPocket is an offline-first Android developer utility toolkit. No backend, no account, no API keys, no subscriptions — just a fast, private, pocket-sized toolbox for the operations developers reach for every day.


---

## Overview

x-DevPocket brings common developer operations onto your phone: JSON formatting, Base64 encoding, hashing, JWT decoding, regex testing, timestamp conversion, and more. Every tool runs entirely on-device using Kotlin/Java standard libraries — nothing is ever uploaded, logged, or transmitted.

## Features

- **JSON** — Formatter, minifier, validator with clear error messages
- **Base64** — Encode/decode with swap and history
- **URL Encoder** — Encode/decode query strings and paths
- **Hash Generator** — MD5, SHA-1, SHA-256, SHA-384, SHA-512 (with a warning on non-recommended algorithms)
- **JWT Decoder** — Decode header & payload locally; signature is never verified, token never leaves the device
- **UUID Generator** — Generate one or many UUID v4 values
- **Regex Tester** — Live pattern matching with match/group breakdown and quick examples
- **Timestamp Converter** — Unix seconds/milliseconds ↔ readable date, both directions
- **.gitignore Generator** — Combine Android, Kotlin/Gradle, IDE, OS, Node, Python & secrets templates into one file, duplicates removed automatically
- **Commit Message Builder** — Structured [Conventional Commits](https://www.conventionalcommits.org) messages (type, scope, subject, body, breaking change, issue refs)
- **History** — Every operation optionally saved locally, searchable, deletable
- **Favorites** — Pin your most-used tools to the top of Home
- **Global Search** — Find any tool instantly from Home
- **Settings** — Theme (dark/light/system), history/auto-copy/clear behavior, data clearing


## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- Kotlin Coroutines + StateFlow
- ViewModel
- Room (local persistence)
- DataStore Preferences (settings)
- Android Clipboard & Sharesheet APIs

No networking libraries. No analytics. No ads.

## Architecture

```
UI (Compose)
  ↓
ViewModel (StateFlow, one per screen)
  ↓
Repository (DeveloperRepository — single access point)
  ↓
Room DAOs · DataStore · Utility engines (domain/utilities)
```

MVVM + Repository pattern, with a hand-rolled `ViewModelProvider.Factory` (`AppViewModelFactory`) instead of a DI framework — the app is small enough that this keeps things simple while staying testable and swappable later.

Business logic never lives inside Composables. All tool logic (JSON parsing, hashing, regex, etc.) lives in `domain/utilities/` as pure, side-effect-free objects that return sealed result types (`Success` / `Error`) — this is what lets every tool fail gracefully on bad input instead of crashing.

## Privacy

Developer data is sensitive by nature — API keys, tokens, passwords, private URLs, source snippets. x-DevPocket is built around that:

- Nothing is ever uploaded or transmitted over the network
- No analytics, no ads, no tracking
- No sensitive input is logged to Logcat
- JWT decoding is explicitly decode-only — the signature is never checked, and the token never leaves your device
- All persistence (history, favorites, settings) is local-only, via Room and DataStore

## Offline-First Design

The app has zero required network dependencies. Every tool works in airplane mode. There is no Firebase, Supabase, Neon, Clerk, or third-party API involved — everything is Android/Kotlin standard library or AndroidX.

## Installation

1. Clone the repo
2. Open in Android Studio (Koala or newer recommended)
3. Let Gradle sync
4. Run on a device or emulator (minSdk 26 / Android 8.0+)

## Building

```bash
./gradlew assembleDebug     # debug APK
./gradlew assembleRelease   # release APK (minified, shrunk)
```

Output APKs land in `app/build/outputs/apk/`.

## Project Structure

```
com.xtech.xdevpocket/
├── data/
│   ├── local/            # Room entities + DAOs
│   ├── preferences/      # DataStore wrapper
│   └── repository/       # DeveloperRepository — single source of truth
├── domain/
│   ├── model/             # Tool registry / categories
│   └── utilities/         # Pure tool engines (JSON, Base64, Hash, JWT, Regex, etc.)
├── presentation/
│   ├── navigation/         # NavGraph, destinations, ViewModel factory
│   ├── screens/            # One package per screen (home, json, base64, ...)
│   ├── components/         # Reusable Compose building blocks
│   └── theme/              # Color tokens, typography, Material3 theme
└── MainActivity.kt
```

## Future Improvements

```
[x] JSON tools
[x] Base64
[x] URL encoder
[x] Hashing
[x] UUID
[x] JWT decoder
[x] Regex
[x] Timestamp
[x] History
[x] Favorites
[x] Search
[x] Case converter
[x] Random string generator
[x] Color converter
[x] Cron expression helper
[x] XML formatter
[x] SQL formatter
[x] HTTP request builder
[x] .gitignore generator
[x] Commit message builder (Conventional Commits)

[ ] Text cleaner
```

## Testing

Domain logic (`domain/utilities/`) is covered by local unit tests in `app/src/test/`, run via JUnit4 and Robolectric (Robolectric is only needed where a utility touches an Android class, like `android.util.Base64` or `org.json`; pure-Kotlin utilities run as plain JVM tests). Run them with:

```bash
./gradlew testDebugUnitTest
```

This is also the first place to look if you're new to the codebase — each test file doubles as a set of usage examples for its utility.

## Sharing Results

Every tool's output can be copied to clipboard or pushed to the Android Sharesheet (Share button next to Copy). Nothing is sent anywhere automatically — sharing is always a deliberate, user-initiated action, consistent with the app's offline/privacy-first design.

## CI

`.github/workflows/android-ci.yml` runs on every push/PR to `main`: lint, unit tests, and a debug APK build, with the APK and lint report uploaded as workflow artifacts. Dependabot (`.github/dependabot.yml`) opens weekly PRs to keep Gradle dependencies and Actions versions current.

## Contributing

Bug reports and PRs are welcome — see [CONTRIBUTING.md](CONTRIBUTING.md) for the local setup, coding conventions, and PR checklist, and please follow the [Code of Conduct](CODE_OF_CONDUCT.md). Found a security issue? Please don't open a public issue — see [SECURITY.md](SECURITY.md) instead.

## License

MIT — see [LICENSE](LICENSE).
