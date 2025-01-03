plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {

    buildFeatures {
        viewBinding = true
    }

    namespace = "iss.nus.edu.sg.ca.matchgame"
    compileSdk = 35

    defaultConfig {
        applicationId = "iss.nus.edu.sg.ca.matchgame"
        minSdk = 29
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.picasso:picasso:2.8")


    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
    // Gson
    implementation 'com.google.code.gson:gson:2.8.9'
}