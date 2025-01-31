import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
//    id("kotlin-kapt")
}

android {

    namespace = "com.contractors.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.contractors.app"
        minSdk = 26

        targetSdk = 35
        multiDexEnabled = true
        versionCode = 1
        versionName = "0.0.9.1"
        setProperty("archivesBaseName", "Сontractors-$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField(
            "String",
            "BASE_URL",
            "\"http://otziv-stroika.ru/\""
        )
        buildConfigField(
            "String",
            "YANDEX_TOKEN",
            "\"a2a65f21-06cb-4434-b4cf-88a0d11d7f9a\""
        )
        signingConfig = signingConfigs.getByName("debug")


    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17


    }
    composeOptions {
        kotlinCompilerExtensionVersion =  "1.15.5"

    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
//    may be doesnt work here
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Dagger
    debugImplementation(libs.hilt.android)
    //Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.navigation.compose)
    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.kt.coil.gif)
    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.websockets)
    //Gson
    implementation(libs.gson)
    implementation(libs.swipe)
    //Yandex
    implementation(libs.maps.mobile)
//    Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation (libs.dagger.android.support)
    implementation(libs.androidx.room.ktx)

    ksp(libs.dagger.compiler) // Dagger compiler
    ksp(libs.hilt.compiler)   // Hilt compiler
}

allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:unchecked")
        options.isDeprecation = true
    }
}