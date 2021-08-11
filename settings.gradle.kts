rootProject.name = "github-api-wrapper"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("ktor", "1.6.2")
            alias("ktor-client-core").to("io.ktor", "ktor-client-core").versionRef("ktor")
            alias("ktor-client-java").to("io.ktor", "ktor-client-java").versionRef("ktor")
            alias("ktor-client-js").to("io.ktor", "ktor-client-js").versionRef("ktor")
            alias("ktor-client-auth").to("io.ktor", "ktor-client-auth").versionRef("ktor")
            alias("ktor-client-serialization").to("io.ktor", "ktor-client-serialization").versionRef("ktor")
            alias("kotlinx-serialization-json").to("org.jetbrains.kotlinx", "kotlinx-serialization-json")
                .version("1.2.2")
            alias("kotlinx-datetime").to("org.jetbrains.kotlinx", "kotlinx-datetime").version("0.2.1")
        }
    }
}

val isCi = System.getenv("GITHUB_ACTIONS") == "true"

buildCache {
    remote<HttpBuildCache> {
        url = uri("https://gradle-cache.nycode.de")
        isPush = isCi
        credentials {
            username = System.getenv("GRADLE_CACHE_USER")
            password = System.getenv("GRADLE_CACHE_PASSWORD")
        }
    }
}
