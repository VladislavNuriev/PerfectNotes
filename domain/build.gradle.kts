plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinAndroidKsp)

}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }

    dependencies {
        implementation(libs.kotlinx.coroutines.core)

        //Dagger
        implementation(libs.dagger)
        ksp(libs.dagger.compiler)
    }
}
