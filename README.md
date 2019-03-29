Locale Gradle Plugin
====================
[![bintray](https://img.shields.io/badge/bintray-maven-brightgreen.svg)](https://bintray.com/hendraanggrian/maven)
[![download](https://api.bintray.com/packages/hendraanggrian/maven/locale-gradle-plugin/images/download.svg)](https://bintray.com/hendraanggrian/maven/locale-gradle-plugin/_latestVersion)
[![build](https://travis-ci.com/hendraanggrian/locale-gradle-plugin.svg)](https://travis-ci.com/hendraanggrian/locale-gradle-plugin)
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
Apply plugin in your module.

```gradle
apply plugin: 'com.hendraanggrian.locale'
```

Configure `locale` extension like below:

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
    