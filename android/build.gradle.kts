plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-android",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    componentName = "release"
)

dependencies {
    api(project(":core"))
    api(Dependencies.androidXCollection)
    api(Dependencies.androidXFragment)
}
