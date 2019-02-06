extra["addCommonPomAttributes"] = fun(publication: MavenPublication) {
    publication.pom {
        name.set("Katana")
        description.set("Lightweight, minimalistic dependency injection library for Android & Kotlin")
        url.set("https://github.com/rewe-digital/katana")

        developers {
            developer {
                name.set("Sven Jacobs")
                email.set("sven.jacobs@rewe-digital.com")
                id.set("svenjacobs")
                organization.set("REWE Digital")
                organizationUrl.set("https://rewe-digital.com/")
            }
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/rewe-digital/katana.git")
            developerConnection.set("scm:git:git://github.com/rewe-digital/katana.git")
            url.set("https://github.com/rewe-digital/katana")
        }
    }
}
