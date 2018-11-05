plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    compileOnly("com.google.android:android:4.1.1.4")
}

tasks.withType<Jar> {
    baseName = "katana-android"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    (publications) {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifactId = "katana-android"
        }
    }
}
