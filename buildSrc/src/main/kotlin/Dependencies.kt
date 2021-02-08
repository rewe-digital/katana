object Versions {
    const val androidJunit5GradlePlugin = "1.7.0.0"
    const val androidPlugin = "4.1.2"
    const val androidXCollection = "1.1.0"
    const val androidXFragment = "1.2.5"
    const val androidXLifecycle = "2.2.0"
    const val gradleVersionsPlugin = "0.36.0"
    const val kluent = "1.65"
    const val kotlin = "1.4.30"
    const val spek = "2.0.15"
}

object Dependencies {
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.androidJunit5GradlePlugin}"
    const val androidPlugin = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val androidXCollection = "androidx.collection:collection:${Versions.androidXCollection}"
    const val androidXFragment = "androidx.fragment:fragment:${Versions.androidXFragment}"
    const val androidXLifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidXLifecycle}"
    const val androidXLifecycleViewModelSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.androidXLifecycle}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val spek2RunnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:${Versions.spek}"
    const val spekApi = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}"
}

object Android {
    const val compileSdkVersion = 30
    const val targetSdkVersion = 30
    const val minSdkVersion = 14
    const val buildToolsVersion = "30.0.3"
}
