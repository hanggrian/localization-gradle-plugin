[![CircleCI](https://img.shields.io/circleci/build/gh/hanggrian/localization-gradle-plugin)](https://app.circleci.com/pipelines/github/hanggrian/localization-gradle-plugin/)
[![Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/com.hanggrian.localization)](https://plugins.gradle.org/plugin/com.hanggrian.localization)
[![OpenJDK](https://img.shields.io/badge/jdk-11%2B-informational)](https://openjdk.org/projects/jdk/11/)

# Localization Gradle Plugin

An write-once-run-anywhere approach to localization in multiple platform
project.

- Writes `ResourceBundle` for Java and XML values for Android.
- Localization data can be placed within Gradle script or CSV file.

## Download

Using plugins DSL:

```gradle
plugins {
    id('com.hanggrian.localization') version "$version"
}
```

Using legacy plugin application:

```gradle
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.hanggrian:localization-gradle-plugin:$version")
    }
}

apply plugin: 'com.hanggrian.localization'
```

## Usage

Apply plugin in your module, and configure `localization` extension like below:

```gradle
localization {
    resourceName.set('strings')
    text('home') {
        en = 'Home'
        id = 'Beranda'
    }
    text('about') {
        en = 'About'
        id = 'Tentang'
    }
}

tasks {
    localizeJvm {
        outputDirectory.set(new File('src/main/resources'))
    }
    localizeAndroid {
        outputDirectory.set(new File('my/custom/directory'))
    }
}
```

It's even simpler with Gradle Kotlin DSL.

```kotlin
localization {
    "home" {
        en = "Home"
        id = "Beranda"
    }
    "about" {
        en = "About"
        id = "Tentang"
    }
}
```

Then use command `localizeJvm` or `localizeAndroid` to write localization files
into their respective directory.
