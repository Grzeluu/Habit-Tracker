plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.android.hilt.get().pluginId)
}
android {
    namespace = "com.grzeluu.habittracker.component.settings"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":common:util"))
    implementation(project(":source:preferences"))

    implementation(libs.kotlinx.serialization)
    implementation(libs.android.hilt)
    implementation(libs.android.hilt.navigation)
    ksp(libs.android.hilt.compiler)
    implementation(libs.timber)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)

    androidTestImplementation(libs.androidx.test.ext.junit)

    testImplementation(libs.mockk)
}