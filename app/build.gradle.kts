
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "uz.apphub.fayzullo"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.apphub.fayzullo"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.retrofit2.kotlinx.serialization.converter) // Ensure correct spelling
    implementation(libs.retrofit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    /**
     * Navigation
     * */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    /**
     * Hilt
     * */
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.fragment)
    kapt(libs.hilt.compiler)
    /**
     * Lifecycle
     * */
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.androidx.fragment.ktx)
    /**
     * glide
     * */
    implementation(libs.glide)
    /**
     * OkHttp
     * */
    implementation(libs.okhttp)

    /**
     * ViewPager2
     */
    implementation(libs.androidx.viewpager2)

    /**
     * room
     */
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    /**
     * DotsIndicator
     */
    implementation(libs.dotsindicator)

    /**
     * chucker
     */
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    /**
     * retrofit
     */
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit)

}

