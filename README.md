[![download](https://api.bintray.com/packages/hendraanggrian/maven/locale-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/maven/locale-gradle-plugin/_latestVersion)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![license](https://img.shields.io/github/license/hendraanggrian/locale-gradle-plugin)](http://www.apache.org/licenses/LICENSE-2.0)

Lokkal Gradle Plugin
====================
An write-once-run-anywhere approach to localization in multiple platform project.
* Writes `ResourceBundle` for Java and XML values for Android.
* Localization data can be placed within Gradle script or CSV file.

Download
--------
Add plugin to buildscript:

```gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.hendraanggrian:locale-gradle-plugin:$version"
    }
}
```

Usage
-----
Apply plugin in your module, and configure `locale` extension like below:

```gradle
apply plugin: 'com.hendraanggrian.lokkal'

lokkal {
    resourceName 'strings'
    text('home') {
        en = 'Home'
        id = 'Beranda'
    }
    text('about') {
        en = 'About'
        id = 'Tentang'
    }
}

task.withTask(com.hendraanggrian.lokkal.LocalizeJavaTask) {
    outputDirectory 'src/main/resources'
}

task.withTask(com.hendraanggrian.lokkal.LocalizeAndroidTask) {
    outputDirectory 'my/custom/directory'
}
```

It's even simpler with Gradle Kotlin DSL.

```gradle
lokkal {
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

Then use command `localizeAndroid` or `localizeJava` to write localization files into their respective directory.
