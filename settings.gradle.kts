include(
    "core",
    "android",
    "androidx-fragment",
    "androidx-viewmodel",
    "androidx-viewmodel-savedstate",

    "speed-comparison",
    "android-example"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}
