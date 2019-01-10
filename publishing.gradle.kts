extra["addCommonPomAttributes"] = fun(publication: MavenPublication) {
    publication.pom {
        name.set("Katana")
        description.set("Lightweight, minimalistic dependency injection library for Android & Kotlin")
        url.set("https://github.com/rewe-digital-incubator/katana")

        developers {
            developer {
                name.set("Sven Jacobs")
                email.set("sven.jacobs@rewe-digital.com")
                id.set("svenjacobs")
            }
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
    }
}
