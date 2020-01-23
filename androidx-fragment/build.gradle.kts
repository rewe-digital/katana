plugins {
    `base-android-plugin`
}

configureBase(
    artifactName = "katana-androidx-fragment",
    sourcePath = android.sourceSets["main"].java.srcDirs,
    publicationComponent = components["android"]
)

dependencies {
    api(project(":core"))
    api(Dependencies.androidXCollection)
    api(Dependencies.androidXFragment)
}
