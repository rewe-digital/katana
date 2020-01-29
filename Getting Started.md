# Getting Started

Katana consists of two core concepts: modules and components.

## Module

A module describes **how** dependencies are provided. Each module should represent a logical unit. For instance there
might be a module for every feature of your application.

```kotlin
val myModule = Module {

  // A "factory" declaration means that this dependency is instantiated every time when it's requested
  factory { MyDependency() }
  
  // A "singleton" declaration means that this dependency is only instantiated once (per component) 
  singleton { AnotherDependency() }
  
  // See how transitive dependencies can be injected with get() within the module's scope
  factory { YetAnotherDependency(get<MyDependency>(), get<AnotherDependency>()) }
  
  // Use named bindings when the type is not unique (there might be more Strings)
  singleton(name = "globalId") { "SOME_GLOBAL_ID" }
  
  // Use "eagerSingleton" for singleton instances which are instantiated as soon as the component
  // is created and not lazily the first time it's requested
  eagerSingleton { SomeEagerDependency() }
  
  // Declares a set-based multi-binding where every declaration inside set { } contribute to the set.
  // Set declarations may be segmented across multiple modules and components. 
  set<String> {
    factory { "Hello" }
    singleton { "World" }
  }
}
```

## Component

A component is composed of one or more modules. It performs the actual injection and is also responsible for holding
instances of dependencies which have been declared as singletons. This concept is important to understand! As long as
the same component reference is used, the same singleton instances will be provided by this component. The developer
is responsible for holding component references and releasing them when necessary. Only when the component is eligible
for garbage collection will its singletons be GC'd, too. This applies for module instances which were passed to a 
component, too. Module instances should only be held by a component and not stored anywhere else. Especially when the
module provides object instances outside of its own scope which were passed to the module during creation.

The component pattern has been introduced so that – especially in an Android environment – it is possible to inject
objects that should be released when the view has been destroyed, like for example the current `Context`.

```kotlin
val component = Component(modules = listOf(myModuleA, myModuleB))

val myDependency: MyDependency by component.inject()
```

By default injection is performed lazily with the `inject()` delegate. Dependencies can also be injected immediately 
with `injectNow()`. Latter should rarely be used!

Components can depend on other components. In this case the current component combines its own dependency declarations
with those from the parent components. Parent components should always have a scope (lifetime) which is equal or greater
than the current component or else memory leaks could be introduced. Imagine a component `A` and a component `B`. 
Component `A` declares `B` as a dependent component. `B` should be released but if `A` has a greater scope and is still
referenced somewhere `B` will remain in memory.

```kotlin
val component = Component(
  modules = listOf(myModule),
  dependsOn = listOf(parentComponentA, parentComponentB)
)
```

## A word on type erasure

Katana doesn't use reflection. Therefor it cannot circumvent Java's [type erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html).
During runtime Katana will not be able to distinguish between `MyProvider<Int>` and `MyProvider<String>` when both 
dependencies are declared. The following code will result in an `OverrideException`:

```kotlin
Module {
    
    factory { MyProvider<Int>(1337) }
    
    factory { MyProvider<String>("Hello world") }
    
    factory { MyDependency(get(), get()) }
}
```

Luckily Katana provides a solution for this! Just use named injection :)

```kotlin
enum class Names { IntProvider, StringProvider }

Module {
    
    factory(name = Names.IntProvider) { MyProvider<Int>(1337) }
    
    factory(name = Names.StringProvider) { MyProvider<String>("Hello world") }
    
    factory { MyDependency(get(Names.IntProvider), get(Names.StringProvider)) }
}
```

## Overrides 

Redeclarations of dependencies within modules, which would override existing declarations, are by design not possible 
in Katana. We believe that overrides, especially when they happen silently, are a source of subtle bugs. If you require
an override for example in a test scope, you should instead structure your modules in a way that overrides are not
necessary. For example:

```kotlin
val commonModule = Module {
    
    singleton { MyCommonDependency() }
}

val engineModule = Module {
    
    factory<MyEngine> { MyEngineImpl(get<MyCommonDependency>()) }
}

val testEngineModule = Module {
    
    factory<MyEngine> { MyTestEngine(get<MyCommonDependency>()) }
}

val productionComponent = Component(commonModule, engineModule)
val testComponent = Component(commonModule, testEngineModule)
```

## Circular dependencies

You should avoid circular dependencies. Let's imagine the following setup:

```kotlin
class A(b: B)

class B(a: A)

val module = Module {

    singleton { A(get()) }

    singleton { B(get()) }
}

val component = Component(module)

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

val module = Module {

    // See how lazy() is used here instead of get()
    singleton { A2(lazy()) }

    singleton { B2(get()) }
}

val component = Component(module)

val a: A by component.inject()
```

## Logging

Katana provides a bit of information about dependency declarations and injections during runtime which might help in
debugging DI-related issues. To enable logging pass an implementation of `Katana.Logger` to the `Katana.logger`
property of the `Katana` singleton.

## Installation & Setup

Katana is published on [JCenter](https://bintray.com/bintray/jcenter). If you haven't already done it, add JCenter as
a repository to your project. Then add the following dependencies:

```gradle
dependencies {
    implementation 'org.rewedigital.katana:katana-core:1.12.1'
    // Use this artifact for Katana on Android
    implementation 'org.rewedigital.katana:katana-android:1.12.1'
}
```

