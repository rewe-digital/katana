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

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

publishing {
    (publications) {
        "mavenJava"(MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifactId = "katana-android"
        }
    }
}
