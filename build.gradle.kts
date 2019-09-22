buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
        classpath(bintrayRelease())
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/hendraanggrian/maven")
    }
    tasks {
        withType<Delete> {
            delete(projectDir.resolve("out"))
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
    named<Wrapper>("wrapper") {
        gradleVersion = VERSION_GRADLE
        distributionType = Wrapper.DistributionType.ALL
    }
}
