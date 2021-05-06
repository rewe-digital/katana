plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-viewmodel",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    componentName = "release"
)

dependencies {
    api(project(":core"))
    api(Dependencies.androidXFragment)
    api(Dependencies.androidXLifecycleViewModel)
}
