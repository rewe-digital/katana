plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel-savedstate",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    componentName = "release"
)

dependencies {
    api(project(":core"))
    api(project(":androidx-viewmodel")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
    api(Dependencies.androidXLifecycleViewModelSavedState)
}
