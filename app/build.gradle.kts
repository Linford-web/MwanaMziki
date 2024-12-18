import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.eventmuziki"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eventmuziki"
        minSdk = 24
        //noinspection OldTargetApi
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation (libs.firebase.storage.v2021)
    implementation (libs.glide)
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation(libs.play.services.cast.framework)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation (libs.firebase.firestore.v2402)
    implementation(platform(libs.firebase.bom))
    annotationProcessor (libs.compiler)
    implementation(libs.firebase.storage)
    implementation(libs.circleimageview)
    implementation (libs.material.v180)
    implementation ("com.hbb20:ccp:2.7.3")
    implementation (libs.material.v130)
    implementation(libs.activity)
    implementation(libs.play.services.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    // firebase ui dependencies
    implementation(libs.firebase.ui.firestore)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.firebase.ui.firestore)
    implementation (libs.material)

}