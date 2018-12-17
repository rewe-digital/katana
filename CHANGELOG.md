## Version 1.2.2

_2018-12-17_

* Improve injection performance

## Version 1.2.1

_2018-12-14_

* Add `MEMORY` and `SPEED` profiles to `AndroidEnvironmentContext`.
  Either choose between optimization of memory consumption or execution speed.

## Version 1.2.0

_2018-12-14_

* Add pluggable `EnvironmentContext` for optimizing Katana for specific runtime environments.
  See [Android implementation](android/README.md).
* [Android](android) artifact is now an Android library (AAR) with a dependency on [AndroidX](https://developer.android.com/jetpack/androidx/).
* Android artifact provides module factory functions for support `Fragments`.

## Version 1.1.0

_2018-12-07_

* Convert module DSL functions to extension functions for better code highlighting/readability.
  This is a **breaking change** as `bind()`, `factory()` etc. now have to be imported!

## Version 1.0.2

_2018-12-07_

* Add Android module factory functions to Android artifact
* Update Kotlin version to 1.3.11

## Version 1.0.1

_2018-10-31_

* Update Kotlin dependency to 1.3 

## Version 1.0

_2018-10-29_

* First public release
