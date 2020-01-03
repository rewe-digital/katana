object Versions {
    // Do not upgrade androidJunit5GradlePlugin and androidPlugin as this breaks ViewModel support (issue #7)
    const val androidJunit5GradlePlugin = "1.4.0.0"
    const val androidPlugin = "3.3.2"
    const val androidXCollection = "1.1.0"
    const val androidXFragment = "1.1.0"
    const val androidXLifecycleViewModel = "2.1.0"
    const val androidXLifecycleViewModelSavedState = "1.0.0-rc03"
    const val gradleVersionsPlugin = "0.27.0"
    const val kluent = "1.58"
    const val kotlin = "1.3.61"
    const val spek = "2.0.9"
}

object Dependencies {
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.androidJunit5GradlePlugin}"
    const val androidPlugin = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val androidXCollection = "androidx.collection:collection:${Versions.androidXCollection}"
    const val androidXFragment = "androidx.fragment:fragment:${Versions.androidXFragment}"
    const val androidXLifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidXLifecycleViewModel}"
    const val androidXLifecycleViewModelSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.androidXLifecycleViewModelSavedState}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val spek2RunnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:${Versions.spek}"
    const val spekApi = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}"
}

object Android {
    const val compileSdkVersion = 29
    const val targetSdkVersion = 29
    const val minSdkVersion = 14
}
