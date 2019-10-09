package org.rewedigital.katana

import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldThrow
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.set
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SetTests : Spek(
    {
        describe("Set bindings") {

            it("should provide set injection") {
                val myComponent1 = MyComponentA()
                val myComponent2 = MyComponentB("hello")

                val module = Module {
                    set<MyComponent> {
                        factory { myComponent1 }
                        factory { myComponent2 }
                        factory { myComponent1 }
                    }
                }

                val component = Component(module)
                val set: Set<MyComponent> = component.injectNow()

                set shouldHaveSize 2
                set shouldContain myComponent1
                set shouldContain myComponent2
            }

            it("should provide named set injection") {
                val module = Module {
                    set<String>(name = "a set") {
                        factory { "Hello" }
                        factory { "World" }
                    }
                }

                val component = Component(module)
                val set: Set<String> = component.injectNow(name = "a set")

                set shouldHaveSize 2
                set shouldContain "Hello"
                set shouldContain "World"
            }

            it("should provide set injection across modules") {
                val module1 = Module {
                    set<String> {
                        factory { "Hello" }
                    }
                }

                val module2 = Module {
                    set<String> {
                        factory { "World" }
                    }
                }

                val component = Component(modules = listOf(module1, module2))
                val set: Set<String> = component.injectNow()

                set shouldHaveSize 2
                set shouldContain "Hello"
                set shouldContain "World"
            }

            it("should provide set injection across components") {
                val module1 = Module {
                    set<String> {
                        factory { "Hello" }
                    }
                }

                val module2 = Module {
                    set<String> {
                        factory { "World" }
                    }
                }

                val component1 = Component(module1)
                val component2 = Component(modules = listOf(module2), dependsOn = listOf(component1))
                val set1: Set<String> = component1.injectNow()
                val set2: Set<String> = component2.injectNow()

                set1 shouldHaveSize 1
                set1 shouldContain "Hello"

                set2 shouldHaveSize 2
                set2 shouldContain "Hello"
                set2 shouldContain "World"
            }

            it("should not allow set redeclaration in same module") {
                val fn = {
                    Module {
                        set<String> {
                            factory { "Hello" }
                        }

                        set<String> {
                            factory { "World" }
                        }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should facilitate singletons in sets") {
                var count1 = 0
                var count2 = 0

                val module1 = Module {
                    set<MyComponent> {
                        singleton {
                            count1++
                            MyComponentA()
                        }
                        factory { MyComponentB("hello") }
                    }
                }

                val module2 = Module {
                    set<MyComponent> {
                        singleton {
                            count2++
                            MyComponentA()
                        }
                    }
                }

                val component = Component(modules = listOf(module1, module2))
                component.injectNow<Set<MyComponent>>()
                val set1: Set<MyComponent> = component.injectNow()
                val set2: Set<MyComponent> = component.injectNow()

                (set1 === set2) shouldEqual false
                count1 shouldEqual 1
                count2 shouldEqual 1
            }

            it("should locate dependencies declared in outer scope") {
                val module = Module {
                    factory(name = "outer scope") { "Hello world" }

                    set<String> {
                        factory { get(name = "outer scope") }
                    }
                }
                val component = Component(module)
                val set: Set<String> = component.injectNow()

                set shouldHaveSize 1
                set shouldContain "Hello world"
            }
        }
    })
