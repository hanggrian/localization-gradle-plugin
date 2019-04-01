Locale Gradle Plugin
====================
[![bintray](https://img.shields.io/badge/bintray-maven-brightgreen.svg)](https://bintray.com/hendraanggrian/maven)
[![download](https://api.bintray.com/packages/hendraanggrian/maven/locale-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/maven/locale-gradle-plugin/_latestVersion)
[![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Cross-platform localization generated in Gradle.

Currently only supports:
 * XML files for Android.
 * Properties files used as `ResourceBundle` for Java.

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
apply plugin: 'com.hendraanggrian.locale'

locale {
    resourceName = 'strings'
    configure('home') {
        en = 'Home'
        id = 'Beranda'
    }
    configure('about') {
        en = 'About'
        id = 'Tentang'
    }
}

task.withTask(com.hendraanggrian.locale.WriteResourceBundlesTask) {
    outputDirectory 'src/main/resources'
}

task.withTask(com.hendraanggrian.locale.WriteAndroidResourcesTask) {
    outputDirectory 'my/custom/directory'
}
```

It's even simpler with Gradle Kotlin DSL.

```gradle
locale {
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

Then use command `writeAndroidResources` or `writeResourceBundles` to write localization files into their respective directory.

License
-------
    Copyright 2018 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    