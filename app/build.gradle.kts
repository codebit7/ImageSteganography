plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.imagesteganographyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.imagesteganographyapp"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)



    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.firebase:firebase-auth:22.1.2")

    implementation ("com.cloudinary:cloudinary-android:2.1.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
}