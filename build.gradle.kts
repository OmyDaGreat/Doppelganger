plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.kotlin) apply false
}
