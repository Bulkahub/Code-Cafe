plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")
}


android {
    namespace = "com.example.cafeapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cafeapp"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
    }

    viewBinding {
        enable = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }

    kapt{
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))
    implementation("com.google.firebase:firebase-analytics")

    //Firebase Auth
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //Firestore
    implementation(libs.firebase.firestore)

    //RealTime
    implementation(libs.firebase.database)


    //lifecycle
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.livedata)
    implementation(libs.androidx.anotationkapt)


    //Json
    implementation(libs.androidx.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")



    //Coroutines
    implementation(libs.androidx.coroutines)


    //Navigation JetPack
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.fragment:fragment-ktx:1.8.9")


    //Hilt
    implementation("com.google.dagger:hilt-android:2.57")
    kapt("com.google.dagger:hilt-compiler:2.57")


    //DataStore
    implementation(libs.androidx.datastore.preferences)


    //LottieAnimation
    implementation("com.airbnb.android:lottie:6.6.7")


    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}