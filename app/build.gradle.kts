import java.io.FileInputStream
import java.util.Properties

// local.properties 로드
val props = Properties()
props.load(FileInputStream(rootProject.file("local.properties")))
val ncid = props.getProperty("NAVER_CLIENT_ID")
val ncsec = props.getProperty("NAVER_CLIENT_SECRET")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)

    alias(libs.plugins.google.services) // CHANGED: plugins alias로 통일
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" // CHANGED: Kotlin(2.0.21)과 맞춤

}

android {
    namespace = "com.aspa2025.aspa2025"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aspa2025.aspa2025"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "NAVER_CLIENT_ID", "\"$ncid\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"$ncsec\"")

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            props.getProperty("kakaoNativeAppKey")
        )
        manifestPlaceholders["kakaoNativeAppKeyforManifest"] =
            props.getProperty("kakaoNativeAppKeyforManifest")
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
        compose = true
        buildConfig = true
    }
}

// hilt 관련 빌드 에러 회피 목적
hilt {
    enableAggregatingTask = false
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-functions-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    // ---------- BOM ----------
    implementation(platform(libs.firebase.bom)) // CHANGED: Firebase BOM 한 번만
    implementation(libs.firebase.appcheck)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.appcheck.playintegrity)

    // ---------- Firebase (KTX만, 버전 X) ----------
    implementation(libs.firebase.auth.ktx)      // CHANGED: -ktx로 통일
    implementation(libs.firebase.firestore.ktx) // CHANGED: -ktx로 통일
    implementation(libs.firebase.functions.ktx)


    // ---------- Play Services / Credentials ----------
    implementation(libs.google.signin) // CHANGED: 중복 좌표 제거, 카탈로그만 사용
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // ---------- Compose / AndroidX ----------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx) // lifecycle 라인 통일(2.8.1 in toml)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // 직접 명시 필요한 Compose Lifecycle 확장
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1") // CHANGED: 라인 맞춤
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.1")   // CHANGED: 라인 맞춤

    // ---------- Retrofit (+ kotlinx-serialization만 사용) ----------
    implementation(libs.retrofit.core)                 // CHANGED: 2.11.0 (toml)
    implementation(libs.retrofit.gson)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // ---------- 기타 ----------
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation("com.kakao.sdk:v2-all:2.21.6")
    // naver login sdk // oauth2.0 based
    implementation("com.navercorp.nid:oauth:5.10.0")

    // ---------- Test / Debug ----------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // 아이콘
    implementation(libs.compose.material.icons.extended)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // EncryptedSharedPreferences
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // ---------- DataStore ----------
    implementation("androidx.datastore:datastore-preferences:1.1.7")
}

