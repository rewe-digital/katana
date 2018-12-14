This artifact provides:

  * Module factory functions for `Application`, `Activity` and `Fragment` modules.
    See functions under `org.rewedigital.katana.android.modules`.

  * An `AndroidEnvironmentContext` for optimizing Katana's behaviour on Android.
    See `Katana.environmentContext`.

  * Android specific Katana logger implementation `AndroidKatanaLogger`.

## Optimizing Katana on Android

This artifact provides an `AndroidEnvironmentContext`, which optimizes Katana's behaviour on Android. To enable this 
`EnvironmentContext`, assign it to `Katana.environmentContext` as soon as possible in your application's lifecycle and 
**before** any Katana modules and components are created.

```kotlin
Katana.environmentContext = AndroidEnvironmentContext()
```

By default this context improves memory consumption at the cost of speed. If memory is not a concern but speed is,
use `AndroidEnvironmentContext(profile = SPEED)`.

## ProGuard

Katana does not require any specific ProGuard configuration 😎.
