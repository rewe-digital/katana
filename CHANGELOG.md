## Version 1.10.0

_2020-01-03_

Happy New Year 🎉

* Add optional injection via injectOrNull() / getOrNull()

## Version 1.9.0

_2019-12-04_

With this release Santa Claus is bringing some premature Christmas presents 😉

* Add `alias` keyword for binding an existing declaration under a different type and/or name
* Add `get` keyword inside `set` declaration for assigning an existing dependency to a set 
* Update Kotlin to 1.3.61
* Remove deprecated functions &amp; features

Special thanks to @dave08 for feature requests &amp; feedback 🙂  
Wishing all users happy holidays &amp; a happy new year! 🎉

## Version 1.8.3

_2019-10-21_

* Bugfix: Fixed bug introduced in 1.8.2 regarding Activity ViewModel injection

## Version 1.8.2

_2019-10-21_

* AndroidX ViewModel support: Remove usage of deprecated `lifecycle-extensions`

## Version 1.8.1

_2019-10-18_

* Rename module factory functions of `katana-android` artifact
* Update AndroidX Lifecycle and ViewModel dependencies

## Version 1.8.0

_2019-10-10_

* Add `set` multi-binding functionality
* Remove deprecated functions
* Deprecate old DSL and promote "compact" DSL to default DSL
* **Note:** Changes of this version are source but **not binary** compatible!

## Version 1.7.1

_2019-08-29_

* Update dependencies (Kotlin 1.3.50)
* Update `lifecycle-viewmodel-savedstate` to 1.0.0-alpha03

## Version 1.7.0

_2019-06-05_

* Add experimental support for ViewModel injection with [saved state](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate).  
  Please use artifact `org.rewedigital.katana:katana-androidx-viewmodel-savedstate:1.7.0-alpha01`.

## Version 1.6.3

_2019-05-16_

* Add `Component.Builder` pattern
* Downgrade Android plugin to 3.3.2 (workaround for issue #7)

## Version 1.6.2

_2019-04-29_

* `savedInstanceState` is passed to `KatanaFragment(Delegate)` 
* Kotlin dependency has been updated to 1.3.31.

## Version 1.6.1

_2019-04-23_

* Maintenance release. Kotlin dependency has been updated to 1.3.30.

## Version 1.6.0

_2019-03-29_

* `createModule()` and `createComponent()` have been deprecated. Use constructors of `Module` and `Component` instead. 

## Version 1.5.1

_2019-03-28_

* Fix initialization of transitive eager singletons

## Version 1.5.0

_2019-03-26_

* Allow `Any` references for dependency names.  
  Now String constants, Kotlin `object`s, enum classes and custom (data) classes representing a unique identity / 
  equality can be used for names.
* Modules may include other modules during declaration via `includes` attribute.

## Version 1.4.0

_2019-03-18_

* Add [AndroidX ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) support via new 
  [katana-androidx-viewmodel](./androidx-viewmodel) artifact.
  
  Thanks to @inshiro for idea & initial code :)

## Version 1.3.0

_2019-02-08_

* Add a new, compact DSL for module declarations.  
  **Imports must be updated!** Either use `org.rewedigital.katana.dsl.classic` or the new 
  `org.rewedigital.katana.dsl.compact`:
  ```kotlin
  createModule {
    factory { MyDependencyA() }
    singleton { MyDependencyB() }  
  }
  ``` 
* Update Kotlin version to 1.3.21

## Version 1.2.8

_2019-02-06_

* Add `KatanaFragmentDelegate` and `KatanaFragment` classes to Android artifact
  for simplifying usage of Katana with Fragments.

## Version 1.2.7

_2019-02-04_

* Implement `+` operator for `Component`, allowing different syntax for creating child components:
  ```kotlin
  val childComponent = component + childModule
  // or
  val childComponent = component + listOf(childModule1, childModule2)
  // or
  val childComponent = listOf(component1, component2) + childModule
  ```

## Version 1.2.6

_2019-01-25_

* Update Kotlin version to 1.3.20

## Version 1.2.5

_2019-01-21_

* First version deployed on [JCenter](https://bintray.com/bintray/jcenter)
* No code changes

## Version 1.2.4

_2019-01-09_

* Fix wrong `OverrideException` for internal module declarations
* Permit `null` values of (eager) singletons

## Version 1.2.3

_2018-12-19_

* Permit injection of `null` values
* Permit internal module bindings

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
