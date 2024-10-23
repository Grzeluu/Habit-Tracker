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
include(":common")
include(":common:base")
include(":common:ui")
include(":common:util")
include(":component")
include(":component:habit")
include(":component:settings")
include(":feature")
include(":feature:addhabit")
include(":feature:habits")
include(":feature:notifications")
include(":feature:onboarding")
include(":feature:settings")
include(":source")
include(":source:database")
include(":source:preferences")
