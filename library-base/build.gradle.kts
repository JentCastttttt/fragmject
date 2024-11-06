import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

val configProperties = Properties()
configProperties.load(FileInputStream(rootProject.file("config.properties")))

android {
    namespace = "com.example.miaow.base"
    compileSdk = configProperties.getProperty("compileSdkVersion").toInt()

    defaultConfig {
        minSdk = configProperties.getProperty("minSdkVersion").toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    kotlin {
        jvmToolchain(21)
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(libs.androidx.constraintlayout)
    api(libs.androidx.core.ktx)
    api(libs.androidx.core.splashscreen)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.webkit)
    api(libs.coil)
    api(libs.coil.gif)
    api(libs.coil.svg)
    api(libs.coil.video)
    api(libs.gson)
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines)
    api(libs.material)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
}