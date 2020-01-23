plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel-savedstate",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

version = "1.11.0-rc03"

dependencies {
    api(project(":core"))
    api(project(":androidx-viewmodel")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
    api(Dependencies.androidXLifecycleViewModelSavedState)
}
