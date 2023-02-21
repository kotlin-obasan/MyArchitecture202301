// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by extra("1.6.21")
    val navVersion by extra("2.5.3")

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.0.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.5")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
