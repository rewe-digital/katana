plugins {
    kotlin("jvm")
}

dependencies {
    compile(project(":core"))
    compile("org.nield:kotlin-statistics:1.2.1")
    compile("org.koin:koin-core:2.0.0")
    compile("org.kodein.di:kodein-di-erased-jvm:6.2.1")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Main-Class"] = "org.rewedigital.katana.comparison.ComparisonKt"
    }
    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
