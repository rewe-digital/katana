object Versions {
    const val androidJunit5GradlePlugin = "1.4.2.0"
    const val androidMavenPublishPlugin = "3.6.2"
    const val androidPlugin = "3.4.0"
    const val androidXCollection = "1.0.0"
    const val androidXFragment = "1.0.0"
    const val androidXLifecycleExtensions = "2.0.0"
    const val androidXLifecycleViewModel = "2.0.0"
    const val bintrayPlugin = "1.8.4"
    const val dokkaPlugin = "0.9.18"
    const val gradleVersionsPlugin = "0.21.0"
    const val kluent = "1.49"
    const val kotlin = "1.3.31"
    const val spek = "2.0.4"
}

object Dependencies {
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.androidJunit5GradlePlugin}"
    const val androidPlugin = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val androidXCollection = "androidx.collection:collection:${Versions.androidXCollection}"
    const val androidXFragment = "androidx.fragment:fragment:${Versions.androidXFragment}"
    const val androidXLifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.androidXLifecycleExtensions}"
    const val androidXLifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidXLifecycleViewModel}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
    const val spek2RunnerJunit5 = "org.spekframework.spek2:spek-runner-junit5:${Versions.spek}"
    const val spekApi = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spek}"
}

object Android {
    const val compileSdkVersion = 28
    const val targetSdkVersion = 28
    const val minSdkVersion = 14
}
