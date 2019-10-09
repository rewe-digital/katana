package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.rewedigital.katana.NamedInjectionTests.Key5
import org.rewedigital.katana.NamedInjectionTests.Key6
import org.rewedigital.katana.NamedInjectionTests.Key7
import org.rewedigital.katana.NamedInjectionTests.Key8
import org.rewedigital.katana.dsl.factory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NamedInjectionTests : Spek(
    {
        val module by memoized {
            Module {
                factory(name = Key1) { "First" }
                factory(name = Key2) { "Second" }

                factory(name = Keys.Key3) { "Third" }
                factory(name = Keys.Key4) { "Fourth" }

                factory(name = Key5) { "Fifth" }
                factory(name = Key6) { "Sixth" }

                factory(name = Key7) { "Seventh" }
                factory(name = Key8) { "Eighth" }
            }
        }

        val component by memoized { Component(module) }

        describe("Named injection") {

            context("with Kotlin objects") {

                it("should work") {
                    component.injectNow<String>(Key1) shouldEqual "First"
                    component.injectNow<String>(Key2) shouldEqual "Second"
                }
            }

            context("with enum class") {

                it("should work") {
                    component.injectNow<String>(Keys.Key3) shouldEqual "Third"
                    component.injectNow<String>(Keys.Key4) shouldEqual "Fourth"
                }
            }

            context("with Strings") {

                it("should work") {
                    component.injectNow<String>(Key5) shouldEqual "Fifth"
                    component.injectNow<String>(Key6) shouldEqual "Sixth"
                }
            }

            context("with custom data class") {

                it("should work") {
                    component.injectNow<String>(Key7) shouldEqual "Seventh"
                    component.injectNow<String>(Key8) shouldEqual "Eighth"
                }
            }
        }
    }) {

    object Key1
    object Key2

    enum class Keys { Key3, Key4 }

    const val Key5 = "Key5"
    const val Key6 = "Key6"

    data class CustomKey(val id: String)

    val Key7 = CustomKey("Key7")
    val Key8 = CustomKey("Key8")
}
