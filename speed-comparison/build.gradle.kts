plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("org.nield:kotlin-statistics:1.2.1")
    implementation("org.koin:koin-core:2.0.1")
    implementation("org.kodein.di:kodein-di-erased-jvm:6.3.3")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Main-Class"] = "org.rewedigital.katana.comparison.ComparisonKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
