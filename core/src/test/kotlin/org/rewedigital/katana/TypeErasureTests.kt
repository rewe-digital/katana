package org.rewedigital.katana

import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TypeErasureTests : Spek(
    {
        describe("Modules with type erasure") {

            it("should throw exception when type erasure kicks in") {
                val fn = {
                    createModule {

                        bind<MyComponentB<Int>> {
                            factory {
                                MyComponentB(1337)
                            }
                        }

                        bind<MyComponentB<String>> {
                            factory {
                                MyComponentB("Hello world")
                            }
                        }
                    }
                }

                fn shouldThrow OverrideException::class
            }

            it("should work when unique names have been specified") {
                val module = createModule {

                    bind<MyComponentB<Int>>("int") {
                        factory {
                            MyComponentB(1337)
                        }
                    }

                    bind<MyComponentB<String>>("string") {
                        factory {
                            MyComponentB("Hello world")
                        }
                    }
                }

                val component = createComponent(module)

                val myComponent1: MyComponentB<Int> by component.inject("int")
                val myComponent2: MyComponentB<String> by component.inject("string")

                myComponent1.value shouldEqual 1337
                myComponent2.value shouldEqual "Hello world"
            }
        }
    })
