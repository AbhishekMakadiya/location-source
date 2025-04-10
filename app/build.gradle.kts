plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.location.location"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.location.location"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "2.0.4"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        //dataBinding = true
    }
    kapt {
        correctErrorTypes = true
    }
    packaging {
        dex {
            useLegacyPackaging = false
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.cast.framework)
    implementation(project(":countrypicker"))
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.retrofit)
    implementation(libs.convertergson)
    implementation(libs.logginginterceptor)
    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.coroutinesandroid)
    implementation(libs.lifecycleviewmodel)
    implementation(libs.fragmentktx)
    implementation(libs.activityktx)
    implementation (libs.androidx.fragment.ktx.v130) // Or newer
    implementation (libs.androidx.activity.ktx.v120) // Or newer
    implementation(libs.otpview)
    implementation(libs.sdpandroid)
    implementation(libs.sspandroid)
    implementation(libs.keyboardvisibilityevent)
    implementation(libs.ucrop)
    implementation(libs.googleandroidgms)
    implementation(libs.firebasemessaging)
    implementation(libs.socketioclient)
    implementation(libs.commonstext)
    implementation(libs.lifecycleprocess)
    implementation(libs.eventbus)
    implementation(libs.jobqueue)
    //implementation(project(":tedimagepicker"))
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.photoview)
    implementation(libs.play.services.location)
    implementation(libs.android.networking)

    implementation ("androidx.paging:paging-runtime-ktx:3.0.0-beta01")

    implementation ("com.github.pzienowicz:androidx-auto-scroll-view-pager:1.2.0")
    // indicator with recycler view
    implementation ("com.github.martinstamenkovski:ARIndicatorView:2.0.0")


    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // WorkManager with Hilt
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

//    implementation ("com.google.maps.android:places-ktx:3.3.1")
    implementation(project(":places-ktx"))
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation ("com.google.maps:google-maps-services:0.18.0")
    implementation ("com.google.maps.android:android-maps-utils:2.3.0")

    // room DB
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
}