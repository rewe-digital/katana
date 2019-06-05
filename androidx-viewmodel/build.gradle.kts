plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

dependencies {
    api(project(":core"))
    api(Dependencies.androidXFragment)
    api(Dependencies.androidXLifecycleViewModel)
    api(Dependencies.androidXLifecycleExtensions)
}
