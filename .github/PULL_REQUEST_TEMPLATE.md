## Summary

<!-- What does this PR do, and why? Link any related issue with "Closes #123". -->

## Type of change

- [ ] New tool
- [ ] Bug fix
- [ ] Enhancement to an existing tool
- [ ] Refactor / internal change (no user-facing behavior change)
- [ ] Documentation
- [ ] CI / build

## Checklist

- [ ] Tool logic lives in `domain/utilities/` as a pure, side-effect-free object returning a sealed result type (no business logic in Composables)
- [ ] No networking, analytics, or logging of sensitive input was introduced
- [ ] New/changed logic has unit tests in `app/src/test/`
- [ ] `./gradlew lintDebug` passes
- [ ] `./gradlew testDebugUnitTest` passes
- [ ] If this adds a tool: it's registered in `Tool.kt`, wired into `Destinations.kt`, `NavGraph.kt`, and `AppViewModelFactory`
- [ ] README updated (feature list / Future Improvements checklist) if relevant

## Screenshots (if UI changed)

<!-- Before/after screenshots or a short screen recording help a lot here. -->

## Additional notes

<!-- Anything reviewers should pay special attention to. -->
