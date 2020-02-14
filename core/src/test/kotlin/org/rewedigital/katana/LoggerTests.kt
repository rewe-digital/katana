package org.rewedigital.katana

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.rewedigital.katana.internal.Logger
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object LoggerTests : Spek(
    {

        data class LogEntry(val msg: String, val throwable: Throwable? = null)

        class TestLogger : Katana.Logger {

            val debug = mutableListOf<LogEntry>()
            val info = mutableListOf<LogEntry>()
            val warn = mutableListOf<LogEntry>()
            val error = mutableListOf<LogEntry>()

            override fun debug(msg: String) {
                debug.add(LogEntry(msg))
            }

            override fun info(msg: String) {
                info.add(LogEntry(msg))
            }

            override fun warn(msg: String) {
                warn.add(LogEntry(msg))
            }

            override fun error(msg: String, throwable: Throwable?) {
                error.add(LogEntry(msg, throwable))
            }
        }

        describe("Logger") {

            it("should log when logger is set") {
                val logger = TestLogger()
                val exception = Exception("Exception")

                Katana.logger = logger

                Logger.debug { "Debug message" }
                Logger.info { "Info message" }
                Logger.warn { "Warn message" }
                Logger.error { Pair("Error message", exception) }

                logger.debug shouldContain LogEntry("Debug message")
                logger.info shouldContain LogEntry("Info message")
                logger.warn shouldContain LogEntry("Warn message")
                logger.error shouldContain LogEntry("Error message", exception)
            }

            it("should NOT log when logger is absent") {
                Katana.logger = null

                var debugCalled = false
                var infoCalled = false
                var warnCalled = false
                var errorCalled = false

                Logger.debug { debugCalled = true; "Debug message" }
                Logger.info { infoCalled = true; "Info message" }
                Logger.warn { warnCalled = true; "Warn message" }
                Logger.error { errorCalled = true; Pair("Error message", Exception()) }

                debugCalled shouldBeEqualTo false
                infoCalled shouldBeEqualTo false
                warnCalled shouldBeEqualTo false
                errorCalled shouldBeEqualTo false
            }
        }
    })
