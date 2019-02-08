@file:Suppress("unused")

package org.rewedigital.katana

interface MyComponent

class MyComponentA : MyComponent

class MyComponentB<T>(val value: T) : MyComponent

class MyComponentC<A, B>(val value: A,
                         val value2: B) : MyComponent

class A(val b: B)

class B(val a: A)

class A2(val b2: Lazy<B2>)

class B2(val a2: A2)
