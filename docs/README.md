[![Plugin Portal](https://img.shields.io/maven-metadata/v?label=plugin-portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fhendraanggrian%2Flocalization%2Fcom.hendraanggrian.localization.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.hendraanggrian.localization/)
[![OpenJDK](https://img.shields.io/badge/jdk-11+-informational)](https://openjdk.java.net/projects/jdk-11/)

# Localization Gradle Plugin

An write-once-run-anywhere approach to localization in multiple platform project.

- Writes `ResourceBundle` for Java and XML values for Android.
- Localization data can be placed within Gradle script or CSV file.

## Download

Using plugins DSL:

```gradle
plugins {
    id('com.hendraanggrian.localization') version "$version"
}
```

Using legacy plugin application:

```gradle
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.hendraanggrian:localization-gradle-plugin:$version")
    }
}

apply plugin: 'com.hendraanggrian.localization'
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

Then use command `localizeJvm` or `localizeAndroid` to write localization files into their respective directory.
