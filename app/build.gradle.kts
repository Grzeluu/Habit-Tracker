plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.compose.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.android.hilt.get().pluginId)
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.grzeluu.habittracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.grzeluu.habittracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    hilt {
        enableAggregatingTask = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":base"))

    implementation(project(":common:ui"))
    implementation(project(":common:util"))

    implementation(project(":component:settings"))
    implementation(project(":component:habit"))

    implementation(project(":feature:addhabit"))
    implementation(project(":feature:home"))
    implementation(project(":feature:list"))
    implementation(project(":feature:details"))
    implementation(project(":feature:notifications"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:settings"))

    implementation(project(":source:database"))
    implementation(project(":source:preferences"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.android.hilt)
    implementation(libs.android.hilt.navigation)
    ksp(libs.android.hilt.compiler)
    implementation(libs.kotlinx.metadata)
    implementation(libs.timber)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    androidTestImplementation(libs.androidx.test.ext.junit)

    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}