plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel-savedstate",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

version = "1.8.2-beta01"

dependencies {
    api(project(":core"))
    api(project(":androidx-viewmodel")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
    // Required by androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-beta01
    api("androidx.fragment:fragment:1.2.0-beta02")
    api(Dependencies.androidXLifecycleViewModelSavedState)
}
