[![Release](https://jitpack.io/v/rewe-digital-incubator/katana.svg)](https://jitpack.io/#rewe-digital-incubator/katana)

# Because a Katana is better than a dagger! ;)

Katana is a lightweight, minimalistic dependency injection framework (similar to the service locator pattern) for Kotlin
on the JVM, designed especially with Android in mind.

* Extremely lightweight footprint (only ~60kb), plain Kotlin, no third-party dependencies
* It's [fast](./speed-comparison)
* "Less is more", therefore:
  * No global singleton state. Likelihood of memory leaks is greatly reduced unless **YOU** are doing something wrong ;P 
  * No reflection (see [this](#a-word-on-type-erasure) regarding type erasure)
  * No code generation (unless `inline` functions count)
  * No dependency overrides possible (see [Overrides](#overrides))

## Getting started

Katana consists of two core concepts: modules and components.

### Module

A module describes **how** dependencies are provided. Each module should represent a logical unit. For instance there
might be a module for every feature of your application. Modules are created with `createModule()`:

```kotlin
val myModule = createModule {

  // Each binding starts with bind<T>()
  // A "factory" declaration means that this dependency is instantiated every time when it's requested
  bind<MyDependency> { factory { MyDependency() } }
  
  // A "singleton" declaration means that this dependency is only instantiated once (per component) 
  bind<AnotherDependency> { singleton { AnotherDependency() } }
  
  // See how transitive dependencies can be injected with get() within the module's scope
  bind<YetAnotherDependency> { factory { YetAnotherDependency(get<MyDependency>(), get<AnotherDependency>()) } }
  
  // Use named bindings when the type is not unique (there might be more Strings)
  bind<String>("globalId") { singleton { "SOME_GLOBAL_ID" } }
  
  // Use "eagerSingleton" for singleton instances which are instantiated as soon as the component
  // is created and not lazily the first time it's requested
  bind<SomeEagerDependency> { eagerSingleton { SomeEagerDependency() } }
}
```

### Component

A component is composed of one or more modules. It performs the actual injection and is also responsible for holding
instances of dependencies which have been declared as singletons. This concept is important to understand! As long as
the same component reference is used, the same singleton instances will be provided by this component. The developer
is responsible for holding component references and releasing them when necessary. Only when the component is eligible
for garbage collection will it's singletons be GC'd, too. This applies for module instances which were passed to a 
component, too. Module instances should only be held by a component and not stored anywhere else. Especially when the
module provides object instances outside of it's own scope which were passed to the module during creation.

The component pattern has been introduced so that – especially in an Android environment – it is possible to inject
objects that should be released when the view has been destroyed, like for example the current `Context`.

```kotlin
val component = createComponent(modules = listOf(myModuleA, myModuleB))

val myDependency: MyDependency by component.inject()
```

By default injection is performed lazily with the `inject()` delegate. Dependencies can also be injected immediately 
with `injectNow()`. Latter should rarely be used!

Components can depend on other components. In this case the current component combines it's own dependency declarations
with those from the parent components. Parent components should always have a scope (lifetime) which is equal or greater
than the current component or else memory leaks could be introduced. Imagine a component `A` and a component `B`. 
Component `A` declares `B` as a dependent component. `B` should be released but if `A` has a greater scope and is still
referenced somewhere `B` will remain in memory.

```kotlin
val component = createComponent(modules = listOf(myModule),
                                dependsOn = listOf(parentComponentA, parentComponentB))
```

### A word on type erasure

Katana doesn't use reflection. Therefor it cannot circumvent Java's [type erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html).
During runtime Katana will not be able to distinguish between `MyProvider<Int>` and `MyProvider<String>` when both 
dependencies are declared. The following code will result in an `OverrideException`:

```kotlin
createModule {
    
    bind<MyProvider<Int>> { factory { MyProvider<Int>(1337) } }
    
    bind<MyProvider<String>> { factory { MyProvider<String>("Hello world") } }
    
    bind<MyDependency> { factory { MyDependency(get(), get()) } }
}
```

Luckily Katana provides a solution for this! Just use named injection :)

```kotlin
createModule {
    
    bind<MyProvider<Int>>("intProvider") { factory { MyProvider<Int>(1337) } }
    
    bind<MyProvider<String>>("stringProvider") { factory { MyProvider<String>("Hello world") } }
    
    bind<MyDependency> { factory { MyDependency(get("intProvider"), get("stringProvider")) } }
}
```

### Overrides 

Redeclarations of dependencies within modules, which would override existing declarations, are by design not possible 
in Katana. We believe that overrides, especially when they happen silently, are a source of subtle bugs. If you require
an override for example in a test scope, you should instead structure your modules in a way that overrides are not
necessary. For example:

```kotlin
val commonModule = createModule {
    
    bind<MyCommonDependency> { singleton { MyCommonDependency() } }
}

val engineModule = createModule {
    
    bind<MyEngine> { factory { MyEngineImpl(get<MyCommonDependency>()) } }
}

val testEngineModule = createModule {
    
    bind<MyEngine> { factory { MyTestEngine(get<MyCommonDependency>()) } }
}

val productionComponent = createComponent(commonModule, engineModule)
val testComponent = createComponent(commonModule, testEngineModule)
```

### Circular dependencies

You should avoid circular dependencies. Let's imagine the following setup:

```kotlin
class A(b: B)

class B(a: A)

val module = createModule {

    bind<A> { singleton { A(get()) } }

    bind<B> { singleton { B(get()) } }
}

val component = createComponent(module)

val a: A by component.inject()
```

This will result in an `StackOverflowError` once Katana tries to provide an instance of `A`. When Katana executes the
provider function of `A` it will try to solve the transitive dependency `B` which is required for `A`'s constructor.
`B` however requires an instance of `A` and such an endless cycle is born.

The solution to this problem is to structure the classes in a way that a circular dependency is not necessary.
For instance by creating a third class `C` which holds the functionality which both `A` and `B` require.

If there's no easy fix to this problem Katana provides a workaround. Instead of `get()` use `lazy()` to provide a 
`Lazy` version of the dependency.

```kotlin
class A2(b2: Lazy<B2>)

class B2(a2: A2)

val module = createModule {

    // See how lazy() is used here instead of get()
    bind<A2> { singleton { A2(lazy()) } }

    bind<B2> { singleton { B2(get()) } }
}

val component = createComponent(module)

val a: A by component.inject()
```

### Logging

Katana provides a bit of information about dependency declarations and injections during runtime which might help in
debugging DI-related issues. To enable logging pass an implementation of `Katana.Logger` to the `Katana.logger`
property of the `Katana` singleton.

## Installation

Katana is published via [JitPack](https://jitpack.io/#rewe-digital-incubator/katana). First add the JitPack repository
as described in JitPack's documentation, then add the following dependencies:

```gradle
dependencies {
    implementation 'com.github.rewe-digital-incubator.katana:katana-core:1.0'
    // Additionally add this dependency for Android-specific extensions to Katana
    implementation 'com.github.rewe-digital-incubator.katana:katana-android:1.0'
}
```

**Note** that the package name is `org.rewedigital.katana` and differs from the artifact names due to how JitPack works.
We will fix this in a future release.

## License

The MIT license (MIT)

Copyright (c) 2018 REWE digital GmbH

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
