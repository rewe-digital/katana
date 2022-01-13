[![Build Status](https://github.com/rewe-digital/katana/workflows/Build/badge.svg)](https://github.com/rewe-digital/katana/actions?query=workflow%3ABuild) [![Kotlin](https://img.shields.io/badge/Kotlin-1.5.0-brightgreen.svg)](https://kotlinlang.org) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Katana-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7365)

‼️ This project is not maintained anymore. For more information
read [this statement](https://github.com/rewe-digital/katana/issues/22). ‼️

# Katana

Katana is a lightweight, minimalistic dependency injection library (similar to the service locator pattern) for Kotlin
on the JVM, designed especially with Android in mind.

* Extremely lightweight footprint (only ~15kB in classes and ~130 methods after ProGuard), plain Kotlin, no third-party dependencies
* It's [fast](./speed-comparison) (also see this [comparison](https://github.com/Sloy/android-dependency-injection-performance))
* "Less is more", therefore:
  * No global singleton state. Likelihood of memory leaks is greatly reduced unless **YOU** are doing something wrong ;P 
  * No reflection (see [this](#a-word-on-type-erasure) regarding type erasure)
  * No code generation (unless `inline` functions count)
  * No dependency overrides possible (see [Overrides](./Getting%20Started.md#overrides))

## Getting Started

Want to know more? Please read [Getting Started](./Getting%20Started.md).

## Artifacts

Katana consists of a core library and several additional libraries that extend
Katana's functionality. All artifact are published in group 
`org.rewedigital.katana`. Here's a quick overview of all available artifacts:

| Artifact                                                                | Description                                                                                   |
| ----------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| katana-core                                                             | Provides core functionality. Suitable for plain Kotlin (JVM) and Android.                     |
| [katana-android](./android)                                             | Android-specific extensions like modules for `Activity` and `Fragment`, `KatanaFragment` etc. |
| [katana-androidx-fragment](./androidx-fragment)                         | Additional support for `androidx.fragment` providing a Katana-based `FragmentFactory`.        |
| [katana-androidx-viewmodel](./androidx-viewmodel)                       | Enables dependency injection for AndroidX ViewModel.                                          |
| [katana-androidx-viewmodel-savedstate](./androidx-viewmodel-savedstate) | Support for AndroidX ViewModel with SavedState.                                               |

## License

The MIT license (MIT)

Copyright (c) 2018-2021 REWE Digital GmbH

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
