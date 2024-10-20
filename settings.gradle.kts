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

rootProject.name = "Habit Tracker"
include(":app")
include(":component")
include(":component:habit")
include(":feature")
include(":feature:addhabit")
include(":feature:habits")
include(":feature:settings")
include(":feature:notifications")
include(":common")
include(":common:ui")
include(":common:util")
include(":source")
include(":source:database")
include(":source:preferences")
