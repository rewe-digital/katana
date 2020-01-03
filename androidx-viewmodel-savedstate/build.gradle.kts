plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel-savedstate",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

version = "1.10.0-rc04"

dependencies {
    api(project(":core"))
    api(project(":androidx-viewmodel")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
    // Required by androidx.lifecycle:lifecycle-viewmodel-savedstate
    api("androidx.fragment:fragment:1.2.0-rc04")
    api(Dependencies.androidXLifecycleViewModelSavedState)
}
