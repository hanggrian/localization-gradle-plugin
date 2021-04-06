buildscript {
    repositories {
        jcenter()
        maven(REPO_GRADLE_PORTAL)
    }
    dependencies {
        classpath(kotlin("gradle-plugin", VERSION_KOTLIN))
        classpath(dokka())
        classpath(gitPublish())
        classpath(gradlePublish())
    }
}

allprojects {
    repositories {
        jcenter()
        maven(REPO_OSSRH_SNAPSHOTS)
        maven("https://kotlin.bintray.com/kotlinx")
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