plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.ricven.richinsights"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.ricven.richinsights"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidxComposeBom))
    androidTestImplementation(platform(libs.androidxComposeBom))

    implementation(libs.androidxActivityCompose)
    implementation(libs.androidxComposeMaterial3)
    implementation(libs.androidxComposeMaterialIconsExtended)
    implementation(libs.androidxComposeUiToolingPreview)
    implementation(libs.androidxNavigation3Runtime)
    implementation(libs.androidxNavigation3Ui)
    implementation(libs.androidxCoreSplashscreen)
    implementation(libs.androidxDataStorePreferences)
    implementation(libs.kotlinxSerializationJson)

    debugImplementation(libs.androidxComposeUiTooling)
}
