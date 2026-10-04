import java.util.Base64

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

    sourceSets.getByName("main").res.directories.add(
        layout.buildDirectory.dir("generated/res/plusJakartaSans/main")
    )
}

val generatedFontResDir = layout.buildDirectory.dir("generated/res/plusJakartaSans/main")
val fontWeights = listOf("regular", "semibold", "bold")

val generateBundledFonts = tasks.register("generateBundledFonts") {
    val encodedFonts = fontWeights.map {
        layout.projectDirectory.file("gradle/fonts/plus_jakarta_sans_$it.ttf.base64")
    }
    inputs.files(encodedFonts)
    outputs.dir(generatedFontResDir)

    doLast {
        val fontDir = generatedFontResDir.get().dir("font").asFile
        fontDir.mkdirs()

        fontWeights.forEach { weight ->
            val encodedFile = layout.projectDirectory.file(
                "gradle/fonts/plus_jakarta_sans_$weight.ttf.base64"
            ).asFile
            val decoded = Base64.getMimeDecoder().decode(encodedFile.readText())
            fontDir.resolve("plus_jakarta_sans_$weight.ttf").writeBytes(decoded)
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn(generateBundledFonts)
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
