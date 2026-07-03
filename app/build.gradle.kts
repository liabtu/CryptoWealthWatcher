plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cryptowealthwatcher2"

    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.cryptowealthwatcher2"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(
        "androidx.recyclerview:recyclerview:1.4.0"
    )

    // Retrofit
    implementation(
        "com.squareup.retrofit2:retrofit:2.9.0"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:2.9.0"
    )

    // Coroutines
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
    )

    // MVVM
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"
    )

    // Glide
    implementation(
        "com.github.bumptech.glide:glide:4.16.0"
    )

    // Biometric
    implementation(
        "androidx.biometric:biometric:1.2.0-alpha05"
    )

    // Jetpack Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")

    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )
}