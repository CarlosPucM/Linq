pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SRLinq"
include(":Linq")

// Evita incluir el módulo :app cuando se construye en JitPack
val skipApp = System.getenv("SKIP_APP")
if (skipApp.isNullOrBlank()) {
    include(":app")
}
