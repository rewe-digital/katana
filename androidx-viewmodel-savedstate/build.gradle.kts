plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel-savedstate",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

version = "1.8.1-alpha03"

dependencies {
    api(project(":core"))
    api(project(":androidx-viewmodel")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
    }
    // Required by androidx.lifecycle:lifecycle-viewmodel-savedstate:1.0.0-alpha03
    api("androidx.fragment:fragment:1.2.0-alpha02")
    api(Dependencies.androidXLifecycleViewModelSavedState)
}
